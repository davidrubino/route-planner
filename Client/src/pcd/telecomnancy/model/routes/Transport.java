package pcd.telecomnancy.model.routes;

import java.util.ArrayList;
import java.util.List;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class Transport {

	private List<Coordinate> points;
	private List<Step> steps;

	private String description;
	private int duration;
	private int distance;
	private int type;

	public Transport() {
		points = new ArrayList<Coordinate>();
		steps = new ArrayList<Step>();
		description = "";
	}

	public List<Coordinate> getPoints() {
		return points;
	}

	public List<Step> getSteps() {
		return steps;
	}

	public String getDescription() {
		return description;
	}

	public void setPoints(List<Coordinate> points) {
		this.points = points;
	}

	public void addPoint(Coordinate point) {
		this.points.add(point);
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

	public void addStep(Step s) {
		this.steps.add(s);
	}

	public void setDescription(String description) {
		this.description = description;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
