package pcd.telecomnancy;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.view.CurrentRequestView;
import pcd.telecomnancy.view.ErrorView;
import pcd.telecomnancy.view.Frame;
import pcd.telecomnancy.view.MapPanel;
import pcd.telecomnancy.view.PopUpView;
import pcd.telecomnancy.view.SearchController;
import pcd.telecomnancy.view.SearchPanel;
import pcd.telecomnancy.view.SessionView;
import pcd.telecomnancy.view.address.AddressListController;
import pcd.telecomnancy.view.address.AddressListView;
import pcd.telecomnancy.view.bus.BusLinesView;
import pcd.telecomnancy.view.member.FavoriteTableView;
import pcd.telecomnancy.view.member.HistoryTableView;
import pcd.telecomnancy.view.routes.RouteDetailsView;
import pcd.telecomnancy.view.routes.RoutesListController;
import pcd.telecomnancy.view.routes.RoutesListView;


public class Entry {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// initialisation Modèle

		final Model model = new Model();

		// initialisation vues et contrôleurs

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try { // apparence des vues
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
				}
				/** Vue des erreurs **/
				ErrorView errorView = new ErrorView(model.getErrorModel());
				
				/** Panel qui contient le formulaire **/
				SearchPanel searchPanel = new SearchPanel();
				SearchController searchController = new SearchController(
						searchPanel, model);
				searchPanel.getSubmitButton().addActionListener(
						searchController);

				/** Panel de la carte **/
				MapPanel mapPanel = new MapPanel(model);

				/** Vue Historique **/
				HistoryTableView historyTableView = new HistoryTableView(model);

				/** Vue Favoris **/
				FavoriteTableView favoriteTableView = new FavoriteTableView(
						model);

				/** Onglet de détails sur un trajet **/
				RouteDetailsView routeDetailsView = new RouteDetailsView(model);

				/** Vue des listes d'adresses **/
				AddressListView addressListView = new AddressListView(model);
				new AddressListController(addressListView, model);

				/** Vue de la liste des trajets **/
				RoutesListController routesListController = new RoutesListController(
						model);

				RoutesListView routesListView = new RoutesListView(model,
						routesListController);
				routesListController.setRoutesListView(routesListView);

				/** vue des lignes de bus **/
				BusLinesView busLinesView = new BusLinesView(model);
				MapPanel busMap = new MapPanel(model,"bus");

				/** vue session **/
				SessionView sessionView = new SessionView(model.getSession());

				/** connexion inscription **/
				
				PopUpView connRegView = new PopUpView(model);
				
				/** vue derniere requete **/
				CurrentRequestView lastRequestView = new CurrentRequestView(model);

				/** Fenêtre principale **/
				Frame frame = new Frame(model, searchPanel, historyTableView,
						favoriteTableView, addressListView, routesListView,
						routeDetailsView, mapPanel, busLinesView, connRegView, sessionView,lastRequestView,busMap);

				
				frame.setErrorView(errorView);
				
				/** affichage fenêtre connexion **/
				connRegView.setMainFrame(frame);
				connRegView.setVisible(true);

			}

		});
	}

}
