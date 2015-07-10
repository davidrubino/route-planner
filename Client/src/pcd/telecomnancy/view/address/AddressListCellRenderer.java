package pcd.telecomnancy.view.address;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import pcd.telecomnancy.model.address.Address;

/** Cette classe permet de spécifier le rendu visuel de la liste des adresses **/
public class AddressListCellRenderer extends JEditorPane implements
		ListCellRenderer<Address> {

	private static final long serialVersionUID = 1L;

	/** Construit une instance du gestionnaire du rendu visuel de la liste d'adresses **/
	public AddressListCellRenderer() {
		this.setContentType("text/html");
		this.setEditable(false);
		this.setDisabledTextColor(this.getSelectedTextColor());
	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends Address> list, Address address, int index,
			boolean isSelected, boolean hasFocus) {

		String style = "padding-left:30px;font-size:11px;";
		if (isSelected) {
			this.setBackground(new Color(35, 80, 255));
			this.setBorder(BorderFactory
					.createLineBorder(new Color(0, 50, 255)));
			style += "color:white;";
		} else {
			this.setBackground(getColorByIndex(index));
			this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			style += "color:black;";
		}

		String addressStr = "<html><div style=\"" + style + "\">";
		if (address.getType() == 1) {
			addressStr += address.getLabel()
					+ " (Arrêt)"

					+ "<br /><span style=\"font-size:10px;color:"
					+ getInfosColor(isSelected)
					+ ";\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ address.getInfos().replace(";",
							"<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;") + "</span>";

		} else {
			addressStr += address.getLabel();
		}
		addressStr += "</div></html>";
		this.setText(addressStr);

		return this;
	}

	/**
	 * Méthode permettant d'obtenir la couleur selon le numéro de ligne de
	 * l'adresse
	 * 
	 * @param i
	 *            numéro de la ligne
	 * @return couleur la couleur de la ligne
	 * **/
	private Color getColorByIndex(int i) {
		return i % 2 != 0 ? Color.white : new Color(225, 225, 255);
	}

	/**
	 * Méthode permettant d'obtenir la couleur de l'adresse selon si elle est
	 * selectionnée ou pas
	 * 
	 * @param selected
	 *            true si l'adresse est selectionnée, false sinon
	 * @return color Le code héxadécimal de la couleur
	 **/
	private String getInfosColor(boolean selected) {
		return !selected ? "#666666" : "#CCCCCC";
	}
}