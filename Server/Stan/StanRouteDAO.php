<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAO.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOException.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Stan/StanRoute.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOFactory.php');

class StanRouteDAO extends DAO
{
	public function __construct(){
		parent::__construct();
	}

	public function findById($id,$direction=null){
		$query=$this->connection->prepare("SELECT * FROM opendata_routes WHERE route_id=:id");
		$query->execute(array('id'=>$id));
		if($query->rowCount()==0)
			throw new DAOException("Ligne introuvable");
		else
		{
			$data = $query->fetch(PDO::FETCH_OBJ);
			$route = new StanRoute($data->route_id,$data->route_short_name,$data->route_long_name,$data->route_type);
			
			if($direction!=null){
				$query1=$this->connection->prepare("SELECT DISTINCT (stop_id) FROM `opendata_stop_times` NATURAL JOIN `opendata_trips` WHERE route_id=:route_id AND direction_id=:direction ORDER BY stop_sequence ASC");
				
				$stopDAO = DAOFactory::createStopDAO();
				
				$query1->execute(array('route_id'=>$id,'direction'=>$direction));
				$stops=array();
				while($data = $query1->fetch(PDO::FETCH_OBJ)){
					$stops[]=$stopDAO->findById($data->stop_id);
				}
				$route->setStops($stops);
			}
			return $route;
		}
	}
}

?>