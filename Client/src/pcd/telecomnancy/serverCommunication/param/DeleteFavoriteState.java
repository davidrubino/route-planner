package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import pcd.telecomnancy.serverCommunication.param.FavoriteParam.Action;



public class DeleteFavoriteState implements FavoriteState {

	private FavoriteParam favoriteParam;

	public DeleteFavoriteState(FavoriteParam favoriteParam) {
		// TODO Auto-generated constructor stub
		this.favoriteParam = favoriteParam;
	}

	@Override
	public String getPostString() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String param = "start_id=" + favoriteParam.getStart().getId() + "&end_id=" + favoriteParam.getEnd().getId()
				+ "&transportType="
				+ URLEncoder.encode(favoriteParam.getTransportType(), "UTF-8");

		return param;
	}

	@Override
	public String getPage() {
		// TODO Auto-generated method stub
		return "/favorite/delete";
	}

	@Override
	public Action getAction() {
		// TODO Auto-generated method stub
		return Action.DELETE;
	}

	@Override
	public String getRequestDescription() {
		// TODO Auto-generated method stub
		return "Suppression d'un trajet favori";
	}

}
