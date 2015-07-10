package pcd.telecomnancy.view.bus;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import pcd.telecomnancy.dal.DataNotFoundException;
import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.model.bus.BusLine;
import pcd.telecomnancy.model.bus.BusNetwork;
import pcd.telecomnancy.model.bus.BusStop;
import pcd.telecomnancy.model.bus.StopTime;
import pcd.telecomnancy.serverCommunication.SimpleCallback;
import pcd.telecomnancy.serverCommunication.param.BusLineParam;
import pcd.telecomnancy.serverCommunication.param.BusNetworkParam;
import pcd.telecomnancy.serverCommunication.param.BusStopParam;
import pcd.telecomnancy.view.LoadingView;


public class BusLinesView extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;

	private JComboBox<BusLine> linesView;
	private JComboBox<BusStop> stopsView;
	private JComboBox<String> directionsView;
	private JLabel linesLabel, directionsLabel, stopsLabel;
	private Model model;
	private BusNetwork network;
	private JTable stopTimes;

	/**
	 * Construit une instance du panneau permettant de visualier les horaires
	 * des arrêts des lignes de bus
	 * 
	 * @param model
	 *            Le modèle global permettant l'accès à tous les modèles
	 */
	public BusLinesView(final Model model) {
		this.model = model;
		this.network = model.getBusNetwork();
		model.getBusNetwork().addObserver(this);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		linesView = new JComboBox<BusLine>();
		stopsView = new JComboBox<BusStop>();
		directionsView = new JComboBox<String>();

		linesView.setMaximumSize(new Dimension(300, 20));
		stopsView.setMaximumSize(new Dimension(300, 20));
		directionsView.setMaximumSize(new Dimension(300, 20));

		linesLabel = new JLabel("Choisissez votre ligne: ", JLabel.TRAILING);
		stopsLabel = new JLabel("Choisissez votre arret: ", JLabel.TRAILING);
		directionsLabel = new JLabel("Choisissez votre direction: ",
				JLabel.TRAILING);

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(3, 2));
		formPanel.add(linesLabel);
		formPanel.add(linesView);
		formPanel.add(directionsLabel);
		formPanel.add(directionsView);
		formPanel.add(stopsLabel);
		formPanel.add(stopsView);
		this.add(formPanel);

		stopTimes = new JTable(new StopTimesTableAdapter(
				this.model.getStopTimesTableModel()));
		stopTimes.setDefaultRenderer(Object.class,
				new StopTimesTableCellRenderer());
		stopTimes.setColumnSelectionAllowed(false);
		stopTimes.getColumnModel().getColumn(0).setMaxWidth(100);

		JScrollPane listPanel = new JScrollPane(stopTimes);
		this.add(listPanel);

		this.onLoad();
		linesView.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						final int index = linesView.getSelectedIndex();
						directionsView.removeAllItems();
						directionsView.addItem(network.getLines()[index]
								.getDirection_0());
						directionsView.addItem(network.getLines()[index]
								.getDirection_1());
					}
				});
			}

		});

		directionsView.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					final int index = linesView.getSelectedIndex();

					BusLineParam busLineParam = model.getApiParams()
							.getBusLineParam();
					int direction = directionsView.getSelectedIndex();
					busLineParam.setDirection(direction);
					busLineParam.setLine_id(network.getLines()[index].getId());
					final LoadingView loading = new LoadingView(
							BusLinesView.this);
					loading.beginLoading();
					model.getBusNetworkDAL().loadStops(busLineParam,
							new SimpleCallback<BusStop[]>() {

								@Override
								public void onDataNotFound(
										DataNotFoundException e) {
									model.getErrorModel().addInfos(
											e.getTitle(), e.getMessage());
									model.getErrorModel().validate();
									model.getSession()
											.getCurrentOnlineRequest().clear();
									loading.finishLoading();
								}

								@Override
								public void onComError(String title,
										String message) {
									model.getErrorModel().addInfos(title,
											message);
									model.getErrorModel().validate();
									model.getSession()
											.getCurrentOnlineRequest().clear();
									loading.finishLoading();
								}

								@Override
								public void onDataReady(BusStop[] stops) {
									model.getBusNetwork()
											.setStops(stops, index);
									model.getSession()
											.getCurrentOnlineRequest().clear();
									loading.finishLoading();
								}
							});

				}
			}

		});

		stopsView.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					BusStopParam param = model.getApiParams().getBusStopParam();
					int line_id = model.getBusNetwork().getLines()[linesView
							.getSelectedIndex()].getId();
					int direction = directionsView.getSelectedIndex();
					int stop_id = model.getBusNetwork().getLines()[linesView
							.getSelectedIndex()].getStops()[stopsView
							.getSelectedIndex()].getId();
					param.setLine_id(line_id);
					param.setDirection(direction);
					param.setStop_id(stop_id);
					final int lineIndex = linesView.getSelectedIndex();
					final int stopIndex = stopsView.getSelectedIndex();

					if (model.getBusNetwork().getLines()[lineIndex].getStops()[stopIndex]
							.getTimes().size() > 0) {
						model.getBusNetwork().setTimes(
								model.getBusNetwork().getLines()[lineIndex]
										.getStops()[stopIndex].getTimes(),
								lineIndex, stopIndex);
					} else {
						final LoadingView loading = new LoadingView(
								BusLinesView.this);
						loading.beginLoading();
						model.getBusNetworkDAL().loadTimes(param,
								new SimpleCallback<List<StopTime>>() {

									@Override
									public void onDataNotFound(
											DataNotFoundException e) {
										model.getErrorModel().addInfos(
												e.getTitle(), e.getMessage());
										model.getErrorModel().validate();
										model.getSession()
												.getCurrentOnlineRequest()
												.clear();
										loading.finishLoading();
									}

									@Override
									public void onComError(String title,
											String message) {
										model.getErrorModel().addInfos(title,
												message);
										model.getErrorModel().validate();
										model.getSession()
												.getCurrentOnlineRequest()
												.clear();
										loading.finishLoading();
									}

									@Override
									public void onDataReady(List<StopTime> times) {
										model.getBusNetwork().setTimes(times,
												lineIndex, stopIndex);
										model.getSession()
												.getCurrentOnlineRequest()
												.clear();
										loading.finishLoading();
									}

								});
					}
				}

			}

		});
	}

	/**
	 * Cette fonction est appelée pour le chargement des lignes de bus.
	 */
	public void onLoad() {
		BusNetworkParam busNetworkParam = model.getApiParams()
				.getBusNetworkParam();
		model.getBusNetworkDAL().find(busNetworkParam,
				new SimpleCallback<BusLine[]>() {

					@Override
					public void onDataNotFound(DataNotFoundException e) {
						model.getErrorModel().addInfos(e.getTitle(),
								e.getMessage());
						model.getErrorModel().validate();
						model.getSession().getCurrentOnlineRequest().clear();
					}

					@Override
					public void onComError(String title, String message) {
						model.getErrorModel().addInfos(title, message);
						model.getErrorModel().validate();
						model.getSession().getCurrentOnlineRequest().clear();
					}

					@Override
					public void onDataReady(BusLine[] lines) {
						model.getBusNetwork().setLines(lines);
						model.getSession().getCurrentOnlineRequest().clear();
					}

				});
	}

	@Override
	public void update(Observable o, final Object type) {
		SwingUtilities.invokeLater(new Runnable() {
			String typ = type.toString().split("-")[0];

			@Override
			public void run() {
				switch (typ) {
				case "network":
					for (int i = 0; i < network.getLines().length; i++)
						linesView.addItem(network.getLines()[i]);
					break;
				case "line":
					stopsView.removeAllItems();
					for (int i = 0; i < network.getLines()[linesView
							.getSelectedIndex()].getStops().length; i++)
						stopsView.addItem(network.getLines()[linesView
								.getSelectedIndex()].getStops()[i]);
					break;
				case "stop":
					model.getStopTimesTableModel().setData(
							network.getLines()[linesView.getSelectedIndex()]
									.getStops()[stopsView.getSelectedIndex()]
									.getTimes());
					break;

				}

			}

		});

	}

}
