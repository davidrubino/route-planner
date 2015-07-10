package pcd.telecomnancy.view.routes;

import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import pcd.telecomnancy.model.DisplayedRoute;
import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.model.routes.Route;
import pcd.telecomnancy.model.routes.Step;
import pcd.telecomnancy.model.routes.Transport;
import pcd.telecomnancy.serverCommunication.param.RoutesParam;
import pcd.telecomnancy.view.Frame;



public class RouteDetailsView extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JEditorPane detailsPanel;
	private JScrollPane jsp;

	private DisplayedRoute currentRoute;

	private RoutesParam routesParam;

	private Frame frame;

	public RouteDetailsView(Model model) {
		this.currentRoute = model.getCurrentRoute();
		this.routesParam = model.getApiParams().getRouteParam();
		this.currentRoute.addObserver(this);
		detailsPanel = new JEditorPane();
		detailsPanel.setContentType("text/html");
		detailsPanel.setEditable(false);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		jsp = new JScrollPane();
		jsp.setViewportView(detailsPanel);
		this.add(jsp);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				final Route route = currentRoute.getRoute();
				final RoutesParam rParam = routesParam;

				String distanceStr = route.getDistance()==0 ? "" : route.getDistance() / 1000 > 1 ? String
						.format("%.1f", ((double) (route.getDistance()) / 1000))
						+ "km "
						: route.getDistance() + "m ";
				String durationStr = Math
						.round((double) (route.getDuration()) / 60) + "min";

				String det = "<html><body style=\" background:#DCDCDC; padding:0px 0px;\"><div  style=\"padding:20px; \">";

				det += "<table><tr><td><img src=\""
						+ getClass().getClassLoader().getResource(
								"green flag.png")
						+ " \"/> </td> <td><strong> <span style =\"color:#339900; font-size:17px;\">Départ:</span> "
						+ route.getStart().getLabel()
						+ "</strong> </td></tr></table>";

				det += "<table><tr><td><img src=\""
						+ getClass().getClassLoader().getResource(
								"red flag.jpg")
						+ " \"/> </td> <td> <strong> <span style =\"color:#FF0000; font-size:17px;\">Arrivée:</span> "
						+ route.getEnd().getLabel()
						+ "</strong> </td></tr></table> ";

				String minute = rParam.getMinute() < 10 ? "0"
						+ rParam.getMinute() : rParam.getMinute() + "";
				det += "<p><span>le " + rParam.getDay() + "/"
						+ rParam.getMonth() + "/" + rParam.getYear() + " à "
						+ rParam.getHour() + ":" + minute + "</span>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;<span>" + distanceStr
						+ durationStr + "</span></p>";

				for (Transport t : route.getTransports()) {

					distanceStr = t.getDistance() / 1000 > 1 ? String.format(
							"%.1f", ((double) (t.getDistance()) / 1000))
							+ "km " : t.getDistance() + "m";

					det += "<hr />";
					det += "<p>";

					det += "<h4 style=\"font-size:12px;\">"
							+ t.getDescription()+"</h4>";
					det += (t.getDuration() / 60) + " minutes ";
					det += distanceStr;
					det += "<ul style=\"list-style-type: none;\">";
					for (Step s : t.getSteps()) {
						det += "<li>&rarr; " + s.getAction() + "</li>";
					}
					det += "</ul>";
					det += "</p>";
				}

				det += "</div></body></html>";

				final String details = det;

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub

						detailsPanel.setText(details);
						detailsPanel.setCaretPosition(0);

						jsp.getVerticalScrollBar().setValue(0);
						frame.showResultsPanel();
					}
				});
			}
		};
		Thread t = new Thread(runnable);
		t.start();
	}

	public void setMainFrame(Frame frame) {
		// TODO Auto-generated method stub
		this.frame = frame;
	}

}
