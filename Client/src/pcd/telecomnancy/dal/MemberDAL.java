package pcd.telecomnancy.dal;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import pcd.telecomnancy.model.ErrorModel;
import pcd.telecomnancy.model.member.Favorite;
import pcd.telecomnancy.model.member.Member;
import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.model.routes.Route;
import pcd.telecomnancy.serverCommunication.ServerCommunication;
import pcd.telecomnancy.serverCommunication.ServerCommunicationImpl;
import pcd.telecomnancy.serverCommunication.ServerCommunicationProxy;
import pcd.telecomnancy.serverCommunication.ServerDataListener;
import pcd.telecomnancy.serverCommunication.SimpleCallback;
import pcd.telecomnancy.serverCommunication.param.IParam;

/**
 * Classe permettant de constuire un membre, ses favoris, historique lors de sa
 * connection et les de mettre à jour sur le serveur
 */
public class MemberDAL extends DAL<SimpleCallback<Member>> {

	public MemberDAL(Session session, ErrorModel errorModel) {
		super(session, errorModel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void find(IParam param, final SimpleCallback<Member> callback) {
		// TODO Auto-generated method stub
		ServerCommunication sc = new ServerCommunicationProxy(this.session,new ServerCommunicationImpl(session));
		sc.addRequest(param);
		sc.doRequests(new ServerDataListener() {

			@Override
			public void dataReceived(List<String> response) {
				// TODO Auto-generated method stub
				// System.out.println(response);
				if (response.get(0) != null) {
					Object obj = JSONValue.parse(response.get(0));
					JSONObject jobj = (JSONObject) obj;
					if (jobj.get("success") != null) {
						if (jobj.get("success").toString().equals("0")) {
							callback.onDataNotFound(new DataNotFoundException(
									"Connexion", jobj.get("message").toString()));
						} else if (jobj.get("member") != null) {
							JSONObject memberJson = (JSONObject) jobj
									.get("member");
							Member member = new Member(memberJson.get("login")
									.toString());
							// System.out.println(memberJson);
							member.setFavorites(buildFavorites(memberJson));
							member.setHistory(buildHistory(memberJson));
							session.setMember(member);
							callback.onDataReady(member);
						}
					} else {
						callback.onDataNotFound(new DataNotFoundException(
								"Connexion",
								"Les données reçues du serveur n'ont pas pu être interprétées"));
					}
				} else {
					callback.onDataNotFound(new DataNotFoundException(
							"Connexion",
							"Les données reçues du serveur n'ont pas pu être interprétées"));
				}
			}

			@Override
			public void onError(String title, String message) {
				// TODO Auto-generated method stub
				callback.onComError(title, message);
			}

			private List<Favorite> buildFavorites(JSONObject memberJson) {
				JSONArray favorites = (JSONArray) memberJson.get("favorites");
				JSONObject fav;
				List<Favorite> favs = new ArrayList<Favorite>();
				for (int i = 0; i < Integer.valueOf(memberJson.get("fnb")
						.toString()); i++) {
					fav = (JSONObject) favorites.get(i);
					favs.add(FromJSONFactory.createFavorite(fav));
				}

				return favs;
			}

			private List<Route> buildHistory(JSONObject memberJson) {
				JSONArray history = (JSONArray) memberJson.get("history");
				JSONObject hist;
				List<Route> hists = new ArrayList<Route>();
				for (int i = 0; i < Integer.valueOf(memberJson.get("hnb")
						.toString()); i++) {
					hist = (JSONObject) history.get(i);
					hists.add(FromJSONFactory.createRoute(hist));
				}
				return hists;
			}

		});
	}

	/**
	 * Met à jour le favoris ou l'historique du membre
	 * 
	 * @param param
	 *            Les paramètres de la requête
	 * @param callback
	 *            L'instance sur laquelle sera appelée la fonction de callback
	 */
	public void update(IParam param, final SimpleCallback<Object> callback) {
		ServerCommunication sc = new ServerCommunicationProxy(session,new ServerCommunicationImpl(session));
		sc.addRequest(param);
		sc.doRequests(new ServerDataListener() {
			@Override
			public void dataReceived(List<String> response) {
				// TODO Auto-generated method stub
				if (response.get(0) != null) {
					Object obj = JSONValue.parse(response.get(0));
					JSONObject jobj = (JSONObject) obj;
					if (jobj != null && jobj.get("success") != null) {

						if (jobj.get("success").toString().equals("0")) {
							callback.onDataNotFound(new DataNotFoundException(
									"Favoris", jobj.get("message").toString()));
						} else if (jobj.get("success").toString().equals("1")) {
							callback.onDataReady(null);
						}
					}
				} else {
					callback.onDataNotFound(new DataNotFoundException("", ""));
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
