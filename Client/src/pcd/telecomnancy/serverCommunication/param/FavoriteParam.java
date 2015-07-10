package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Properties;

import pcd.telecomnancy.model.address.Address;


public class FavoriteParam implements IParam {

	private Address start;
	private Address end;
	private String transportType;
	private String url;

	public enum Action {
		ADD, DELETE, CALCULATE
	};

	private FavoriteState state, addState, deleteState, calculateState;
	private Calendar cal;

	public FavoriteParam(Properties properties) {
		addState = new AddFavoriteState(this);
		deleteState = new DeleteFavoriteState(this);
		calculateState = new CalculateFavoriteState(this);
		state = addState;
		this.url = properties.getProperty("url");
		cal = Calendar.getInstance();
	}

	public Address getStart() {
		return start;
	}

	public Address getEnd() {
		return end;
	}

	public String getTransportType() {
		return transportType;
	}

	public void setStart(Address start) {
		this.start = start;
	}

	public void setEnd(Address end) {
		this.end = end;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
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
	public void buildPart(URLConnection con) {
		// TODO Auto-generated method stub
		con.setRequestProperty("User-Agent", "Mozilla/24.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	}

	@Override
	public String getRequestMethod() {
		// TODO Auto-generated method stub
		return "POST";
	}

	public Action getAction() {
		return state.getAction();
	}

	public void setAction(Action action) {
		switch (action) {
		case ADD:
			state = addState;
			break;
		case DELETE:
			state = deleteState;
			break;
		case CALCULATE:
			state = calculateState;
			break;
		default:
			state = addState;
			break;
		}
	}

	public int getHour() {
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinute() {
		return cal.get(Calendar.MINUTE);
	}

	public int getYear() {
		return cal.get(Calendar.YEAR);
	}

	public int getMonth() {
		return cal.get(Calendar.MONTH)+1;
	}

	public int getDay() {
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public void setCal(Calendar cal) {
		this.cal = cal;
	}

	@Override
	public String getRequestDescription() {
		// TODO Auto-generated method stub
		return state.getRequestDescription()+" sur "+getUrl()+getPage();
	}
}
