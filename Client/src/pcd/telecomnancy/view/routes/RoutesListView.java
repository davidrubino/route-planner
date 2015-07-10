package pcd.telecomnancy.view.routes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.model.routes.Route;
import pcd.telecomnancy.model.routes.RoutesListModel;
import pcd.telecomnancy.serverCommunication.param.RoutesParam;
import pcd.telecomnancy.view.Frame;


public class RoutesListView extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RoutesListModel model;
	private JList<Route> list;;
	private JLabel description;
	private JPanel submitPanel;
	private JButton submitButton;
	private RoutesParam routesParam;

	public RoutesListView(Model model, RoutesListController routesListController) {
		this.setLayout(new BorderLayout());
		this.model = model.getRoutesListModel();
		this.routesParam = model.getApiParams().getRouteParam();
		this.model.addObserver(this);
		this.setMinimumSize(new Dimension(200, 0));

		this.description = new JLabel("");
		description.setBorder(BorderFactory.createEmptyBorder(5, 20, 0, 0));
		description.setOpaque(true);
		description.setBackground(new Color(220, 220, 220));
		this.add(description, BorderLayout.NORTH);
		list = new JList<Route>(new RoutesListAdapter(this.model));
		list.setCellRenderer(new RoutesListCellRenderer());
		submitPanel = new JPanel();
		submitButton = new JButton("Valider l'itinéraire");
		submitButton.addActionListener(routesListController);
		submitPanel.add(submitButton);
		this.add(list, BorderLayout.CENTER);
		this.add(submitPanel, BorderLayout.SOUTH);
	}

	public RoutesListModel getModel() {
		return this.model;
	}

	public JList<Route> getJList() {
		return list;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// parent.getTabbedPane().setSelectedIndex(1);
				RoutesParam param = routesParam;
				String minute = param.getMinute() < 10 ? "0"
						+ param.getMinute() : param.getMinute() + "";
				description.setText("<html>De " + param.getStart().getLabel()
						+ "<br />à " + param.getEnd().getLabel() + "<br />("
						+ param.getDay() + "/" + (param.getMonth()+1) + "/"
						+ param.getYear() + " " + param.getHour() + ":"
						+ minute + ")</html>");

				// changement onglet
				frame.switchToRoutesList();
			}

		});

	}
	private Frame frame;

	public void setMainFrame(Frame frame) {
		// TODO Auto-generated method stub
		this.frame=frame;
	}
}
