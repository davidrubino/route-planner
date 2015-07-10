package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;



public class RegistrationParam implements IParam {

	private String login, password, mail, name, firstName;
	private String url;

	public RegistrationParam(Properties properties) {
		this.url = properties.getProperty("url");
	}

	public String getMail() {
		return mail;
	}

	public String getName() {
		return name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getPostString() throws UnsupportedEncodingException {
		return "login=" + URLEncoder.encode(login, "UTF-8") + "&password="
				+ URLEncoder.encode(password, "UTF-8") + "&mail="
				+ URLEncoder.encode(mail, "UTF-8") + "&name="
				+ URLEncoder.encode(name, "UTF-8") + "&firstName="
				+ URLEncoder.encode(firstName, "UTF-8");
	}

	@Override
	public String getPage() {
		return "/register";
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

	@Override
	public String getRequestDescription() {
		return "Inscription sur " + getUrl() + getPage();
	}

}
