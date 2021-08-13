package configuration;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CapacitiesFactory {
	
	
	private String file_direction = TestConfigs.resources_path+"configuration"+File.separator+ "Env_Capacities"+File.separator;
	
	private String file_name;
	
	//data_node containt the capacities list get from XML file
	
	

	//private  boolean headless;
	//===========Capacities
	
	public CapacitiesFactory()
	{
		
		file_name = "WebLocal_Caps.json";
		
		if(TestConfigs.isRemote==true)
		{
			file_name = "WebRemote_Caps.json";
		}
			
		
		
	}
	
	private FirefoxOptions Set_FFOptions()
	{
		FirefoxOptions options = new FirefoxOptions();
				
		FirefoxProfile profile = new FirefoxProfile();
		
		String destination_folder = TestConfigs.Download_foldername;
		
		 //Set Location to store files after downloading.
		 profile.setPreference("browser.download.dir", destination_folder);
		 
		 profile.setPreference("browser.download.folderList", 2);
		 
		 //Set Preference to not show file download confirmation dialogue using MIME types Of different file extension types.
		 profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/vnd.openxmlformats-officedocument.wordprocessingml.document, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/pdf, text/csv, text/plain"); 
		
		 profile.setPreference( "browser.download.manager.showWhenStarting", false );
		 
		 //profile.setPreference( "-headless", true );
		 
		// profile.setPreference( "-disable-gpu", true );
		
		 profile.setPreference( "pdfjs.disabled", true );
		
		 options.setProfile(profile);
		 //options.setHeadless(glb_silentMode);
		 return options;
	}//end void
	
	
	private ChromeOptions Set_ChromeOptions()
	{
		ChromeOptions options = new ChromeOptions();
	
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", TestConfigs.Download_foldername);
			
		
		//chromePrefs.put("-headless",1);
		chromePrefs.put("-disable-gpu",1);
		
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("start-maximized");
		
		options.addArguments("--proxy-server='direct://'");
		options.addArguments("--proxy-bypass-list=*");
		
		return options;
	}//end void
	
	//******Currently this function is not used due to FF Profile is confg on TestConfigs
	/*
	public  void ChangeFFDownloadSetting_func()
	{
		if(glb_webdriver!=null)
		{
		glb_webdriver.get("about:config");

		((JavascriptExecutor) glb_webdriver).executeScript("document.getElementsByTagName('button')[0].click();");
		
		((JavascriptExecutor) glb_webdriver).executeScript("var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);prefs.setBoolPref('browser.download.useDownloadDir', false);");
		}
		else
		{
			glb_webdriver.navigate().to(TestConfigs.ARCPortal_env);
		}

	}
	
	*/
	
	
	  private static JSONArray parseJSON(String jsonLocation) throws Exception {
	        JSONParser jsonParser = new JSONParser();
	        return (JSONArray) jsonParser.parse(new FileReader(jsonLocation));
	    }

	    public ArrayList<Object> getCapacitiesSet() throws Exception {
	    String jsonLocation = file_direction+file_name;
	    ArrayList<Object> capacitiesSets = new ArrayList<>();  
    	
    	JSONArray capabilitiesArray = parseJSON(jsonLocation);
	        
    	for (Object jsonObj : capabilitiesArray) {
            JSONObject capability = (JSONObject) jsonObj;
            capacitiesSets.add(capability.get("caps"));
            
            }
        return capacitiesSets;
	    }

	    private static HashMap<String, Object> convertCapsToHashMap(Object jsonobject) throws Exception {
	        ObjectMapper objectMapper = new ObjectMapper();
	        return objectMapper.readValue(jsonobject.toString(), HashMap.class);
	    }

	    public  DesiredCapabilities getDesiredCapabilities(Object jsonobject) throws Exception {
	        
	        HashMap<String, Object> caps = convertCapsToHashMap(jsonobject);
	        
	        DesiredCapabilities caps_var = new DesiredCapabilities(caps);
	        
	        LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
           
	    
	       // String platform = "";
			
	        String browser_name = "";
	      	if(caps_var.getCapability("platformName")==null)
			{
				browser_name = caps_var.getCapability("browserName").toString();
				
				//Setup the some extra browser config: like default download folder for Desktop OS
				
				 switch(browser_name)
				   	{
				   	case "FireFox":
				   		FirefoxOptions ffoptions = Set_FFOptions();
				   		
				   		ffoptions.setCapability("goog:loggingPrefs", logPrefs);
				   		
				   		caps_var.merge(ffoptions);
				   		break;
				   		   	   		
					case "Chrome":
						ChromeOptions chromeoptions = Set_ChromeOptions();
				   		
						chromeoptions.setCapability("goog:loggingPrefs", logPrefs);
						
						caps_var.setCapability(ChromeOptions.CAPABILITY,chromeoptions);
						
						
				   		break;
				   	}//end switch 
						 
				}//if platform
				
	    System.out.println(caps_var.toString());
		return caps_var;
	        
	        
	       
	    }

}
