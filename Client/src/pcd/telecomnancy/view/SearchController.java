package pcd.telecomnancy.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.BorderFactory;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

import pcd.telecomnancy.dal.AddressDAL;
import pcd.telecomnancy.dal.DataNotFoundException;
import pcd.telecomnancy.model.ErrorModel;
import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.model.address.Address;
import pcd.telecomnancy.model.address.AddressListModelManager;
import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.serverCommunication.MultipleCallback;
import pcd.telecomnancy.serverCommunication.param.AddressParam;
import pcd.telecomnancy.serverCommunication.param.RoutesParam;

public class SearchController implements ActionListener {

	private SearchPanel searchPanel;
	private AddressListModelManager addressListModelManager;
	private final String DATE_ERROR = "Le format de la date que vous avez saisi est incorrect. Nous vous invitons à utiliser le calendrier.";
	private final String DATE_TITLE = "Date incorrecte";
	private AddressDAL addressDAL;
	private AddressParam startParam;
	private AddressParam endParam;
	private RoutesParam routesParam;
	private Session session;

	public SearchController(SearchPanel searchPanel, Model model) {
		this.searchPanel = searchPanel;
		this.addressListModelManager = model.getAddressListModelManager();
		this.addressDAL = model.getAddressDAL();
		this.errorModel = model.getErrorModel();
		this.startParam = model.getApiParams().getStartParam();
		this.endParam = model.getApiParams().getEndParam();
		this.routesParam = model.getApiParams().getRouteParam();
		this.session=model.getSession();
	}

	private ErrorModel errorModel;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		final LoadingView loading = new LoadingView(searchPanel,
				searchPanel.getSubmitButton());
		loading.beginLoading();
		Runnable runnable = new Runnable() {
			private final Semaphore sem = new Semaphore(0);

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean error = false;
				boolean date_error = false;

				String start = searchPanel.getStartField().getText();

				String end = searchPanel.getEndField().getText();

				String time = searchPanel.getTimeField().getText();
				String hourString = time.substring(0, 2);
				int hour = Integer.parseInt(hourString);
				String minuteString = time.substring(3, 5);
				int minute = Integer.parseInt(minuteString);

				String date = searchPanel.getDateField().getText();

				String regex = "^[0-3][0-9]/((0[1-9])|(1[0-2]))/[0-9]{4}";
				int day = 0, month = 0, year = 0;
				if (date.matches(regex)) { // si la date est correcte

					// découpe la date pour avoir les 3 entiers jour, mois,
					// année
					String dayString = date.substring(0, 2);
					day = Integer.parseInt(dayString);
					String monthString = date.substring(3, 5);
					month = Integer.parseInt(monthString);
					String yearString = date.substring(6, 10);
					year = Integer.parseInt(yearString);
					searchPanel.getDateField().setBorder(
							UIManager.getBorder("TextField.border"));
				} else {
					searchPanel.getDateField().setBorder(
							BorderFactory.createEtchedBorder(Color.RED,
									Color.BLACK));
					SearchController.this.errorModel.addInfos(DATE_TITLE,
							DATE_ERROR);
				}

				// récupère le mode de transport sélectionné
				JRadioButton radioButtons[] = searchPanel.getTransportButtons();
				String transportMode = "";
				for (JRadioButton jrb : radioButtons) {
					if (jrb.isSelected()) {
						transportMode = jrb.getText();
					}
				}
				// System.out.println("Mode de transport : " + transportMode);

				if (start.length() < 3) {
					searchPanel.getStartField().setBorder(
							BorderFactory.createEtchedBorder(Color.RED,
									Color.BLACK));
					error = true;
					SearchController.this.errorModel
							.addInfos("Champ départ",
									"Le champ de départ doit contenir 3 caractères minimum.");
				} else {
					searchPanel.getStartField().setBorder(
							UIManager.getBorder("TextField.border"));
				}

				if (end.length() < 3) {
					searchPanel.getEndField().setBorder(
							BorderFactory.createEtchedBorder(Color.RED,
									Color.BLACK));
					SearchController.this.errorModel
							.addInfos("Champ arrivée",
									"Le champ d'arrivée doit contenir 3 caractères minimum.");
					error = true;
				} else {
					searchPanel.getEndField().setBorder(
							UIManager.getBorder("TextField.border"));
				}

				if (!error && !date_error) {
					final AddressParam sParam = startParam;
					final AddressParam eParam = endParam;
					sParam.setEntry(start);
					eParam.setEntry(end);
					RoutesParam rParam = routesParam;
					rParam.setTransportType(transportMode);
					rParam.setMinute(minute);
					rParam.setHour(hour);
					rParam.setDay(day);
					rParam.setMonth(month);
					rParam.setYear(year);

					addressDAL.find(sParam, new MultipleCallback<Address>() {

						@Override
						public void onDataReady(List<Address> list) {
							// TODO Auto-generated method stub
							addressListModelManager.getStartListModel()
									.setData(list);
							sem.release();
						}

						@Override
						public void onDataNotFound(DataNotFoundException e) {
							// TODO Auto-generated method stub
							errorModel
									.addInfos("Résultats départ",
											"Aucune adresse de départ n'a été trouvée.");
							sem.release();
						}

						@Override
						public void onComError(String title, String message) {
							// TODO Auto-generated method stub
							errorModel.addInfos(title, message);
							sem.release();
						}

					});

					addressDAL.find(eParam, new MultipleCallback<Address>() {

						@Override
						public void onDataReady(List<Address> list) {
							// TODO Auto-generated method stub
							addressListModelManager.getEndListModel().setData(
									list);
							sem.release();
						}

						@Override
						public void onDataNotFound(DataNotFoundException e) {
							// TODO Auto-generated method stub
							errorModel
									.addInfos("Résultats arrivée",
											"Aucune adresse d'arrivée n'a été trouvée.");
							sem.release();
						}

						@Override
						public void onComError(String title, String message) {
							// TODO Auto-generated method stub
							errorModel.addInfos(title, message);
							sem.release();
						}

					});
				} else {
					errorModel.validate();
					sem.release();
					sem.release();
				}

				try {
					sem.acquire();
					sem.acquire();
					session.getCurrentOnlineRequest().clear();
					if (errorModel.getMessages().size() > 0)
						errorModel.validate();
					loading.finishLoading();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};

		Thread t = new Thread(runnable);
		t.start();

	}
}
