package pcd.telecomnancy.view;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.view.member.ConnectionView;
import pcd.telecomnancy.view.member.RegistrationView;

/**
 * Représente la fenêtre permettant se s'identifier ou de s'inscrire lancée au
 * début du programme
 */
public class PopUpView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private final CardLayout layout;
	private ConnectionView connection;
	private RegistrationView registration;
	private Frame mainFrame;

	public PopUpView(Model model) {

		connection = new ConnectionView(model, this);
		registration = new RegistrationView(model, this);
		this.setSize(450, 375);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		layout = new CardLayout();
		contentPanel = new JPanel();
		contentPanel.setLayout(layout);

		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPanel.add(connection, "connection");
		contentPanel.add(registration, "registration");
		this.setContentPane(contentPanel);
		this.setResizable(false);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {

			}
		});

		pack();
	}

	/**
	 * Cette fonction permet d'afficher la fenêtre du layout dont le nom est
	 * passé en paramètre
	 * 
	 * @param name
	 *            Le nom de la fenêtre à afficher (connexion, inscription)
	 */
	public void show(String name) {
		layout.show(contentPanel, name);
	}

	public void setMainFrame(Frame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public Frame getMainFrame() {
		return mainFrame;
	}
}
