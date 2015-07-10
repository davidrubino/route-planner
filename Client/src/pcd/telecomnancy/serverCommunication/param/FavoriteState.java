package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;

import pcd.telecomnancy.serverCommunication.param.FavoriteParam.Action;



public interface FavoriteState {
	public String getPostString() throws UnsupportedEncodingException;

	public String getPage();

	public Action getAction();

	public String getRequestDescription();
}
