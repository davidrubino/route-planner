<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/Encodable.php');
class Member implements Encodable
{
	private $login;
	private $favorites;
	private $history;
	public function __construct($login,$favorite=array(),$history=array()){
		$this->login=$login;
		$this->favorites=$favorite;
		$this->history=$history;
	}
	
	public function getLogin(){
		return $this->login;
	}
	
	public function getFavorites(){
		return $this->favorites;
	}	
	
	public function getHistory(){
		return $this->history;
	}
	
	public function setFavorites($favorites){
		$this->favorites=$favorites;
	}
	
	public function setHistory($history){
		$this->history=$history;
	}
	
	public function encode(){
		$json = '{"login":"'.$this->login.'","fnb":"'.count($this->favorites).'","hnb":"'.count($this->history).'","favorites":[';
		foreach($this->favorites as $i=>$fav){
			$json.=$fav->encode();
			if($i>0)
				$json.=',';
		}
		$json.='],"history":[';
		foreach($this->history as $i=>$hist){
			$json.=$hist->encode();
			if($i>0)
				$json.=',';
		}
		$json.=']}';
		return $json;
	}
}
?>