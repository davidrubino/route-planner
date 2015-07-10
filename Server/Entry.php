
<?php
//ENTRY API : ROUTER
	
	session_start();
	header('Content-Type: text/html; charset=utf-8');
	$config = parse_ini_file ("config.ini",TRUE);
	$_SESSION['impl']=$config["API"]["Impl"];
	
	/*TEST*/
	/*$_SESSION['login']='toto';
	$_POST['start_id']='221';
	$_POST['end_id']='226';
	$_POST['transportType']='Bus';
	*/
	/******/
	
	require_once($_SERVER["DOCUMENT_ROOT"].'/APIController/ApiException.php');
	$service = $_GET['s'];
	$page = $_GET['p'];
	
	$concreteController = $_SESSION['impl'].'Controller';

						//array('page'=>'','controller'=>,'action'=>'')
	$router = array(	
						array('service'=>'api',		'page'=>'getAddress',			'controller'=>$concreteController,		'action'=>'getAddress',	'folder'=>'APIController'),
						array('service'=>'api',		'page'=>'getRoutes',			'controller'=>$concreteController,		'action'=>'getRoutes',	'folder'=>'APIController'),
						array('service'=>'member',	'page'=>'connect',				'controller'=>'ConnectionController',	'action'=>'doAction',	'folder'=>'Members'),
						array('service'=>'member',	'page'=>'register',				'controller'=>'RegistrationController',	'action'=>'doAction',	'folder'=>'Members'),
						array('service'=>'member',	'page'=>'favorite/add',			'controller'=>'FavoriteController',		'action'=>'add',		'folder'=>'Favorites'),
						array('service'=>'member',	'page'=>'favorite/delete',		'controller'=>'FavoriteController',		'action'=>'delete',		'folder'=>'Favorites'),
						array('service'=>'member',	'page'=>'favorite/calculate',	'controller'=>'FavoriteController',		'action'=>'calculate',	'folder'=>'Favorites'),
						array('service'=>'member',	'page'=>'history/add',			'controller'=>'HistoryController',		'action'=>'add',		'folder'=>'History'),
						array('service'=>'member',	'page'=>'history/clear',		'controller'=>'HistoryController',		'action'=>'clear',		'folder'=>'History'),
						array('service'=>'stan',	'page'=>'bus',					'controller'=>'StanController',			'action'=>'getNetwork',	'folder'=>'Stan'),
						array('service'=>'stan',	'page'=>'bus-route',			'controller'=>'StanController',			'action'=>'getRoute',	'folder'=>'Stan'),
						array('service'=>'stan',	'page'=>'bus-stop',				'controller'=>'StanController',			'action'=>'getStop',	'folder'=>'Stan')
					);
					
	$found=false;
	foreach($router as $rule)
	{
		if($rule['service']==$service && $rule['page']==$page)
		{
			try{
				$found=true;
				require_once($_SERVER["DOCUMENT_ROOT"].'/'.$rule['folder'].'/'.$rule['controller'].'.php');
				$reflect = new ReflectionClass($rule['controller']);
				$controller = $reflect->newInstance();
				call_user_func(array($controller,$rule['action']));
				$controller->render();
			}catch(ApiException $e){
				echo json_encode(array("error"=>1,"message"=>$e->getMessage()));
			}
		}
	}
	
	if(!$found){
		header('HTTP/1.0 404 Not Found');
		echo '<html><head><title>Not Found</title></head><body><h1>Page not found</h1></body></html>';
		exit();
	}
?>