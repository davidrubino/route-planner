<?php
	require_once($_SERVER["DOCUMENT_ROOT"].'/Members/AbstractConnectionRegistrationController.php');
	
	class ConnectionController extends AbstractConnectionRegistrationController
	{
		public function __construct (){
			parent::__construct();
		}
		
		protected function process(){
			if($this->checkParams())
			{
				try{
				
					$member = $this->dao->findById(array('login'=>$this->login,'password'=>$this->password));
					$_SESSION['login']=$member->getLogin();
					$this->response='{"success":"1","member":'.$member->encode().'}'; 
				}catch(DAOException $e){
					$this->response='{"success":"0","message":"'.$e->getMessage().'"}'; 
				}
			}
		}
	}
?>