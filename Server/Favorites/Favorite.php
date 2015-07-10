
<?php

require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/Encodable.php');
class Favorite implements Encodable
{
	private $id;
	private $member;
	private $start;
	private $end;
	private $transportType;
	
	public function __construct($member,$start,$end,$transportType,$id=null){
		$this->id=$id;
		$this->member=$member;
		$this->start=$start;
		$this->end=$end;
		$this->transportType=$transportType;
	}

	public function encode(){
		return '{"id":"'.$this->id.'","start":'.$this->start->encode().',"end":'.$this->end->encode().',"transportType":"'.$this->transportType.'"}';
	}
	
	public function getId(){
		return $this->id;
	}
	
	public function getMember(){
		return $this->member;
	}
	
	public function getStart(){
		return $this->start;
	}
	
	public function getEnd(){
		return $this->end;
	}
	
	public function getTransportType(){
		return $this->transportType;
	}
}

?>