package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.util.Properties;



/**
 * Représente un paramètre de requête pour récupérer les informations et arrêts d'une ligne de bus
 */
public class BusLineParam implements IParam {

	private String url;
	private int line_id, direction;

	public BusLineParam(Properties properties) {
		url = properties.getProperty("url");
	}

	@Override
	public String getPostString() throws UnsupportedEncodingException {
		return null;
	}

	@Override
	public String getPage() {
		return "/api/bus/" + line_id + "/" + direction;
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
		return "Récupération des arrêt de bus sur " + getUrl() + getPage();
	}

	/**
	 * Mutateur de l'ID de la ligne de bus correspondant à ce paramètre de requête
	 * @param line_id
	 */
	public void setLine_id(int line_id) {
		this.line_id = line_id;
	}

	/**
	 * Mutateur de la direction de la ligne de bus correspondant à ce paramètre de requête
	 * @param direction
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}
}
