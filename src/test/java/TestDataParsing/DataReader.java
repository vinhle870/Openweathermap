package TestDataParsing;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import configuration.TestConfigs;
import custom_Func.FileManage;
import objects.Location;

public class DataReader {
	
	
	public static List<Location> RetrieveLocationFromFile(String file_name)
	{
		file_name = "data.json";
		
		JSONObject json_obj = null;
		
		String path = TestConfigs.resources_path+"TestData"+File.separator;
		try {
			 json_obj= FileManage.ReadJsonFile(path+file_name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Location> Location_lst  = new ArrayList<>();
		
		JSONArray json_array = (JSONArray) json_obj.get("Locations");
		
		for(Iterator<JSONObject> cur_iter = json_array.iterator();cur_iter.hasNext();)
		{
			JSONObject element = cur_iter.next();
			
			String value = (String)element.get("value");
			
			JSONObject coord = (JSONObject)element.get("coordinates");
			
			Double coor_lat = (Double)coord.get("lat");
			
			Double coor_lon = (Double)coord.get("lon");
			
			Location tmp_loc = new Location();
			
			tmp_loc.location_value = value;
			
			tmp_loc.coor_lat = coor_lat.toString();
			
			tmp_loc.coor_lon = coor_lon.toString();
			
			Location_lst.add(tmp_loc);
			
		}
		
		System.out.println("Test Data: "+Location_lst.toString());
		
		return Location_lst;
	}

}



