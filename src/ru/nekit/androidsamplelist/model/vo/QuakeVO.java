package ru.nekit.androidsamplelist.model.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.location.Location;

public class QuakeVO {

	public Date date;
	public String description;
	public Location location;
	public double magnitude;
	public String link;


	public QuakeVO(Date date, String description, Location location, double magnitude, String link) {
		this.date = date;
		this.description = description;
		this.location = location;
		this.magnitude = magnitude;
		this.link = link;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
		String dateString = sdf.format(date);
		return dateString + ": " + magnitude + " " + description;
	}
}