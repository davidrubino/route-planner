package pcd.telecomnancy.view.member;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class FavoriteTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		Component cell = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		if (isSelected) {
			this.setBackground(new Color(35, 80, 255));
		} else {
			this.setBackground(getColorByIndex(row));
		}

		JComponent jcell = (JComponent) cell;
		jcell.setToolTipText(table.getValueAt(row, column).toString());

		this.setHorizontalAlignment(JLabel.CENTER);
		((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
				.setHorizontalAlignment(JLabel.CENTER);

		table.setShowVerticalLines(false);
		table.setGridColor(Color.LIGHT_GRAY);
		table.setRowHeight(30);
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);

		return cell;
	}

	private Color getColorByIndex(int i) {
		return i % 2 != 0 ? Color.white : new Color(235, 235, 255);
	}

}