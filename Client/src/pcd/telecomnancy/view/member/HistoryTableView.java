package pcd.telecomnancy.view.member;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import pcd.telecomnancy.dal.DataNotFoundException;
import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.model.member.Favorite;
import pcd.telecomnancy.model.member.HistoryTableModel;
import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.model.routes.Route;
import pcd.telecomnancy.serverCommunication.ServerCommunication;
import pcd.telecomnancy.serverCommunication.ServerCommunicationImpl;
import pcd.telecomnancy.serverCommunication.ServerCommunicationProxy;
import pcd.telecomnancy.serverCommunication.ServerDataListener;
import pcd.telecomnancy.serverCommunication.SimpleCallback;
import pcd.telecomnancy.serverCommunication.param.FavoriteParam;
import pcd.telecomnancy.serverCommunication.param.HistoryParam;
import pcd.telecomnancy.serverCommunication.param.FavoriteParam.Action;


public class HistoryTableView extends JPanel {

	private static final long serialVersionUID = 1L;

	private HistoryTableModel historyTableModel;
	private JTable tableHistory;
	private JPopupMenu popupMenu;
	private Route selectedHistory;

	public HistoryTableView(final Model sharedModel) {

		final Session session = sharedModel.getSession();
		this.historyTableModel = sharedModel.getHistoryTableModel();
		this.setLayout(new BorderLayout());
		tableHistory = new JTable(new HistoryTableAdapter(
				this.historyTableModel));
		tableHistory.setDefaultRenderer(Object.class,
				new HistoryTableCellRenderer());
		this.add(new JScrollPane(tableHistory), BorderLayout.CENTER);

		popupMenu = new JPopupMenu();
		JMenuItem menuItem1 = new JMenuItem("Ajouter aux favoris",
				new ImageIcon(getClass().getClassLoader().getResource(
						"add-favorite-icon.jpg")));
		popupMenu.add(menuItem1);
		menuItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Favorite favorite = new Favorite(selectedHistory.getStart(),
						selectedHistory.getEnd(), selectedHistory
								.getTransportType());
				if (!sharedModel.getFavoriteTableModel().contains(favorite)) {
					sharedModel.getFavoriteTableModel().addData(favorite);
					if (sharedModel.getSession().isLoggedIn()) {
						FavoriteParam param = sharedModel.getApiParams()
								.getFavoriteParam();
						param.setAction(Action.ADD);
						param.setStart(selectedHistory.getStart());
						param.setEnd(selectedHistory.getEnd());
						param.setTransportType(selectedHistory
								.getTransportType());
						sharedModel.getMemberDAL().update(param,
								new SimpleCallback<Object>() {

									@Override
									public void onDataReady(Object obj) {
										// TODO Auto-generated method stub
										session.getCurrentOnlineRequest()
												.clear();
									}

									@Override
									public void onDataNotFound(
											DataNotFoundException e) {
										// TODO Auto-generated method stub
										session.getCurrentOnlineRequest()
												.clear();
									}

									@Override
									public void onComError(String title,
											String message) {
										// TODO Auto-generated method stub
										session.getCurrentOnlineRequest()
												.clear();
										sharedModel.getErrorModel().addInfos(
												title, message);
										sharedModel.getErrorModel().validate();
									}

								});
					}
				} else {
					sharedModel.getErrorModel().addInfos("Favoris",
							"Ce favori existe déjà");
					sharedModel.getErrorModel().validate();
				}
			}
		});

		JMenuItem menuItem2 = new JMenuItem("Refaire ce trajet", new ImageIcon(
				getClass().getClassLoader()
						.getResource("recalculate2-icon.gif")));
		popupMenu.add(menuItem2);
		menuItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sharedModel.getCurrentRoute().setRoute(selectedHistory);
			}
		});

		JMenuItem menuItem3 = new JMenuItem("Vider l'historique",
				new ImageIcon(getClass().getClassLoader().getResource(
						"clear-icon.png")));
		menuItem3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				sharedModel.getHistoryTableModel().clearData();
				if (sharedModel.getSession().isLoggedIn()) {
					HistoryParam param = sharedModel.getApiParams()
							.getHistoryParam();
					param.setAction(HistoryParam.Action.CLEAR);
					ServerCommunication sc = new ServerCommunicationProxy(
							sharedModel.getSession(),new ServerCommunicationImpl(session));
					sc.addRequest(param);
					sc.doRequests(new ServerDataListener() {

						@Override
						public void dataReceived(List<String> response) {
							// TODO Auto-generated method stub
							session.getCurrentOnlineRequest().clear();
							sharedModel.getSession().getMember().getHistory()
									.clear();

						}

						@Override
						public void onError(String title, String message) {
							// TODO Auto-generated method stub
							session.getCurrentOnlineRequest().clear();
							sharedModel.getErrorModel()
									.addInfos(title, message);
							sharedModel.getErrorModel().validate();
						}
					});
				}
			}

		});
		popupMenu.add(menuItem3);

		tableHistory.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
				int row = tableHistory.rowAtPoint(e.getPoint());
				ListSelectionModel model = tableHistory.getSelectionModel();
				model.setSelectionInterval(row, row);
				selectedHistory = historyTableModel.getData().get(row);
				if (e.getClickCount() == 2) {
					sharedModel.getCurrentRoute().setRoute(selectedHistory);
				}

			}

		});
	}
}
