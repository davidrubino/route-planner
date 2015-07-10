<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAO.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOException.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Route/Route.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Route/Transport.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Route/Step.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Route/Point.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOFactory.php');

class RouteDAO extends DAO
{
	public function __construct(){
		parent::__construct();
	}

	public function findById($id){
		$rquery = $this->connection->prepare("SELECT * FROM route WHERE id=:id");
			$rquery->execute(array('id'=>$id));
			if($rquery->rowCount()==0)
				throw new DAOException("Trajet introuvable");
			else
			{
				$rdata = $rquery->fetch(PDO::FETCH_OBJ);
				$addressDAO = DAOFactory::createAddressDAO();
				$start=$addressDAO->findById($rdata->start_id);
				$end = $addressDAO->findById($rdata->end_id);
				$route = new Route($start,$end,$rdata->transportType,new DateTime($rdata->{'date'}));
				$route->setDuration($rdata->duration);
				$route->setDistance($rdata->distance);
				$route->setId($id);
				
				$tquery = $this->connection->prepare("SELECT * FROM transport WHERE route_id=:route_id");
				$tquery->execute(array('route_id'=>$rdata->id));
				while($tdata = $tquery->fetch(PDO::FETCH_OBJ)){
					$transport = new Transport($tdata->description);
					$transport->setDuration($tdata->duration);
					$transport->setDistance($tdata->distance);
					$transport->setType($tdata->type);
					$squery = $this->connection->prepare("SELECT * FROM step WHERE transport_id=:transport_id");
					$squery->execute(array('transport_id'=>$tdata->id));
					while($sdata = $squery->fetch(PDO::FETCH_OBJ)){
						$step = new Step($sdata->action,$sdata->lat,$sdata->lon,$transport);
						$transport->addStep($step);
					}
					
					$pquery = $this->connection->prepare("SELECT * FROM point WHERE transport_id=:transport_id");
					$pquery->execute(array('transport_id'=>$tdata->id));
					while($pdata = $pquery->fetch(PDO::FETCH_OBJ)){
						$point = new Point($pdata->lat,$pdata->lon,$transport);
						$transport->addPoint($point);
					}
					
					$route->addTransport($transport);
				}
				return $route;
			}	
	}
	
	public function create($route){
		try{
			$this->connection->beginTransaction();
			$rquery = $this->connection->prepare("INSERT INTO route (id,start_id,end_id,transportType,distance,duration,date) VALUES ('',:start_id,:end_id,:transportType,:distance,:duration,:date)");
			$rquery->execute(array('start_id'=>$route->getStart()->getId(),'end_id'=>$route->getEnd()->getId(),'transportType'=>$route->getTransportType(),
				'distance'=>$route->getDistance(),'duration'=>$route->getDuration(),'date'=>$route->getDateTime()->format('Y-m-d H:i:s')));
			
			$route_id = $this->connection->lastInsertId();
			$route->setId($route_id);
			foreach($route->getTransports() as $transport){
				$tquery = $this->connection->prepare("INSERT INTO transport (id,route_id,description,distance,duration,type) VALUES ('',:route_id,:description,:distance,:duration,:type)");
				$tquery->execute(array('route_id'=>$route_id,'description'=>$transport->getDescription(),'distance'=>$transport->getDistance(),
					'duration'=>$transport->getDuration(),'type'=>$transport->getType()));
				$transport_id=$this->connection->lastInsertId();
				foreach($transport->getSteps() as $step){
					$squery = $this->connection->prepare("INSERT INTO step (id,transport_id,lat,lon,action) VALUES ('',:transport_id,:lat,:lon,:action)");
					$squery->execute(array('transport_id'=>$transport_id,'lat'=>$step->getLat(),'lon'=>$step->getLon(),
						'action'=>$step->getAction()));
				}
				
				foreach($transport->getPoints() as $point){
					$pquery = $this->connection->prepare("INSERT INTO point (id,transport_id,lat,lon) VALUES ('',:transport_id,:lat,:lon)");
					$pquery->execute(array('transport_id'=>$transport_id,'lat'=>$point->getLat(),'lon'=>$point->getLon()));
				}
			}			
			
			$this->connection->commit();
			
			
		}catch(PDOException $e){
			$this->connection->rollback();
		}
		
	}
}
