package pcd.telecomnancy.view.member;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import pcd.telecomnancy.model.member.HistoryTableModel;

public class HistoryTableAdapter extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 1L;

	private HistoryTableModel historyTableModel;
	private String[] columnsName = { "Départ", "Arrivée", "Transport", "Date",
			"Heure", "Durée" };

	public HistoryTableAdapter(HistoryTableModel historyTableModel) {
		this.historyTableModel = historyTableModel;
		historyTableModel.addObserver(this);
	}

	@Override
	public int getColumnCount() {
		return columnsName.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnsName[columnIndex];
	}

	@Override
	public int getRowCount() {
		return historyTableModel.getData().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {

		case 0:
			// Depart
			return historyTableModel.getData().get(rowIndex).getStart()
					.getLabel();

		case 1:
			// Arrivee
			return historyTableModel.getData().get(rowIndex).getEnd()
					.getLabel();

		case 2:
			// Transport
			return historyTableModel.getData().get(rowIndex).getTransportType();

		case 3: // Date
			String date = historyTableModel.getData().get(rowIndex).getDay()
					+ "/"
					+ historyTableModel.getData().get(rowIndex).getMonth()
					+ "/" + historyTableModel.getData().get(rowIndex).getYear();
			return date;

		case 4: // Heure
			int hour = historyTableModel.getData().get(rowIndex).getHour();
			String heure = hour < 10 ? "0" + hour : hour + "";
			int minute = historyTableModel.getData().get(rowIndex).getMinute();
			heure += ":" + (minute < 10 ? "0" + minute : minute);
			return heure;

		case 5: // Duree
			return (((historyTableModel.getData().get(rowIndex).getDuration()) / 60) + 1)
					+ " min";

		default:
			break;
		}
		return "";
	}

	@Override
	public void update(Observable o, Object arg) {
		this.fireTableDataChanged();

	}

}
