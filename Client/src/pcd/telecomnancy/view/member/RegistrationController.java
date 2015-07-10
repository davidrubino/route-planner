package pcd.telecomnancy.view.member;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;


import pcd.telecomnancy.dal.DataNotFoundException;
import pcd.telecomnancy.dal.Registration;
import pcd.telecomnancy.model.ErrorModel;
import pcd.telecomnancy.model.Model;
import pcd.telecomnancy.serverCommunication.SimpleCallback;
import pcd.telecomnancy.serverCommunication.param.RegistrationParam;
import pcd.telecomnancy.view.LoadingView;



public class RegistrationController implements ActionListener {

	private RegistrationView registrationView;
	private ErrorModel errorModel;
	private Registration registration;
	private RegistrationParam param;

	public RegistrationController(RegistrationView registrationView, Model model) {
		this.registrationView = registrationView;
		this.errorModel = model.getErrorModel();
		this.registration = model.getRegistration();
		this.param = model.getApiParams().getRegistrationParam();

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		(new Thread() {

			@Override
			public void run() {
				String login = registrationView.getLoginTextField().getText();
				String password = new String(registrationView
						.getPasswordTextField().getPassword());
				String email = registrationView.getMailField().getText();
				String name = registrationView.getNameField().getText();
				String firstName = registrationView.getFirstNameField()
						.getText();
				if (login.length() < 3 || password.length() < 3
						|| email.length() < 3 || name.length() < 3
						|| firstName.length() < 3) {
					errorModel
							.addInfos("Inscription",
									"Les champs sont obligatoires et doivent comporter 3 caractères minimum.");

				} else {
					if (!email
							.matches("(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$"))
						errorModel.addInfos("E-mail",
								"L'addresse mail n'est pas correcte.");
					else {
						
						param.setLogin(login);
						param.setPassword(password);
						param.setMail(email);
						param.setFirstName(firstName);
						param.setName(name);
						final LoadingView loading = new LoadingView(registrationView,
								registrationView.getSubmitButton());
						loading.beginLoading();
						registration.create(param,
								new SimpleCallback<String>() {

									@Override
									public void onDataNotFound(
											DataNotFoundException e) {
										// TODO Auto-generated method stub
										loading.finishLoading();
										errorModel.addInfos(e.getTitle(), e.getMessage());
										errorModel.validate();
									}

									@Override
									public void onComError(String title,
											String message) {
										// TODO Auto-generated method stub
										loading.finishLoading();
										errorModel.addInfos(title, message);
										errorModel.validate();
									}

									@Override
									public void onDataReady(String message) {
										// TODO Auto-generated method stub
										loading.finishLoading();
										JOptionPane.showMessageDialog(registrationView,message);
										registrationView.getParentFrame().show("connection");
										registrationView.resetFields();
									}
								});
					}
				}
				if (errorModel.getMessages().size() > 0)
					errorModel.validate();
			}
		}).start();
	}

	public ErrorModel getErrorModel() {
		return errorModel;
	}

}
