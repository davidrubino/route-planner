package pcd.telecomnancy.serverCommunication;

import pcd.telecomnancy.dal.DataNotFoundException;

/**
 * Cette interface fait office de liaison entre la couche de reconstruction 
 * des objets avec les données du serveur et les clients qui attendent les données
 *
 */
public interface Callback {
	/**
	 * Cette fonction d'erreur est appelée lorsque les données attendues n'ont pas été trouvées dans la réponse du serveur
	 * @param e L'exception correspondante à l'erreur
	 */
	public void onDataNotFound(DataNotFoundException e);

	/**
	 * Cette fonction d'erreur est appelée lorsqu'une erreur lors de la communication serveur est survenue
	 * @param title Le titre de l'erreur
	 * @param message Le message de l'erreur
	 */
	public void onComError(String title, String message);
}
