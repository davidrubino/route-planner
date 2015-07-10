package pcd.telecomnancy.dal;

/**
 * Exception levée lorsque le serveur a retourné qu'il n'avait pas trouvé de données
 */
public class DataNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	public DataNotFoundException(String title,String msg) {
		super(msg);
		this.title=title;
	}
	/**
	 * Accesseur du titre de l'erreur
	 * @return Le titre
	 */
	public String getTitle() {
		return title;
	}
}
