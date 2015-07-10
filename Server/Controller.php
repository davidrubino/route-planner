<?php

	abstract class Controller
	{
		protected $post;
		protected $param;
		protected $content;
		
	    public function __construct(){
			$this->post=$_POST;
			$this->param=$_GET;
			$this->content=@file_get_contents('php://input');
		}
		public function getRequestPost(){
			return $this->post;
		}
		
		public function getRequestParam(){
			return $this->param;
		}
		
		public function getRequestContent(){
			return $this->content;
		}
		
		public abstract function render();
	}
	
	
?>