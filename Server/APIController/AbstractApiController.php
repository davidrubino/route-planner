		
<?php
require_once($_SERVER["DOCUMENT_ROOT"].'/Controller.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/APIController/APIController.php');
abstract class AbstractApiController extends Controller implements APIController{

		protected abstract function getReferer();
		
		/**
		*	Execute une requete HTTP POST sur un URL avec des paramètres
		*	@param url l'URL sur laquelle on effectué la requête
		*	@param param la chaine encodée JSON
		*	@return Le résultat de la requête (on s'attend à un format JSON ici)
		**/
		protected function post($url,$param)
		{
			$ch = curl_init();
			curl_setopt($ch,CURLOPT_URL, $url); //définition de l'URL
			curl_setopt($ch,CURLOPT_POST, 1); //définition du type de requête (post)
			curl_setopt($ch,CURLOPT_RETURNTRANSFER,1); //curl_exec retourne la valeur au lieu de l'afficher
			curl_setopt($ch,CURLOPT_POSTFIELDS, $param); //paramètres postés = $param (ici c'est du JSON que le site attend)
			curl_setopt($ch,CURLOPT_REFERER,$this->getReferer()); //referer de la requête
			curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false); //on ignore le SSL
			curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false); //on ignore le SSL
			$result = curl_exec($ch); //execution de la requête
			if($result===false)
				throw new ApiException("La requête POST a echoué ".curl_error($ch),E_USER_WARNING);
			curl_close($ch);
			return $result;
		}
		
		protected function get($url){
			$c = curl_init();
			curl_setopt($c, CURLOPT_URL, $url);
			curl_setopt($c, CURLOPT_RETURNTRANSFER, true);
			curl_setopt($c, CURLOPT_HEADER, false);
			curl_setopt($c, CURLOPT_HTTPHEADER, array('Accept: 	application/json'));
			$output = curl_exec($c);
			if($output === false)
				throw new ApiException("La requête GET a echoué : ".curl_error($c),E_USER_WARNING);
			curl_close($c);
			return $output;
		}
		
		
}