package pcd.telecomnancy.model.routes;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pcd.telecomnancy.serverCommunication.UpdatableFromServerData;


public class RoutesListModel extends Observable implements
		UpdatableFromServerData<Route> {

	private List<Route> routes;

	public RoutesListModel() {
		routes = new ArrayList<Route>();
	}

	@Override
	public void setData(List<Route> l) {
		// TODO Auto-generated method stub
		this.routes = l;
		setChanged();
		notifyObservers();
	}

	@Override
	public List<Route> getData() {
		// TODO Auto-generated method stub
		return routes;
	}

	@Override
	public void removeData(Route data) {
		// TODO Auto-generated method stub
		this.routes.remove(data);
		this.setChanged();
		this.notifyObservers();
	}
}
