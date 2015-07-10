package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.util.Properties;



/**
 * Représente les paramètres d'une requête pour récupérer les informations basiques sur les lignes du réseau de bus
 */
public class BusNetworkParam implements IParam{

	private String url;
	
	public BusNetworkParam(Properties properties) {
		this.url = properties.getProperty("url");
	}

	@Override
	public String getPostString() throws UnsupportedEncodingException {
		return null;
	}

	@Override
	public String getPage() {
		return "/api/bus";
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getRequestMethod() {
		return "GET";
	}

	@Override
	public void buildPart(URLConnection con) {
	}

	@Override
	public String getRequestDescription() {
		return "Récupération des lignes de bus sur "+getUrl()+getPage();
	}

}
