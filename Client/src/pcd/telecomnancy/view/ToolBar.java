package pcd.telecomnancy.view;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.model.member.Session;

public class ToolBar extends JToolBar implements Observer {

	private static final long serialVersionUID = 1L;

	private JButton registerButton, loginButton, logoutButton, refreshButton,aboutButton;
	private Session session;

	private JToggleButton chooseButton;

	private JToggleButton seeButton;

	private JToggleButton busButton;

	public ToolBar(Model model, final PopUpView connRegView, final Frame frame) {
		this.setFloatable(false);
		this.session = model.getSession();
		this.session.addObserver(this);
		registerButton = new JButton("S'enregistrer");
		loginButton = new JButton("Se connecter");
		logoutButton = new JButton("Se déconnecter");
		refreshButton = new JButton("Restaurer l'interface");
		aboutButton = new JButton("A propos");
		registerButton.setPreferredSize(new Dimension(120, 32));
		loginButton.setPreferredSize(new Dimension(120, 32));
		logoutButton.setPreferredSize(new Dimension(120, 32));
		refreshButton.setPreferredSize(new Dimension(170, 32));
		aboutButton.setPreferredSize(new Dimension(120,32));
		logoutButton.setEnabled(false);

		ImageIcon registerIcon, loginIcon, logoutIcon, restoreIcon, aboutIcon;

		registerIcon = new ImageIcon(getClass().getClassLoader().getResource(
				"register.png"));
		loginIcon = new ImageIcon(getClass().getClassLoader().getResource(
				"login.png"));
		logoutIcon = new ImageIcon(getClass().getClassLoader().getResource(
				"logout.png"));
		restoreIcon = new ImageIcon(getClass().getClassLoader().getResource(
				"restore.png"));
		aboutIcon = new ImageIcon(getClass().getClassLoader().getResource(
				"about.png"));

		registerButton.setIcon(registerIcon);
		loginButton.setIcon(loginIcon);
		logoutButton.setIcon(logoutIcon);
		refreshButton.setIcon(restoreIcon);
		aboutButton.setIcon(aboutIcon);
		
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connRegView.show("registration");
				connRegView.setVisible(true);
			}
		});

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connRegView.show("connection");
				connRegView.setVisible(true);
			}
		});

		logoutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				session.logout();
			}

		});

		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LoadingView loading = new LoadingView(ToolBar.this,refreshButton);
				loading.beginLoading();
				frame.restoreView();
				loading.finishLoading();
			}

		});
		
		aboutButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.showAboutDialog();
			}
			
		});

		ButtonGroup group = new ButtonGroup();
		this.chooseButton = new JToggleButton("Choix du trajet");
		chooseButton.setMargin(new Insets(8, 10, 8, 10));
		chooseButton.setFocusPainted(false);
		chooseButton.setSelected(true);
		chooseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.showFormPanel();
			}

		});
		this.seeButton = new JToggleButton("Visualiser le trajet");
		seeButton.setMargin(new Insets(8, 10, 8, 10));
		seeButton.setFocusPainted(false);
		seeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.showResultsPanel();
			}

		});
		
		this.busButton = new JToggleButton("Lignes de bus");
		busButton.setMargin(new Insets(8, 10, 8, 10));
		busButton.setFocusPainted(false);
		busButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.showBusPanel();
			}

		});
		
		group.add(chooseButton);
		group.add(seeButton);
		group.add(busButton);

		this.add(loginButton);
		this.add(logoutButton);
		this.addSeparator();
		this.add(registerButton);
		this.addSeparator();
		this.add(refreshButton);
		this.addSeparator();
		this.add(chooseButton);
		this.add(seeButton);
		this.add(busButton);
		this.add(aboutButton);

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if (session.isLoggedIn()) {
			loginButton.setEnabled(false);
			logoutButton.setEnabled(true);
		} else {
			loginButton.setEnabled(true);
			logoutButton.setEnabled(false);
		}
	}

	public JToggleButton getChooseButton() {
		return chooseButton;
	}

	public JToggleButton getSeeButton() {
		return seeButton;
	}

	public JToggleButton getBusButton() {
		// TODO Auto-generated method stub
		return busButton;
	}

}
