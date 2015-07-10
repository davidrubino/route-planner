package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.util.Properties;



public class HistoryParam implements IParam {

	private String url;
	private int route_id;
	private HistoryState state,addState,clearState;
	public enum Action{ADD,CLEAR};

	public HistoryParam(Properties properties) {
		// TODO Auto-generated constructor stub
		url = properties.getProperty("url");
		addState = new HistoryAddState(this);
		clearState = new HistoryClearState();
		state=addState;
	}

	public int getRoute_id() {
		return route_id;
	}

	public void setRoute_id(int route_id) {
		this.route_id = route_id;
	}

	@Override
	public String getPostString() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return state.getPostString();
	}

	@Override
	public String getPage() {
		// TODO Auto-generated method stub
		return state.getPage();
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return url;
	}

	@Override
	public String getRequestMethod() {
		// TODO Auto-generated method stub
		return "POST";
	}
	
	public void setAction(Action action){
		if(action==Action.ADD)
			state=addState;
		else if(action==Action.CLEAR)
			state=clearState;
	}

	@Override
	public void buildPart(URLConnection con) {
		con.setRequestProperty("User-Agent", "Mozilla/24.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	}

	@Override
	public String getRequestDescription() {
		// TODO Auto-generated method stub
		return state.getRequestDescription()+" sur "+getUrl()+getPage();
	}

}
