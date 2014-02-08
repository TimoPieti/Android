package com.tipi.androidsporttracker;

import java.util.List;

public class Exercise {
	
	
	private long id;
	private String header;
	private String distance;
	private String speed;
	private String duration;
	private List<Double> lat;
	private List<Double> lon;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getHeader() {
		return header;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public List<Double> getLat() {
		return lat;
	}
	public void setLat(List<Double> lat) {
		this.lat = lat;
	}
	
	public List<Double> getLon() {
		return lon;
	}
	public void setLon(List<Double> lon) {
		this.lon = lon;
	} 
	
	@Override
	public String toString() {
		return header;
	} 

}

