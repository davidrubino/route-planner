<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Address/Address.php');

class AddressGNy extends Address
{
	private $str;
	private $map;
	
	public static $SUPPORTED_CTYPES = array(
		"304"=>"Arrêts",
		"302"=>"Arrêts",
		"175"=>"Enseignement secondaire",
		"188"=>"Enseignement supérieur",
		"125"=>"Culture",
		"311"=>"Sport"
	);

	public function __construct($label=null,$str=null,$lat=null,$lon=null,$map=array(),$id=null){
		parent::__construct($id,$label,$lat,$lon);
		$this->str=$str;
		$this->setMap($map);
		$this->typeStr="";
		if(isset($this->map["ctype"])){
			$ctype=$this->map["ctype"];
			if(array_key_exists($ctype,AddressGny::$SUPPORTED_CTYPES))
				$this->typeStr=AddressGny::$SUPPORTED_CTYPES[$ctype];
			else
				$this->typeStr='Unknown';
		}
		else
			$this->typeStr="Adresse";
			
		$this->type=self::$types[$this->typeStr];
	}
	
	public function setStr($str){
		$this->str=$str;
	}
	
	public function setMap($map){
		$this->map=$map;
		if(isset($this->map['destinations']))
			$this->buildInfos($this->infos=$this->map['destinations']);
	}
	
	public function addInMap($key,$value)
	{
		$this->map[$key]=$value;
		if(isset($this->map['destinations']))
			$this->buildInfos($this->map['destinations']);
	}
	
	public function getStr()
	{
		return $this->str;
	}
	
	public function getMap()
	{
		return $this->map;
	}
	
	private function buildInfos($raw){
		$raw.=",";
		preg_match_all('#(.+)((\|)|(\s\-\s))(.+),#U',$raw,$matches,PREG_PATTERN_ORDER);
		$this->infos="";
		for($i=0;$i<count($matches[0]);$i++){
			if($i>0)
				$this->infos.=';';
			$this->infos.='Ligne '.$matches[1][$i].' vers '.$matches[5][$i];
		}
	}
}


?>