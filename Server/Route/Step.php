<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/Encodable.php');
class Step implements Encodable
{
	private $id;
	private $lat;
	private $lon;
	private $action;
	private $transport;

	public function __construct($action,$lat,$lon,$transport){
		$this->lat=$lat;
		$this->lon=$lon;
		$this->action=$action;
		$this->transport=$transport;
	}
	
	public function getLat(){
		return $this->lat;
	}
	
	public  function getLon(){
		return $this->lon;
	}
	
	public  function getAction(){
		return $this->action;
	}
	
	public function getTransport(){
		return $this->transport;
	}
	
	public function setTransport($transport)
	{
		$this->transport=$transport;
	}
	
		public function setEnd($end){
		$this->end=$end;
	}
	
	public function encode()
	{
		return json_encode(array("action"=>$this->action,"lat"=>$this->lat,"lon"=>$this->lon));
	}
}
?>