package pcd.telecomnancy.view;

import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import pcd.telecomnancy.model.ErrorModel;

/**
 * Représente la boite de dialogue qui apparait dans le cas d'une erreur
 */
public class ErrorView implements Observer {

	private ErrorModel model;

	public ErrorView(ErrorModel errorModel) {
		this.model = errorModel;
		this.model.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg1) {
		String msg = "<html>";
		for (Entry<String, String> e : model.getMessages().entrySet()) {
			msg += "<strong>" + e.getKey() + "</strong> : " + e.getValue()
					+ "<br />";
		}
		msg += "</html>";
		final String message = msg;
		final String title = model.getMessages().size() > 1 ? "Erreurs"
				: "Erreur";
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				JLabel label = new JLabel(message);
				JOptionPane.showMessageDialog(null, label, title,
						JOptionPane.ERROR_MESSAGE);
			}

		});
	}

	public void setModel(ErrorModel e) {
		this.model = e;
		e.addObserver(this);
	}

}
