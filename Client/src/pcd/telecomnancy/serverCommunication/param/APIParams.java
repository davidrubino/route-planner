package pcd.telecomnancy.serverCommunication.param;

import java.util.Properties;



public class APIParams {

	final private RoutesParam routeParam;
	final private AddressParam startParam;
	final private AddressParam endParam;

	final private ConnectionParam connectionParam;
	final private RegistrationParam registrationParam;
	final private HistoryParam historyParam;
	final private FavoriteParam favoriteParam;
	final private BusNetworkParam busNetworkParam;
	final private BusLineParam busLineParam;
	private BusStopParam busStopParam;

	public APIParams(Properties properties) {
		this.routeParam = new RoutesParam(properties);
		this.startParam = new AddressParam(properties);
		this.endParam = new AddressParam(properties);
		this.connectionParam = new ConnectionParam(properties);
		this.registrationParam = new RegistrationParam(properties);
		this.favoriteParam = new FavoriteParam(properties);
		this.historyParam = new HistoryParam(properties);
		this.busNetworkParam = new BusNetworkParam(properties);
		this.busLineParam = new BusLineParam(properties);
		this.busStopParam = new BusStopParam(properties);
	}

	public RoutesParam getRouteParam() {
		return routeParam;
	}

	public AddressParam getStartParam() {
		return startParam;
	}

	public AddressParam getEndParam() {
		return endParam;
	}

	public ConnectionParam getConnectionParam() {
		return connectionParam;
	}

	public FavoriteParam getFavoriteParam() {
		return favoriteParam;
	}

	public RegistrationParam getRegistrationParam() {
		return registrationParam;
	}

	public HistoryParam getHistoryParam() {
		return historyParam;
	}

	public BusNetworkParam getBusNetworkParam() {
		return busNetworkParam;
	}

	public BusLineParam getBusLineParam() {
		return busLineParam;
	}

	public BusStopParam getBusStopParam() {
		// TODO Auto-generated method stub
		return this.busStopParam;
	}

}
