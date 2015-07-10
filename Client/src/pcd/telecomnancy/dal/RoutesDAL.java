package pcd.telecomnancy.dal;

import java.util.ArrayList;
import java.util.List;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import pcd.telecomnancy.model.ErrorModel;
import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.model.routes.Route;
import pcd.telecomnancy.serverCommunication.MultipleCallback;
import pcd.telecomnancy.serverCommunication.ServerCommunication;
import pcd.telecomnancy.serverCommunication.ServerCommunicationImpl;
import pcd.telecomnancy.serverCommunication.ServerCommunicationProxy;
import pcd.telecomnancy.serverCommunication.ServerDataListener;
import pcd.telecomnancy.serverCommunication.param.IParam;
import pcd.telecomnancy.serverCommunication.param.RoutesParam;

/**
 * Classe permettant de construire la liste des trajets à partir des données du
 * serveur
 **/
public class RoutesDAL extends DAL<MultipleCallback<Route>> {

	private RoutesParam rParam;

	public RoutesDAL(Session session, ErrorModel errorModel,final RoutesParam rParam) {
		super(session, errorModel);
		this.rParam = rParam;
		// TODO Auto-generated constructor stub
	}

	public void find(final IParam param, final MultipleCallback<Route> callback) {
		final List<Route> result = new ArrayList<Route>();
		ServerCommunication sc = new ServerCommunicationProxy(session,new ServerCommunicationImpl(session));
		sc.addRequest(param);
		sc.doRequests(new ServerDataListener() {

			@Override
			public void dataReceived(List<String> response) {
				// TODO Auto-generated method stub
				DataNotFoundException routeNotFound = new DataNotFoundException(
						"Recherche de trajet", "Aucun trajet n'a été trouvé.");
				try {
					if (response.size() == 0)
						throw routeNotFound;
					Object obj = JSONValue.parse(response.get(0));
					JSONObject o = (JSONObject) obj;
					if (o == null || o.get("route") == null) {
						if (o != null && o.get("error") != null)
							throw routeNotFound;
						else
							throw routeNotFound;
					}
					JSONArray routes = (JSONArray) o.get("route");

					int routeNb;
					try {
						routeNb = Integer.valueOf(routes.get(0).toString());
					} catch (NumberFormatException e) {
						throw routeNotFound;
					}
					Route newRoute;
					JSONObject route;
					for (int i = 1; i <= routeNb; i++) {
						route = (JSONObject) routes.get(i);
						newRoute = FromJSONFactory.createRoute(route);
						newRoute.setDateHour(rParam.getDay(),
								rParam.getMonth(), rParam.getYear(),
								rParam.getHour(), rParam.getMinute());
						newRoute.setTransportType(rParam.getTransportType());
						result.add(newRoute);
					}
					callback.onDataReady(result);
				} catch (DataNotFoundException e) {
					callback.onDataNotFound(e);
				}

			}

			@Override
			public void onError(String title, String message) {
				// TODO Auto-generated method stub
				callback.onComError(title, message);
			}

		});
	}

}
