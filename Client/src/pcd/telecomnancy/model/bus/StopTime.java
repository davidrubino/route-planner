package pcd.telecomnancy.model.bus;

/**
 * Représente une horaire pour un arrêt de bus
 */
public class StopTime {

	private int hour, minute;

	/**
	 * Construit une instance d'une horaire
	 * 
	 * @param hour
	 *            L'heure (subit un modulo 24)
	 * @param minute
	 *            Les minutes (0-59)
	 */
	public StopTime(int hour, int minute) {
		this.hour = hour % 24;
		this.minute = minute;
	}

	/**
	 * Accesseur de l'heure (0-23)
	 * 
	 * @return L'heure
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * Accesseur des minutes
	 * 
	 * @return Les minutes (0-59)
	 */
	public int getMinute() {
		return minute;
	}

	public String toString() {
		return hour + ":" + minute;
	}

}
