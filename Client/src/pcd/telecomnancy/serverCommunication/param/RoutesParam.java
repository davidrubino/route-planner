package pcd.telecomnancy.serverCommunication.param;

import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;

import pcd.telecomnancy.model.address.Address;


public class RoutesParam implements IParam {

	private Address start;
	private Address end;
	private String transportType;
	private int hour;
	private int minute;
	private int year;
	private int month;
	private int day;

	private final String PAGE = "/getRoutes";
	private final String REQUEST_METHOD = "POST";
	private String url;

	public RoutesParam(Properties properties) {
		// TODO Auto-generated constructor stub
		this.url = properties.getProperty("url") + "/api";
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

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
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

	public void setHour(int hour) {
		this.hour = hour;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setDay(int day) {
		this.day = day;
	}

	@Override
	public String getPostString() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		String param = "{\"start\":\"" + start.getId() + "\",\"end\":\""
				+ end.getId() + "\",\"year\":\"" + this.getYear()
				+ "\",\"month\":\"" + this.getMonth() + "\",\"day\":\""
				+ this.getDay() + "\",\"hour\":\"" + this.getHour()
				+ "\",\"minute\":\"" + this.getMinute() + "\",\"transport\":\""
				+ this.getTransportType() + "\"}";
		return URLEncoder.encode(param, "UTF-8");
	}

	@Override
	public String getPage() {
		// TODO Auto-generated method stub
		return PAGE;
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
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
	}

	@Override
	public String getRequestMethod() {
		// TODO Auto-generated method stub
		return REQUEST_METHOD;
	}

	@Override
	public String getRequestDescription() {
		// TODO Auto-generated method stub
		return "Récupération des trajets sur "+getUrl()+getPage();
	}
}
