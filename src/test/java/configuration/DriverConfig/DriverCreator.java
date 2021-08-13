package configuration.DriverConfig;


import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverCreator{
	
public static DriverBase GetDriverBase(DesiredCapabilities capacities)
{
	if(capacities.getCapability("platformName")!=null)
	{
		return new Mobile_Driver(capacities);
	}
	else
		return new  Web_Driver(capacities);

}
		
	

}
