package custom_Func;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


import java.text.DateFormat;

/*
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
*/
public class DateTime_Manage {
	public static Date ServerDate;

	public DateTime_Manage() {
		// TODO Auto-generated constructor stub
		ServerDate = null;
	}
	
	public static Date GetLocalDatetime() {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

		LocalDateTime now = LocalDateTime.now();
		String output = now.format(dtf);

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		Date d_temp=null;
		try {
			d_temp = formatter.parse(output);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return d_temp;

	}



	public static String getCurrentLocalTime() {

		///DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		String return_str = Integer.toString(now.getHour()) + Integer.toString(now.getMinute()) + Integer.toString(now.getSecond());
		//String return_str = 
		return return_str;
	}
	
	public static String getCurrentLocalTime_HHMM() {

		//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		
		LocalDateTime now = LocalDateTime.now();
		
		String hr_tmp = String.format("%02d", now.getHour());
		String min_tmp  = String.format("%02d", now.getMinute());
		
		String return_str = hr_tmp +":"+ min_tmp;
		
		//String return_str = Integer.toString(now.getHour())+":"+  Integer.toString(now.getMinute());
		System.out.println("Time Format:" + return_str);
		
		return return_str;
	}

	public static Date GetCurrentLocalDate_Date() {
		//timedatectl set-time '2018-05-25 11:10:40'
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDateTime now = LocalDateTime.now();
		String output = dtf.format(now);
		Date d_temp=null;
		try {
			d_temp = ConvertStringtoDate(output);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d_temp;
	}

	public static String GetCurrentLocalDate_Str() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDateTime now = LocalDateTime.now();
		String output = dtf.format(now);
		return output;
	}

	
	public static Date IncreaseDay(Date orgindate, int add_day) {

		Date temp_date = orgindate;
		temp_date.setDate(temp_date.getDate()+add_day);
		System.out.println(temp_date);
		return temp_date;
	}

	public static Date Get_DateTime_WithTimeZone(String timezone)
	{
	//Descrition: this also include the saved time
		
		Date date_tmp = GetLocalDatetime();
		try {
				
				System.out.println(date_tmp);
				//2018-06-18 8:00:00
		
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
				
				formatter.setTimeZone(TimeZone.getTimeZone(timezone));
				
				String strDate = formatter.format(date_tmp);
				
				System.out.println("UTC date 12format: "+ strDate);
				
			
				date_tmp = formatter.parse(strDate);
				
				//format_string = "yyyy-MM-dd HH:mm:ss"
				
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return date_tmp;
	}

	 
	public static String FormatDateTime_WithTimeZone(String timezone, Date local_date,String format_str)  {
		
		
		String strDate="";
		try {
			if(format_str.equals(""))
				format_str = "MM/dd/yyyy HH:mm";
			

			Date temp_date = local_date;

			System.out.println(temp_date);
			//2018-06-18 8:00:00

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
			
			formatter.setTimeZone(TimeZone.getTimeZone(timezone));
			
			strDate = formatter.format(temp_date);
			
			System.out.println("UTC date 12format: "+ strDate);
			
			temp_date = formatter.parse(strDate);
			//format_string = "yyyy-MM-dd HH:mm:ss"
			
			formatter = new SimpleDateFormat(format_str);
			
			//Default = "UTC"
			if(!timezone.equals(""))
				formatter.setTimeZone(TimeZone.getTimeZone(timezone));
			else
				formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			strDate = formatter.format(temp_date);
			
			System.out.println(timezone+" date 24format: "+ strDate);
			
			
		}catch(Exception e)
		{
			System.out.println(e);
		}
		return strDate;
		
	}


	public static String Format_DateTime_To_String(Date temp_date) {

		String strDate="";
			
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		
		strDate = formatter.format(temp_date);
		
		System.out.println(strDate);
		
		return strDate;
	}


	public static String ConvertDatetoString(Date temp_date){

		//SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String strDate = formatter.format(temp_date);
		System.out.println(strDate);
		return strDate;
	}

	public static Date ConvertStringtoDate(String temp_date) throws ParseException{

		//SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		Date strDate = formatter.parse(temp_date);
		return strDate;
	}
	
	public static String Calculate_Days_Numb(String temp_date) throws ParseException
	{
		
		Calendar c1 = Calendar.getInstance();
		
		Calendar c2 = Calendar.getInstance();
		
		Date today = DateTime_Manage.GetCurrentLocalDate_Date();
		
		Date date = DateTime_Manage.ConvertStringtoDate(temp_date);
		
		c1.setTime(today);
		
		c2.setTime(date);
		
		long noDay = (c1.getTime().getTime() - c2.getTime().getTime()) / (24 * 3600 * 1000);
		
		String str = String.valueOf(noDay);
		
		return str;
		
	}
	
	public static Date ParseLongToDateTime(Long numb)
	{
		//long value = 1628737906;
		
		//SimpleDateFormat originalFormat = new SimpleDateFormat("E, MMM dd,hh:mm a");
		
		Date date = new Date(numb * 1000);
		
		//String formatedDate = originalFormat.format(date);
		
		return date;
	}
	

}
