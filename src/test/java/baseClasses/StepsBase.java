/**
 * 
 */
package baseClasses;

import configuration.DriverConfig.DriverBase;

/**
 * @author Admin
 *
 */
public abstract class StepsBase {
		
	public DriverBase driver;
	
	public void GetConfig(DriverBase driver)
	{
		this.driver = driver;
		
		InitStepsDefinition();
	}
	
	public abstract void InitStepsDefinition();

}
