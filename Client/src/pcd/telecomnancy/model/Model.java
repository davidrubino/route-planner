package pcd.telecomnancy.model;

import java.io.IOException;
import java.util.Properties;

import pcd.telecomnancy.dal.AddressDAL;
import pcd.telecomnancy.dal.BusNetworkDAL;
import pcd.telecomnancy.dal.MemberDAL;
import pcd.telecomnancy.dal.Registration;
import pcd.telecomnancy.dal.RoutesDAL;
import pcd.telecomnancy.model.address.AddressListModelManager;
import pcd.telecomnancy.model.bus.BusNetwork;
import pcd.telecomnancy.model.bus.StopTimesTableModel;
import pcd.telecomnancy.model.member.FavoriteTableModel;
import pcd.telecomnancy.model.member.HistoryTableModel;
import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.model.routes.RoutesListModel;
import pcd.telecomnancy.serverCommunication.param.APIParams;

/** Le modèle de l'application **/
public class Model {

	private final Session session = new Session();

	private AddressListModelManager addressListModelManager;
	private RoutesListModel routesListModel;
	private ErrorModel errorModel;
	private HistoryTableModel historyTableModel;
	private FavoriteTableModel favoriteTableModel;
	private AddressDAL addressDAL;
	private RoutesDAL routesDAL;
	private MemberDAL memberDAL;
	private BusNetworkDAL busNetworkDAL;
	private Registration registration;
	private Properties properties;
	private APIParams apiParams;
	private DisplayedRoute currentRoute;
	private BusNetwork busNetwork;
	private StopTimesTableModel stopTimesTableModel;

	/** Construit l'instance du modèle **/
	public Model() {

		properties = new Properties();
		try {
			properties.load(getClass().getClassLoader().getResourceAsStream(
					"config.properties"));
			apiParams = new APIParams(properties);

			addressListModelManager = new AddressListModelManager();
			routesListModel = new RoutesListModel();
			errorModel = new ErrorModel();
			historyTableModel = new HistoryTableModel(session);
			favoriteTableModel = new FavoriteTableModel(session);
			addressDAL = new AddressDAL(session, errorModel);
			routesDAL = new RoutesDAL(session, errorModel,
					apiParams.getRouteParam());
			memberDAL = new MemberDAL(session, errorModel);
			busNetwork = new BusNetwork();
			busNetworkDAL = new BusNetworkDAL(session, errorModel);
			registration = new Registration(session);

			currentRoute = new DisplayedRoute(apiParams.getRouteParam());

			stopTimesTableModel = new StopTimesTableModel();
		} catch (IOException e) {
			System.err.println("Fichier de configuration introuvable !");
			System.exit(1);
		}

	}

	/**
	 * Accesseur des propriétés du modèle
	 * 
	 * @return properties les propriétés
	 **/
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Accesseur des paramètres de l'api
	 * 
	 * @return apiParams paramètres de l'api
	 **/
	public APIParams getApiParams() {
		return apiParams;
	}

	/**
	 * Accesseur de la route qui est traité dans le modèle
	 * 
	 * @return currentRoute la route
	 **/
	public DisplayedRoute getCurrentRoute() {
		return currentRoute;
	}

	/**
	 * Accesseur du manager des modèles de liste d'adresse
	 * 
	 * @return addressListModelManager le manager des modèles de liste d'adresse
	 **/
	public AddressListModelManager getAddressListModelManager() {
		return addressListModelManager;
	}

	/**
	 * Accesseur du modèle de la liste des routes
	 * 
	 * @return le modèle de la liste des routes
	 **/
	public RoutesListModel getRoutesListModel() {
		return routesListModel;
	}
	/**
	 * Accesseur du modèle d'erreur 
	 * 
	 * @return errorModel le modèle d'erreur
	 **/
	public ErrorModel getErrorModel() {
		return errorModel;
	}

	/**
	 * Accesseur du modèle du tableaud'historique
	 * 
	 * @return historyTableModel le modèle dutableau d'historique
	 **/
	public HistoryTableModel getHistoryTableModel() {
		return historyTableModel;
	}

	/**
	 * Accesseur du modèle de tableau des favoris 
	 * 
	 * @return favoriteTableModel modèle du tableau des fovris
	 **/
	public FavoriteTableModel getFavoriteTableModel() {
		return favoriteTableModel;
	}

	/**
	 * Accesseur de la sessio,
	 * 
	 * @return session la session
	 **/
	public Session getSession() {
		return session;
	}

	/**
	 * Accesseur du DAL des adresses
	 * 
	 * @return adressDAL le DAL des adresses
	 **/
	public AddressDAL getAddressDAL() {
		return addressDAL;
	}

	/**
	 * Accesseur du DAL des routes
	 * 
	 * @return routesDAL le DAL des routes
	 **/
	public RoutesDAL getRoutesDAL() {
		return routesDAL;
	}

	/**
	 * Accesseur du DAL des membres
	 * 
	 * @return memberDAL le DAL des membres
	 **/
	public MemberDAL getMemberDAL() {
		return memberDAL;
	}

	/**
	 * Accesseur de l'enregistrement
	 * 
	 * @return registration l'enregistrement
	 **/
	public Registration getRegistration() {
		return registration;
	}

	/**
	 * Accesseur du DAL du réseau de bus
	 * 
	 * @return busNetworkDAL le DAL du réseau de bus
	 **/
	public BusNetworkDAL getBusNetworkDAL() {
		return busNetworkDAL;
	}

	/**
	 * Accesseur du réseau de bus
	 * 
	 * @return busNetwork le réseau de bus
	 **/
	public BusNetwork getBusNetwork() {
		return busNetwork;
	}

	/**
	 * Accesseur du modèle du tableau des horaires d'un arrêt
	 * 
	 * @return stopTimesTableModel le modèle du tableau des horaires d'un arrêt
	 **/
	public StopTimesTableModel getStopTimesTableModel() {
		return stopTimesTableModel;
	}
}
