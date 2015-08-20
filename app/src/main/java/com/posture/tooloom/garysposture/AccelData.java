package com.posture.tooloom.garysposture;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccelData implements Serializable {
	private Date timestamp;
	private double z;
	private double longtermz;
	private double upper, lower;
	private long timestampms;
	
	
	public AccelData(Date timestamp, double z, double longtermz, double upper, double lower,
					 long timestampms) {
		this.timestamp = timestamp;
		this.z = z;
		this.longtermz = longtermz;
		this.upper = upper;
		this.lower = lower;
		this.timestampms = timestampms;
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
	public double getUpper() {
		return upper;
	}
	public double getLower() {
		return lower;
	}
	public long getTimestampms() { return timestampms; }

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
