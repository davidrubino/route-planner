<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAO.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOException.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Stan/Stop.php');

class StopDAO extends DAO
{
	public function __construct(){
		parent::__construct();
	}

	public function findById($stop_id,$direction=0,$route_id=null){
		$query=$this->connection->prepare("SELECT * FROM opendata_stops WHERE stop_id=:id");
		$query->execute(array('id'=>$stop_id));
		if($query->rowCount()==0)
			throw new DAOException("Arrêt introuvable");
		else
		{
			$data = $query->fetch(PDO::FETCH_OBJ);
			$stop = new Stop($data->stop_id,$data->stop_code,$data->stop_name,$data->stop_lat,$data->stop_lon);
			if($route_id!=null){
				$query1=$this->connection->prepare("SELECT DISTINCT(DATE_FORMAT(departure_time, '%H:%i')) AS dtime FROM `opendata_stop_times` NATURAL JOIN `opendata_trips`  WHERE stop_id=:stop_id AND route_id=:route_id AND direction_id=:direction ORDER BY departure_time");
				$query1->execute(array('stop_id'=>$stop_id,'route_id'=>$route_id,'direction'=>$direction));
				
				$times=array();
				while($data = $query1->fetch(PDO::FETCH_OBJ)){
					$times[]=$data->dtime;
				}
				$stop->setTimes($times);
			}
			return $stop;
		}
	}
	
	public function findByName($stopName){
		$query=$this->connection->prepare("SELECT * FROM opendata_stops WHERE stop_name LIKE :name ORDER BY stop_name");
		$query->execute(array('name'=>'%'.$stopName.'%'));
		if($query->rowCount()==0)
			throw new DAOException("Arrêt introuvable");
		else
		{
			$stops=array();
			while($data = $query->fetch(PDO::FETCH_OBJ)){
				$stop = new Stop($data->stop_id,$data->stop_code,$data->stop_name,$data->stop_lat,$data->stop_lon);
				$stops[]=$stop;
			}
			return $stops;
		}
	}
}

?>