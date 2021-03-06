package testcases.SearchWeather;




import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.remote.DesiredCapabilities;

import TestDataParsing.DataReader;
import baseClasses.TestBase;

import configuration.TestConfigs;

import net.thucydides.core.annotations.Title;

import objects.Weather;
import testcases.E2E_suite;
import objects.Location;



@Category(E2E_suite.class)
public class TC_008_010 extends TestBase {
	
    //Get driver from parameterized Driver list
	public TC_008_010(DesiredCapabilities glb_capacities)
	{
		super(glb_capacities);
	}
	
	//Before run Test method
	@Before
	public void setup() 
	{
		super.setup();
		
    }
	

	@Test
	@Title("TC_008: Verify that the current weather data is returned correctly when user search for the City Name")
	 
	public void TC_008_Verify_CurrentWeatherInfo_Is_Returned_For_ValidCity() {
		
		//Test Case Summary:
		/*
		 * Title: Verify that the Current Weather forecast data is returned correctly when user
		 * search for the City Name
		 * 
		 * 1. Access to the test enviroment
		 * 
		 * 2. Search weather with the <location_value> as City Name, State Name, Country
		 * 
		 * 3. Get the response's data from the request name: "api.onecall"
		 * 
		 * 4. Compare to the response data to the UI section: + Current Weather data
		 * 
		 * Expected Result: On Daily forecast: data is displayed as same as the response data
		 * 
		 */
		
		//Test Data
		
		Location city = new Location();
		
		city  = DataReader.RetrieveLocationFromFile("data.json").get(0);
		
		city.weather = new Weather("C");
		
		//Steps:
		//1. Access to the site
		endUser.access_Site();
		
		//Search Weather by CityName
		endUser.SearchWeatherbyCityName(city);
		
		//Assume that the test application is triggering the correct API as expectation: OncCall
		city = endUser.getWeatherInfoViaAPIResponse(city);
		
		//Validate Current Weather
		endUser.Validate_CurrentWeather(city);
		
		Assert.assertTrue(TestConfigs.glb_TCFailedMessage, TestConfigs.glb_TCStatus);
	}

	@Test
	@Title("TC_010:Verify that the daily forecast data is returned correctly for the searched City Name")
	 
	public void TC_010_Verify_DailyForCast_Is_Returned_For_ValidCity() {
		
		//Test Case Summary:
		/*
		 * Title: Verify that the daily forecast data is returned correctly when user
		 * search for the City Name
		 * 
		 * 		
		 * STeps:
		 * 		1. Access to the test enviroment
		 * 	
		 * 		2. Search weather with the <location_value> as City Name, State Name, Country
		 * 		
		 * 		3. Get the response's data from the request name: "api.onecall"
		 * 			
		 * 		4. Compare to the response data to the UI section:
		 * 			+ Daily forecast
		 * 
		 *  
		 * Expected Result: On Daily forecast:  
		 * + Daily list must be 7 days
		 * + Data is displayed as same as the response data
		 */
		//===========================
		
		//Test Data
		
		Location city = new Location();
		
		city  = DataReader.RetrieveLocationFromFile("data.json").get(0);
		
		city.weather = new Weather("C");
		
				
		//Steps:
		//1. Access to the site
		endUser.access_Site();
		
		//Search Weather by CityName
		endUser.SearchWeatherbyCityName(city);
		
		//Assume that the test application is triggering the correct API as expectation: OncCall
		city = endUser.getWeatherInfoViaAPIResponse(city);
		
		endUser.Validate_DailyForcastList(7,city);
		
		endUser.Validate_DailyForcastDetail_AllDay(city);
		
		Assert.assertTrue(TestConfigs.glb_TCFailedMessage, TestConfigs.glb_TCStatus);
	}
	
}
