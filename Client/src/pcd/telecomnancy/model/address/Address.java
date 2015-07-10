package pcd.telecomnancy.model.address;

public class Address {

	private int id;
	private String label;
	private double lat;
	private double lon;
	private int type;
	private String typeStr;
	private String infos;

	/**
	 * Construit une instance d'une adresse
	 * 
	 * @param id
	 *            L'id de l'adresse (si connu, sinon null)
	 * @param label
	 *            Le libellé de l'adresse
	 * @param lat
	 *            La latitude
	 * @param lon
	 *            La longitude
	 * @param infos
	 *            Les détails complémentaires de l'adresse (ligne de bus s'il
	 *            s'agit d'un arrêt)
	 **/
	public Address(int id, String label, double lat, double lon, String infos) {
		this.id = id;
		this.label = label;
		this.lat = lat;
		this.lon = lon;
		this.infos = infos;
	}

	/**
	 * Construit une instance d'une adresse
	 * 
	 * @param id
	 *            L'id de l'adresse (si connu, sinon null)
	 * @param label
	 *            Le libellé de l'adresse
	 * @param lat
	 *            La latitude
	 * @param lon
	 *            La longitude
	 * @param infos
	 *            Les détails complémentaires de l'adresse (ligne de bus s'il
	 *            s'agit d'un arrêt)
	 * @param type
	 *            Le type de l'adresse sous la forme nombre entier
	 * @param typeStr
	 *            Le type de l'adresse sous forme chaîne de caractères
	 **/
	public Address(int id, String label, double lat, double lon, String infos,
			int type, String typeStr) {
		this(id, label, lat, lon, infos);
		this.type = type;
		this.typeStr = typeStr;
	}

	/** Construit une adresse vide **/
	public Address() {
		this.label = "";
	}

	/**
	 * Mutateur du libellé de l'adresse
	 * 
	 * @param label
	 *            Le nouveau libellé de l'adresse
	 **/
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Accesseur du libellé de l'adresse
	 * 
	 * @return label Le libellé
	 **/
	public String getLabel() {
		return this.label;
	}

	/**
	 * Accesseur du type de l'adresse
	 * 
	 * @return type Le type
	 **/
	public int getType() {
		return type;
	}

	/**
	 * Accesseur du type de l'adresse
	 * 
	 * @return typeStr Le type
	 **/
	public String getTypeStr() {
		return typeStr;
	}

	/**
	 * Accesseur de la latitude de l'adresse
	 * 
	 * @return lat La latitude
	 **/
	public double getLat() {
		return lat;
	}

	/**
	 * Accesseur de la latitude de l'adresse
	 * 
	 * @return lon La longitude
	 **/
	public double getLon() {
		return lon;
	}

	/**
	 * Mutateur de la latitude de l'adresse
	 * 
	 * @param lat
	 *            La nouvelle latitude
	 **/
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * Mutateur de la longitude de l'adresse
	 * 
	 * @param lon
	 *            La nouvelle longitude
	 **/
	public void setLon(double lon) {
		this.lon = lon;
	}

	/**
	 * Accesseur de l'ID de l'adresse
	 * 
	 * @return id L'ID
	 **/
	public int getId() {
		return id;
	}

	/**
	 * Accesseur des détails complémentaires de l'adresse, comme les lignes de
	 * bus s'il s'agit d'un arrêt
	 * 
	 * @return infos Les détails
	 **/
	public String getInfos() {
		return infos;
	}

	/**
	 * Mutateur des détails de l'adresse
	 * 
	 * @param infos
	 *            les nouveaux détails
	 **/
	public void setInfos(String infos) {
		this.infos = infos;
	}

}
