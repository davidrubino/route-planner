<?php
class DAOException extends Exception{
	public function __construct($msg){
		parent::__construct($msg);
	}
}
?>