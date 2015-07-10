package pcd.telecomnancy.model.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import pcd.telecomnancy.model.routes.Route;
import pcd.telecomnancy.serverCommunication.UpdatableFromServerData;




public class HistoryTableModel extends Observable implements
		UpdatableFromServerData<Route>, Observer {

	private List<Route> history;
	private Session session;

	public HistoryTableModel(Session session) {
		history = new ArrayList<Route>();
		this.session = session;
		this.session.addObserver(this);
	}

	@Override
	public void setData(List<Route> data) {
		this.history = data;
		this.setChanged();
		this.notifyObservers();

	}

	public void addData(Route data) {
		this.history.add(data);
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public List<Route> getData() {
		return this.history;
	}

	@Override
	public void removeData(Route data) {
		// TODO Auto-generated method stub
		this.history.remove(data);
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		this.history.clear();
		this.history.addAll(session.getMember().getHistory());
		this.setChanged();
		this.notifyObservers();
	}

	public void clearData() {
		// TODO Auto-generated method stub
		this.history.clear();
		this.setChanged();
		this.notifyObservers();
	}

}
