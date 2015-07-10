<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/Encodable.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/Helper.php');

class StanRoute implements Encodable
{
	private $route_id;
	private $route_short_name;
	private $route_long_name;
	private $route_type;
	private $stops;
	private $d0;
	private $d1;
	
	public function __construct($route_id,$route_short_name,$route_long_name,$route_type){
		$this->route_id=$route_id;
		$this->route_short_name=$route_short_name;
		$this->route_long_name=$route_long_name;
		$this->route_type=$route_type;
		$this->stops=array();
		$this->d0 = null;
		$this->d1 = null;
		if(preg_match('#.+\s-|/\s.+#',$this->route_long_name)){
			$exp = preg_split('#\s-|/\s#',$this->route_long_name);
			$this->d0 = $exp[1];
			$this->d1 = $exp[0];
		}
	}
	
	public function encode(){
		$encode = '{"infos":'.json_encode(array('route_id'=>$this->route_id,'route_short_name'=>$this->route_short_name,'route_long_name'=>$this->route_long_name,'direction_0'=>$this->d0,'direction_1'=>$this->d1,'route_type'=>$this->route_type));
		if(count($this->stops)>0)
			$encode.= ',"stopsNb":"'.count($this->stops).'","stops":'.Helper::encodeArrayOfObject($this->stops).'}';
		else
			$encode.='}';
		return $encode;
	}
	
	public function getDirection_0(){
		return $this->d0;
	}
	
	public function getDirection_1(){
		return $this->d1;
	}
	
	public function getRoute_id(){
		return $this->route_id;
	}
	
	public function getRoute_short_name(){
		return $this->route_short_name;
	}
	
	public function getRoute_long_name(){
		return $this->route_long_name;
	}
	
	public function getRoute_type(){
		return $this->route_type;
	}
	
	public function setStops($stops){
		$this->stops=$stops;
	}
}

?>