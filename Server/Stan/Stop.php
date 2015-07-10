<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/Encodable.php');

class Stop implements Encodable
{
	private $stop_id;
	private $stop_code;
	private $stop_name;
	private $stop_lat;
	private $stop_lon;
	private $times;
	
	public function __construct($stop_id,$stop_code,$stop_name,$stop_lat,$stop_lon){
		$this->stop_id=$stop_id;
		$this->stop_code=$stop_code;
		$this->stop_name=$stop_name;
		$this->stop_lat=$stop_lat;
		$this->stop_lon=$stop_lon;
		$this->times=array();
	}
	
	public function encode(){
		$encode = '{"infos":'.json_encode(array('stop_id'=>$this->stop_id,'stop_code'=>$this->stop_code,'stop_name'=>$this->stop_name,'stop_lat'=>$this->stop_lat,'stop_lon'=>$this->stop_lon));
		if(count($this->times)>0)
			$encode.=',"timesNb":"'.count($this->times).'","times":'.json_encode($this->times).'}';
		else
			$encode.='}';
		return $encode;
	}
	
	public function getStop_id(){
		return $this->stop_id;
	}
	
	public function getStop_code(){
		return $this->stop_code;
	}
	
	public function getStop_name(){
		return $this->stop_name;
	}
	
	public function getStop_lat(){
		return $this->stop_lat;
	}
	
	public function getStop_lon(){
		return $this->stop_lon;
	}
	
	public function setTimes($times){
		$this->times=$times;
	}
}

?>