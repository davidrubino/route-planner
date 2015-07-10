<?php

require_once($_SERVER["DOCUMENT_ROOT"].'/APIController/ApiException.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Controller.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Stan/IBusController.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/DAOFactory.php');

class StanController extends Controller implements IBusController
{
	private $json;
	
	public function render(){
		header('Content-type: application/json');
		echo $this->json;	
	}
	
	public function getStop(){
		if(isset($this->getRequestParam()['stop_id']) && is_numeric($this->getRequestParam()['stop_id'])){
			$stopDAO = DAOFactory::createStopDAO();
			try{
				if(isset($this->getRequestParam()['direction']) && ($this->getRequestParam()['direction']==0 || $this->getRequestParam()['direction']==1))
				{
					if(isset($this->getRequestParam()['route_id']) && is_numeric($this->getRequestParam()['route_id'])){
						$stop=$stopDAO->findById($this->getRequestParam()['stop_id'],$this->getRequestParam()['direction'],$this->getRequestParam()['route_id']);
					}
					else
						$stop=$stopDAO->findById($this->getRequestParam()['stop_id'],$this->getRequestParam()['direction']);
				}
				else
				{
					$stop=$stopDAO->findById($this->getRequestParam()['stop_id']);
				}
				$this->json = $stop->encode();
			}catch(DAOException $e){
				$this->json = json_encode(array("error"=>"1","message"=>$e->getMessage()));
			}
		}
	}
	
	public function getRoute(){
		if(isset($this->getRequestParam()['route_id']) && is_numeric($this->getRequestParam()['route_id'])){
			$routeDAO = DAOFactory::createStanRouteDAO();
			try{
				if(isset($this->getRequestParam()['direction']) && ($this->getRequestParam()['direction']==0 || $this->getRequestParam()['direction']==1))
					$route=$routeDAO->findById($this->getRequestParam()['route_id'],$this->getRequestParam()['direction']);
				else
					$route=$routeDAO->findById($this->getRequestParam()['route_id']);
				$this->json = $route->encode();
			}catch(DAOException $e){
				$this->json = json_encode(array("error"=>"1","message"=>$e->getMessage()));
			}
		}
	}
	
	public function getNetwork(){
		$networkDAO = DAOFactory::createNetworkDAO();
		try{
			$network=$networkDAO->findById(null);
			$this->json = $network->encode();
		}catch(DAOException $e){
			$this->json = json_encode(array("error"=>"1","message"=>$e->getMessage()));
		}
	}
}