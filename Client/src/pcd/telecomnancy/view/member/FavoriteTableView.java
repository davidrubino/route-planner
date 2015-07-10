package pcd.telecomnancy.view.member;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import pcd.telecomnancy.dal.DataNotFoundException;
import pcd.telecomnancy.dal.MemberDAL;
import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.model.member.Favorite;
import pcd.telecomnancy.model.member.FavoriteTableModel;
import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.model.routes.Route;
import pcd.telecomnancy.serverCommunication.MultipleCallback;
import pcd.telecomnancy.serverCommunication.SimpleCallback;
import pcd.telecomnancy.serverCommunication.param.FavoriteParam;
import pcd.telecomnancy.serverCommunication.param.RoutesParam;
import pcd.telecomnancy.serverCommunication.param.FavoriteParam.Action;
import pcd.telecomnancy.view.LoadingView;


public class FavoriteTableView extends JPanel {

	private static final long serialVersionUID = 1L;

	private FavoriteTableModel favoriteTableModel;
	private JTable tableFavorite;
	private JPopupMenu popupMenu;
	private Favorite selectedFavorite;

	public FavoriteTableView(final Model model) {
		final Session session = model.getSession();
		this.favoriteTableModel = model.getFavoriteTableModel();
		this.setLayout(new BorderLayout());
		tableFavorite = new JTable(new FavoriteTableAdapter(
				this.favoriteTableModel));
		tableFavorite.setDefaultRenderer(Object.class,
				new FavoriteTableCellRenderer());
		this.add(new JScrollPane(tableFavorite), BorderLayout.CENTER);

		popupMenu = new JPopupMenu();
		JMenuItem menuItem1 = new JMenuItem("Supprimer ce favori",
				new ImageIcon(getClass().getClassLoader().getResource(
						"delete2-icon.png")));
		popupMenu.add(menuItem1);
		menuItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				favoriteTableModel.removeData(selectedFavorite);
				if (session.isLoggedIn()) {
					FavoriteParam param = model.getApiParams()
							.getFavoriteParam();
					param.setStart(selectedFavorite.getStart());
					param.setEnd(selectedFavorite.getEnd());
					param.setTransportType(selectedFavorite.getTransportType());
					param.setAction(Action.DELETE);
					MemberDAL memberDAL = model.getMemberDAL();
					memberDAL.update(param, new SimpleCallback<Object>() {

						@Override
						public void onDataReady(Object object) {
							// TODO Auto-generated method stub
							session.getCurrentOnlineRequest().clear();
						}

						@Override
						public void onDataNotFound(DataNotFoundException e) {
							// TODO Auto-generated method stub
							session.getCurrentOnlineRequest().clear();
						}

						@Override
						public void onComError(String title, String message) {
							// TODO Auto-generated method stub
							session.getCurrentOnlineRequest().clear();
							model.getErrorModel().addInfos(title, message);
							model.getErrorModel().validate();
						}
					});
				}
			}
		});

		final Runnable calculateFavorite = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				FavoriteParam fparam = model.getApiParams().getFavoriteParam();
				RoutesParam rparam = model.getApiParams().getRouteParam();

				fparam.setAction(Action.CALCULATE);
				fparam.setCal(Calendar.getInstance());
				fparam.setStart(selectedFavorite.getStart());
				fparam.setEnd(selectedFavorite.getEnd());
				fparam.setTransportType(selectedFavorite.getTransportType());

				rparam.setStart(selectedFavorite.getStart());
				rparam.setStart(selectedFavorite.getStart());
				rparam.setEnd(selectedFavorite.getEnd());
				rparam.setTransportType(selectedFavorite.getTransportType());
				rparam.setDay(fparam.getDay());
				rparam.setMonth(fparam.getMonth());
				rparam.setYear(fparam.getYear());
				rparam.setMinute(fparam.getMinute());
				rparam.setHour(fparam.getHour());

				final LoadingView loading = new LoadingView(
						FavoriteTableView.this);
				loading.beginLoading();
				model.getRoutesDAL().find(fparam,
						new MultipleCallback<Route>() {

							@Override
							public void onDataReady(List<Route> list) {
								// TODO Auto-generated method stub
								model.getRoutesListModel().setData(list);
								loading.finishLoading();
								session.getCurrentOnlineRequest().clear();
							}

							@Override
							public void onDataNotFound(DataNotFoundException e) {
								// TODO Auto-generated method stub
								session.getCurrentOnlineRequest().clear();
								model.getErrorModel().addInfos(
										"Calcul du trajet", e.getMessage());
								model.getErrorModel().validate();
								loading.finishLoading();
							}

							@Override
							public void onComError(String title, String message) {
								// TODO Auto-generated method stub
								session.getCurrentOnlineRequest().clear();
								model.getErrorModel().addInfos(title, message);
								model.getErrorModel().validate();
								loading.finishLoading();
							}
						});

			}

		};

		JMenuItem menuItem2 = new JMenuItem("Refaire ce trajet", new ImageIcon(
				getClass().getClassLoader()
						.getResource("recalculate2-icon.gif")));
		popupMenu.add(menuItem2);
		menuItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Thread t = new Thread(calculateFavorite);
				t.start();
			}
		});

		tableFavorite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
				int row = tableFavorite.rowAtPoint(e.getPoint());
				ListSelectionModel model = tableFavorite.getSelectionModel();
				model.setSelectionInterval(row, row);
				selectedFavorite = favoriteTableModel.getData().get(row);
				if (e.getClickCount() == 2) {
					Thread t = new Thread(calculateFavorite);
					t.start();
				}
			}

		});
	}

}
