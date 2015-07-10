<?php

require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/Encodable.php');

class Address implements Encodable
{
	protected $id;
	protected $label;
	protected $lat;
	protected $lon;
	protected $type;
	protected $typeStr;
	protected $infos="";
	
	protected static $types=array('Unknown'=>-1,"Adresse"=>0,"Arrêts"=>1,"Enseignement secondaire"=>2,"Enseignement supérieur"=>3,"Culture"=>4,"Sport"=>5);
	
	public function __construct($id,$label,$lat,$lon,$typeStr='Unknown'){
		$this->id=$id;
		$this->label=$label;
		$this->lat=$lat;
		$this->lon=$lon;
		if(array_key_exists($typeStr,self::$types)){
			$this->typeStr=$typeStr;
			$this->type=self::$types[$typeStr];
		}
	}
	
	public function encode(){
		return json_encode(array("id"=>$this->id,"label"=>$this->label,"type"=>$this->type,"typeStr"=>$this->typeStr,"lat"=>$this->lat,"lon"=>$this->lon,"infos"=>$this->infos));
	}
	
	public function setLat($lat){
		$this->lat=$lat;
	}
	
	public function setLon($lon){
		$this->lon=$lon;
	}
	
	public function setLabel($label){
		$this->label=$label;
	}
	
	public function getLabel(){
		return $this->label;
	}
	
	public function getLat()
	{
		return $this->lat;
	}
	
	public function getLon()
	{
		return $this->lon;
	}
	
	public function getId(){
		return $this->id;
	}
	
	public function setId($id){
		$this->id=$id;
	}
	
	public function setInfos($infos){
		$this->infos=$infos;
	}
}

?>