
package baseClasses;

import java.io.File;


import org.jdom2.Element;

import configuration.TestConfigs;
import custom_Func.FileManage;



public class glb_RefData {
	
	public  static String env_url ;

	
	private String file_direction = TestConfigs.resources_path+ "configuration"+File.separator+"Execution_config"+File.separator; 
	
	private String file_name;
	
	
	public glb_RefData() {
		
		switch (TestConfigs.EnvName)
		{
		case "QA":
			file_name = "QA_TestData.xml";
			break;
			
		}
			
		Element rootElement = new FileManage().Parse_XMLFile(file_direction+file_name);
			
		//Get URLs of the portals
		env_url = rootElement.getChildText("Env");
	
		System.out.println(env_url);
			
	}
}
