package stepsDefiniton;




import java.io.IOException;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


import baseClasses.StepsBase;
import baseClasses.glb_RefData;
import configuration.TestConfigs;
import net.thucydides.core.annotations.Step;
import objects.DailyForcast;
import objects.Response;
import objects.Weather;
import objects.Location;
import pages.openweathermap_page;



public class User_AllSteps extends StepsBase{
	
	openweathermap_page searchweather_page;
	
	JSONObject json_rootobject;
	
	@Override
	public void InitStepsDefinition() {
		// TODO Auto-generated method stub
		searchweather_page = new openweathermap_page(driver);
	}
	
	@Step("User Access to site")
	public void access_Site()
	{
		//Add the Core method from page.java
		searchweather_page.driverbase.GetDriver().get(glb_RefData.env_url);
		
		searchweather_page.driverbase.GetDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
				
	}//end void
	
	@Step("User Search Weather for City by {city_name}")
	public void SearchWeatherbyCityName(Location expected_location)
	{
		searchweather_page.searchWeatherbyCityName(expected_location);
		
	}
	
	@Step("Get Response Body Via API Request")
	public Location getWeatherInfoViaAPIResponse(Location expected_location)
	{
		JSONObject json = null;
		try {
			
			json =  Response.getResponseBodyViaAPIRequest("https://openweathermap.org/data/2.5/onecall",expected_location.coor_lat,expected_location.coor_lon);
			
			JSONObject json_curweathr = Response.getResponseCurrentWeather(json);
			
			JSONArray expect_dailyjson = Response.getResponseDailyForcast(json);
			
			expected_location.getLocationInfoViaAPIResps(json);
			
			expected_location.weather.getCurrentWeatherInfo(json_curweathr);
						
			expected_location.dailyForcast.getResponseDailyForcast(expect_dailyjson, expected_location.timezone);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return expected_location;
	}
	
	@Step("User Can View The Current Weather with correct Info")
	public void Validate_CurrentWeather(Location expected_location)
	{
		searchweather_page.Validate_CurrentWeather(expected_location);
		
	}
	
	@Step("User Can View The DailyForCastList with brief correct info")
	
	public void Validate_DailyForcastList(int expectdays, Location search_location)
	{
		
		searchweather_page.Validate_DailyForcastList(expectdays, search_location);
		
	}
	
	@Step("User Can View The DailyForCast Detals of All days list")
	public void Validate_DailyForcastDetail_AllDay(Location search_location)
	{
		searchweather_page.Validate_DailyForcastDetail_AllDay(search_location);
		
	}
	
	
	@Step("User Can View The DailyForCast Detals with correct info")
	public void Validate_DailyForcastDetail_SingleDay(int day_index, Weather daily_weather)
	{
		searchweather_page.Validate_DailyForcastDetail_SingleDay(day_index, daily_weather);
	
		
	}
		
	//END  MANAGE====================================
}
