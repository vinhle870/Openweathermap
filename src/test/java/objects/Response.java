package objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Response {
	
	public JSONObject root_object;
	
	public static JSONObject getResponseBodyViaAPIRequest(String requesturl,String lat,String lon) throws ClientProtocolException, IOException, ParseException, URISyntaxException{
		 
		//requesturl ="https://openweathermap.org/data/2.5/onecall";
				
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(requesturl);
		URI uri = new URIBuilder(request.getURI())
			      .addParameter("lat", lat)
			      .addParameter("lon", lon)
			      .addParameter("units", "metric")
			      .addParameter("appid", "439d4b804bc8187953eb36d2a8c26a02")
			      .build();
		
		((HttpRequestBase) request).setURI(uri);
		
		HttpResponse response =  client.execute(request);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		StringBuffer buffer = new StringBuffer();
		while((line=reader.readLine())!=null)
		{
			buffer.append(line);
			
		}
		System.out.println(buffer);
		
		JSONParser parser = new JSONParser();
    	
       	JSONObject json = null;
		
		json = (JSONObject) parser.parse(buffer.toString());
		
		
		System.out.println(json.toString());
		
		return json;
		
		}
	
	public static JSONObject getResponseCurrentWeather(JSONObject rootobject)
	{
		  JSONObject current_weather = (JSONObject)rootobject.get("current");
	       
	      System.out.println("\nCurrent Weather Data: " + current_weather);
	      
		return  current_weather;
	}
	
	public static JSONArray getResponseHourlyForcast(JSONObject rootobject)
	{
	   JSONArray hourly_forcast = (JSONArray)rootobject.get("hourly");
       
       System.out.println("\nHourly Forcast Weather Data: " + hourly_forcast);
	      
		return  hourly_forcast;
	}
		
	public static JSONArray getResponseDailyForcast(JSONObject rootobject)
	{
	   JSONArray hourly_forcast = (JSONArray)rootobject.get("daily");
       
       System.out.println("\nHourly Forcast Weather Data: " + hourly_forcast);
	      
		return  hourly_forcast;
	}
	


}
