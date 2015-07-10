<?php

require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAO.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOException.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Members/Member.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Favorites/Favorite.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOFactory.php');

class MemberDAO extends DAO 
{
	private $salt = '@TN';
	public function __construct(){
		parent::__construct();
	}
	
	public function findById($id){
		$query = $this->connection->prepare("SELECT login FROM members WHERE login=:login AND password=:password");
		if(!$query->execute(array('login'=>$id['login'],'password'=>sha1($id['password'].$this->salt))))
			throw new DAOException("Une erreur du serveur est survenue pendant la connexion, réessayez ultérieurement.");
		else
			return $this->buildMember($query);
	}
	
	private function buildMember($query){
			if($query->rowCount()==0)
				throw new DAOException("Login ou mot de passe incorrect");
			else
			{
				$data = $query->fetch(PDO::FETCH_OBJ);
				$fquery = $this->connection->prepare("SELECT * FROM favorite WHERE member_id=:member_id ORDER BY date DESC");
				$fquery->execute(array('member_id'=>$data->login));
				$hquery = $this->connection->prepare("SELECT * FROM history WHERE member_id=:member_id ORDER BY date DESC");
				$hquery->execute(array('member_id'=>$data->login));
				$member = new Member($data->login);
				$addressDAO = DAOFactory::createAddressDAO();
				$routeDAO = DAOFactory::createRouteDAO();
				$favorites = array();
				$history = array();
				while($fdata = $fquery->fetch(PDO::FETCH_OBJ)){
					$transportType = $fdata->transportType;
					$start=$addressDAO->findById($fdata->start_id);
					$end=$addressDAO->findById($fdata->end_id);
					$favorites[]= new Favorite($member,$start,$end,$transportType);
				}
				while($hdata = $hquery->fetch(PDO::FETCH_OBJ)){
					$history[]=$routeDAO->findById($hdata->route_id);
				}
				$member->setFavorites($favorites);
				$member->setHistory($history);
				return $member;
			}	
		}
		
	public function create($param){
		$login = $param['login'];
		$password=$param['password'];
		$mail = $param['mail'];
		$name = $param['name'];
		$firstName = $param['firstName'];
		$query = $this->connection->prepare("SELECT login FROM members WHERE login=:login");
		$query->execute(array('login'=>$login));
		if($query->rowCount()==0){
			$query = $this->connection->prepare("INSERT INTO members (login,password,mail,name,firstName) VALUES (:login,:password,:mail,:name,:firstName)");
			if(!$query->execute(array('login'=>$login,'password'=>sha1($password.$this->salt),'mail'=>$mail,'name'=>$name,'firstName'=>$firstName)))
				throw new DAOException("Une erreur du serveur est survenue pendant l'inscription, réessayez ultérieurement.");
		}else
			throw new DAOException("Pseudo indisponible");
	}
}
?>