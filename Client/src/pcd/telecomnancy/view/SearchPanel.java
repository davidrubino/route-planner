package pcd.telecomnancy.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class SearchPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SearchField[] searchFields;
	private static final int START = 0;
	private static final int END = 1;

	private JLabel[] searchLabel;

	private final String[] searchLabelValue = { "Départ :", "Arrivée :",
			"Date :", "Heure :" };
	private final int FORM_LENGTH = searchLabelValue.length;

	private JDateChooser jdc;
	private JFormattedTextField jftf;

	private JPanel formInputLabelPanel;
	private JPanel formInputPanel;
	private JPanel formLabelPanel;

	private JPanel transportPanel;
	private final String[] transportLabel = { "Bus", "A pied", "Voiture" };
	private final int TRANSPORT_LENGTH = transportLabel.length;
	private JRadioButton[] transportButtons;
	private ButtonGroup bg = new ButtonGroup();

	private JPanel submitPanel;
	private JButton submitButton;

	private JPanel resultsPanel;

	public SearchPanel() {

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		formInputPanel = new JPanel();
		formLabelPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(4, 1);
		gridLayout.setVgap(20);
		formInputPanel.setLayout(gridLayout);
		formLabelPanel.setLayout(gridLayout);
		searchFields = new SearchField[FORM_LENGTH - 2];
		searchLabel = new JLabel[FORM_LENGTH];
		for (int i = 0; i < FORM_LENGTH - 2; i++) {
			searchFields[i] = new SearchField();
			searchLabel[i] = new JLabel(searchLabelValue[i],JLabel.TRAILING);
			searchLabel[i].setLabelFor(searchFields[i]);
			searchFields[i].setPreferredSize(new Dimension(100, 30));
			searchLabel[i].setPreferredSize(new Dimension(100, 30));
			formLabelPanel.add(searchLabel[i]);
			formInputPanel.add(searchFields[i]);
		}

		// Choix de la date grâce au calendrier
		searchLabel[2] = new JLabel(searchLabelValue[2]);
		searchLabel[2].setPreferredSize(new Dimension(100, 30));
		formLabelPanel.add(searchLabel[2]);
		Date currentDate = new Date();
		jdc = new JDateChooser(currentDate, "dd/MM/yyyy");
		formInputPanel.add(jdc);

		// Choix de l'heure sous le format HH:mm
		searchLabel[3] = new JLabel(searchLabelValue[3]);
		searchLabel[3].setPreferredSize(new Dimension(100, 30));
		formLabelPanel.add(searchLabel[3]);

		Date date = new Date();
		SpinnerDateModel sdm = new SpinnerDateModel(date, null, null,
				Calendar.HOUR_OF_DAY);
		JSpinner spinner = new JSpinner(sdm);
		JSpinner.DateEditor sde = new JSpinner.DateEditor(spinner, "HH:mm");
		spinner.setEditor(sde);

		jftf = sde.getTextField();
		formInputPanel.add(spinner);

		formLabelPanel.setPreferredSize(new Dimension(75, 175));
		formInputPanel.setPreferredSize(new Dimension(200, 175));
		formInputLabelPanel = new JPanel();
		formInputLabelPanel.setMinimumSize(new Dimension(0, 185));
		formInputLabelPanel.setMaximumSize(new Dimension(400, 350));
		formInputLabelPanel.add(formLabelPanel);
		formInputLabelPanel.add(formInputPanel);

		transportPanel = new JPanel();
		transportPanel.setBorder(BorderFactory
				.createTitledBorder("Modes de transport"));
		transportPanel.setMinimumSize(new Dimension(0, 60));
		transportPanel.setMaximumSize(new Dimension(400, 60));

		transportButtons = new JRadioButton[TRANSPORT_LENGTH];
		for (int i = 0; i < TRANSPORT_LENGTH; i++) {
			transportButtons[i] = new JRadioButton(transportLabel[i]);
			bg.add(transportButtons[i]);
			transportPanel.add(transportButtons[i]);
		}
		transportButtons[0].setSelected(true);

		/*
		 * ImageIcon footImage = new ImageIcon("walking-icon.png"); ImageIcon
		 * carImage = new ImageIcon("car-icon.png"); ImageIcon busImage = new
		 * ImageIcon("bus-icon.png");
		 */

		submitPanel = new JPanel();
		submitPanel.setMaximumSize(new Dimension(225, 50));
		submitButton = new JButton("Valider");
		submitPanel.add(submitButton);

		resultsPanel = new JPanel();

		this.add(formInputLabelPanel);
		this.add(transportPanel);
		this.add(submitPanel);
		this.add(resultsPanel);
		
	}

	public SearchField getStartField() {
		return this.searchFields[START];
	}

	public SearchField getEndField() {
		return this.searchFields[END];
	}

	public JTextFieldDateEditor getDateField() {
		return (JTextFieldDateEditor) jdc.getDateEditor();
	}

	public JFormattedTextField getTimeField() {
		return this.jftf;
	}

	public JButton getSubmitButton() {
		return submitButton;
	}

	public JRadioButton[] getTransportButtons() {
		return transportButtons;
	}
}
