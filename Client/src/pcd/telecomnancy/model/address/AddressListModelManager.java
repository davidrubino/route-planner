package pcd.telecomnancy.model.address;

import java.util.Observable;
import java.util.Observer;

/** La classe permettant de gérer les listes d'adresses de départ et d'arrivée **/
public class AddressListModelManager extends Observable implements Observer {

	private AddressListModel startListModel, endListModel;

	/**
	 * Construit une instance du gestionnaire des listes d'adresses de départ et
	 * d'arrivée
	 **/
	public AddressListModelManager() {
		this.startListModel = new AddressListModel();
		this.endListModel = new AddressListModel();
		this.startListModel.addObserver(this);
		this.endListModel.addObserver(this);
	}

	/**
	 * Accesseur de la liste des adresses de départ
	 * 
	 * @return startListModel La liste d'adresse de départ
	 **/
	public AddressListModel getStartListModel() {
		return startListModel;
	}

	/**
	 * Accesseur de la liste des adresses d'arrivée
	 * 
	 * @return endListModel la liste d'adresse d'arrivée
	 **/
	public AddressListModel getEndListModel() {
		return endListModel;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		setChanged();
		notifyObservers();
	}
}
