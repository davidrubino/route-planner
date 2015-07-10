package pcd.telecomnancy.model.bus;

import java.util.ArrayList;
import java.util.List;


/**
 * Représente un arrêt de bus d'une ligne
 */
public class BusStop {

	private int id;
	private String name;
	private double lat;
	private double lon;
	private List<StopTime> times;

	/**
	 * Construit un arrêt de bus
	 * 
	 * @param id
	 *            Son ID
	 * @param name
	 *            Son nom
	 * @param lat
	 *            Sa latitude
	 * @param lon
	 *            Sa longitude
	 */
	public BusStop(int id, String name, double lat, double lon) {
		this.id = id;
		this.name = name;
		this.lat = lat;
		this.lon = lon;
		times = new ArrayList<StopTime>();
	}

	public String toString() {
		return name;
	}

	/**
	 * Accesseur de l'ID de l'arrêt
	 * 
	 * @return L'ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Accesseur du nom de l'arrêt
	 * 
	 * @return Son nom
	 */
	public String getName() {
		return name;
	}

	/**
	 * Accesseur de la latitude de l'arrêt
	 * 
	 * @return La latitude
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * Accesseur de la longitude de l'arrêt
	 * 
	 * @return La longitude
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * Accesseur des horaires de l'arrêts
	 * 
	 * @return Les horaires
	 */
	public List<StopTime> getTimes() {
		return times;
	}

	/**
	 * Mutateur des horaires de l'arrêt
	 * 
	 * @param times
	 *            Les nouveaux horaires
	 */
	public void setTimes(List<StopTime> times) {
		this.times = times;
	}
}
