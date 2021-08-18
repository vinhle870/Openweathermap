package objects;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import configuration.TestConfigs;
import custom_Func.Data_Optimize;
import custom_Func.DateTime_Manage;
import custom_Func.FileManage;

public class Location {
	
	public String location_value;
	public String coor_lon;
	public String coor_lat;
	public Weather weather;
	public String timezone;
	public String timezoneoffset;
	public DailyForcast dailyForcast;
	public String currenttime;
	
	
	public Location()
	{
		location_value="";
		coor_lon = "";
		coor_lat = "";
		weather = new Weather();
		dailyForcast = new DailyForcast();
		currenttime = "";
	}
	
	
	
	
	public void getLocationInfoViaAPIResps(JSONObject obj)
	{
		timezone = (String)obj.get("timezone");
		
		timezoneoffset = ((Long)obj.get("timezone_offset")).toString();
		
		JSONObject current_obj = (JSONObject) obj.get("current");
				
		Date datetime = DateTime_Manage.ParseLongToDateTime(Long.parseLong(current_obj.get("dt").toString()));
		
		currenttime = DateTime_Manage.FormatDateTime_WithTimeZone(timezone, datetime,"MMM dd, hh:mma");
		
		currenttime = currenttime.replace("AM", "am").replace("PM","pm");
		
		
	}

}
