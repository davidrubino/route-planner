<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/Encodable.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/Helper.php');

class Network implements Encodable
{
	private $lines;
	
	public function encode(){
		return '{"linesNb":"'.count($this->lines).'","lines":'.Helper::encodeArrayOfObject($this->lines).'}';
	}
	
	public function getLines(){
		return $this->lines;
	}
	
	public function setLines($lines){
		$this->lines=$lines;
	}
}

?>