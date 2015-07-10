package pcd.telecomnancy.dal;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import pcd.telecomnancy.model.ErrorModel;
import pcd.telecomnancy.model.address.Address;
import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.serverCommunication.MultipleCallback;
import pcd.telecomnancy.serverCommunication.ServerCommunicationImpl;
import pcd.telecomnancy.serverCommunication.ServerCommunicationProxy;
import pcd.telecomnancy.serverCommunication.ServerDataListener;
import pcd.telecomnancy.serverCommunication.param.IParam;

/**
 * Classe permettant de construire les objets Address à partir des données du
 * serveur
 **/
public class AddressDAL extends DAL<MultipleCallback<Address>> {

	/**
	 * Construit une instance de la couche DAL (Data Access Layer) permettant de
	 * construire des adresses
	 * 
	 * @param session
	 *            La session de l'utisateur
	 * @param errorModel
	 *            Le modèle d'erreur
	 * **/
	public AddressDAL(Session session, ErrorModel errorModel) {
		super(session, errorModel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void find(IParam param, final MultipleCallback<Address> callback) {
		// TODO Auto-generated method stub
		final List<Address> result = new ArrayList<Address>();
		ServerCommunicationProxy sc = new ServerCommunicationProxy(this.session,new ServerCommunicationImpl(session));
		sc.addRequest(param);
		sc.doRequests(new ServerDataListener() {

			@Override
			public void dataReceived(List<String> response) {
				// TODO Auto-generated method stub
				DataNotFoundException AddressNotFound = new DataNotFoundException(
						"Recherche d'adresse", "");
				// System.out.println(response.get(0));
				try {
					if (response.size() == 0)
						throw AddressNotFound;
					Object obj = JSONValue.parse(response.get(0));
					JSONObject array = (JSONObject) obj;
					if (array == null || array.get("address") == null
							|| array.get("place") == null) {
						if (array != null && array.get("error") != null)
							throw new DataNotFoundException("Adresse", array
									.get("error").toString());
						else
							throw AddressNotFound;
					} else if (Integer.parseInt(((JSONArray) array
							.get("address")).get(0).toString()) == 0
							&& Integer.parseInt(((JSONArray) array.get("place"))
									.get(0).toString()) == 0) {
						throw AddressNotFound;
					}

					JSONArray address = ((JSONArray) array.get("address"));
					JSONArray places = ((JSONArray) array.get("place"));
					Address a;

					JSONObject o;
					for (int i = 1; i <= Integer.valueOf(address.get(0)
							.toString()); i++) {
						o = ((JSONObject) address.get(i));

						a = FromJSONFactory.createAddress(o);
						result.add(a);
					}

					for (int i = 1; i <= Integer.valueOf(places.get(0)
							.toString()); i++) {
						o = ((JSONObject) places.get(i));

						a = FromJSONFactory.createAddressWithType(o);
						result.add(a);
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
