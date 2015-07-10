package pcd.telecomnancy.view.address;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import pcd.telecomnancy.dal.DAL;
import pcd.telecomnancy.dal.DataNotFoundException;
import pcd.telecomnancy.model.ErrorModel;
import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.model.address.Address;
import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.model.routes.Route;
import pcd.telecomnancy.serverCommunication.MultipleCallback;
import pcd.telecomnancy.serverCommunication.UpdatableFromServerData;
import pcd.telecomnancy.serverCommunication.param.RoutesParam;
import pcd.telecomnancy.view.LoadingView;


/** L'écouteur et contrôleur de la liste d'adresses **/
public class AddressListController implements ActionListener {

	private AddressListView addressListView;
	private ErrorModel errorModel;
	private UpdatableFromServerData<Route> routesListModel;
	private DAL<MultipleCallback<Route>> routesDAL;
	private RoutesParam routesParam;
	private Session session;

	/**
	 * Construit l'écouteur/contrôleur de la liste des adresses
	 * 
	 * @param addressListView
	 *            la vue de la liste des adresses
	 * @param model
	 *            Le modèle global qui permet d'accéder à tous les modèles
	 **/
	public AddressListController(AddressListView addressListView, Model model) {
		addressListView.setController(this);
		this.addressListView = addressListView;
		this.routesListModel = model.getRoutesListModel();
		this.routesDAL = model.getRoutesDAL();
		this.routesParam = model.getApiParams().getRouteParam();
		this.errorModel = model.getErrorModel();
		this.session = model.getSession();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		Runnable r = new Runnable() {

			@Override
			/****/
			public void run() {

				int sIndex = addressListView.getStartList().getSelectedIndex();
				int eIndex = addressListView.getEndList().getSelectedIndex();
				if (sIndex == -1)
					errorModel.addInfos("Sélection du départ",
							"Veuillez sélectionner une adresse de départ.");
				if (eIndex == -1)
					errorModel.addInfos("Sélection de l'arrivée",
							"Veuillez sélectionner une adresse d'arrivée.");
				final LoadingView loading = new LoadingView(addressListView,
						addressListView.getSubmitButton());
				if (!(sIndex == -1 || eIndex == -1)) {
					loading.beginLoading();
					Address start = addressListView.getModel()
							.getStartListModel().getData().get(sIndex);

					Address end = addressListView.getModel().getEndListModel()
							.getData().get(eIndex);
					RoutesParam rParam = routesParam;
					rParam.setStart(start);
					rParam.setEnd(end);

					routesDAL.find(rParam, new MultipleCallback<Route>() {

						@Override
						public void onDataReady(List<Route> list) {
							// TODO Auto-generated method stub
							routesListModel.setData(list);
							loading.finishLoading();
							session.getCurrentOnlineRequest().clear();
						}

						@Override
						public void onDataNotFound(DataNotFoundException e) {
							// TODO Auto-generated method stub
							errorModel.addInfos("Recherche de trajet",
									"Aucun trajet n'a été trouvé.");
							errorModel.validate();
							loading.finishLoading();
							session.getCurrentOnlineRequest().clear();
						}

						@Override
						public void onComError(String title, String message) {
							// TODO Auto-generated method stub
							errorModel.addInfos(title, message);
							errorModel.validate();
							loading.finishLoading();
							session.getCurrentOnlineRequest().clear();
						}

					});
				} else {
					errorModel.validate();
				}

			}
		};
		Thread t = new Thread(r);
		t.start();

	}
}
