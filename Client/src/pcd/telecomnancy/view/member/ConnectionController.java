package pcd.telecomnancy.view.member;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import pcd.telecomnancy.dal.DataNotFoundException;
import pcd.telecomnancy.dal.MemberDAL;
import pcd.telecomnancy.model.ErrorModel;
import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.model.member.Member;
import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.serverCommunication.SimpleCallback;
import pcd.telecomnancy.serverCommunication.param.ConnectionParam;
import pcd.telecomnancy.view.LoadingView;


/**
 * Représente l'écouteur/contrôleur lors de la connexion d'un membre
 */
public class ConnectionController implements ActionListener {
	private ConnectionView connectionView;
	private ErrorModel errorModel;
	private MemberDAL memberDAL;
	private ConnectionParam connectionParam;
	private Session session;

	/**
	 * Construit une instance de l'écouteur
	 * @param connectionView 
	 * @param model
	 */
	public ConnectionController(ConnectionView connectionView, Model model) {
		this.connectionView = connectionView;
		this.errorModel = model.getErrorModel();
		this.memberDAL = model.getMemberDAL();
		this.connectionParam = model.getApiParams().getConnectionParam();
		this.session = model.getSession();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		(new Thread() {

			@Override
			public void run() {
				String login = connectionView.getLoginTextField().getText();
				String password = new String(connectionView
						.getPasswordTextField().getPassword());
				if (login.length() < 3 || password.length() < 3) {
					errorModel
							.addInfos("Connexion",
									"Le pseudo et mot de passe doivent comporter 3 caractères minimum.");
					errorModel.validate();
				} else {
					final LoadingView loading = new LoadingView(connectionView,
							connectionView.getValidate());
					loading.beginLoading();
					connectionParam.setLogin(login);
					connectionParam.setPassword(password);
					memberDAL.find(connectionParam,
							new SimpleCallback<Member>() {

								@Override
								public void onDataReady(Member member) {
									loading.finishLoading();
									session.getCurrentOnlineRequest().clear();
									connectionView.getPasswordTextField()
											.setText("");
								}

								@Override
								public void onDataNotFound(
										DataNotFoundException e) {
									errorModel.addInfos(e.getTitle(),
											e.getMessage());
									errorModel.validate();
									loading.finishLoading();
									session.getCurrentOnlineRequest().clear();
									connectionView.getPasswordTextField()
											.setText("");
								}

								@Override
								public void onComError(String title,
										String message) {
									errorModel.addInfos(title, message);
									errorModel.validate();
									loading.finishLoading();
									session.getCurrentOnlineRequest().clear();
									connectionView.getPasswordTextField()
											.setText("");
								}

							});

				}
			}
		}).start();
	}

	public ErrorModel getErrorModel() {
		return errorModel;
	}
}
