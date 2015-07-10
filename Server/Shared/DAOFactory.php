<?php

require_once($_SERVER["DOCUMENT_ROOT"].'/Address/AddressDAO.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Address/AddressGNyDAO.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Route/RouteDAO.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Members/MemberDAO.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Favorites/FavoriteDAO.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Stan/StopDAO.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Stan/StanRouteDAO.php');
require_once($_SERVER["DOCUMENT_ROOT"].'/Stan/NetworkDAO.php');

	class DAOFactory
	{
		public static function createAddressDAO(){
			return new AddressDAO();
		}
		
		public static function createAddressGnyDAO(){
			return new AddressGnyDAO();
		}
	
		public static function createRouteDAO(){
			return new RouteDAO();
		}
		
		public static function createFavoriteDAO(){
			return new FavoriteDAO();
		}
		
		public static function createMemberDAO(){
			return new MemberDAO();
		}
		
		public static function createStopDAO(){
			return new StopDAO();
		}
		
		public static function createStanRouteDAO(){
			return new StanRouteDAO();
		}
		
		public static function createNetworkDAO(){
			return new NetworkDAO();
		}
	}
?>