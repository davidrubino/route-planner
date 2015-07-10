package pcd.telecomnancy.dal;

import java.util.List;


import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.serverCommunication.ServerCommunication;
import pcd.telecomnancy.serverCommunication.ServerCommunicationImpl;
import pcd.telecomnancy.serverCommunication.ServerCommunicationProxy;
import pcd.telecomnancy.serverCommunication.ServerDataListener;
import pcd.telecomnancy.serverCommunication.SimpleCallback;
import pcd.telecomnancy.serverCommunication.param.IParam;

/**
 * Classe envoyant au serveur les informations d'un membre lors de son inscription
 */
public class Registration {

	private final Session session;

	public Registration(Session session) {
		this.session = session;
	}

	public Session getSession() {
		return session;
	}

	/**
	 * Cette fonction permet de créer la requête pour l'inscription d'un utilisateur à envoyer au serveur
	 * @param param Les paramètres de la requête
	 * @param callback Les paramètres de liaison
	 */
	public void create(IParam param, final SimpleCallback<String> callback) {
		ServerCommunication sc = new ServerCommunicationProxy(session,new ServerCommunicationImpl(session));
		sc.addRequest(param);
		sc.doRequests(new ServerDataListener() {

			@Override
			public void dataReceived(List<String> response) {
				if (response.get(0) != null) {
					JSONObject jobj = (JSONObject) JSONValue.parse(response
							.get(0));
					if (jobj != null && jobj.get("success") != null) {
						if (jobj.get("success").toString().equals("0")
								&& jobj.get("message") != null) {
							callback.onDataNotFound(new DataNotFoundException(
									"Inscription", jobj.get("message")
											.toString()));
						} else if (jobj.get("success").toString().equals("1")
								&& jobj.get("message") != null) {
							callback.onDataReady(jobj.get("message").toString());
						}
					}
				}
			}

			@Override
			public void onError(String title, String message) {
				callback.onComError(title, message);
			}
		});

	}
}
