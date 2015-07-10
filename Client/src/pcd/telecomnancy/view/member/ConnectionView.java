package pcd.telecomnancy.view.member;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.view.Frame;
import pcd.telecomnancy.view.PopUpView;
import pcd.telecomnancy.view.SpringUtilities;


public class ConnectionView extends AbstractConnectionRegistrationView
		implements Observer {

	private static final long serialVersionUID = 1L;

	private JButton validate, guest, signup;
	private ConnectionController connectionController;
	private Session session;

	public ConnectionView(Model model, PopUpView parent) {
		super(parent);
		this.session = model.getSession();
		this.session.addObserver(this);
		connectionController = new ConnectionController(this, model);
		validate.addActionListener(connectionController);
	}

	public JButton getValidate() {
		return validate;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (session.isLoggedIn() && this.isVisible()) {
			parentFrame.setDefaultCloseOperation(Frame.HIDE_ON_CLOSE);
			parentFrame.setVisible(false);
			parentFrame.getMainFrame().setVisible(true);
		}
	}

	@Override
	public void buildButtonPanel() {
		validate = new JButton("Se connecter");

		guest = new JButton("Continuer en tant qu'invité");
		guest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				parentFrame.setVisible(false);
				parentFrame.getMainFrame().setVisible(true);
				parentFrame.setDefaultCloseOperation(Frame.HIDE_ON_CLOSE);
			}

		});
		signup = new JButton("S'enregistrer");
		signup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				parentFrame.show("registration");
			}

		});
		buttonPanel.add(signup);
		buttonPanel.add(guest);
		buttonPanel.add(validate);
	}

	@Override
	protected void buildFormPanel() {
		this.buildDefaultFormPanel();
		SpringUtilities.makeCompactGrid(formPanel, 2, 2, 50, 6, 6, 6);
	}

	@Override
	protected String getTitle() {
		return "Connexion";
	}
}
