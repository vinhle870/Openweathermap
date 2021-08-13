package objects;


import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



import custom_Func.Data_Optimize;
import custom_Func.DateTime_Manage;


public class Weather {
	
	public Double temp;
	
	public Double visibility;
	public Double uvi;
	public Double pressure;
	public Double clouds;
	public Double feels_like;
	public Long date;
	public Double wind_deg;
	public Double dew_point;
	
	public String weather_descr;
	public Double humidity;
	public Double wind_speed;
	public String temp_unit;
	public String windspeed_unit;
	public String datetime;
	
	//Dailyforcast
	public Double wind_gust;
	public Double pop;
	public Double rain;
	public String sunset;
	public String sunrise;
	public Map<Object, Object> dailytemps, dailyfeel_like;
	
	
	
	public Weather(String temp_unit)
	{
		dailytemps = new HashMap<Object, Object>();
		dailyfeel_like = new HashMap<Object, Object>();
		temp = 0.0;
		sunrise = "";
		visibility = 0.0;
		uvi = 0.0;
		pressure = 0.0;
		clouds = 0.0;
		feels_like = 0.0;
		
		wind_deg = 0.0;
		dew_point = 0.0;
		sunset = "";
		weather_descr = "";
		humidity = 0.0;
		wind_speed = 0.0;
		datetime = "";
		this.temp_unit = temp_unit;//F
		
		if(temp_unit.equals("C"))
			windspeed_unit = "m/s";
		else
			windspeed_unit = "mph";
		
		
	}
	
	public Weather()
	{
		
	}
	
	public void getCurrentDateInfo(JSONObject json)
	{
		
	}
	
	public void getCurrentWeatherInfo(JSONObject json)
	{
		getMainInfo(json);
		
		temp = Double.parseDouble(json.get("temp").toString());
		
		visibility = Double.parseDouble(json.get("visibility").toString())/1000;
				
		feels_like = Double.parseDouble(json.get("feels_like").toString()); 
			
	}
	
	
	public void getSingleDayForcast(JSONObject json)
	{
		getMainInfo(json);
		
		JSONObject json_temp = (JSONObject) json.get("temp");
		
		dailytemps.put("day",String.format("%,.0f", Double.parseDouble(json_temp.get("day").toString()) )+"°"+temp_unit);
		
		dailytemps.put("min",String.format("%,.0f", Double.parseDouble(json_temp.get("min").toString()))+"°"+temp_unit);
		
		dailytemps.put("max",String.format("%,.0f", Double.parseDouble(json_temp.get("max").toString()))+"°"+temp_unit);
		
		dailytemps.put("night",String.format("%,.0f", Double.parseDouble(json_temp.get("night").toString()))+"°"+temp_unit);
		
		dailytemps.put("eve",String.format("%,.0f", Double.parseDouble(json_temp.get("eve").toString()))+"°"+temp_unit);
		
		dailytemps.put("morn",String.format("%,.0f", Double.parseDouble(json_temp.get("morn").toString()))+"°"+temp_unit);
		
		
		//Feel Like
		JSONObject json_feel_like = (JSONObject) json.get("feels_like");
		
		dailyfeel_like.put("day",String.format("%,.0f",Double.parseDouble(json_feel_like.get("day").toString()))+"°"+temp_unit);
		
		dailyfeel_like.put("night",String.format("%,.0f", Double.parseDouble(json_feel_like.get("night").toString()))+"°"+temp_unit);
		
		dailyfeel_like.put("eve",String.format("%,.0f", Double.parseDouble(json_feel_like.get("eve").toString()))+"°"+temp_unit);
		
		dailyfeel_like.put("morn",String.format("%,.0f",Double.parseDouble(json_feel_like.get("morn").toString()))+"°"+temp_unit);
		
		sunrise = json.get("sunrise").toString();
		
		wind_gust = Double.parseDouble(json.get("wind_gust").toString());
		
		pop = Double.parseDouble(json.get("pop").toString());
		
		
		if(json.get("rain")!=null)
		rain = Double.parseDouble(json.get("rain").toString());
	
		
	}
	
	
	private void getMainInfo(JSONObject json)
	{
		sunrise = json.get("sunrise").toString();
		
		uvi =Double.parseDouble(json.get("uvi").toString());
		
		pressure = Double.parseDouble(json.get("pressure").toString());
				
		clouds = Double.parseDouble(json.get("clouds").toString());

		wind_deg = Double.parseDouble(json.get("wind_deg").toString());
		
		dew_point = Double.parseDouble(json.get("dew_point").toString());
		
		sunset =  json.get("sunset").toString();

		JSONArray weather_details = (JSONArray)json.get("weather");
				
		for(Object cur:weather_details)
		{
			JSONObject json_o = (JSONObject) cur;
			
			weather_descr =json_o.get("description").toString();
		}
				
		weather_descr = weather_descr.substring(0,1).toUpperCase() + weather_descr.substring(1).toLowerCase();

	
		humidity = Double.parseDouble(json.get("humidity").toString()) / 100;
		wind_speed =Double.parseDouble(json.get("wind_speed").toString());
	}
	
	

}
