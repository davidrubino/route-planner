package pcd.telecomnancy.view.address;

import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

import pcd.telecomnancy.model.address.Address;
import pcd.telecomnancy.model.address.AddressListModel;

/**
 * Adaptateur entre le modèle et la JList
 */
public class AddressListAdapter extends AbstractListModel<Address> implements
		Observer {

	private static final long serialVersionUID = 1L;

	private AddressListModel addressListModel;

	/**
	 * Construit une instance de l'adaptateur
	 * 
	 * @param addressListModel
	 *            L'instance du modèle qui gère la liste des adresses d'une
	 *            recherche
	 * 
	 * **/
	public AddressListAdapter(AddressListModel addressListModel) {
		this.addressListModel = addressListModel;
		addressListModel.addObserver(this);
	}

	@Override
	public Address getElementAt(int index) {
		return this.addressListModel.getData().get(index);
	}

	@Override
	public int getSize() {
		return this.addressListModel.getData().size();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.fireContentsChanged(this, 0, this.getSize());
	}

}