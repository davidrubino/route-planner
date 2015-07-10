package pcd.telecomnancy.model.member;

import pcd.telecomnancy.model.address.Address;


public class Favorite {

	private Address start, end;
	private String transportType;

	public Favorite(Address start, Address end, String transportType) {
		this.start = start;
		this.end = end;
		this.transportType = transportType;
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
}
