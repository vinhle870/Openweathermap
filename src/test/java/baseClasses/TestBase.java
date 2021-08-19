package baseClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.remote.DesiredCapabilities;

import configuration.CapacitiesFactory;
import configuration.DriverConfig.DriverBase;
import configuration.DriverConfig.DriverCreator;
import custom_Func.DateTime_Manage;
import io.github.bonigarcia.wdm.WebDriverManager;
import configuration.TestConfigs;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.Concurrent;
import net.thucydides.junit.annotations.TestData;
import objects.Response;
import objects.Weather;
import objects.Location;
import stepsDefiniton.User_AllSteps;


@RunWith(SerenityParameterizedRunner.class)
@Concurrent(threads = "10x")
public class TestBase {
	
	public static TestConfigs Test_Configs;
	
	public DesiredCapabilities glb_capacities;
	
	public DriverBase exec_driver;
	
	public glb_RefData Glb_Variable;
	
 
	
	@TestData
    public static Collection<Object[]> testData() throws Exception{
    	 
    	Test_Configs = new TestConfigs();
    	
    	//return data name elements
    	CapacitiesFactory capacity_config = new CapacitiesFactory();
    	
    	ArrayList<Object> cap_sets = capacity_config.getCapacitiesSet();
    	    	
       	Object caps_array[][] = new Object[cap_sets.size()][1];
       	
       	for(Object cur_set:cap_sets)
    	{
    		DesiredCapabilities capacities = capacity_config.getDesiredCapabilities(cur_set);
    		
    		System.out.println("=========SUCCESS GET CAPACITIES");
    		
    		caps_array[cap_sets.indexOf(cur_set)][0] = capacities;
    	
    	}
       	
    	return Arrays.asList(caps_array);
    }
	
    //Get driver from parameterized Driver list
	public TestBase(DesiredCapabilities glb_capacities)
	{
		this.glb_capacities = glb_capacities;
		
		System.out.println("Completed the TestData driver-parameters to main DriverBase");
	}
	
	@Steps
	public User_AllSteps endUser;

	
	@BeforeClass
	public static void beforeclass()
	{
	}
		
	
	@AfterClass
	public static void afterclass()
	{
		
	}
	
	//Before run Test method
	@Before
	public void setup() 
	{
		
		Glb_Variable = new glb_RefData();
		
		exec_driver = DriverCreator.GetDriverBase(glb_capacities);
		
		try {
			exec_driver.CreateDriver();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
		endUser.GetConfig(exec_driver);
    }
	
	@After
	public void after()
	{
		exec_driver.CloseDriver();	
	}
	
	

	
}
