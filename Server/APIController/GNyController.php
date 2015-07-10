<?php

require_once($_SERVER["DOCUMENT_ROOT"].'/APIController/ApiException.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/APIController/AbstractApiController.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Address/AddressGNy.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Route/Point.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Route/Step.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Route/Transport.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Route/Route.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/Helper.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOFactory.php');

class GNyController extends AbstractApiController
{
	const ADDRESS_URL = "https://www.g-ny.org/thrift/js/geocoding"; //URL pour récupérer adresse
	const ROUTE_URL = "https://www.g-ny.org/thrift/js/ride";  //URL pour récupérer le trajet entre un départ et une arrivée
	const PLACE_URL = "https://www.g-ny.org/thrift/js/placemark";  //URL pour récupérer arrets de tram/bus, lieux connus
	
	private $result=array(); //Le tableau de données récupérées
	private $address; //l'adresse ou le bout d'adresse provenant du champ
	private $start; //le départ
	private $end; //l'arrivée
	private $transportMode; //le mode de transport provenant du formulaire
	
	private $hour;
	private $minute;
	private $year;
	private $month;
	private $day;

	private $json; //Le résultat encodé en JSON passé à la vue
	private $errorParam=array('erreur'=>'Appel invalide sur l\'API'); //Le tableau erreur en cas d'une requête invalide (paramètre POST vide ou manquant)
	private $errorApi=array('erreur'=>'Une erreur interne est survenue');
	
	protected function getReferer(){
		return 'https://www.g-ny.org/';
	}
	
	/**
	*	Récupère les adresses en fonction de l'adresse ou du bout d'adresse passé en paramètre et les encode en JSON
	*	@param post Le tableau des paramètres postés
	**/
	public function getAddress()
	{
		$post=$this->getRequestPost();
		if(!isset($post['address']) || empty($post['address'])) //si l'adresse passée est vide ou inexistante => erreur
			$this->result=$this->errorParam;
		else
		{
			$this->address=$post['address'];
			
			$addressCompleted = $this->completeAddressRequest($this->address);
			$placeCompleted = $this->completePlaceRequest($this->address);
			
			$addressResponse = $this->post(GNyController::ADDRESS_URL,$addressCompleted);
			$placeResponse = $this->post(GNyController::PLACE_URL,$placeCompleted);

			$this->createAddress($addressResponse);
			$this->createPlaces($placeResponse);
			
			$this->json='{"address":'.Helper::encodeArrayOfObject($this->result['address']).',"place":'.Helper::encodeArrayOfObject($this->result['place']).'}';
		}
	}
	
	public function render(){
		header('Content-type: application/json');
		echo $this->json;		
	}
	
	public function getResponse(){
		return $this->json;
	}
	
	/**
	*	Recupère les trajets en fonction des paramètres postés
	*	@param post Le tableau des paramètres postés
	**/
	public function getRoutes()
	{
		try{
			$post=$this->getRequestContent();
			
			$post=urldecode($post);
			
			$data=json_decode($post);
			if(isset($data->start) && isset($data->end)){
				$completedRoute = $this->completeRoutesRequest($data);
				$response = $this->post(GNyController::ROUTE_URL,$completedRoute);
				$this->createRoutes($response);
				$this->json='{"route":'.Helper::encodeArrayOfObject($this->result['route']).'}';
			}
		}catch(DAOException $e){
			$this->json=json_encode(array('erreur'=>$e->getMessage()));
		}
	}
	
	/**
	*	Complète le paramètre JSON à poster pour récupérer une adresse
	*	@param L'adresse ou le bout d'adresse posté par l'utilisateur
	*	@return Le paramètre complété sous forme JSON
	**/
	private function completeAddressRequest($address)
	{
		return '[1,"geocoding",1,0,{"1":{"rec":{"1":{"str":"0.1.5"},"2":{"str":"portail user"},"3":{"str":"SNCF"},"4":{"str":"SNCF"},"5":{"str":"SNCF"},"6":{"i32":3}}},"2":{"lst":["rec",1,{"1":{"str":"'.$address.'"},"2":{"str":"'.$address.'"},"4":{"rec":{"1":{"rec":{"1":{"dbl":5.021},"2":{"dbl":47.595}}},"2":{"rec":{"1":{"dbl":7.294},"2":{"dbl":49.788}}}}}}]}}]';
	}
	
	/**
	*	Complète le paramètre JSON à poster pour récupérer un lieu/arrêt 
	*	@param L'adresse ou le bout d'adresse posté par l'utilisateur
	*	@return Le paramètre complété sous forme JSON
	**/
	private function completePlaceRequest($address)
	{
		return '[1,"getPlacemarksFreeSearchGeo",1,0,{"1":{"rec":{"1":{"str":"0.1.5"},"2":{"str":"portail user"},"3":{"str":"SNCF"},"4":{"str":"SNCF"},"5":{"str":"SNCF"},"6":{"i32":3}}},"2":{"rec":{"1":{"rec":{"1":{"dbl":5.021},"2":{"dbl":47.595}}},"2":{"rec":{"1":{"dbl":7.294},"2":{"dbl":49.788}}}}},"3":{"str":"'.$address.'"},"4":{"lst":["i32",123,305,306,307,309,311,303,304,302,300,301,102,103,106,114,121,123,124,125,126,128,129,130,131,138,143,146,147,151,155,157,158,159,160,161,162,163,164,165,166,167,168,169,172,175,178,179,183,185,187,188,194,195,195,196,197,198,199,201,202,203,204,205,206,207,208,209,210,212,211,213,214,215,216,217,218,219,220,221,222,223,224,225,239,226,227,228,229,230,231,232,233,234,235,236,237,238,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,257,259,260,265,266,267,269,270,273,275,276]}}]';
	}
	
	/**
	*	Complète le paramètre JSON à poster pour récupérer des trajets
	*	@param La chaine json des paramètres postés par l'utilisateur
	*	@return Le paramètre complété sous forme JSON
	**/
	private function completeRoutesRequest($data)
	{

		
		//rebuild
		$startId = $data->start;
		$endId = $data->end;
		$dao = DAOFactory::createAddressGnyDAO();
		
		$this->start=$dao->findById($startId);
		$this->end=$dao->findById($endId);
		
		$this->hour=$data->hour;
		$this->minute=$data->minute;
		$this->year=$data->year;
		$this->month=$data->month;
		$this->day=$data->day;
		$this->transportMode=$data->transport;
		
		switch($this->transportMode)
		{
			case "Bus" : $transportInts='2,14,11'; break;
			case "Voiture" : $transportInts='1,3'; break;
			case "A pied" : $transportInts='1,2'; break;
			default: $transportInts='2,14,11'; break;
		}

		$return='[1,"getPropositions",1,0,{"1":{"rec":{"1":{"str":"0.1.5"},"2":{"str":"portail user"},"3":{"str":"SNCF"},"4":{"str":"SNCF"},"5":{"str":"SNCF"},"6":{"i32":3}}},'
		.'"2":{"rec":{"1":{"str":"'.$this->start->getStr().'"},"3":{"lst":["str",1,"'.$this->start->getLabel().'"]},"5":{"rec":{"1":{"lst":["rec",1,{"1":{"dbl":'.$this->start->getLat().'},"2":{"dbl":'.$this->start->getLon().'}}]}}},"6":{"lst":["i32",1,14]},"7":'.$this->completeMap($this->start->getMap()).'}},'
		.'"3":{"rec":{"1":{"str":"'.$this->end->getStr().'"},"3":{"lst":["str",1,"'.$this->end->getLabel().'"]},"5":{"rec":{"1":{"lst":["rec",1,{"1":{"dbl":'.$this->end->getLat().'},"2":{"dbl":'.$this->end->getLon().'}}]}}},"6":{"lst":["i32",1,14]},"7":'.$this->completeMap($this->end->getMap()).'}}'
		.',"4":{"rec":{"1":{"i16":'.$this->year.'},"2":{"i32":'.$this->month.'},"3":{"i16":'.$this->day.'}}},"5":{"rec":{"1":{"i16":'.$this->hour.'},"2":{"i16":'.$this->minute.'},"3":{"i16":0}}},"6":{"lst":["i32",'.$transportInts.']},"7":{"rec":{}}}]';
		//restant : transport 
		return $return;
	}
	
	private function completeMap($params)
	{
		return '{"map":["str","str",'.count((array)$params).','.json_encode($params,JSON_UNESCAPED_SLASHES | JSON_UNESCAPED_UNICODE).']}';
	}

	
	/**
	*	Filtre la réponse de retour d'une recherche d'adresse pour extraire les paramètres utiles et les stocket dans la variable d'instance result
	*	@param La réponse JSON de la requête POST
	**/
	private function createAddress($address)
	{
		$address=json_decode($address);
		$this->result['address']['nb']=$address[4]->{0}->map[3]->{$this->address}[1];
		for($i=0;$i<$this->result['address']['nb'];$i++){
			
			$label=$address[4]->{0}->map[3]->{$this->address}[2+$i]->{3}->lst[2];
			$str=$address[4]->{0}->map[3]->{$this->address}[2+$i]->{1}->str;
			$map=(array)$address[4]->{0}->map[3]->{$this->address}[2+$i]->{7}->map[3];
			$lat=$address[4]->{0}->map[3]->{$this->address}[2+$i]->{5}->rec->{1}->lst[2]->{1}->dbl;
			$lon=$address[4]->{0}->map[3]->{$this->address}[2+$i]->{5}->rec->{1}->lst[2]->{2}->dbl;
			$addr=new AddressGNy($label,$str,$lat,$lon,$map);
			$this->result['address'][$i]=$addr;
			
			$dao = DAOFactory::createAddressGnyDAO();
			$id=$dao->persist($addr);
			$addr->setId($id);
		}
	}
	
	/**
	*	Filtre la réponse de retour d'une recherche de lieu/arrêt de transport pour extraire les paramètres utiles et les stocket dans la variable d'instance result
	*	@param La réponse JSON de la requête POST
	**/
	private function createPlaces($address)
	{
		$address=json_decode($address);
		$this->result['place']['nb']=$address[4]->{0}->lst[1];
		$k=0;
		for($i=0;$i<$this->result['place']['nb'];$i++){
			$label=$address[4]->{0}->lst[2+$i]->{3}->lst[2];
			$str=$address[4]->{0}->lst[2+$i]->{1}->str;
			$map=(array)$address[4]->{0}->lst[2+$i]->{7}->map[3];
			$lat=$address[4]->{0}->lst[2+$i]->{5}->rec->{1}->lst[2]->{1}->dbl;
			$lon=$address[4]->{0}->lst[2+$i]->{5}->rec->{1}->lst[2]->{2}->dbl;
			$addr=new AddressGNy($label,$str,$lat,$lon,$map);
			if(array_key_exists($addr->getMap()["ctype"],AddressGny::$SUPPORTED_CTYPES)){
				$this->result['place'][$k++]=$addr;
				$dao = DAOFactory::createAddressGnyDAO();
				$id=$dao->persist($addr);
				$addr->setId($id);
			}
		}
		$this->result['place']['nb']=$k;
	}
	
	/**
	*	Filtre la réponse de retour d'une recherche de trajet pour extraire les paramètres utiles et les stocket dans la variable d'instance result
	*	@param La réponse JSON de la requête POST
	**/
	private function createRoutes($routes)
	{
		$routes=json_decode($routes);
		if($routes===false){
			throw new ApiException("Echec du décodage JSON");
		}
		$this->result['route']['nb']=$routes[4]->{0}->rec->{2}->lst[1];
		
		for($i=0;$i<$this->result['route']['nb'];$i++)
		{
			$distance=$routes[4]->{0}->rec->{2}->lst[2+$i]->{4}->rec->{7}->i32;
			$duration=$routes[4]->{0}->rec->{2}->lst[2+$i]->{4}->rec->{8}->i32;
			
			$this->result['route'][$i] = new Route($this->start,$this->end,$this->transportMode,new DateTime($this->year."-".$this->month."-".$this->day." ".$this->hour.":".$this->minute.":00")); //
			$this->result['route'][$i]->setTransportsNb($routes[4]->{0}->rec->{2}->lst[2+$i]->{6}->lst[1]);
			$this->result['route'][$i]->setDistance($distance);
			$this->result['route'][$i]->setDuration($duration);
			$routeDAO = DAOFactory::createRouteDAO();
			for($t=0;$t<$this->result['route'][$i]->getTransportsNb();$t++)
			{
				$description = $routes[4]->{0}->rec->{2}->lst[2+$i]->{6}->lst[2+$t]->{2}->str;
				$distance = $routes[4]->{0}->rec->{2}->lst[2+$i]->{6}->lst[2+$t]->{6}->i32;
				$duration = $routes[4]->{0}->rec->{2}->lst[2+$i]->{6}->lst[2+$t]->{7}->i32;
				$type = $routes[4]->{0}->rec->{2}->lst[2+$i]->{6}->lst[2+$t]->{1}->i32;
				$transportToAdd = new Transport($description);
				$transportToAdd->setRoute($this->result['route'][$i]);
				
				$stepsNb=$routes[4]->{0}->rec->{2}->lst[2+$i]->{6}->lst[2+$t]->{3}->lst[1];
				$pointsNb=$routes[4]->{0}->rec->{2}->lst[2+$i]->{6}->lst[2+$t]->{8}->rec->{1}->lst[1];
				$transportToAdd->setStepsNb($stepsNb);
				$transportToAdd->setPointsNb($pointsNb);
				$transportToAdd->setDistance($distance);
				$transportToAdd->setDuration($duration);
				switch($type){
					case 2 : $type=0; break;
					case 3 : $type=1; break;
					case 11 : $type=2; break;
					case 14 : $type=2; break;
					default : break;
				}
				$transportToAdd->setType($type);
				for($k=0;$k<$stepsNb;$k++)
				{
					
						$lon=		$routes[4]->{0}->rec->{2}->lst[2+$i]->{6}->lst[2+$t]->{3}->lst[2+$k]->{1}->lst[2]->{5}->rec->{1}->lst[2]->{1}->dbl;
						$lat=		$routes[4]->{0}->rec->{2}->lst[2+$i]->{6}->lst[2+$t]->{3}->lst[2+$k]->{1}->lst[2]->{5}->rec->{1}->lst[2]->{2}->dbl;
						$action=	$routes[4]->{0}->rec->{2}->lst[2+$i]->{6}->lst[2+$t]->{3}->lst[2+$k]->{5}->str;
						$transportToAdd->addStep(new Step($action,$lat,$lon,$transportToAdd));
				}
				
				for($k=0;$k<$pointsNb;$k++)
				{
						$lon=	$routes[4]->{0}->rec->{2}->lst[2+$i]->{6}->lst[2+$t]->{8}->rec->{1}->lst[2+$k]->{1}->dbl;
						$lat=	$routes[4]->{0}->rec->{2}->lst[2+$i]->{6}->lst[2+$t]->{8}->rec->{1}->lst[2+$k]->{2}->dbl;
						$xepsg=	$routes[4]->{0}->rec->{2}->lst[2+$i]->{6}->lst[2+$t]->{9}->rec->{1}->lst[2+$k]->{1}->dbl;
						$yepsg=	$routes[4]->{0}->rec->{2}->lst[2+$i]->{6}->lst[2+$t]->{9}->rec->{1}->lst[2+$k]->{1}->dbl;
						
						$transportToAdd->addPoint(new Point($lat,$lon,$xepsg,$yepsg,$transportToAdd));
				}
				
				$this->result['route'][$i]->addTransport($transportToAdd);
			}
			$routeDAO->create($this->result['route'][$i]);
		}
		
		
	}
}
