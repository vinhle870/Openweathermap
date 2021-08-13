package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import baseClasses.PageObjects;
import configuration.TestConfigs;
import configuration.DriverConfig.DriverBase;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.Step;
import objects.Weather;
import objects.Location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class openweathermap_page extends PageObjects{
	
	public openweathermap_page(DriverBase driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private  String curWeather_css = "div[class='current-container mobile-padding']";
	
	private  String dailyForcast_css = "div[class='daily-container block mobile-padding']";
	
	private String dailydtail_css = ".daily-detail-container";
	
	
	private String searh_txt_css = ".search-container >input";
	
	private String searh_btn_css = ".search >button";
	
	private String suggess_location_list_css = ".search-container >ul";
	
	
	public  WebElement curWeather() {
		
		return GetElement(curWeather_css);
		 
		 }
	
	public  WebElement dailyForcast() {
			
		return GetElement(dailyForcast_css);
	}
	
	public  WebElement dailydtail() {
		
		return GetElement(dailydtail_css);
	}
	
	public  WebElement searh_txt() {
			
			return GetElement(searh_txt_css);
		}
	
	public  WebElement searh_btn() {
		
		return GetElement(searh_btn_css);
	}
	
	public  WebElement suggess_location_ddl() {
			
			return GetElement(suggess_location_list_css);
	}
	

	
	//============================================================METHODS
	public void searchWeatherbyCityName(Location location_arg)
	{
		ClearThenEnterValueToField(searh_txt(), location_arg.location_value);
		
		Optimize_ElementClick(searh_btn());
		
		List<WebElement> options = getSuggestLocationList();
		
		for(Iterator<WebElement> cur_iter = options.iterator(); cur_iter.hasNext();)
		{
			WebElement element = cur_iter.next();
			
			String textContent = element.getAttribute("textContent");
			if(textContent.contains(location_arg.coor_lat+", "+location_arg.coor_lon))
			{
				element.click();
				
				return;
			}
					
		}
		
		
	}
	
	public List<WebElement> getSuggestLocationList()
	{
		return suggess_location_ddl().findElements(By.tagName("li"));
	}
	
	public List<String> getDailyItemsText()
	{
		 return dailyForcast().findElements(By.tagName("li")).stream()
	                .map( element -> element.getAttribute("textContent") )
	                .collect(Collectors.toList());
	}
	
	public String getDailyItemDetail(String date)
	{
		List<WebElement> E_list = dailyForcast().findElements(By.tagName("li"));
		
		for (Iterator<WebElement> iter = E_list.iterator(); iter.hasNext();) {
			WebElement element = iter.next();
			if(element.getText().matches(date))
			{
				element.findElement(By.cssSelector(".icon-down")).click();
				
				Wait_For_ElementDisplay(dailydtail_css);
				
				String text = dailydtail().getAttribute("textContent");
				
				System.out.println(text);
				
				return text;
				
			}
		   
		}
		return "";
	}
	
	public String getDailyItemDetail(int order)
	{
		//This is arrow element
		List<WebElement> E_list = dailyForcast().findElements(By.cssSelector(".chevron-container"));
		
		WebElement element = E_list.get(order);
		
		Optimize_ElementClick(element);
		
		Wait_For_ElementDisplay(dailydtail_css);
		
		String text = dailydtail().getAttribute("textContent");
		
		System.out.println(text);
		
		//Close the daily forcast detail section
		Optimize_ElementClick(element);
		
		return text;
			
	}
	
	public String getCurrentWeatherDetail()
	{
		String text = curWeather().getAttribute("textContent");
		
		System.out.println(text);
		
		return text;
	}
	
	public int getTotalForcastDaysOnSection() {
		List<WebElement> E_list = dailyForcast().findElements(By.tagName("li"));
		
		return E_list.size();
	}
	
	
	public void Validate_CurrentWeather(Location expected_location)
	{
		String observed_str = getCurrentWeatherDetail();
		String expect_str ="";
		
		Weather expected_weather = expected_location.weather;
		
		//Verify Location returned
		if(!observed_str.contains(expected_location.currenttime))
		{
			TestConfigs.glb_TCStatus=false;
			TestConfigs.glb_TCFailedMessage+="Currently Weather - Incorrect Current datetime[Observed: "+observed_str+"- Expected: "+expected_location.currenttime+"].\n";
			
		}
				
		if(!observed_str.contains(expected_location.location_value))
		{
			TestConfigs.glb_TCStatus=false;
			TestConfigs.glb_TCFailedMessage+="Currently Weather - Incorrect location_value[Observed: "+observed_str+"- Expected: "+expected_location.location_value+"].\n";
			
		}
		//Verify Temp with correct unit
		expect_str =  String.format("%.0f", expected_weather.temp)+"°"+expected_weather.temp_unit;
		if(!observed_str.contains(expect_str))
		{
			TestConfigs.glb_TCStatus=false;
			TestConfigs.glb_TCFailedMessage+="Currently Weather - Incorrect temp[Observed: "+observed_str+"- Expected: "+expected_location.weather.temp+"].\n";
			
		}
		
		//Verify Feels_Like with correct unit
		expect_str = "Feels like "+String.format("%,.0f", expected_weather.feels_like)+"°"+expected_weather.temp_unit;
		if(!observed_str.contains(expect_str))
		{
			TestConfigs.glb_TCStatus=false;
			TestConfigs.glb_TCFailedMessage+="Currently Weather - Incorrect feels_like[Observed: "+observed_str+"- Expected: "+expected_location.weather.feels_like+"].\n";
			
		}
		
		//Verify Weather_descr with correct unit
		if(!observed_str.contains(expected_location.weather.weather_descr))
		{
			TestConfigs.glb_TCStatus=false;
			TestConfigs.glb_TCFailedMessage+="Currently Weather - Incorrect weather_descr[Observed: "+observed_str+"- Expected: "+expected_location.weather.weather_descr+"].\n";
			
		}
		
			
		//Verify Wind Speed
		expect_str = String.format("%.1f", expected_weather.wind_speed) + expected_weather.windspeed_unit;
		if(!observed_str.contains(expect_str))
		{
			TestConfigs.glb_TCStatus=false;
			TestConfigs.glb_TCFailedMessage+="Currently Weather - Incorrect Winspeed[Observed: "+observed_str+"- Expected: "+expected_location.weather.wind_speed+"].\n";
		}
		
		//Verify pressure with correct unit
		expect_str = String.format("%.0f", expected_weather.pressure)+"hPa";
		if(!observed_str.contains(expect_str))
		
		{
			TestConfigs.glb_TCStatus=false;
			TestConfigs.glb_TCFailedMessage+="Currently Weather - Incorrect Pressure[Observed: "+observed_str+"- Expected: "+expected_location.weather.pressure+"].\n";
		}
		
		//Verify humidity with correct unit
		expect_str = "Humidity:"+ NumberFormat.getPercentInstance(Locale.ENGLISH).format(expected_weather.humidity);
		if(!observed_str.contains(expect_str))
		
		{
			TestConfigs.glb_TCStatus=false;
			TestConfigs.glb_TCFailedMessage+="Currently Weather - Incorrect humidity[Observed: "+observed_str+"- Expected: "+expected_location.weather.humidity+"].\n";
			
		}
		
		//Verify dew_point with correct unit
		expect_str = "Dew point:"+String.format("%,.0f", expected_weather.dew_point)+"°"+expected_weather.temp_unit;
		if(!observed_str.contains(expect_str))
		
		{
			TestConfigs.glb_TCStatus=false;
			TestConfigs.glb_TCFailedMessage+="Currently Weather - Incorrect dew_point[Observed: "+observed_str+"- Expected: "+expected_location.weather.dew_point+"].\n";
		}
		
		//Verify visibility with correct unit
		expect_str = "Visibility:"+ expected_location.weather.visibility +"km";
		if(!observed_str.contains(expect_str))
		{
			TestConfigs.glb_TCStatus=false;
			TestConfigs.glb_TCFailedMessage+="Currently Weather - Incorrect visibility[Observed: "+observed_str+"- Expected: "+expected_location.weather.visibility+"].\n";
		}
		
		
		
	}//end method
	
	
	
	public void Validate_DailyForcastList(int expectdays, Location search_location)
	{
		List<String> observed_str = getDailyItemsText();
		
		System.out.println(observed_str.toString());
		
		if(observed_str.size()!=expectdays)
		{
			TestConfigs.glb_TCStatus=false;
			TestConfigs.glb_TCFailedMessage+="Daily Forcast List must contain "+expectdays+"instead of "+observed_str.size()+"].\n";
		
		}
		
		Set<Object> keyset= search_location.dailyForcast.day_weatherlist.keySet();
		
		for(int i = 0;i<search_location.dailyForcast.day_weatherlist.size();i++)
		{
			String key = keyset.toArray()[i].toString();
			
			Weather cur_weather = (Weather) search_location.dailyForcast.day_weatherlist.get(key);
			
			String min_max_temp = cur_weather.dailytemps.get("max").toString().replace("°"+cur_weather.temp_unit, "") +" / "+ cur_weather.dailytemps.get("min");
			
			String expect_str = key+min_max_temp+cur_weather.weather_descr.toLowerCase();
			
			if(!observed_str.contains(expect_str))
			{
				TestConfigs.glb_TCStatus=false;
				
				TestConfigs.glb_TCFailedMessage+="Dayly Forcast - incorrect Daily row text[Observed:"+observed_str+"Expected infos: "+expect_str+"].\n";
			
			}//end if
		}//end for
		
		
		
	}
	
	
	public void Validate_DailyForcastDetail_AllDay(Location search_location)
	{
		List<WebElement> E_list = dailyForcast().findElements(By.tagName("li"));
		
		Set<Object> keyset= search_location.dailyForcast.day_weatherlist.keySet();
		
		
		for(int i = 0;i<keyset.size();i++)
		{
			String key = keyset.toArray()[i].toString();
			
			boolean isfound = false;
			
			for(int ui_cur=0;ui_cur<E_list.size();ui_cur++)
			{
				if(E_list.get(ui_cur).getAttribute("innerText").contains(key))
				{
					Weather daily_weather = (Weather) search_location.dailyForcast.day_weatherlist.get(key);
					
					Validate_DailyForcastDetail_SingleDay(ui_cur , daily_weather);	
					
					isfound = true;
					
					break;
									
				}//end if
				
			}//end for
			
			if(isfound==false)
			{
				String area = "Daily Forcast Weather";
				
				TestConfigs.glb_TCStatus=false;
				
				TestConfigs.glb_TCFailedMessage+=area+" - Can't found the Forcast on day["+key+"] on UI .\n";
				
			}//end if	
			
		}//end for
		
	}
	
	public void Validate_DailyForcastDetail_SingleDay(int ui_index, Weather expected_weather)
	{
		
		
		String expect_str = "";
		String observed_str = getDailyItemDetail(ui_index);
		
		String temp_sumr = "The high will be "+expected_weather.dailytemps.get("max")+", the low will be "+expected_weather.dailytemps.get("min")+".";
		
		String rain_str = "";
		if(expected_weather.rain==null)
		{
			rain_str = NumberFormat.getPercentInstance(Locale.ENGLISH).format(expected_weather.pop).toString();
			
		}
		else
		{
			rain_str = expected_weather.rain +"mm" + "("+NumberFormat.getPercentInstance(Locale.ENGLISH).format(expected_weather.pop).toString()+")";
			
		}
		
		
		String temp_list = "Temperature "+expected_weather.dailytemps.get("morn")+"  "+expected_weather.dailytemps.get("day")+"  "+expected_weather.dailytemps.get("eve")+"  "+expected_weather.dailytemps.get("night");
		
		String feel_like_list = "Feels like "+expected_weather.dailyfeel_like.get("morn")+"  "+expected_weather.dailyfeel_like.get("day")+"  "+expected_weather.dailyfeel_like.get("eve")+"  "+expected_weather.dailyfeel_like.get("night");
		
		
		//Verify Weather_descr with correct unit
		expect_str = expected_weather.weather_descr;
		int user_ui_index = ui_index+1;
		String area = "DailyForcast Weather Row ["+user_ui_index+"]";
		
		if(!observed_str.contains(expect_str))
		{
			TestConfigs.glb_TCStatus=false;
			
			TestConfigs.glb_TCFailedMessage+=area+" - Incorrect weather_descr[Observed: "+observed_str+"- Expected: "+expected_weather.weather_descr+"].\n";
			
		}
		
		//Verify temp_summary with correct unit
		expect_str = temp_sumr;
		if(!observed_str.contains(expect_str))
		{
			TestConfigs.glb_TCStatus=false;
			
			TestConfigs.glb_TCFailedMessage+=area+" - Incorrect temp_sumr[Observed: "+observed_str+"- Expected: "+temp_sumr+"].\n";
			
		}
		
		//Verify Rain Info
		expect_str = rain_str;
		
		if(!observed_str.contains(expect_str))
		{
			TestConfigs.glb_TCStatus=false;
			
			TestConfigs.glb_TCFailedMessage+=area+" - Incorrect Rain info[Observed: "+observed_str+"- Expected: "+rain_str+"].\n";
			
		}
		
		//Verify Wind Speed
		expect_str = String.format("%.1f", expected_weather.wind_speed) + expected_weather.windspeed_unit;
		
		if(!observed_str.contains(expect_str))
		{
			TestConfigs.glb_TCStatus=false;
			
			TestConfigs.glb_TCFailedMessage+=area+" - Incorrect Winspeed[Observed: "+observed_str+"- Expected: "+expect_str+"].\n";
		}
		
		//Verify pressure with correct unit
		expect_str = String.format("%.0f", expected_weather.pressure)+"hPa";
		
		if(!observed_str.contains(expect_str))
		{
			TestConfigs.glb_TCStatus=false;
			
			TestConfigs.glb_TCFailedMessage+=area+" - Incorrect Pressure[Observed: "+observed_str+"- Expected: "+expect_str+"].\n";
		}
		
		//Verify humidity with correct unit
		expect_str = "Humidity:"+ NumberFormat.getPercentInstance(Locale.ENGLISH).format(expected_weather.humidity);
				
		if(!observed_str.contains(expect_str))
		{
			TestConfigs.glb_TCStatus=false;
			
			TestConfigs.glb_TCFailedMessage+=area+" - Incorrect humidity[Observed: "+observed_str+"- Expected: "+expect_str+"].\n";
			
		}
		
		//Verify dew_point with correct unit
		expect_str = "Dew point:"+String.format("%,.0f", expected_weather.dew_point)+"°"+expected_weather.temp_unit;
	
		if(!observed_str.contains(expect_str))
		{
			TestConfigs.glb_TCStatus=false;
			
			TestConfigs.glb_TCFailedMessage+=area+" - Incorrect dew_point[Observed: "+observed_str+"- Expected: "+expect_str+"].\n";
		
		}
		
		//Verify InDay's Temperature  with correct unit for testing
		expect_str = temp_list;
	
		if(!observed_str.contains(expect_str))
		{
			TestConfigs.glb_TCStatus=false;
			
			TestConfigs.glb_TCFailedMessage+=area+" - Incorrect InDay's Temperature [Observed: "+observed_str+"- Expected: "+expect_str+"].\n";
		}
		
		//Verify InDay's Temperature  with correct unit
		expect_str = feel_like_list;
		
		if(!observed_str.contains(expect_str))
		{
			TestConfigs.glb_TCStatus=false;
			
			TestConfigs.glb_TCFailedMessage+=area+" - Incorrect InDay's Feels Like [Observed: "+observed_str+"- Expected: "+expect_str+"].\n";
		}
		
		
	}


}
