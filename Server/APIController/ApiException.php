
<?php 
class ApiException extends Exception
{
	public function ApiException($message)
	{
		parent::__construct($message);
	}
}
?>