package configuration.DriverConfig;




import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import org.openqa.selenium.support.ThreadGuard;

import configuration.TestConfigs;
import io.appium.java_client.AppiumDriver;


public class Mobile_Driver extends DriverBase{
	
	public Mobile_Driver(DesiredCapabilities capacities)
	{
		Capacities = capacities;
	}
	
	@Override
	public void CreateDriver() throws MalformedURLException
	{
		
		System.out.println("All capacibilities:"+Capacities.toString());
		
		System.out.println("START TO CREATE APPIUM DRIVER");
	
		
			//If run on local machine
			if (TestConfigs.isRemote==false)
			{
				driver = new AppiumDriver<>(new URL(TestConfigs.LocalAppiumURL),Capacities);
					
				System.out.println("SUCCESSFULL APPIUM SERVER CONNECTION");
				
			}//end if of Local Run
			//-------Setup for Remote on Cloud or SEleniuGrid
			else
			{
				if(TestConfigs.RemoteMode.equals("Cloud"))
				{
					String remote_access_str = "https://"+TestConfigs.RemoteUserName+":"+TestConfigs.RemoteAccessKey+"@"+TestConfigs.RemoteUrl;
					
					this.driver =  ThreadGuard.protect(new AppiumDriver<> (new URL(remote_access_str),Capacities)); 
						
						
				}//else if #1
				
				else if(TestConfigs.RemoteMode.contains("SeleniumGrid"))
				{
					this.driver =  ThreadGuard.protect(new AppiumDriver<> (new URL(TestConfigs.RemoteUrl),Capacities));
					
				}//else if #2
			}
			
			System.out.println("=======SUCCESS CREATED APPIUM DRIVER");
				
		
	
	}


	

	
	
}
