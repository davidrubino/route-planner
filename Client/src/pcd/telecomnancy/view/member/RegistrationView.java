package pcd.telecomnancy.view.member;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.view.PopUpView;
import pcd.telecomnancy.view.SpringUtilities;


public class RegistrationView extends AbstractConnectionRegistrationView {

	private static final long serialVersionUID = 1L;

	private JButton register, reset;
	private RegistrationController controller;
	private JTextField mailField, nameField, firstNameField;
	private JLabel mailLabel, nameLabel, firstNameLabel;

	public RegistrationView(Model model, PopUpView parent) {
		super(parent);
		controller = new RegistrationController(this, model);
		register.addActionListener(controller);
	}

	public JButton getSubmitButton() {
		return register;
	}

	@Override
	public void buildButtonPanel() {
		register = new JButton("Valider");
		reset = new JButton("Tout effacer");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFields();
			}
		});
		buttonPanel.add(reset);
		buttonPanel.add(register);
	}

	@Override
	protected void buildFormPanel() {
		this.buildDefaultFormPanel();
		mailField = new JTextField();
		nameField = new JTextField();
		firstNameField = new JTextField();
		mailLabel = new JLabel("E-mail : ", JLabel.TRAILING);
		nameLabel = new JLabel("Nom : ", JLabel.TRAILING);
		firstNameLabel = new JLabel("Prénom : ", JLabel.TRAILING);
		mailLabel.setLabelFor(mailField);
		nameLabel.setLabelFor(nameField);
		firstNameLabel.setLabelFor(firstNameField);
		formPanel.add(mailLabel);
		formPanel.add(mailField);
		formPanel.add(nameLabel);
		formPanel.add(nameField);
		formPanel.add(firstNameLabel);
		formPanel.add(firstNameField);
		SpringUtilities.makeCompactGrid(formPanel, 5, 2, 50, 6, 6, 6);
	}

	@Override
	protected String getTitle() {
		return "Enregistrement";
	}

	public JTextField getMailField() {
		return mailField;
	}

	public JTextField getNameField() {
		return nameField;
	}

	public JTextField getFirstNameField() {
		return firstNameField;
	}

	/**
	 * Cette fonction permet d'effacer les champs contenus dans la fenêtre
	 * inscription/connexion après une première connexion
	 */
	public void resetFields() {
		loginTextField.setText("");
		passwordTextField.setText("");
		mailField.setText("");
		nameField.setText("");
		firstNameField.setText("");
	}
}
