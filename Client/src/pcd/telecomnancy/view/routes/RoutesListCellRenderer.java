package pcd.telecomnancy.view.routes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import pcd.telecomnancy.model.routes.Route;
import pcd.telecomnancy.model.routes.Transport;

public class RoutesListCellRenderer extends JPanel implements
		ListCellRenderer<Route> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel durationLabel;
	private JLabel distanceLabel;
	private JPanel transportsPanel;

	public RoutesListCellRenderer() {
		durationLabel = new JLabel();
		distanceLabel = new JLabel();
		transportsPanel = new JPanel();
		this.setLayout(new FlowLayout());
		this.add(transportsPanel);
		this.add(durationLabel);
		this.add(distanceLabel);
		setOpaque(true);
	}

	public void setTextColor(Color c) {
		durationLabel.setForeground(c);
		distanceLabel.setForeground(c);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Route> list,
			Route route, int index, boolean isSelected, boolean hasFocus) {
		// TODO Auto-generated method stub
		transportsPanel.removeAll();
		for (Transport t : route.getTransports()) {
			switch (t.getType()) {
			case 0:
				transportsPanel.add(new JLabel("", new ImageIcon(getClass()
						.getClassLoader().getResource("pedestrian.png")),
						JLabel.CENTER));
				break;
			case 1:
				transportsPanel.add(new JLabel("", new ImageIcon(getClass()
						.getClassLoader().getResource("car.png")),
						JLabel.CENTER));
				break;
			case 2:
				transportsPanel.add(new JLabel("", new ImageIcon(getClass()
						.getClassLoader().getResource("bus.png")),
						JLabel.CENTER));
				break;
			default:
				System.err.println("Type inconnu : " + t.getType());
				break;
			}
		}
		transportsPanel.repaint();
		this.durationLabel
				.setText(Math.round((double) (route.getDuration()) / 60)
						+ "min");

		String distanceStr = route.getDistance()==0 ? "" : route.getDistance() / 1000 > 1 ? String.format(
				"%.1f", ((double) (route.getDistance()) / 1000)) + "km" : route
				.getDistance() + "m";
		this.distanceLabel.setText(distanceStr);

		if (isSelected) {
			this.setBackground(new Color(35, 80, 255));
			this.setBorder(BorderFactory
					.createLineBorder(new Color(0, 50, 255)));
			this.setTextColor(Color.WHITE);
		} else {
			this.setBackground(getColorByIndex(index));
			this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			this.setTextColor(Color.BLACK);
		}
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return this;
	}

	private Color getColorByIndex(int i) {
		return i % 2 != 0 ? Color.white : new Color(225, 225, 255);
	}
}
