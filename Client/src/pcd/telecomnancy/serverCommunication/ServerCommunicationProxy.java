package pcd.telecomnancy.serverCommunication;

import java.util.ArrayList;
import java.util.List;

import pcd.telecomnancy.model.member.Session;
import pcd.telecomnancy.serverCommunication.param.IParam;



public class ServerCommunicationProxy implements ServerCommunication{
	
	private ServerCommunication serverCommunication;
	private List<String> descriptions;
	private Session session;
	public ServerCommunicationProxy(Session session, ServerCommunicationImpl impl) {
		serverCommunication = impl;
		descriptions=new ArrayList<String>();
		this.session=session;
	}

	@Override
	public void addRequest(IParam param) {
		descriptions.add(param.getRequestDescription());
		serverCommunication.addRequest(param);
	}

	@Override
	public void doRequests(ServerDataListener serverDataListener) {
		for(String s : descriptions){
			session.getCurrentOnlineRequest().setDescription(s);
		}
		serverCommunication.doRequests(serverDataListener);
	}
}
