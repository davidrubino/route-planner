<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Controller.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOFactory.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Address/Address'.$_SESSION['impl'].'.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Members/Member.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/APIController/'.$_SESSION['impl'].'Controller.php');

class FavoriteController extends Controller{

	private $response;
	private $dao;

	public function __construct(){
		parent::__construct();
		$this->dao=DAOFactory::createFavoriteDAO();
	}

	public function render(){
		header('Content-type: application/json');
		echo $this->response;		
	}
	
	public function add(){
		if(isset($_SESSION['login'])){
			$post=$this->getRequestPost();
			if(isset($post['start_id']) && is_numeric($post['start_id']) && isset($post['end_id']) && is_numeric($post['end_id']) && isset($post['transportType']) && !empty($post['transportType'])){
				$reflect = new ReflectionClass('Address'.$_SESSION['impl']);
				
				$start = $reflect->newInstance();
				$start->setId($post['start_id']);
				
				$end = $reflect->newInstance();
				$end->setId($post['end_id']);
				
				$member=new Member($_SESSION['login']);
				
				$transportType=$post['transportType'];
				
				$favorite = new Favorite($member,$start,$end,$transportType);
				try{
					$this->dao->create($favorite);
					$this->response=json_encode(array('success'=>'1'));
				}catch(DAOException $e){
					$this->response=json_encode(array('success'=>'0','message'=>$e->getMessage()));
				}
			}
		}
	}
	
	public function delete(){
		if(isset($_SESSION['login'])){
			$post=$this->getRequestPost();
			if(isset($post['start_id']) && is_numeric($post['start_id']) && isset($post['end_id']) && is_numeric($post['end_id']) && isset($post['transportType']) && !empty($post['transportType'])){
				$reflect = new ReflectionClass('Address'.$_SESSION['impl']);
				
				$start = $reflect->newInstance();
				$start->setId($post['start_id']);
				
				$end = $reflect->newInstance();
				$end->setId($post['end_id']);
				
				$member=new Member($_SESSION['login']);
				
				$transportType=$post['transportType'];
				
				$favorite = new Favorite($member,$start,$end,$transportType);
				try{
					$this->dao->delete($favorite);
					$this->response=json_encode(array('success'=>'1'));
				}catch(DAOException $e){
					$this->response=json_encode(array('success'=>'0','message'=>$e->getMessage()));
				}
			}
		}
	}
	
	public function calculate(){
		$reflect = new ReflectionClass($_SESSION['impl'].'Controller');
		$apiController = $reflect->newInstance();
		$apiController->getRoutes();
		$this->response=$apiController->getResponse();
	}
}

?>