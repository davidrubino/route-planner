package pcd.telecomnancy.view;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

/**
 * Représente le chargement lors de l'attente des résultats du serveur suite à
 * une demande de trajet
 */
public class LoadingView {

	private Component component;
	private JButton button;

	public LoadingView(Component c, JButton b) {
		this.component = c;
		this.button = b;
	}

	public LoadingView(Component c) {
		this.component = c;
	}

	/**
	 * Permet le début du chargement et met le programme en état d'attente
	 */
	public void beginLoading() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (button != null)
					button.setEnabled(false);
				component.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			}
		});
	}

	/**
	 * Appelée après la fin d'un chargement et rend la main
	 */
	public void finishLoading() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (button != null)
					button.setEnabled(true);
				component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}
}
