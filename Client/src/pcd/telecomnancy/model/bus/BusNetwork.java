package pcd.telecomnancy.model.bus;

import java.util.List;
import java.util.Observable;


/**
 * Représente le réseau de lignes de bus
 */
public class BusNetwork extends Observable {

	private BusLine[] lines;

	/**
	 * Construit un réseau vide
	 */
	public BusNetwork() {
		lines = new BusLine[0];
	}

	/**
	 * Accesseur des lignes de bus
	 * 
	 * @return Les lignes de bus
	 */
	public BusLine[] getLines() {
		return lines;
	}

	/**
	 * Mutateur des lignes de bus
	 * 
	 * @param lines
	 *            Les lignes de bus du réseau
	 */
	public void setLines(BusLine[] lines) {
		this.lines = lines;
		setChanged();
		notifyObservers("network");
	}

	/**
	 * Mutateur des arrêts d'une ligne de bus du réseau
	 * 
	 * @param stops
	 *            Les arrêts de la ligne
	 * @param index
	 *            L'index de la ligne de bus
	 */
	public void setStops(BusStop[] stops, int index) {
		this.lines[index].setStops(stops);
		setChanged();
		notifyObservers("line-" + index);
	}

	/**
	 * Mutateur des horaires d'un arrêt d'une ligne de bus du réseau
	 * 
	 * @param times
	 *            Les horaires
	 * @param lineIndex
	 *            L'index de la ligne
	 * @param stopIndex
	 *            L'index de l'arrêt
	 */
	public void setTimes(List<StopTime> times, int lineIndex, int stopIndex) {
		this.lines[lineIndex].getStops()[stopIndex].setTimes(times);
		setChanged();
		notifyObservers("stop-" + lineIndex + "-" + stopIndex);
	}

}
