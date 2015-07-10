<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/Encodable.php');
class Point implements Encodable
{
	private $id;
	private $lat;
	private $lon;
	private $xepsg;
	private $yepsg;
	private $transport;

	public function __construct($lat,$lon,$transport){
		$this->lat=$lat;
		$this->lon=$lon;
		$this->transport=$transport;
	}
	
	public function getLat(){
		return $this->lat;
	}
	
	public  function getLon(){
		return $this->lon;
	}
	
	public function getTransport(){
		return $this->transport;
	}
	
	public function setTransport($transport)
	{
		$this->transport=$transport;
	}
	
	public function encode()
	{
		return json_encode(array("lat"=>$this->lat,"lon"=>$this->lon));
	}
}
?>