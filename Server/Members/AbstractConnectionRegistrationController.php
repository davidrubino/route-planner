<?php

require_once($_SERVER["DOCUMENT_ROOT"].'/Controller.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOException.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOFactory.php');

abstract class AbstractConnectionRegistrationController extends Controller 
{	
	protected $dao;
	protected $response;
	protected $login;
	protected $password;
	public function __construct(){
		parent::__construct();
		if($this->checkParams())
		{
			try
			{
					$post=$this->getRequestPost();
					$this->login = $post['login'];
					$this->password = $post['password'];
					$this->dao = DAOFactory::createMemberDAO();
			}
			catch(DAOException $e) { 
				$this->response='{"success":"0","message":"'.$e->getMessage().'"}'; 
			}
		}
	}
	
	public function render(){
		header('Content-type: application/json; charset=utf-8');
		echo $this->response;
	}
	
	public function doAction(){
		$this->process();
	}
	
	protected abstract function process();
	
	protected function checkParams(){
		$post=$this->getRequestPost();
		return (isset($post['login']) && !empty($post['login']) && isset($post['password']) && !empty($post['password']));
	}
}



?>
