package pcd.telecomnancy.dal;

import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import pcd.telecomnancy.model.ErrorModel;
import pcd.telecomnancy.model.bus.BusLine;
import pcd.telecomnancy.model.bus.BusStop;
import pcd.telecomnancy.model.bus.StopTime;
import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.serverCommunication.ServerCommunication;
import pcd.telecomnancy.serverCommunication.ServerCommunicationImpl;
import pcd.telecomnancy.serverCommunication.ServerCommunicationProxy;
import pcd.telecomnancy.serverCommunication.ServerDataListener;
import pcd.telecomnancy.serverCommunication.SimpleCallback;
import pcd.telecomnancy.serverCommunication.param.IParam;


/**
 * Classe permettant de construire/compléter les lignes de bus, les arrêts, les
 * horaires à partir des données du serveur
 */
public class BusNetworkDAL extends DAL<SimpleCallback<BusLine[]>> {

	/**
	 * Construit une instance
	 * 
	 * @param session
	 *            La session de l'utilisateur courant
	 * @param errorModel
	 *            Le modèle d'erreur
	 */
	public BusNetworkDAL(Session session, ErrorModel errorModel) {
		super(session, errorModel);
	}

	@Override
	public void find(IParam param, final SimpleCallback<BusLine[]> callback) {
		ServerCommunication sc = new ServerCommunicationProxy(session,new ServerCommunicationImpl(session));
		sc.addRequest(param);
		sc.doRequests(new ServerDataListener() {

			@Override
			public void dataReceived(List<String> response) {
				DataNotFoundException busNotFound = new DataNotFoundException(
						"Recherche de lignes", "Aucune ligne n'a été trouvée.");
				try {
					if (response.size() == 0)
						throw busNotFound;
					Object obj = JSONValue.parse(response.get(0));
					JSONObject o = (JSONObject) obj;
					if (o == null || o.get("lines") == null) {
						if (o != null && o.get("error") != null)
							throw busNotFound;
						else
							throw busNotFound;
					}
					BusLine[] lines = FromJSONFactory.createLines(o);
					callback.onDataReady(lines);
				} catch (DataNotFoundException e) {
					callback.onDataNotFound(e);
				}

			}

			@Override
			public void onError(String title, String message) {
				callback.onComError(title, message);
			}

		});

	}

	/**
	 * Cette fonction est appelée lorsqu'on charge les arrêts de bus à partir du
	 * serveur
	 * 
	 * @param param
	 *            Les paramètres de la requête à envoyer au serveur
	 * @param callback
	 *            L'instance sur laquelle va être appelée la fonction de
	 *            callback
	 */
	public void loadStops(IParam param, final SimpleCallback<BusStop[]> callback) {
		ServerCommunication sc = new ServerCommunicationProxy(session,new ServerCommunicationImpl(session));
		sc.addRequest(param);
		sc.doRequests(new ServerDataListener() {

			@Override
			public void dataReceived(List<String> response) {
				DataNotFoundException stopsNotFound = new DataNotFoundException(
						"Chargement des arrêts", "Aucun arrêt n'a été trouvé.");
				try {
					if (response.size() == 0)
						throw stopsNotFound;
					Object obj = JSONValue.parse(response.get(0));
					JSONObject o = (JSONObject) obj;
					if (o == null || o.get("stops") == null) {
						if (o != null && o.get("error") != null)
							throw stopsNotFound;
						else
							throw stopsNotFound;
					}
					BusStop[] stops = FromJSONFactory.createStops(o);
					callback.onDataReady(stops);
				} catch (DataNotFoundException e) {
					callback.onDataNotFound(e);
				}
			}

			@Override
			public void onError(String title, String message) {
				callback.onComError(title, message);
			}

		});
	}

	/**
	 * Cette fonction est appelée lorsqu'on charge les horaires de bus d'un
	 * arrêt à partir du serveur
	 * 
	 * @param param
	 *            Les paramètres de la requête à envoyer au serveur
	 * @param simpleCallback
	 *            L'instance sur laquelle sera appelée la fonction de callback
	 */
	public void loadTimes(IParam param,
			final SimpleCallback<List<StopTime>> simpleCallback) {
		ServerCommunication sc = new ServerCommunicationProxy(session,new ServerCommunicationImpl(session));
		sc.addRequest(param);
		sc.doRequests(new ServerDataListener() {

			@Override
			public void dataReceived(List<String> response) {
				DataNotFoundException timesNotFound = new DataNotFoundException(
						"Chargement des horaires",
						"Aucune horaire n'a été trouvée.");
				try {
					if (response.size() == 0)
						throw timesNotFound;
					Object obj = JSONValue.parse(response.get(0));
					JSONObject o = (JSONObject) obj;
					if (o == null || o.get("times") == null) {
						if (o != null && o.get("error") != null)
							throw timesNotFound;
						else
							throw timesNotFound;
					}
					List<StopTime> times = FromJSONFactory.createBusTimes(o);
					simpleCallback.onDataReady(times);
				} catch (DataNotFoundException e) {
					simpleCallback.onDataNotFound(e);
				}
			}

			@Override
			public void onError(String title, String message) {
				simpleCallback.onComError(title, message);
			}

		});
	}
}