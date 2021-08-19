package configuration.DriverConfig;


import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ThreadGuard;

import configuration.TestConfigs;
import custom_Func.Custom_Func;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Web_Driver extends DriverBase{
	
	
	
	public Web_Driver(DesiredCapabilities capacities) {
		Capacities = capacities;
		
	}//init
	
	
	@Override
	public void CreateDriver() throws Exception{
		System.out.println("All capacibilities:"+Capacities.toString());
		
		System.out.println("START TO CREATE WEB DRIVER");
		
		String drivername = "";
			//If run on local machine
			if (TestConfigs.isRemote==false)
			{
				
				String browserName = "";
				String OS_str = Custom_Func.GetOS_func();
				//==============Set driver location as System.Property
				switch(browserName)
				{
				case "FireFox":
					WebDriverManager.firefoxdriver().setup();
					drivername = "webdriver.gecko.driver";
					
					/*
					if(OS_str.contains("mac"))
					{
						System.setProperty(drivername, TestConfigs.WebDriver_folder+TestConfigs.Mac_FFDriver);
					}
					else
						System.setProperty(drivername, TestConfigs.WebDriver_folder+TestConfigs.Win_FFDriver);
					*/					
					FirefoxOptions ff_options = new FirefoxOptions();
					ff_options.merge(Capacities);
					driver = ThreadGuard.protect(new FirefoxDriver(ff_options));
					
					break;
					
				case "MsEdge":
					WebDriverManager.edgedriver().setup();
					EdgeOptions edge_options = new EdgeOptions();
					edge_options.merge(Capacities);
					driver = ThreadGuard.protect(new EdgeDriver(edge_options));
					break;
					
				default:
					WebDriverManager.chromedriver().setup();
					drivername = "webdriver.chrome.driver";
					
					/*
					if(OS_str.contains("mac"))
					{
						System.setProperty(drivername, TestConfigs.WebDriver_folder+TestConfigs.Mac_ChromeDriver);
					}
					else
						System.setProperty(drivername, TestConfigs.WebDriver_folder+TestConfigs.Win_ChromeDriver);
					*/
					ChromeOptions chrome_options = new ChromeOptions();
					chrome_options.merge(Capacities);
					driver = ThreadGuard.protect(new ChromeDriver(chrome_options));
					break;
					
				}
				
				
				driver.manage().window().maximize();
							
						
			}//end if of Local Run
			//-------Setup for Remote on Cloud or SEleniuGrid
			else
				
				if(TestConfigs.RemoteMode.equals("Cloud"))
				{
					String remote_access_str = "https://"+TestConfigs.RemoteUserName+":"+TestConfigs.RemoteAccessKey+"@"+TestConfigs.RemoteUrl;
					
					try {
						driver =  new RemoteWebDriver(new URL(remote_access_str), Capacities);
						System.out.println("SUCCESSFULL Remote CONNECTED");
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
										
						
				}//else if #1
				
				else if(TestConfigs.RemoteMode.contains("SeleniumGrid"))
				{
					try {
						driver =  new RemoteWebDriver(new URL(TestConfigs.RemoteUrl), Capacities);
						System.out.println("SUCCESSFULL Remote CONNECTED");
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}//else if #2
			
			System.out.println("=======SUCCESS CREATED WEB DRIVER");
				
	}
	
	
	
	

	

	
	
}
