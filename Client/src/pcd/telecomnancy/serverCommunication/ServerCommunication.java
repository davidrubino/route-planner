package pcd.telecomnancy.serverCommunication;

import pcd.telecomnancy.serverCommunication.param.IParam;


public interface ServerCommunication {
	/**
	 * Ajoute une requête à la liste des requêtes à effectuer
	 * @param param Les paramètres de la requête
	 */
	public void addRequest(IParam param);

	/**
	 * Effectue les requêtes ajoutés
	 * @param serverDataListener L'instance sur laquelle le callback va se faire lorsque les requêtes seront terminées
	 */
	public void doRequests(ServerDataListener serverDataListener);
}
