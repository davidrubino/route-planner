<?php
	require_once($_SERVER["DOCUMENT_ROOT"].'/Members/AbstractConnectionRegistrationController.php');
	
	class RegistrationController extends AbstractConnectionRegistrationController
	{
		private $mail;
		private $name;
		private $firstName;
		
		public function __construct (){
			parent::__construct();
			$post=$this->getRequestPost();
			$this->mail = @$post['mail'];
			$this->name = @$post['name'];
			$this->firstName = @$post['firstName'];
		}
		
		protected function process(){ //throws DAOException
			if($this->checkParams())
			{
				try{
					$this->dao->create(array('login'=>$this->login,'password'=>$this->password,'mail'=>$this->mail,'name'=>$this->name,'firstName'=>$this->firstName));
					$this->response='{"success":"1","message":"Inscription réalisée avec succès !"}'; 
				}
				catch(DAOException $e) { 
					$this->response='{"success":"0","message":"'.$e->getMessage().'"}'; 
				}
			}
		}
		
		protected function checkParams(){
			$post=$this->getRequestPost();
			return parent::checkParams() && isset($post['mail']) && !empty($post['mail']) && isset($post['name']) && !empty($post['name']) && isset($post['firstName']) && !empty($post['firstName']);
		}
	}
?>