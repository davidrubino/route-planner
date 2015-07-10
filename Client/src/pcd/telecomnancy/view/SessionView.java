package pcd.telecomnancy.view;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pcd.telecomnancy.model.member.Session;


public class SessionView extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	private Session session;
	private JLabel label;

	public SessionView(Session model) {
		this.session = model;
		this.session.addObserver(this);
		label = new JLabel("<html>Connecté en tant que <strong>"
				+ session.getMember().getLogin() + "</strong></html>");
		this.add(label);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		label.setText("<html>Connecté en tant que <strong>"
				+ session.getMember().getLogin() + "</strong></html>");

	}

}
