package pcd.telecomnancy.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

import pcd.telecomnancy.model.DisplayedRoute;
import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.model.bus.BusLine;
import pcd.telecomnancy.model.bus.BusNetwork;
import pcd.telecomnancy.model.bus.BusStop;
import pcd.telecomnancy.model.routes.Route;
import pcd.telecomnancy.model.routes.Transport;

/**
 * Représente le panel affichant la carte de Nancy
 */
public class MapPanel extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	private JMapViewer map;
	private DisplayedRoute currentRoute;
	private BusNetwork busNetwork;

	public MapPanel(Model model, String param) {
		this.setLayout(new BorderLayout());
		map = new JMapViewer();
		this.add(map, BorderLayout.CENTER);
		map.setDisplayPositionByLatLon(48.683333, 6.2, 14);
		this.busNetwork = model.getBusNetwork();
		this.busNetwork.addObserver(this);
	}

	public MapPanel(Model model) {
		this.setLayout(new BorderLayout());
		map = new JMapViewer();
		this.add(map, BorderLayout.CENTER);
		map.setDisplayPositionByLatLon(48.683333, 6.2, 14);
		this.currentRoute = model.getCurrentRoute();
		this.currentRoute.addObserver(this);
	}

	@Override
	public void update(Observable o, Object para) {
		// TODO Auto-generated method stub
		String param = para.toString().split("-")[0];
		if (param.equals("line")) {
			final int index = Integer.valueOf(para.toString().split("-")[1]);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					drawBusLine(busNetwork.getLines()[index]);
				}
			});
		} else if (param.equals("stop")) {
			final int lindex = Integer.valueOf(para.toString().split("-")[1]);
			final int sindex = Integer.valueOf(para.toString().split("-")[2]);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					double lat = busNetwork.getLines()[lindex].getStops()[sindex]
							.getLat();
					double lon = busNetwork.getLines()[lindex].getStops()[sindex]
							.getLon();
					String name = busNetwork.getLines()[lindex].getStops()[sindex]
							.getName();
					MapMarkerDot start = new MapMarkerDot(name, new Coordinate(
							lat, lon));
					map.removeAllMapMarkers();
					map.addMapMarker(start);
					map.repaint();
				}
			});
		} else if (param.equals("route")) {
			Route route = currentRoute.getRoute();
			final ArrayList<Coordinate> points = new ArrayList<Coordinate>();
			for (Transport t : route.getTransports()) {
				points.addAll(t.getPoints());
			}

			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					map.removeAllMapPolygons();
					map.removeAllMapMarkers();
					if (points.size() > 0)
						drawPath(points, true);
					map.setDisplayToFitMapPolygons(); // map centrée sur trajet
														// et
														// pas sur départ
														// arrivée
				}

			});
		}
	}

	private void drawBusLine(BusLine line) {
		// TODO Auto-generated method stub
		List<Coordinate> points = new ArrayList<Coordinate>();
		for (BusStop stop : line.getStops()) {
			points.add(new Coordinate(stop.getLat(), stop.getLon()));
		}
		map.removeAllMapPolygons();
		map.removeAllMapMarkers();
		if (points.size() > 0)
			drawPath(points, false);
		map.setDisplayToFitMapPolygons();
	}

	/**
	 * Permet de tracer un trajet sur la carte
	 * 
	 * @param one
	 *            La coordonnée du point de départ
	 * @param two
	 *            La coordonnée du point d'arrivée
	 */
	public void draw(Coordinate one, Coordinate two) {
		List<Coordinate> route = new ArrayList<Coordinate>(Arrays.asList(one,
				two, two));
		MapPolygonImpl mapPolygonImpl = new MapPolygonImpl(route);
		mapPolygonImpl.setColor(new Color(0, 102, 204)); // permet de changer la
															// couleur du trajet
		map.addMapPolygon(mapPolygonImpl);
	}

	/**
	 * Permet de tracer un trajet sur la carte en affichant les points de départ
	 * et d'arrivée
	 * 
	 * @param chemin
	 *            La liste des coordonnées des différents points du trajet
	 * @param markers
	 */
	public void drawPath(List<Coordinate> chemin, boolean markers) {
		if (markers) {
			MapMarkerDot start = new MapMarkerDot("Départ", chemin.get(0));
			MapMarkerDot end = new MapMarkerDot("Arrivée", chemin.get(chemin
					.size() - 1));
			start.setColor(Color.GREEN);
			end.setColor(Color.RED);
			start.setBackColor(Color.GREEN);
			end.setBackColor(Color.RED);
			map.addMapMarker(start);
			map.addMapMarker(end);
		}
		for (int i = 0; i < chemin.size() - 1; i++) {
			draw(chemin.get(i), chemin.get(i + 1));
		}
	}

}
