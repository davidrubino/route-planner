package pcd.telecomnancy.model;

import java.util.Observable;

import pcd.telecomnancy.model.routes.Route;
import pcd.telecomnancy.serverCommunication.param.RoutesParam;

public class DisplayedRoute extends Observable {

	private Route route;
	private RoutesParam routesParam;

	/**
	 * Construit une instance DisplayedRoute
	 * 
	 * @param routesParam
	 *            les nouveaux paramètres de la route
	 **/
	public DisplayedRoute(RoutesParam routesParam) {
		this.routesParam = routesParam;
	}

	/**
	 * Mutateur de la route
	 * 
	 * @param route
	 *            la nouvelle route
	 **/
	public void setRoute(Route route) {
		this.route = route;
		this.routesParam.setStart(this.route.getStart());
		this.routesParam.setEnd(this.route.getStart());
		this.routesParam.setTransportType(this.route.getTransportType());
		this.routesParam.setYear(this.route.getYear());
		this.routesParam.setMonth(this.route.getMonth());
		this.routesParam.setDay(this.route.getDay());
		this.routesParam.setHour(this.route.getHour());
		this.routesParam.setMinute(this.route.getMinute());
		this.setChanged();
		this.notifyObservers("route");
	}

	/**
	 * Accesseur de la route
	 * 
	 * @return route La route
	 **/
	public Route getRoute() {
		return route;
	}

}
