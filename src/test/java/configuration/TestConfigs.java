package configuration;

import java.io.File;

public class TestConfigs {
	
	

	//private  String file_path = System.getProperty("user.dir")+config_container;
	public static boolean glb_TCStatus;
	public static String  glb_TCFailedMessage;
		
	public static boolean isRemote;
	public static String RemoteMode;
	public static String RemoteUrl;
	public static String RemoteUserName;
	public static String RemoteAccessKey;
			
	public static String EnvName;

	public static boolean glb_silentMode;
	public static int glb_WaitTime;
	public static boolean glb_CloseBrowser;
	//Mail Account for testing
	
	public static String RunCloud_status;
		
	//File Folders
	public static String local_home = System.getProperty("user.home");
	
	public static String resources_path = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator;
	
	private String Exec_Config_file = "Execution_config"+File.separator+"Exec_Config.properties";
		
	public  static String test_data_folder = "TestDocument";
	
	public static  String Download_foldername = "Downloads";
	
	public  static String test_data_full_path = resources_path+ test_data_folder+File.separator;

	public  static String Download_full_folder_path = local_home+File.separator+Download_foldername;
	
	public static String WebDriver_folder = resources_path+"BrowserDrivers"+File.separator;
	
	
	public static String LocalAppiumURL;
	public static String Win_ChromeDriver;
	public static String Mac_ChromeDriver;
	public static String Win_FFDriver;
	public static String Mac_FFDriver;
	
	public TestConfigs() {
				
		
		glb_TCStatus = true;
		glb_TCFailedMessage = "";
		RunCloud_status = "passed";
		 
		//1. ===================Read Env Properties Files
		ProptiesFile_manage Env_property = new ProptiesFile_manage();
		
		Env_property.LoadPropertyFile(Exec_Config_file);
		
		System.out.println("Env Properties File:"+Env_property.property_var.toString());
		
		//Check what env is selected
		EnvName = Env_property.property_var.getProperty("EnvName");
			
		//2.===================Read Browsers Properties Files
		isRemote = Boolean.parseBoolean(Env_property.property_var.getProperty("isRemote"));
		RemoteMode = Env_property.property_var.getProperty("RemoteMode");
		
		RemoteUrl = Env_property.property_var.getProperty("RemoteUrl");
		
		RemoteUserName = Env_property.property_var.getProperty("RemoteUserName");
		
		RemoteAccessKey = Env_property.property_var.getProperty("RemoteAccessKey");
		
		LocalAppiumURL = Env_property.property_var.getProperty("LocalAppiumURL");
		
		
		
		Win_ChromeDriver = Env_property.property_var.getProperty("Win_ChromeDriver");
		
		Mac_ChromeDriver = Env_property.property_var.getProperty("Mac_ChromeDriver");
		
		Win_FFDriver = Env_property.property_var.getProperty("Win_FFDriver");
		
		Mac_FFDriver = Env_property.property_var.getProperty("Mac_FFDriver");
		
		glb_WaitTime = Integer.parseInt(Env_property.property_var.getProperty("glb_WaitTime"));
		
		glb_CloseBrowser = Boolean.parseBoolean(Env_property.property_var.getProperty("glb_CloseBrowser"));
		
			
	}//init		

	
	
	
	

	

	
	
}
