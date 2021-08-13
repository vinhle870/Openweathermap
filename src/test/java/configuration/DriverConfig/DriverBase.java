package configuration.DriverConfig;




import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import configuration.TestConfigs;


public abstract class DriverBase {
	
	 public WebDriver driver;
	
	DesiredCapabilities Capacities;
	
	abstract public void CreateDriver () throws Exception;
	
	public WebDriver GetDriver()
	{
		return this.driver;
	}
	
	
	public void CloseDriver() {
		
		if(TestConfigs.glb_CloseBrowser==true)
		{
			this.driver.quit();
		}
	}
	
	
	
}
