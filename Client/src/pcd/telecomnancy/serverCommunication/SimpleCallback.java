package pcd.telecomnancy.serverCommunication;

public interface SimpleCallback<T> extends Callback {
	/**
	 * Cette fonction est appelée lorsque la couche construction des objets a reconstruit un objet à partir des données du serveur
	 * @param object L'objet construit
	 */
	public void onDataReady(T object);
}
