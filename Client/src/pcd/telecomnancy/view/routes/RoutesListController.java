package pcd.telecomnancy.view.routes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import pcd.telecomnancy.model.DisplayedRoute;
import pcd.telecomnancy.model.ErrorModel;
import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.model.member.HistoryTableModel;
import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.model.routes.Route;
import pcd.telecomnancy.serverCommunication.ServerCommunication;
import pcd.telecomnancy.serverCommunication.ServerCommunicationImpl;
import pcd.telecomnancy.serverCommunication.ServerCommunicationProxy;
import pcd.telecomnancy.serverCommunication.ServerDataListener;
import pcd.telecomnancy.serverCommunication.param.HistoryParam;


public class RoutesListController implements ActionListener {

	private RoutesListView routesListView;
	private ErrorModel errorModel;
	private HistoryTableModel historyTableModel;
	private DisplayedRoute currentRoute;
	private HistoryParam historyParam;
	private Session session;

	public RoutesListController(Model model) {
		this.currentRoute = model.getCurrentRoute();
		this.historyTableModel=model.getHistoryTableModel();
		this.errorModel=model.getErrorModel();
		this.historyParam=model.getApiParams().getHistoryParam();
		this.session=model.getSession();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub

		int index = routesListView.getJList().getSelectedIndex();
		if (index == -1) {
			errorModel.addInfos("Choix trajet", "Veuillez choisir un trajet");
			errorModel.validate();
		} else {
			Route route = routesListView.getJList().getModel()
					.getElementAt(routesListView.getJList().getSelectedIndex());
			historyTableModel.addData(route);
			if(session.isLoggedIn() && route.getId()>-1){
				historyParam.setRoute_id(route.getId());
				ServerCommunication sc = new ServerCommunicationProxy(session,new ServerCommunicationImpl(session));
				sc.addRequest(historyParam);
				sc.doRequests(new ServerDataListener(){

					@Override
					public void dataReceived(List<String> response) {
						// TODO Auto-generated method stub
						session.getCurrentOnlineRequest().clear();
					}

					@Override
					public void onError(String title, String message) {
						// TODO Auto-generated method stub
						session.getCurrentOnlineRequest().clear();
						errorModel.addInfos(title, message);
						errorModel.validate();
					}
					
				});
			}
			currentRoute.setRoute(route);
		}

	}

	public void setRoutesListView(RoutesListView routesListView) {
		// TODO Auto-generated method stub
		this.routesListView = routesListView;
	}

	public void setErrorModel(ErrorModel errorModel) {
		// TODO Auto-generated method stub
		this.errorModel = errorModel;
	}

	public void setHistoryTableModel(HistoryTableModel historyTableModel) {
		this.historyTableModel = historyTableModel;

	}

}
