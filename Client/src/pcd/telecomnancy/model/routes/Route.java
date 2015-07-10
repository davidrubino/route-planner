package pcd.telecomnancy.model.routes;

import java.util.ArrayList;
import java.util.List;

import pcd.telecomnancy.model.address.Address;



public class Route {

	private int id=-1;
	private Address start;
	private Address end;
	private List<Transport> transports;
	private String transportType;
	private int duration;
	private int distance;
	private int hour;
	private int minute;
	private int year;
	private int month;
	private int day;

	public Route() {
		start = new Address();
		end = new Address();
		transports = new ArrayList<Transport>();
		transportType = "";
	}

	public String getTransportType() {
		return transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	public List<Transport> getTransports() {
		return transports;
	}

	public void setTransports(List<Transport> transports) {
		this.transports = transports;
	}

	public Route(Address start, Address end) {
		this.start = start;
		this.end = end;
	}

	public Address getStart() {
		return start;
	}

	public Address getEnd() {
		return end;
	}

	public void setStart(Address start) {
		this.start = start;
	}

	public void setEnd(Address end) {
		this.end = end;
	}

	public void addTransport(Transport t) {
		this.transports.add(t);
	}

	public String toString() {
		return this.start.getLabel() + " => " + this.end.getLabel();
	}

	public int getDuration() {
		return duration;
	}

	public int getDistance() {
		return distance;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public void setDateHour(int day, int month, int year, int hour, int minute) {
		this.day = day;
		this.month = month;
		this.year = year;
		this.hour = hour;
		this.minute = minute;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
