<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/Encodable.php');

class Route implements Encodable
{
	private $id;
	private $start;
	private $end;
	private $transports;
	private $transportType;
	private $distance;
	private $duration;
	private $dateTime;
	private $transportsNb;
	
	public function __construct($start,$end,$transportType,$dateTime){
		$this->start=$start;
		$this->end=$end;
		$this->transportType=$transportType;
		$this->dateTime=$dateTime;
		$this->transports=array();
	}
	
	public function addTransport($transport){
		array_push($this->transports,$transport);
	}
	
	public function getTransport($i){
		return $this->transports[$i];
	}
	
	public function getTransports(){
		return $this->transports;
	}
	
	public function getTransportsNb(){
		return $this->transportsNb;
	}
	
	public function setTransportsNb($nb){
		$this->transportsNb=$nb;
	}
	
	public function getStart(){
		return $this->start;
	}
	
	public function getEnd(){
		return $this->end;
	}
	
	public function setStart($start){
		$this->start=$start;
	}
	
	public function setEnd($end){
		$this->end=$end;
	}
	
	public function setDuration($duration){
		$this->duration=$duration;
	}
	
	public function setDistance($distance){
		$this->distance=$distance;
	}
	
	public function getDuration(){
		return $this->duration;
	}
	
	public function getDistance(){
		return $this->distance;
	}
	
	public function getDateTime(){
		return $this->dateTime;
	}
	
	public function getTransportType(){
		return $this->transportType;
	}
	
	public function setId($id){
		$this->id=$id;
	}
	
	public function getId(){
		return $this->id;
	}
	
	public function encode()
	{
		$transportsEncode='[';
		for($i=0;$i<count($this->transports);$i++){
			if($i==0)
				$transportsEncode.=$this->transports[$i]->encode();
			else
				$transportsEncode.=','.$this->transports[$i]->encode();
		}
		$transportsEncode.=']';
		return '{"id":"'.$this->id.'","start":'.$this->start->encode().',"end":'.$this->end->encode().',"date":"'.$this->dateTime->format('Y-m-d H:i').'","distance":"'.$this->distance.'","duration":"'.$this->duration.'","transportType":"'.$this->transportType.'","transportsNb":"'.count($this->transports).'","transports":'.$transportsEncode.'}';
	}
}

?>