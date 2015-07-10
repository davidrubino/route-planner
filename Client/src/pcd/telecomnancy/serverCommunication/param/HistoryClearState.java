package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;

import pcd.telecomnancy.serverCommunication.param.HistoryParam.Action;



public class HistoryClearState implements HistoryState {

	@Override
	public String getPostString() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getPage() {
		// TODO Auto-generated method stub
		return "/history/clear";
	}

	@Override
	public Action getAction() {
		// TODO Auto-generated method stub
		return Action.CLEAR;
	}

	@Override
	public String getRequestDescription() {
		// TODO Auto-generated method stub
		return "Vidage de l'hitorique";
	}

}
