<?php

	require_once($_SERVER["DOCUMENT_ROOT"].'/Shared/MySQLConnection.php');
	abstract class DAO
	{
		protected $connection;
		
		protected function __construct(){
			$this->connection = MySQLConnection::getInstance();
		}
		
		public abstract function findById($id); //$id = array()
	}

?>