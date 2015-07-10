
<?php

require_once($_SERVER["DOCUMENT_ROOT"].'/APIController/ApiException.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/APIController/AbstractApiController.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Route/Point.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Route/Step.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Route/Transport.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Route/Route.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Address/Address.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/Helper.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOFactory.php');

class OtpController extends AbstractApiController
{
	const ADDRESS_URL = "http://maps.googleapis.com/maps/api/geocode/json?region=fr&sensor=false&bounds=48.5833,6.1|48.7833,6.3&"; //URL pour récupérer adresse
	const ROUTE_URL = "http://127.0.0.1:8085/opentripplanner-api-webapp/ws/plan?";  //URL pour récupérer le trajet entre un départ et une arrivée
	private $json;
	private $result=array();
	private $address;
	private $start;
	private $end;
	private $transportMode;
	
	protected function getReferer(){
		return '';
	}
	
	public function render(){
		header('Content-type: application/json');
		echo $this->json;		
	}
	
	public function getResponse(){
		return $this->json;
	}
	
	public function getAddress(){
		$post=$this->getRequestPost();
		if(!isset($post['address']) || empty($post['address'])) //si l'adresse passée est vide ou inexistante => erreur
			$this->result=$this->errorParam;
		else{
			$this->address=$post['address'];
			$addressResponse = $this->get(OtpController::ADDRESS_URL.'address='.urlencode($this->address));
			$this->createAddress($addressResponse);
			$this->createPlace($this->address);
			$this->json='{"address":'.Helper::encodeArrayOfObject($this->result['address']).',"place":'.Helper::encodeArrayOfObject($this->result['place']).'}';
		}
	}
	
	private function createAddress($json){
		$object = json_decode($json);
		$this->result['address']=array();
		if($object->status=='OK'){
			$addrs=array();
			foreach($object->results as $i=>$address){
				if($this->isValid($address)){
					$addr = new Address(null,$address->formatted_address,$address->geometry->location->lat,$address->geometry->location->lng,'Adresse');
					$addrs[] = $addr;
					$dao = DAOFactory::createAddressDAO();
					$id=$dao->persist($addr);
					$addr->setId($id);
				}
			}
			$this->result['address']['nb']=count($addrs);
			for($i=0;$i<count($addrs);$i++)
				$this->result['address'][$i]=$addrs[$i];
		}
	}
	
	private function isValid($address){
		return 	$address->geometry->location->lat>48.5833 && $address->geometry->location->lat<48.7833 && 
				$address->geometry->location->lng>6.1 && $address->geometry->location->lng<6.3;
	}
	
	private function createPlace($address){
		$this->result['place']=array();
		$this->result['place']['nb']=0;
		try{
			if(strlen($address)>2){
				$dao = DAOFactory::createAddressDAO();
				$stops = $dao->findStopsAsAddress($address);
				$this->result['place']['nb']=count($stops);
				for($i=0;$i<count($stops);$i++)
					$this->result['place'][$i] = $stops[$i];
			}
		}catch(DAOException $e){

		}
	}
	
	public function getRoutes(){
		try{
			$post=$this->getRequestContent();
			$post=urldecode($post);
			$data=json_decode($post);
			if(isset($data->start) && isset($data->end)){
				$completedRoute = $this->completeRoutesRequest($data);
				//echo OtpController::ROUTE_URL.$completedRoute;
				$response = $this->get(OtpController::ROUTE_URL.$completedRoute);
				$this->createRoutes($response);
				$this->json='{"route":'.Helper::encodeArrayOfObject($this->result['route']).'}';
			}
		}catch(DAOException $e){
			$this->json=json_encode(array('erreur'=>$e->getMessage()));
		}
	}
	
	private function createRoutes($json){
		$routes=json_decode($json);
		if($routes===false){
			throw new ApiException("Echec du décodage JSON");
		}else{
			$routeDAO = DAOFactory::createRouteDAO();
			$this->result['route']=array();
			$this->result['route']['nb']=count($routes->plan->itineraries);
			foreach($routes->plan->itineraries as $i=>$route){
				$rou = new Route($this->start,$this->end,$this->transportMode,new DateTime($this->year."-".$this->month."-".$this->day." ".$this->hour.":".$this->minute.":00"));
				$rou->setDuration(round($route->duration/1000));
				$rou->setDistance(0);
				$rou->setTransportsNb(count($route->legs));
				
				foreach($route->legs as $transport){
					switch($transport->mode){
						case 'WALK' : $transportStr = 'à pied'; $type=0; break;
						case 'BUS' : $transportStr = 'en bus'; $type=2; break;
						case 'TRAM' : $transportStr = 'en tramway'; $type=2; break;
						default : $transportStr = ""; $type=-1; break;
					}
					if(preg_match('#way\s\d+\sfrom#Ui',$transport->to->name))
						$transport->to->name='inconnu';
					if(preg_match('#way\s\d+\sfrom#Ui',$transport->from->name))
						$transport->from->name='inconnu';
									
					if($type==2)
						$description = "Parcours ".$transportStr." de l'arrêt ".$transport->from->name." à l'arrêt ".$transport->to->name." de la ligne ".$transport->routeShortName;
					else
						$description = "Parcours ".$transportStr." de ".$transport->from->name." à ".$transport->to->name;
						
					$t = new Transport($description);
					$t->setRoute($rou);
					if(isset($transport->steps)){
						$t->setStepsNb(count($transport->steps));
						$t->setPointsNb(count($transport->steps));
					}
					
					$t->setType($type);
					$t->setDuration(round(($transport->endTime-$transport->startTime)/1000));
					$t->setDistance(round($transport->distance));
					
					if($type==2){
						$t->addStep(new Step("Prenez la ligne ".$transport->routeShortName." et descendez à l'arrêt ".$transport->to->name,$transport->to->lat,$transport->to->lon,$t));
					}
					
					if(isset($transport->steps)){
						$directions = array(
							"CONTINUE"=>"Continuez",
							"RIGHT"=>"Prenez à droite",
							"LEFT"=>"Prenez à gauche",
							"SLIGHTLY_RIGHT"=>"Prenez légèrement à droite",
							"SLIGHTLY_LEFT"=>"Prenez légèrement à gauche",
							"HARD_LEFT"=>"Prenez à gauche",
							"HARD_RIGHT"=>"Prenez à droite",
							"CIRCLE_CLOCKWISE"=>"Prenez le rond point",
							"CIRCLE_COUNTERCLOCKWISE"=>"Prenez le rond point"
						);
						foreach($transport->steps as $step){
								$exit="";
								if(isset($step->exit) && !empty($step->exit))
									$exit = ", sortez à la sortie n°".$step->exit;
								if(preg_match('#way\s\d+\sfrom#Ui',$step->streetName))
									$step->streetName='inconnu';
								if(isset($directions[$step->relativeDirection]))
									$action=$directions[$step->relativeDirection].$exit." sur ".$step->streetName;
								else
									$action=$step->relativeDirection.$exit." sur ".$step->streetName;

							$s = new Step($action,$step->lat,$step->lon,$t);
							$p = new Point($step->lat,$step->lon,$t);
							$t->addStep($s);
							$t->addPoint($p);
						}
					}
					$rou->addTransport($t);
				}
				
				$this->result['route'][]=$rou;
				$routeDAO->create($this->result['route'][$i]);
			}
		}
	}
	
	private function completeRoutesRequest($data){
		$hour = $data->hour%12;
		$minute =  $data->minute;
		$ampm = $data->hour<12 ? "am" : "pm";
		$addressDAO = DAOFactory::createAddressDAO();
		$this->start = $addressDAO->findById($data->start);
		$this->end = $addressDAO->findById($data->end);
		
		$this->hour=$data->hour;
		$this->minute=$data->minute;
		$this->year=$data->year;
		$this->month=$data->month;
		$this->day=$data->day;
		$this->transportMode=$data->transport;
		switch($this->transportMode){
			case 'Bus' : $transportMode = 'TRANSIT%2CWALK'; break;
			case 'A pied' : $transportMode = 'WALK'; break;
			default : $transportMode = 'TRANSIT%2CWALK'; break;
		}
		$param = 'locale=fr_FR&arriveBy=false&time='.$hour.'%3A'.$minute.$ampm.'&ui_date='.$data->day.'%2F'.$data->month.'%2F'.$data->year.'&mode='.$transportMode.'&optimize=QUICK&maxWalkDistance=1000&walkSpeed=2&date='.$data->year.'-'.$data->month.'-'.$data->day.'&toPlace='.$this->end->getLat().'%2C'.$this->end->getLon().'&fromPlace='.$this->start->getLat().'%2C'.$this->start->getLon();
		return $param;
	}
}

?>