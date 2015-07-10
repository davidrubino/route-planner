package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import pcd.telecomnancy.serverCommunication.param.FavoriteParam.Action;



public class CalculateFavoriteState implements FavoriteState {

	private FavoriteParam favoriteParam;

	public CalculateFavoriteState(FavoriteParam favoriteParam) {
		// TODO Auto-generated constructor stub
		this.favoriteParam = favoriteParam;
	}

	@Override
	public String getPostString() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String param = "{\"start\":\""
				+ favoriteParam.getStart().getId()
				+ "\",\"end\":\""
				+ favoriteParam.getEnd().getId()
				+ "\",\"start_label\":\""
				+ URLEncoder.encode(favoriteParam.getStart().getLabel(),
						"UTF-8") + "\",\"end_label\":\""
				+ URLEncoder.encode(favoriteParam.getEnd().getLabel(), "UTF-8")
				+ "\",\"year\":\"" + favoriteParam.getYear()
				+ "\",\"month\":\"" + favoriteParam.getMonth()
				+ "\",\"day\":\"" + favoriteParam.getDay() + "\",\"hour\":\""
				+ favoriteParam.getHour() + "\",\"minute\":\""
				+ favoriteParam.getMinute() + "\",\"transport\":\""
				+ URLEncoder.encode(favoriteParam.getTransportType(), "UTF-8")
				+ "\"}";

		return param;
	}

	@Override
	public String getPage() {
		// TODO Auto-generated method stub
		return "/favorite/calculate";
	}

	@Override
	public Action getAction() {
		// TODO Auto-generated method stub
		return Action.CALCULATE;
	}

	@Override
	public String getRequestDescription() {
		// TODO Auto-generated method stub
		return "Calcul d'un trajet favori";
	}

}
