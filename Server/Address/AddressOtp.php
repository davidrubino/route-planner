<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Address/Address.php');

class AddressOtp extends Address
{
	public function __construct($label=null,$str=null,$lat=null,$lon=null,$map=array(),$id=null){
		parent::__construct($id,$label,$lat,$lon);
	}

}