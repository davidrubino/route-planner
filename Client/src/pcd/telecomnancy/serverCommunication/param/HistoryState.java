package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;

import pcd.telecomnancy.serverCommunication.param.HistoryParam.Action;



public interface HistoryState {
	public String getPostString() throws UnsupportedEncodingException;

	public String getPage();

	public Action getAction();

	public String getRequestDescription();
}
