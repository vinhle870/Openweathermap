package objects;


import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



import custom_Func.Data_Optimize;
import custom_Func.DateTime_Manage;


public class DailyForcast {
	
	
	public Map<Object,Object>day_weatherlist;
	
	
	public DailyForcast()
	{
		day_weatherlist = new HashMap<Object, Object>(0);
	
	}
	
	public Weather getDailyWeatherByIndex(int key_index)
	{
		Set<Object> keyset= day_weatherlist.keySet();
		
		String key = keyset.toArray()[key_index].toString();
			
		Weather cur_weather = (Weather) day_weatherlist.get(key);
		
		return cur_weather;
		
	}
	
	
	public void getResponseDailyForcast(JSONArray arrayjson,String timezone)
	{
		
		for(Object cur_obj:arrayjson)
		{
			
			JSONObject json_obj = (JSONObject) cur_obj;
		
			System.out.println(json_obj.toString());
			
			Long res_dt = Long.parseLong(json_obj.get("dt").toString());
			
			Date dt = DateTime_Manage.ParseLongToDateTime(res_dt);
			
			String datetime = DateTime_Manage.FormatDateTime_WithTimeZone(timezone, dt, "E, MMM dd");
			
			Weather weather = new Weather("C");
			
			weather.getSingleDayForcast(json_obj);
			
			day_weatherlist.put(datetime, weather);
			
			System.out.println("====================");
					
		}
		
		
		
		
			
	}
	
	
	

}
