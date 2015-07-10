package pcd.telecomnancy.view.routes;

import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

import pcd.telecomnancy.model.routes.Route;
import pcd.telecomnancy.model.routes.RoutesListModel;

public class RoutesListAdapter extends AbstractListModel<Route> implements
		Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RoutesListModel routesListModel;

	public RoutesListAdapter(RoutesListModel routesListModel) {
		this.routesListModel = routesListModel;
		routesListModel.addObserver(this);
	}

	@Override
	public Route getElementAt(int index) {
		// TODO Auto-generated method stub
		return this.routesListModel.getData().get(index);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return this.routesListModel.getData().size();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		this.fireContentsChanged(this, 0, this.getSize());
	}
}
