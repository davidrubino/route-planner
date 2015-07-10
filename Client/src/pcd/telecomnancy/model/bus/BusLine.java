package pcd.telecomnancy.model.bus;

/**
 * Représente une ligne de bus
 */
public class BusLine {

	private int id;
	private String shortName;
	private String longName;
	private int type;
	private String direction_0;
	private String direction_1;
	private BusStop[] stops;

	/**
	 * Construit une ligne de bus
	 * 
	 * @param id
	 *            L'ID de la ligne
	 * @param shortName
	 *            Son nom court
	 * @param longName
	 *            Son nom long
	 * @param type
	 *            Son type
	 * @param direction_0
	 *            Sa première direction
	 * @param direction_1
	 *            Sa seconde direction
	 */
	public BusLine(int id, String shortName, String longName, int type,
			String direction_0, String direction_1) {
		this.id = id;
		this.shortName = shortName;
		this.longName = longName;
		this.type = type;
		this.direction_0 = direction_0;
		this.direction_1 = direction_1;
		stops = new BusStop[0];
	}

	/**
	 * Accesseur de l'ID de l'adresse
	 * 
	 * @return l'ID de l'adresse
	 */
	public int getId() {
		return id;
	}

	/**
	 * Accesseur du nom court de la ligne
	 * 
	 * @return Le nom court de la ligne
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * Accesseur du nom long de la ligne
	 * 
	 * @return Le nom long de la ligne
	 */
	public String getLongName() {
		return longName;
	}

	/**
	 * Accesseur du type de la ligne
	 * 
	 * @return Le type de la ligne
	 */
	public int getType() {
		return type;
	}

	/**
	 * Accesseur de la première direction de la ligne
	 * 
	 * @return La première direction de la ligne
	 */
	public String getDirection_0() {
		return direction_0;
	}

	/**
	 * Accesseur de la seconde direction de la ligne
	 * 
	 * @return La seconde direction de la ligne
	 */
	public String getDirection_1() {
		return direction_1;
	}

	public String toString() {
		return shortName + " (" + longName + ")";
	}

	/**
	 * Retourne les arrêts de la ligne
	 * 
	 * @return Les arrêts de la ligne
	 */
	public BusStop[] getStops() {
		return stops;
	}

	/**
	 * Modifie les arrêts de la ligne
	 * 
	 * @param stops
	 *            Les arrêts de la ligne
	 */
	public void setStops(BusStop[] stops) {
		this.stops = stops;
	}
}
