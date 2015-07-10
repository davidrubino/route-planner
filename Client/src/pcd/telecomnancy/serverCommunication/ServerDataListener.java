package pcd.telecomnancy.serverCommunication;

import java.util.List;

public interface ServerDataListener {
	/**
	 * Cette fonction est appelée lorsque les données provenant du serveur ont été reçues
	 * @param response La liste des réponses
	 */
	public void dataReceived(List<String> response);
	
	/**
	 * Cette fonction est appelée lorsqu'une erreur est survenue lors de la communication serveur 
	 * @param title Le titre de l'erreur
	 * @param message Le message de l'erreur
	 */
	public void onError(String title,String message);
}
