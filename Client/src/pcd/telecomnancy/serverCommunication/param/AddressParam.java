package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;



/**
 * Représente les paramètres d'une requête effectuant une recherche d'adresses
 */
public class AddressParam implements IParam {

	private String entry;
	private final String PAGE = "/getAddress";
	private final String REQUEST_METHOD = "POST";
	private String url;

	public AddressParam(Properties properties) {
		// TODO Auto-generated constructor stub
		this.url = properties.getProperty("url") + "/api";
	}

	/**
	 * Notifie l'adresse complète ou partielle saisi dans le champ du formulaire
	 * 
	 * @param entry
	 *            La saisie
	 */
	public void setEntry(String entry) {
		this.entry = entry;
	}

	/**
	 * Accesseur de la saisie de l'utilisateur dans le champ du formulaire
	 * 
	 * @return La saisie
	 */
	public String getEntry() {
		return entry;
	}

	@Override
	public String getPostString() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return "address=" + URLEncoder.encode(entry, "UTF-8");
	}

	@Override
	public String getPage() {
		// TODO Auto-generated method stub
		return PAGE;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return url;
	}

	@Override
	public void buildPart(URLConnection con) {
		// TODO Auto-generated method stub
		con.setRequestProperty("User-Agent", "Mozilla/24.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	}

	@Override
	public String getRequestMethod() {
		// TODO Auto-generated method stub
		return REQUEST_METHOD;
	}

	@Override
	public String getRequestDescription() {
		// TODO Auto-generated method stub
		return "Récupération des adresses sur " + getUrl() + getPage();
	}

}
