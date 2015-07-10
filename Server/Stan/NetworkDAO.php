<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAO.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOException.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Stan/Network.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOFactory.php');

class NetworkDAO extends DAO
{
	public function __construct(){
		parent::__construct();
	}

	public function findById($id=null){
		$query=$this->connection->prepare("SELECT route_id FROM opendata_routes WHERE route_id>=100");
		$query->execute();
		if($query->rowCount()==0)
			throw new DAOException("Réseau introuvable");
		else
		{
			$network = new Network();
			$routeDAO = DAOFactory::createStanRouteDAO();
			$lines = array();
			while($data = $query->fetch(PDO::FETCH_OBJ)){
				$lines[]=$routeDAO->findById($data->route_id);
			}
			$network->setLines($lines);
			return $network;
		}
	}
}

?>