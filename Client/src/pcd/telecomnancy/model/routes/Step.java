package pcd.telecomnancy.model.routes;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class Step extends Coordinate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String action;
	private double lat, lon;

	public Step(String action, double lat, double lon) {
		super(0, 0);
		this.action = action;
		this.lat = lat;
		this.lon = lon;
		// TODO Auto-generated constructor stub
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
}
