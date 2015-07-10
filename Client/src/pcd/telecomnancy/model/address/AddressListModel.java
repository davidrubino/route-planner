package pcd.telecomnancy.model.address;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pcd.telecomnancy.serverCommunication.UpdatableFromServerData;

/** Modèle de la liste des adresses **/
public class AddressListModel extends Observable implements
		UpdatableFromServerData<Address> {
	private ArrayList<Address> address;

	/** Construit une instance du modèle gérant la liste des adresses **/
	public AddressListModel() {
		address = new ArrayList<Address>();
	}

	/**
	 * Mutateur des données de la liste d'adresse
	 * 
	 * @param l
	 *            la liste des nouvelles adresses
	 **/
	public void setData(List<Address> l) {
		// TODO Auto-generated method stub
		address = new ArrayList<Address>();
		if (l.size() > 0) {
			address.addAll(l);
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * Accesseur des données la liste des adresses
	 * 
	 * @return List<Address> la liste des adresses
	 **/
	public List<Address> getData() {
		return address;
	}

	/**
	 * Suppprime une addresse de la liste des adresses
	 * 
	 * @param data
	 *            l'adresse a supprimer de la liste
	 **/
	public void removeData(Address data) {
		// TODO Auto-generated method stub
		address.remove(data);
		this.setChanged();
		this.notifyObservers();
	}
}
