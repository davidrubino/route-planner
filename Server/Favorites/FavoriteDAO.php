<?php
	require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAO.php');
	require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOException.php');
	require_once($_SERVER["DOCUMENT_ROOT"].'/Favorites/Favorite.php');
	require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOFactory.php');
	
	class FavoriteDAO extends DAO
	{
		public function __construct(){
			parent::__construct();
		}
		
		public function findById($id){
			$query = $this->connection->prepare("SELECT * FROM favorite WHERE id=:id");
			$query->execute(array('id'=>$id));
			if($query->rowCount()==0)
				throw new DAOException("Favoris introuvable");
			else
			{
				$data = $query->fetch(PDO::FETCH_OBJ);
				$addressDAO = DAOFactory::createAddressDAO();
				$memberDAO = DAOFactory::createMemberDAO();
				$start = $addressDAO->findById($data->start_id);
				$end = $addressDAO->findById($data->end_id);
				$member = $memberDAO->findById($data->login);
				return new Favorite($member,$start,$end,$data->transportType,$data->id);
			}	
		}
		
		public function create($favorite){
			$query = $this->connection->prepare("INSERT INTO favorite (id,member_id,start_id,end_id,transportType,date) VALUES ('',:member_id,:start_id,:end_id,:transportType,:date)");
			if(!$query->execute(array('member_id'=>$favorite->getMember()->getLogin(),'start_id'=>$favorite->getStart()->getId(),'end_id'=>$favorite->getEnd()->getId(),'transportType'=>$favorite->getTransportType(),'date'=>date("Y-m-d H:i:s"))))
				throw new DAOException("Une erreur est survenue l'ajout du favoris");
		}
		
			
		public function addHistory($route_id){
				$query = $this->connection->prepare("INSERT INTO history (member_id,route_id,date) VALUES (:member_id,:route_id,:date)");
				if(!$query->execute(array('member_id'=>$_SESSION['login'],'route_id'=>$route_id,'date'=>date("Y-m-d H:i:s"))))
					throw new DAOException("Une erreur est survenue l'ajout de l'historique");
		}
		
		public function clearHistory(){
				$query = $this->connection->prepare("DELETE FROM history WHERE member_id=:member_id");
				if(!$query->execute(array('member_id'=>$_SESSION['login'])))
					throw new DAOException("Une erreur est survenue lors de la suppression de l'historique");
		}
		
		public function delete($favorite){
			$query = $this->connection->prepare("DELETE FROM favorite WHERE member_id=:member_id AND start_id=:start_id AND end_id=:end_id AND transportType=:transportType");
			if(!$query->execute(array('member_id'=>$favorite->getMember()->getLogin(),'start_id'=>$favorite->getStart()->getId(),'end_id'=>$favorite->getEnd()->getId(),'transportType'=>$favorite->getTransportType())))
				throw new DAOException("Une erreur est survenue lors de la suppression du favoris");
		}
		
	}
?>