<?php
	require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAO.php');
	require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOException.php');
	require_once($_SERVER["DOCUMENT_ROOT"].'/Address/Address.php');
	require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOFactory.php');
	class AddressDAO extends DAO
	{
	
		public function __construct(){
			parent::__construct();
		}
		
		public function findOne($label,$lat,$lon){
			$query = $this->connection->prepare("SELECT * FROM address WHERE label=:label, lat=:lat, lon=:lon");
			$query->execute(array('label'=>$label,'lat'=>$lat,'lon'=>$lon));
			return $this->buildAddress($query);
		}
		
		public function findById($id){
			$query = $this->connection->prepare("SELECT * FROM address WHERE id_address=:id");
			$query->execute(array('id'=>$id));
			return $this->buildAddress($query);
		}
		
		private function buildAddress($query){
			if($query->rowCount()==0)
				throw new DAOException("Adresse introuvable");
			else
			{
				$data = $query->fetch(PDO::FETCH_OBJ);
				return new Address($data->id_address,$data->label,$data->lat,$data->lon);
			}	
		}
		
		public function persist($address){
			$this->connection->beginTransaction();
			$id=false;
			try{
					$query = $this->connection->prepare("SELECT id_address FROM address WHERE label=:label AND lat=:lat AND lon=:lon");
					$query->execute(array('label'=>$address->getLabel(),'lat'=>$address->getLat(),'lon'=>$address->getLon()));
					if($query->rowCount()==0){
						$query = $this->connection->prepare("INSERT INTO address (`id_address`,`label`,`lat`,`lon`) VALUES('', :label, :lat, :lon)");
						$query->execute(array('label'=>$address->getLabel(),'lat'=>$address->getLat(),'lon'=>$address->getLon()));
						$id=$this->connection->lastInsertId();
					}
					else{
						$data = $query->fetch(PDO::FETCH_OBJ);
						$id=$data->id_address;
					}
					$this->connection->commit();
				
			}catch(PDOException $e){
				$this->connection->rollback();
			}
			return $id;
		}
		
		public function findStopsAsAddress($name){
			$dao = DAOFactory::createStopDAO();
			$stops = $dao->findByName($name);
			$address=array();
			foreach($stops as $stop){
				$a = new Address(null,$stop->getStop_name(),$stop->getStop_lat(),$stop->getStop_lon(),"Arrêts");
				$a->setId($this->persist($a));
				$query = $this->connection->prepare('SELECT route_id, route_short_name, route_long_name,direction_id FROM `stops_route` 
				WHERE stop_id=:stopId AND route_id>=100');
				$daor=DAOFactory::createStanRouteDAO();
				$query->execute(array('stopId'=>$stop->getStop_Id()));
				$i=0;
				$infos="";
				while($data = $query->fetch(PDO::FETCH_OBJ)){
					$route = $daor->findById($data->route_id);
					if($i>0)
						$infos.=';';
					$dir = $data->direction_id;
					if($dir==0)
						$infos.='Ligne '.$data->route_short_name.' vers '.$route->getDirection_0();
					else
						$infos.='Ligne '.$data->route_short_name.' vers '.$route->getDirection_1();
					$i++;
				}
				$a->setInfos($infos);
				$address[] = $a;
			}
			return $address;
		}
	}

?>