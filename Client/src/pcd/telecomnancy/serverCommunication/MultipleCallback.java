package pcd.telecomnancy.serverCommunication;

import java.util.List;

public interface MultipleCallback<T> extends Callback {
	/**
	 * Cette fonction est appelée lorsque la couche construction des objets a reconstruit des objets à partir des données du serveur
	 * @param list La liste d'objets construits
	 */
	public void onDataReady(List<T> list);
}
