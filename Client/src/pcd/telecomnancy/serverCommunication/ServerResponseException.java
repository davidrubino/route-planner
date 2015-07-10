package pcd.telecomnancy.serverCommunication;

public class ServerResponseException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param code Le code d'erreur
	 * @param page La page de l'erreur
	 */
	public ServerResponseException(int code, String page) {
		super("Le serveur a renvoyé une erreur " + code + " pour la page "
				+ page + ".");
	}
}
