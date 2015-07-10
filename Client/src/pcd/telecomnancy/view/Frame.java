package pcd.telecomnancy.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.theme.LookAndFeelDockingTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;
import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.view.address.AddressListView;
import pcd.telecomnancy.view.bus.BusLinesView;
import pcd.telecomnancy.view.member.FavoriteTableView;
import pcd.telecomnancy.view.member.HistoryTableView;
import pcd.telecomnancy.view.routes.RouteDetailsView;
import pcd.telecomnancy.view.routes.RoutesListView;

/**
 * Représente la fenêtre principale de l'application
 */
public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;

	private SearchPanel searchPanel;
	private MapPanel mapPanel;

	private JPanel bottomPanel;
	private ErrorView errorView;

	private View[] formViews, resultViews;

	private ViewMap viewMapForm, viewMapResults, viewMapBus;
	private RootWindow rootWindowForm, rootWindowResults, rootWindowBus;

	private JMenuBar menuBar;
	private ToolBar toolBar;
	private CardLayout layout;

	private JPanel cardPanel;

	private TabWindow adrRouTabWindow;

	private LookAndFeelDockingTheme theme;

	private HistoryTableView historyTableView;
	private FavoriteTableView favoriteTableView;
	private AddressListView addressListView;
	private RoutesListView routesListView;
	private RouteDetailsView routeDetailsView;
	private BusLinesView busLinesView;

	private MapPanel busMap;

	private JDialog aboutDialog;

	public Frame(Model model, SearchPanel searchPanel,
			HistoryTableView historyTableView,
			FavoriteTableView favoriteTableView,
			AddressListView addressListView, RoutesListView routesListView,
			RouteDetailsView routeDetailsView, MapPanel mapPanel,
			BusLinesView busLinesView, PopUpView connRegView,
			SessionView sessionView, CurrentRequestView lastRequestView,
			MapPanel busMap) {

		this.searchPanel = searchPanel;
		this.mapPanel = mapPanel;
		this.historyTableView = historyTableView;
		this.favoriteTableView = favoriteTableView;
		this.addressListView = addressListView;
		this.routesListView = routesListView;
		this.routeDetailsView = routeDetailsView;
		this.busLinesView = busLinesView;
		this.busMap = busMap;

		this.setTitle("Projet PCD");
		this.setMinimumSize(new Dimension(700, 500));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setContentPane(new JPanel());
		this.getContentPane().setLayout(new BorderLayout());
		layout = new CardLayout();
		this.cardPanel = new JPanel();
		cardPanel.setLayout(layout);
		this.getContentPane().add(cardPanel, BorderLayout.CENTER);

		this.theme = new LookAndFeelDockingTheme();

		addressListView.setMainFrame(this);
		routesListView.setMainFrame(this);
		routeDetailsView.setMainFrame(this);

		this.buildFormView();
		this.buildResultsView();
		this.buildBusView();

		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(lastRequestView, BorderLayout.EAST);
		bottomPanel.add(sessionView, BorderLayout.WEST);
		bottomPanel.setPreferredSize(new Dimension(0, 40));
		this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		this.setExtendedState(this.getExtendedState() | MAXIMIZED_BOTH);

		menuBar = new JMenuBar();
		toolBar = new ToolBar(model, connRegView, this);
		menuBar.add(toolBar);
		this.setJMenuBar(menuBar);

		this.createAboutDialog();

		this.pack();
	}

	private void createAboutDialog() {
		// TODO Auto-generated method stub
		aboutDialog = new JDialog();
		aboutDialog.setResizable(false);
		aboutDialog.setSize(500, 350);
		aboutDialog.setLocationRelativeTo(null);
		aboutDialog.setBackground(Color.WHITE);
		JPanel dialogPanel = new JPanel();
		dialogPanel.setSize(500, 350);
		dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
		JEditorPane content = new JEditorPane();
		content.setContentType("text/html");
		content.setEditable(false);
		content.setText("<html><div style=\"padding:20px;\"><div style=\"text-align:center;\"><img src=\""
				+ getClass().getClassLoader().getResource("tncy.png")
				+ "\"/></div><h2 style=\"font-size:14px;\">Projet de conception et de développement</h2><p>Cette application permet de planifier des trajets dans le Grand Nancy, et de visualiser les horaires des arrêts des différentes lignes de transport en commun.</p><p>Pour plus d'informations, visitez notre site web :  <a href=\"http://pcd.telecomnancy.neness.net/\">http://pcd.telecomnancy.neness.net</a> !</p></div></html>");
		content.addHyperlinkListener(new HyperlinkListener() {

			@Override
			public void hyperlinkUpdate(HyperlinkEvent l) {
				// TODO Auto-generated method stub
				if (Desktop.isDesktopSupported()
						&& l.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					Desktop desktop = Desktop.getDesktop();
					if (desktop.isSupported(Desktop.Action.BROWSE)) {
						try {
							desktop.browse(l.getURL().toURI());
						} catch (IOException | URISyntaxException e) {
							// TODO Auto-generated catch block
							System.err.println("Lien invalide");
						}
					}
				}
			}

		});
		dialogPanel.add(content);

		aboutDialog.add(dialogPanel);
	}

	private void buildFormView() {
		/** 1ere vue **/
		final Component[] formView = { searchPanel, historyTableView,
				favoriteTableView, addressListView, routesListView };
		formViews = new View[formView.length];
		viewMapForm = new ViewMap();
		final String[] formViewsName = { "Recherche", "Trajets récents",
				"Favoris", "Choix des adresses", "Choix du trajet" };
		for (int i = 0; i < formView.length; i++) {
			formViews[i] = new View(formViewsName[i], null, formView[i]);
			viewMapForm.addView(i, formViews[i]);
		}
		rootWindowForm = DockingUtil.createRootWindow(viewMapForm, true);
		rootWindowForm.getRootWindowProperties().addSuperObject(
				theme.getRootWindowProperties());

		// historique + favoris
		TabWindow tabWindow1 = new TabWindow(new DockingWindow[] {
				formViews[1], formViews[2] });
		// listes des addresses + liste des trajets
		this.adrRouTabWindow = new TabWindow(new DockingWindow[] {
				formViews[3], formViews[4] });
		SplitWindow splitWindow1 = new SplitWindow(false, 0.55f, formViews[0],
				tabWindow1);
		SplitWindow splitWindow2 = new SplitWindow(true, 0.45f, splitWindow1,
				adrRouTabWindow);

		rootWindowForm.setWindow(splitWindow2);
		rootWindowForm.getWindowBar(Direction.DOWN).setEnabled(true);
		cardPanel.add(rootWindowForm, "form");
		tabWindow1.setSelectedTab(0);
		adrRouTabWindow.setSelectedTab(0);
	}

	private void buildResultsView() {
		final Component[] resultView = { routeDetailsView, mapPanel };
		resultViews = new View[resultView.length];
		viewMapResults = new ViewMap();
		final String[] resultViewsName = { "Détails du trajet", "Carte" };
		for (int i = 0; i < resultView.length; i++) {
			resultViews[i] = new View(resultViewsName[i], null, resultView[i]);
			viewMapResults.addView(i, resultViews[i]);
		}
		rootWindowResults = DockingUtil.createRootWindow(viewMapResults, true);
		rootWindowResults.getRootWindowProperties().addSuperObject(
				theme.getRootWindowProperties());

		SplitWindow splitWindow3 = new SplitWindow(true, 0.4f, resultViews[0],
				resultViews[1]);

		rootWindowResults.setWindow(splitWindow3);
		rootWindowResults.getWindowBar(Direction.DOWN).setEnabled(true);
		cardPanel.add(rootWindowResults, "result");
	}

	private void buildBusView() {
		View busView = new View("Lignes de Bus", null, busLinesView);
		View busMapView = new View("Carte des lignes", null, busMap);
		viewMapBus = new ViewMap();
		viewMapBus.addView(0, busView);
		viewMapBus.addView(1, busMapView);
		rootWindowBus = DockingUtil.createRootWindow(viewMapBus, true);
		rootWindowBus.getRootWindowProperties().addSuperObject(
				theme.getRootWindowProperties());

		SplitWindow splitWindow = new SplitWindow(true, 0.5f, busView,
				busMapView);
		rootWindowBus.setWindow(splitWindow);
		rootWindowBus.getWindowBar(Direction.DOWN).setEnabled(true);
		cardPanel.add(rootWindowBus, "bus");
	}

	public ErrorView getErrorView() {
		return errorView;
	}

	public void setErrorView(ErrorView errorView) {
		this.errorView = errorView;
	}

	public SearchPanel getSearchPanel() {
		return searchPanel;
	}

	public MapPanel getMapPanel() {
		return mapPanel;
	}

	public ToolBar getToolBar() {
		return toolBar;
	}

	/**
	 * Affiche le panel contenant le formulaire de recherche d'un trajet
	 */
	public void showFormPanel() {
		layout.show(cardPanel, "form");
		toolBar.getChooseButton().setSelected(true);
	}

	/**
	 * Affiche le panel contenant la liste des addresses et des trajets trouvés
	 */
	public void showResultsPanel() {
		layout.show(cardPanel, "result");
		toolBar.getSeeButton().setSelected(true);
	}

	/**
	 * Permet de restaurer l'interface selon sa disposition initiale
	 */
	public void restoreView() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				cardPanel.removeAll();
				buildFormView();
				buildResultsView();
				buildBusView();
				layout.show(cardPanel, "form");
				toolBar.getChooseButton().setSelected(true);
				cardPanel.revalidate();
				cardPanel.repaint();
			}

		});
	}

	/**
	 * Permet d'afficher la liste des addresses trouvées à la place de la liste
	 * des trajets
	 */
	public void switchToAddressList() {
		adrRouTabWindow.setSelectedTab(0);
	}

	/**
	 * Permet d'afficher la liste des trajets trouvés à la place de la liste des
	 * addresses
	 */
	public void switchToRoutesList() {
		adrRouTabWindow.setSelectedTab(1);
	}

	/**
	 * Affiche le panel contenant la liste des horaires de bus et le plan des
	 * lignes
	 */
	public void showBusPanel() {
		// TODO Auto-generated method stub
		layout.show(cardPanel, "bus");
		toolBar.getBusButton().setSelected(true);
	}

	/**
	 * Affiche la boite de dialogue contenant des informations au sujet du
	 * logiciel
	 */
	public void showAboutDialog() {
		// TODO Auto-generated method stub
		this.aboutDialog.setVisible(true);
	}
}
