package pcd.telecomnancy.model.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import pcd.telecomnancy.serverCommunication.UpdatableFromServerData;




public class FavoriteTableModel extends Observable implements
		UpdatableFromServerData<Favorite>,Observer {

	private List<Favorite> favorites;
	private Session session;

	public FavoriteTableModel(Session session) {
		this.session=session;
		this.session.addObserver(this);
		favorites = new ArrayList<Favorite>();
	}

	@Override
	public void setData(List<Favorite> data) {
		this.favorites = data;
		this.setChanged();
		this.notifyObservers();

	}

	public void addData(Favorite data) {
		this.favorites.add(data);
		this.setChanged();
		this.notifyObservers();
	}

	public boolean contains(Favorite data) {
		for (Favorite f : this.favorites) {
			if (data.getStart().getLat() == f.getStart().getLat()
					&& data.getStart().getLon() == f.getStart().getLon()
					&& data.getStart().getLabel()
							.equals(f.getStart().getLabel())
					&& data.getEnd().getLabel().equals(f.getEnd().getLabel())
					&& data.getEnd().getLat() == f.getEnd().getLat()
					&& data.getEnd().getLon() == f.getEnd().getLon()
					&& data.getTransportType().equals(f.getTransportType())) {
				return true;
			}

		}
		return false;
	}

	@Override
	public List<Favorite> getData() {
		return this.favorites;
	}

	@Override
	public void removeData(Favorite data) {
		// TODO Auto-generated method stub
		this.favorites.remove(data);
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		this.favorites.clear();
		this.favorites.addAll(session.getMember().getFavorites());
		this.setChanged();
		this.notifyObservers();
	}

}
