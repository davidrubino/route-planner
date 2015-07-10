package pcd.telecomnancy.dal;

import pcd.telecomnancy.model.ErrorModel;
import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.serverCommunication.Callback;
import pcd.telecomnancy.serverCommunication.param.IParam;

/**
 * Représente un objet de la couche construction des objets avec les informations provenant du serveur
 * @param <C> Un type de callback où les fonctions de callback seront appelées via l'instance passée en paramètre de la fonction find
 */
public abstract class DAL<C extends Callback> {
	protected ErrorModel errorModel;
	protected Session session;

	public DAL(Session session,ErrorModel errorModel) {
		this.session=session;
		this.errorModel = errorModel;
	}

	/**
	 * Cette fonction demande à la couche reconstruction d'objet de construire un objet ou une liste d'objet en interrogeant le serveur
	 * @param param Le paramêtre de la requête
	 * @param callback L'instance sur laquelle va être appelé la fonction de callback
	 */
	public abstract void find(final IParam param, final C callback);
}
