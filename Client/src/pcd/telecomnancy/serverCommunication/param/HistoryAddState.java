package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;

import pcd.telecomnancy.serverCommunication.param.HistoryParam.Action;



public class HistoryAddState implements HistoryState{

	private HistoryParam historyParam;
	public HistoryAddState(HistoryParam historyParam) {
		// TODO Auto-generated constructor stub
		this.historyParam=historyParam;
	}

	@Override
	public String getPostString() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return "route_id=" + historyParam.getRoute_id();
	}

	@Override
	public String getPage() {
		// TODO Auto-generated method stub
		return "/history/add";
	}

	@Override
	public Action getAction() {
		// TODO Auto-generated method stub
		return Action.ADD;
	}

	@Override
	public String getRequestDescription() {
		// TODO Auto-generated method stub
		return "Envoi d'un historique";
	}

}
