package pcd.telecomnancy.view.bus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class StopTimesTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	private Font normalFont = new Font("Verdana", Font.PLAIN, 12);
	private Font boldFont = new Font("Verdana", Font.BOLD, 12);

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel cell = (JLabel) super.getTableCellRendererComponent(table,
				value, isSelected, hasFocus, row, column);
		if (column == 0) {
			cell.setHorizontalAlignment(JLabel.CENTER);
			cell.setFont(boldFont);
		} else {
			cell.setHorizontalAlignment(JLabel.LEFT);
			cell.setFont(normalFont);
		}
		cell.setToolTipText(value.toString());

		cell.setForeground(Color.decode("#111111"));
		return cell;
	}

}
