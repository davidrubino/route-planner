package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.util.Properties;



/**
 * Représente les paramètres de la requête pour récupérer les horaires d'un
 * arrêt de ligne de bus
 */
public class BusStopParam implements IParam {

	private String url;
	private int line_id, direction, stop_id;

	public BusStopParam(Properties properties) {
		this.url = properties.getProperty("url");
	}

	@Override
	public String getPostString() throws UnsupportedEncodingException {
		return null;
	}

	@Override
	public String getPage() {
		return "/api/bus/" + line_id + "/" + direction + "/" + stop_id;
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
		return "Récupération des horaires de bus sur " + getUrl() + getPage();
	}

	/**
	 * Mutateur de l'ID de la ligne correspondant pour la requête
	 * 
	 * @param line_id
	 *            L'ID de la ligne
	 */
	public void setLine_id(int line_id) {
		this.line_id = line_id;
	}

	/**
	 * Mutateur de la direction de la ligne correspondant pour la requête
	 * 
	 * @param direction
	 *            La direction de la ligne
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * Mutateur de l'ID de l'arrêt de la ligne correspondant pour la requête
	 * 
	 * @param stop_id
	 *            L'ID de l'arrêt de la ligne
	 */
	public void setStop_id(int stop_id) {
		this.stop_id = stop_id;
	}
}
