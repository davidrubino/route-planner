package pcd.telecomnancy.serverCommunication;

import java.util.List;

public interface UpdatableFromServerData<T> {
	/**
	 * Modifie la liste des données stockées par l'instance
	 * @param data La liste des données
	 */
	public void setData(List<T> data);

	/**
	 * Retourne la liste des données stockées
	 * @return La liste des données
	 */
	public List<T> getData();
	
	/**
	 * Supprime une donnée de la liste
	 * @param data La donnée à supprimer
	 */
	public void removeData(T data);

}
