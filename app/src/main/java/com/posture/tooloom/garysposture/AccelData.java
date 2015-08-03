package com.posture.tooloom.garysposture;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccelData implements Serializable {
	private Date timestamp;
	private double x;
	private double y;
	private double z;
	private double longtermz;
	
	
	
	public AccelData(Date timestamp, double z, double longtermz) {
		this.timestamp = timestamp;
		this.z = z;
		this.longtermz = longtermz;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public double getZ() {
		return z;
	}
	public double getLongtermZ() {
		return longtermz;
	}

	public String toString()
	{
		return "t="+timestamp+", z="+z+", longtermz="+longtermz;
	}
	public String toCsv()
{
	SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm:ss a");
	long currentTime = System.currentTimeMillis();

	Date resultDate = new Date(currentTime);
	String timeString  = timeformat.format(resultDate);

	return (timeString+","+z+","+longtermz+"\n");
}

}
