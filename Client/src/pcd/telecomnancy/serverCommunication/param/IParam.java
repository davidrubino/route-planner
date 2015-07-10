package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;
import java.net.URLConnection;

/**
 * 
 * Cette interface spécifie les paramètres d'une requête pour une communication serveur
 *
 */
public interface IParam {
	
	/**
	 * Retourne une chaine de caractère encodée en UTF8 en tant que paramètre de la requête http POST
	 * @return La chaine encodée
	 * @throws UnsupportedEncodingException
	 */
	public String getPostString() throws UnsupportedEncodingException;

	/**
	 * Retourne La page web sur laquelle on effectue la requête
	 * @return le nom de la page
	 */
	public String getPage();

	/**
	 * Retourne le domaine/ip de la page où on effetue la requête
	 * @return le domaine/ip
	 */
	public String getUrl();

	/**
	 * Retourne le type de requête (POST,GET)
	 * @return le type de requête
	 */
	public String getRequestMethod();

	/**
	 * Ajoute des paramètres à la connexion http comme les headers
	 * @param con La connexion
	 */
	public void buildPart(URLConnection con);
	
	/**
	 * Retourne une description de la requête
	 * @return La description de la requête
	 */
	public String getRequestDescription();
}
