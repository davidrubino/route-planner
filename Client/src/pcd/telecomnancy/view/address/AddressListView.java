package pcd.telecomnancy.view.address;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.model.address.Address;
import pcd.telecomnancy.model.address.AddressListModelManager;
import pcd.telecomnancy.view.Frame;



/** La classe qui gère la vue de la liste des adresses **/
public class AddressListView extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;

	private AddressListModelManager model;
	private JPanel startPanel, endPanel;
	private JList<Address> startList, endList;

	private JPanel resultPanel, buttonPanel;
	private JScrollPane verticalStart, verticalEnd;
	private JButton validate;
	private AddressListController addressListController;

	private Frame frame;

	/**
	 * Construit une instance de la vue affichant une liste d'adresses
	 * 
	 * @param model
	 *            Le modèle global permettant d'accéder à tous les modèles
	 **/
	public AddressListView(final Model model) {
		Color gray = Color.decode("#CCCCCC");
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.model = model.getAddressListModelManager();
		this.model.addObserver(this);
		startList = new JList<Address>(new AddressListAdapter(
				this.model.getStartListModel()));
		startList.setCellRenderer(new AddressListCellRenderer());

		endList = new JList<Address>(new AddressListAdapter(
				this.model.getEndListModel()));
		endList.setCellRenderer(new AddressListCellRenderer());

		JPanel startTitle = new JPanel();
		startTitle.setBackground(gray);
		startTitle.setBorder(BorderFactory.createEtchedBorder());
		startTitle
				.add(new JLabel(
						"<html><div style=\"margin:5px;font-weight:bold;\">Sélectionnez un départ</div></html>"));

		startPanel = new JPanel();
		startPanel.setLayout(new BorderLayout());
		startPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		startPanel.add(startList, BorderLayout.CENTER);
		startPanel.add(startTitle, BorderLayout.NORTH);

		verticalStart = new JScrollPane();
		verticalStart.setViewportView(startPanel);
		verticalStart
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		JPanel endTitle = new JPanel();
		endTitle.setBackground(gray);
		endTitle.setBorder(BorderFactory.createEtchedBorder());
		endTitle.add(new JLabel(
				"<html><div style=\"margin:5px;font-weight:bold;\">Sélectionnez une arrivée</div></html>"));

		endPanel = new JPanel();
		endPanel.setLayout(new BorderLayout());
		endPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		endPanel.add(endTitle, BorderLayout.NORTH);
		endPanel.add(endList, BorderLayout.CENTER);

		verticalEnd = new JScrollPane();
		verticalEnd.setViewportView(endPanel);
		verticalEnd
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		resultPanel = new JPanel();
		resultPanel.setLayout(new GridLayout(1, 2));
		resultPanel.setBorder(BorderFactory.createLineBorder(gray));
		resultPanel.add(verticalStart);
		resultPanel.add(verticalEnd);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		validate = new JButton("Valider");
		validate.setAlignmentX(Component.CENTER_ALIGNMENT);

		buttonPanel.add(validate);

		this.add(resultPanel);
		this.add(buttonPanel);

	}

	/**
	 * Méthode permettant d'obtenir le gestionnaire des deux listes d'adresses
	 * de départ et d'arrivée
	 * 
	 * @return model AddressListModelManager
	 **/
	public AddressListModelManager getModel() {
		return this.model;
	}

	/**
	 * Méthode permettant de changer l'écouteur/contrôleur de la liste
	 * d'adresses
	 * 
	 * @param addressListController
	 *            L'écouteur/contrôleur
	 **/
	public void setController(AddressListController addressListController) {
		this.addressListController = addressListController;
		validate.addActionListener(this.addressListController);
	}

	/**
	 * Méthode permettant d'obtenir la liste des adresses de départ
	 * 
	 * @return startList
	 **/
	public JList<Address> getStartList() {
		return startList;
	}

	/**
	 * Méthode permettant d'obtenir la liste des adresses d'arrivée
	 * 
	 * @return endList
	 **/
	public JList<Address> getEndList() {
		return endList;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		frame.switchToAddressList();
	}

	/**
	 * Accesseur du bouton valider
	 * 
	 * @return validate Le bouton valider
	 **/
	public JButton getSubmitButton() {
		return validate;
	}

	/**
	 * Fait connaître la fenètre principale à la liste d'adresses
	 * 
	 * @param frame
	 *            La fenêtre
	 **/
	public void setMainFrame(Frame frame) {
		// TODO Auto-generated method stub
		this.frame = frame;
	}
}
