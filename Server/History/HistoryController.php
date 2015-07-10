<?php

require_once($_SERVER["DOCUMENT_ROOT"].'/Controller.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOFactory.php');

class HistoryController extends Controller{
	private $response;
	private $dao;
	
	public function render(){
		header('Content-type: application/json');
		echo $this->response;		
	}
	
	public function __construct(){
		parent::__construct();
	}
	
	public function add(){
		$post = $this->getRequestPost();
		if(isset($post['route_id']) && isset($_SESSION['login'])){
			$this->dao=DAOFactory::createFavoriteDAO();
			try{
				$this->dao->addHistory($post['route_id']);
				$this->response='{"success":"1"}';
			}catch(DAOException $e){
				$this->response='{"success":"0","message":"'.$e->getMessage().'"}';
			}
		}
	}
	
	public function clear(){
		$post = $this->getRequestPost();
		if(isset($_SESSION['login'])){
			$this->dao=DAOFactory::createFavoriteDAO();
			try{
				$this->dao->clearHistory();
				$this->response='{"success":"1"}';
			}catch(DAOException $e){
				$this->response='{"success":"0","message":"'.$e->getMessage().'"}';
			}
		}
	}
}


?>