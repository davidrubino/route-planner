<?php
	class MySQLConnection
	{
	
		private static $instance=null;
		
		private $HOST="127.0.0.1";
		private $USER="projetPCD";
		private $PASSWORD="tdajdrad";
		private $DB="projetPCD";
		private $connection;
		
		private function __construct(){
			$this->connect();
		}
		
		public static function getInstance(){
			if(self::$instance==null){
				self::$instance=new MySQLConnection();
			}
			return self::$instance->getConnection();
		}
		
		private function connect(){
			$dns = 'mysql:host='.$this->HOST.';dbname='.$this->DB;
			try{
				$this->connection = new PDO($dns, $this->USER, $this->PASSWORD,array(PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8'));
			} catch ( Exception $e ) {
			  echo "Connection à MySQL impossible : ", $e->getMessage();
			  die();
			}
		}
		
		private function getConnection(){
			return $this->connection;
		}
	}
?>