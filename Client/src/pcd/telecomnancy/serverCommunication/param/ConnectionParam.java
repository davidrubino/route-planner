package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;



public class ConnectionParam implements IParam {

	private String login, password;
	private String url;

	public ConnectionParam(Properties properties) {
		this.url = properties.getProperty("url");
	}

	/**
	 * Accesseur du login saisi dans le formulaire
	 * @return Le login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Accesseur du mot de passe saisi dans le formulaire
	 * @return Le mot de passe
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Mutateur du login de la connexion
	 * @param login
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	@Override
	public String getPostString() throws UnsupportedEncodingException {
		return "login=" + URLEncoder.encode(login, "UTF-8") + "&password="
				+ URLEncoder.encode(password, "UTF-8");
	}

	@Override
	public String getPage() {
		return "/connect";
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getRequestMethod() {
		return "POST";
	}

	@Override
	public void buildPart(URLConnection con) {
		con.setRequestProperty("User-Agent", "Mozilla/24.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	}

	/**
	 * Mutateur du mot de passe de la connexion
	 * @param password Le mot de passe
	 */
	public void setPassword(String password) {
		this.password=password;
	}

	@Override
	public String getRequestDescription() {
		return "Tentative de connexion sur "+getUrl()+getPage();
	}
}
