<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/Encodable.php');
class Transport implements Encodable
{
	private $id;
	private $steps;
	private $points;
	private $description;
	private $stepsNb;
	private $pointsNb;
	private $route;
	private $distance;
	private $duration;
	private $type;
	
	public function __construct($description){
		$this->description=$description;
		$this->steps=array();
		$this->points=array();
	}
	
	public function addStep($step){
		array_push($this->steps,$step);
	}
	
	public function getStep($i){
		return $this->steps[$i];
	}
	
	public function setStep($i,$step){
		return $this->steps[$i]=$step;
	}
	
	public function setSteps($steps)
	{
		$this->steps=$steps;
	}
	
	public function setType($type){
		$this->type=$type;
	}
	
	public function getType(){
		return $this->type;
	}
	
	public function setPoint($i,$point){
		return $this->points[$i]=$point;
	}
	
	public function addPoint($point){
		array_push($this->points,$point);
	}
	
	public function getPoint($i){
		return $this->points[$i];
	}
	
	public function getDescription(){
		return $this->description;
	}
	
	public function getStepsNb()
	{
		return $this->stepsNb;
	}
	
	public function getPointsNb()
	{
		return $this->pointsNb;
	}
	
	public function setStepsNb($nb)
	{
		$this->stepsNb=$nb;
	}
	
	public function setPointsNb($nb)
	{
		$this->pointsNb=$nb;
	}
	
	public function getSteps()
	{
		return $this->steps;
	}
	
	public function getPoints()
	{
		return $this->points;
	}
	
	public function getDuration(){
		return $this->duration;
	}
	
	public function getDistance(){
		return $this->distance;
	}
	
	public function setDuration($duration){
		$this->duration=$duration;
	}
	
	public function setDistance($distance){
		$this->distance=$distance;
	}
	
	public function setRoute($route){
		$this->route=$route;
	}
	
	public function encode()
	{
		$stepsEncode='[';
		for($i=0;$i<count($this->steps);$i++){
			if($i==0)
				$stepsEncode.=$this->steps[$i]->encode();
			else
				$stepsEncode.=','.$this->steps[$i]->encode();
		}
		$stepsEncode.=']';
		
		$pointsEncode='[';
		for($i=0;$i<count($this->points);$i++){
			if($i==0)
				$pointsEncode.=$this->points[$i]->encode();
			else
				$pointsEncode.=','.$this->points[$i]->encode();
		}
		$pointsEncode.=']';
		
		return '{"description":"'.$this->description.'","type":"'.$this->type.'","distance":"'.$this->distance.'","duration":"'.$this->duration.'","stepsNb":"'.count($this->steps).'","steps":'.$stepsEncode.',"pointsNb":"'.count($this->points).'","points":'.$pointsEncode.'}'; //
	}
}
?>