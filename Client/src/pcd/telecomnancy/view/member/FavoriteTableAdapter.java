package pcd.telecomnancy.view.member;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import pcd.telecomnancy.model.member.FavoriteTableModel;

public class FavoriteTableAdapter extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 1L;

	private FavoriteTableModel favoriteTableModel;
	private String[] columnsName = { "Départ", "Arrivée", "Transport"};

	public FavoriteTableAdapter(FavoriteTableModel favoriteTableModel) {
		this.favoriteTableModel = favoriteTableModel;
		favoriteTableModel.addObserver(this);
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
		return favoriteTableModel.getData().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {

		case 0:
			// Depart
			return favoriteTableModel.getData().get(rowIndex).getStart()
					.getLabel();

		case 1:
			// Arrivee
			return favoriteTableModel.getData().get(rowIndex).getEnd()
					.getLabel();

		case 2:
			// Transport
			return favoriteTableModel.getData().get(rowIndex).getTransportType();
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
