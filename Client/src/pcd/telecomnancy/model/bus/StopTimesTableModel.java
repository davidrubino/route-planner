package pcd.telecomnancy.model.bus;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pcd.telecomnancy.serverCommunication.UpdatableFromServerData;


public class StopTimesTableModel extends Observable implements UpdatableFromServerData<StopTime> {
	
	private List<StopTime> stopTimes;
	
	public StopTimesTableModel() {
		stopTimes = new ArrayList<StopTime>();
	}

	@Override
	public void setData(List<StopTime> stopTimes) {
		this.stopTimes = stopTimes;
		setChanged();
		notifyObservers();
	}

	@Override
	public List<StopTime> getData() {
		return this.stopTimes;
	}

	@Override
	public void removeData(StopTime data) {
		this.stopTimes.remove(data);
		setChanged();
		notifyObservers();
	}

}
