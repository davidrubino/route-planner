package pcd.telecomnancy.dal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import pcd.telecomnancy.model.address.Address;
import pcd.telecomnancy.model.bus.BusLine;
import pcd.telecomnancy.model.bus.BusStop;
import pcd.telecomnancy.model.bus.StopTime;
import pcd.telecomnancy.model.member.Favorite;
import pcd.telecomnancy.model.routes.Route;
import pcd.telecomnancy.model.routes.Step;
import pcd.telecomnancy.model.routes.Transport;

/**
 * Fabrique d'objet de différents types à partir d'objets JSON
 */
public abstract class FromJSONFactory {

	/**
	 * Construit une adresse avec type à partir d'un objet JSON
	 * @param o L'objet JSON
	 * @return L'adresse créée
	 */
	public static Address createAddressWithType(JSONObject o) {
		return new Address(Integer.valueOf(o.get("id").toString()), o.get(
				"label").toString(), Double.valueOf(o.get("lat").toString()),
				Double.valueOf(o.get("lon").toString()), o.get("infos")
						.toString(), Integer.valueOf(o.get("type").toString()),
				o.get("typeStr").toString());
	}

	/**
	 * Construit une adresse à partir d'un objet JSON
	 * @param o L'objet JSON
	 * @return L'adresse créée
	 */
	public static Address createAddress(JSONObject o) {
		return new Address(Integer.valueOf(o.get("id").toString()), o.get(
				"label").toString(), Double.valueOf(o.get("lat").toString()),
				Double.valueOf(o.get("lon").toString()), o.get("infos")
						.toString());
	}

	/**
	 * Construit un trajet à partir d'un objet JSON
	 * @param route L'objet JSON
	 * @return Le trajet créé
	 */
	public static Route createRoute(JSONObject route) {
		int transportNb, stepNb, pointsNb;
		JSONObject transport, step, point;
		JSONArray transports, steps, points;

		Route newRoute;
		Transport newTransport;
		Step newStep;
		Coordinate newPoint;
		transportNb = Integer.valueOf(route.get("transportsNb").toString());

		transports = (JSONArray) route.get("transports");

		newRoute = new Route();
		newRoute.setId(Integer.valueOf(route.get("id").toString()));
		newRoute.setTransportType(route.get("transportType").toString());
		newRoute.setDistance(Integer.valueOf(route.get("distance").toString()));
		newRoute.setDuration(Integer.valueOf(route.get("duration").toString()));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date d = dateFormat.parse(route.get("date").toString());
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			newRoute.setDateHour(cal.get(Calendar.DAY_OF_MONTH),
					cal.get(Calendar.MONTH), cal.get(Calendar.YEAR),
					cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Address start = createAddress((JSONObject) route.get("start"));
		Address end = createAddress((JSONObject) route.get("end"));

		newRoute.setStart(start);
		newRoute.setEnd(end);

		for (int k = 0; k < transportNb; k++) {
			transport = (JSONObject) transports.get(k);
			stepNb = Integer.valueOf(transport.get("stepsNb").toString());
			steps = (JSONArray) transport.get("steps");

			pointsNb = Integer.valueOf(transport.get("pointsNb").toString());
			points = (JSONArray) transport.get("points");

			newTransport = new Transport();
			newTransport.setDistance(Integer.valueOf(transport.get("distance")
					.toString()));
			newTransport.setDuration(Integer.valueOf(transport.get("duration")
					.toString()));
			newTransport
					.setDescription(transport.get("description").toString());
			newTransport.setType(Integer.valueOf(transport.get("type")
					.toString()));

			for (int s = 0; s < stepNb; s++) {
				step = (JSONObject) steps.get(s);
				newStep = new Step(step.get("action").toString(),
						Double.valueOf(step.get("lat").toString()),
						Double.valueOf(step.get("lon").toString()));
				newTransport.addStep(newStep);
			}

			for (int s = 0; s < pointsNb; s++) {
				point = (JSONObject) points.get(s);
				newPoint = new Coordinate(Double.valueOf(point.get("lat")
						.toString()), Double.valueOf(point.get("lon")
						.toString()));
				newTransport.addPoint(newPoint);
			}
			newRoute.addTransport(newTransport);

		}
		return newRoute;
	}

	/**
	 * Construit un favori à partir d'un objet JSON
	 * @param fav L'objet JSON
	 * @return Le favori créé
	 */
	public static Favorite createFavorite(JSONObject fav) {
		JSONObject start = (JSONObject) fav.get("start");
		JSONObject end = (JSONObject) fav.get("end");
		return new Favorite(createAddress(start), createAddress(end), fav.get(
				"transportType").toString());
	}
	
	/**
	 * Construit un tableau d'arrêts de bus à partir d'un objet JSON
	 * @param o L'objet JSON
	 * @return Le tableau d'arrêts de bus créé
	 */
	public static BusStop[] createStops(JSONObject o){
		JSONArray stopsJson = (JSONArray) o.get("stops");
		JSONObject stopJson;
		int nbStops = 0;
		nbStops = Integer.valueOf(o.get("stopsNb").toString());
		BusStop[] stops = new BusStop[nbStops];
		for (int i = 0; i < nbStops; i++) {
			stopJson = (JSONObject) (((JSONObject) (stopsJson
					.get(i))).get("infos"));
			int id = Integer.valueOf(stopJson.get("stop_id")
					.toString());
			String name = stopJson.get("stop_name").toString();
			double lat = Double.valueOf(stopJson.get("stop_lat")
					.toString());
			double lon = Double.valueOf(stopJson.get("stop_lon")
					.toString());
			stops[i] = new BusStop(id, name, lat, lon);
		}
		return stops;
	}
	
	/**
	 * Construit un tableau de lignes de bus à partir de l'objet JSON
	 * @param o L'objet JSON
	 * @return Le tableau de lignes de bus
	 */
	public static BusLine[] createLines(JSONObject o){
		JSONArray linesJson = (JSONArray) o.get("lines");
		JSONObject lineJson;
		int nbLines = 0;
		nbLines = Integer.valueOf(o.get("linesNb").toString());
		BusLine[] lines = new BusLine[nbLines];
		for (int i = 0; i < nbLines; i++) {
			lineJson = (JSONObject) (((JSONObject) (linesJson
					.get(i))).get("infos"));
			int id = Integer.valueOf(lineJson.get("route_id")
					.toString());
			String longName = lineJson.get("route_long_name")
					.toString();
			String shortName = lineJson.get("route_short_name")
					.toString();
			int type = Integer.valueOf(lineJson.get("route_type")
					.toString());
			String direction_0 = lineJson.get("direction_0")
					.toString();
			String direction_1 = lineJson.get("direction_1")
					.toString();
			lines[i] = new BusLine(id, shortName, longName, type,
					direction_0, direction_1);
		}
		return lines;
	}
	
	/**
	 * Construit un tableau d'horaires pour un arrêt de bus à partir d'un objet JSON
	 * @param o L'objet JSON
	 * @return Le tableau d'horaires
	 */
	public static List<StopTime> createBusTimes(JSONObject o){
		JSONArray timesJson = (JSONArray) o.get("times");
		int nbTimes = 0;
			nbTimes = Integer.valueOf(o.get("timesNb").toString());
		List<StopTime> times = new ArrayList<StopTime>(nbTimes);
		for (int i = 0; i < nbTimes; i++) {
			String time = timesJson.get(i).toString();
			int hour = Integer.valueOf(time.split(":")[0]);
			int minute = Integer.valueOf(time.split(":")[1]);
			times.add(new StopTime(hour, minute));
		}
		return times;
	}
}
