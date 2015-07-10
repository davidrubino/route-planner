<?php
	require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAO.php');
	require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOException.php');
	class AddressGNyDAO extends DAO
	{
	
		public function __construct(){
			parent::__construct();
		}
		
		public function findOne($label,$lat,$lon){
			$query = $this->connection->prepare("SELECT * FROM addressgny NATURAL JOIN address WHERE label=:label, lat=:lat, lon=:lon");
			$query->execute(array('label'=>$label,'lat'=>$lat,'lon'=>$lon));
			return $this->buildAddress($query);
		}
		
		public function findById($id){
			$query = $this->connection->prepare("SELECT * FROM addressgny NATURAL JOIN address WHERE id_address=:id");
			$query->execute(array('id'=>$id));
			return $this->buildAddress($query);
		}
		
		private function buildAddress($query){
			if($query->rowCount()==0)
				throw new DAOException("Adresse introuvable");
			else
			{
				$data = $query->fetch(PDO::FETCH_OBJ);
				$map = unserialize($data->map);
				return new AddressGNy($data->label,$data->str,$data->lat,$data->lon,$map,$data->id_address);
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
					
					$query = $this->connection->prepare("SELECT * FROM AddressGNy WHERE id_address=:id");
					$query->execute(array('id'=>$id));
					if($query->rowCount()==0){
						$query = $this->connection->prepare("INSERT INTO addressgny (`id_address`,`str`,`map`) VALUES(:id_address, :str, :map)");
						$query->execute(array('id_address'=>$id,'str'=>$address->getStr(),'map'=>serialize($address->getMap())));
					}
					else{
						$data = $query->fetch(PDO::FETCH_OBJ);
						if($data->str!=$address->getStr() || $data->map!=$address->getMap()){
							$query = $this->connection->prepare("UPDATE addressgny SET `str`=:str,`map`=:map WHERE id_address=:id_address");
							$query->execute(array('id_address'=>$id,'str'=>$address->getStr(),'map'=>serialize($address->getMap())));
						}
					}
					
					$this->connection->commit();
				
			}catch(PDOException $e){
				$this->connection->rollback();
			}
			return $id;
		}
	}

?>