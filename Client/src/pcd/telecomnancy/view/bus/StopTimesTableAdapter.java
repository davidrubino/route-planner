package pcd.telecomnancy.view.bus;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import pcd.telecomnancy.model.bus.StopTime;
import pcd.telecomnancy.model.bus.StopTimesTableModel;

/**
 * Adaptateur entre les horaires des arrêts de bus et le modèle du tableau qui les affiche
 */
public class StopTimesTableAdapter extends AbstractTableModel implements
		Observer {

	private static final long serialVersionUID = 1L;

	private StopTimesTableModel stopTimesTableModel;
	private final String[] columnNames = new String[] { "Heures", "Minutes" };

	/**
	 * Construit une instance de l'adaptateur
	 * @param stopTimesTableModel Le modèle qui gère les horaires à afficher
	 */
	public StopTimesTableAdapter(StopTimesTableModel stopTimesTableModel) {
		this.stopTimesTableModel = stopTimesTableModel;
		this.stopTimesTableModel.addObserver(this);
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public int getRowCount() {
		return hours == null ? 0 : hours.size();
	}

	@Override
	public Object getValueAt(int i, int j) {
		if (j == 0) {
			return hours.get(i) + "h";
		} else {
			return minutes.get(i);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.splitHoursMinutes(this.stopTimesTableModel.getData());
		this.fireTableDataChanged();
	}

	private List<Integer> hours;
	private List<List<Integer>> minutes;

	/**
	 * Cette fonction est appelée lors de la séparation heures et minutes pour l'affichage dans la JTable
	 * @param stopTimes La liste des horaires de bus
	 */
	public void splitHoursMinutes(List<StopTime> stopTimes) {
		hours = new ArrayList<Integer>();
		minutes = new ArrayList<List<Integer>>();
		for (int i = 0; i < stopTimes.size(); i++) {
			if (!hours.contains(stopTimes.get(i).getHour())) {
				hours.add(stopTimes.get(i).getHour());
			}
		}

		boolean newHour = true;
		List<Integer> minutesForOneHour = null;
		for (int j = 0; j < stopTimes.size(); j++) {
			if (newHour) {
				newHour = false;
				if (j != 0)
					minutes.add(minutesForOneHour);
				minutesForOneHour = new ArrayList<Integer>() {
					private static final long serialVersionUID = 1L;

					public String toString() {
						String s = "";
						for (int m : this)
							s += m + "  ";
						return s;
					}
				};
			}
			minutesForOneHour.add(stopTimes.get(j).getMinute());
			if (j + 1 < stopTimes.size()
					&& stopTimes.get(j + 1).getHour() != stopTimes.get(j)
							.getHour())
				newHour = true;
		}
		minutes.add(minutesForOneHour);
	}
}
