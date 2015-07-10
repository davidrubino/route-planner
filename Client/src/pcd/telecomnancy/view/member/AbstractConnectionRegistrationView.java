package pcd.telecomnancy.view.member;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import pcd.telecomnancy.view.PopUpView;

/**
 * Classe abstraite regroupant le comportement commun des vues de connexion et
 * d'inscription
 */
public abstract class AbstractConnectionRegistrationView extends JPanel {

	private static final long serialVersionUID = 1L;
	protected JPanel logoPanel, titlePanel, formPanel, buttonPanel;
	protected JTextField loginTextField;
	protected JPasswordField passwordTextField;
	protected JLabel loginLabel, passwordLabel;
	protected PopUpView parentFrame;

	public AbstractConnectionRegistrationView(PopUpView parent) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		parentFrame = parent;
		formPanel = new JPanel();
		formPanel.setOpaque(true);
		formPanel.setBackground(new Color(230, 230, 230));
		formPanel.setLayout(new SpringLayout());
		formPanel.setBorder(BorderFactory.createEtchedBorder(1));

		buttonPanel = new JPanel();

		this.buildFormPanel();
		this.buildButtonPanel();

		logoPanel = new JPanel();
		logoPanel.add(new JLabel("", new ImageIcon(getClass().getClassLoader()
				.getResource("univ.png")), 0));
		logoPanel.add(new JLabel("", new ImageIcon(getClass().getClassLoader()
				.getResource("tncy.png")), 0));

		titlePanel = new JPanel();
		titlePanel.add(new JLabel(
				"<html><h1 style=\"font-size:14px;\">Projet PCD - "
						+ getTitle() + "</h1></html>"));

		this.add(logoPanel);
		this.add(titlePanel);
		this.add(formPanel);
		this.add(Box.createRigidArea(new Dimension(0, 30)));
		this.add(buttonPanel);
	}

	/**
	 * Cette fonction permet de construire un panel par défaut comprenant un
	 * champ pour renseigner le login et le mot de passe.
	 */
	protected void buildDefaultFormPanel() {
		loginTextField = new JTextField();
		loginLabel = new JLabel("Login :", JLabel.TRAILING);
		loginLabel.setLabelFor(loginTextField);

		passwordTextField = new JPasswordField();
		passwordLabel = new JLabel("Password :", JLabel.TRAILING);
		passwordLabel.setLabelFor(passwordTextField);

		formPanel.add(loginLabel);
		formPanel.add(loginTextField);
		formPanel.add(passwordLabel);
		formPanel.add(passwordTextField);
	}

	/**
	 * Cette fonction construit le formulaire. Pour un utilisateur enregistré,
	 * le panel sera celui par défaut. Pour une inscription, il comprend des
	 * champs supplémentaires.
	 */
	protected abstract void buildFormPanel();

	/**
	 * Cette fonction permet la construction du panel des boutons
	 */
	protected abstract void buildButtonPanel();

	/**
	 * Retourne le titre de la fenêtre (Inscription, Connexion)
	 * 
	 * @return Le titre de la fenêtre
	 */
	protected abstract String getTitle();

	/**
	 * Accesseur sur le champ login
	 * 
	 * @return Le champ
	 */
	public JTextField getLoginTextField() {
		return loginTextField;
	}

	/**
	 * Accesseur sur le champ du mot de passe
	 * 
	 * @return Le champ
	 */
	public JPasswordField getPasswordTextField() {
		return passwordTextField;
	}

	/**
	 * Accesseur sur la fenêtre parent
	 * 
	 * @return La fenêtre parent
	 */
	public PopUpView getParentFrame() {
		return parentFrame;
	}

}
