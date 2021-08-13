package custom_Func;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.binary.Base64InputStream;



import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;

import configuration.TestConfigs;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
public class FileManage {
	
	//public  String file_path = workingDirectory;
	
	public String Content;
	public String File_Name;
	public FileManage() {
		// TODO Auto-generated constructor stub
		Content = "";
		File_Name = "";
	}
	
	public void ReadFileContent_func(String file_name) {
		
		String folder_str = TestConfigs.local_home+File.separator+ TestConfigs.Download_foldername+File.separator;
		
		File[] files = new File(folder_str).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 
		
		int size = files.length;
	
		System.out.println(size);
		
		for (int i = 0;i<size;i++) {
		    if (files[i].isFile()&&files[i].getName().contains(file_name)) {
		    	System.out.println(files[i].getName());
		    	file_name = files[i].getName();
		    	break;
			    }
		}//end for
		
		File_Name = file_name;
		
		try {
			
			if(file_name.contains("pdf"))
			{
				ReadPDFFile_func(File_Name);
			}
		
			else
			{
				//String folder_str = local_home+File.separator+ Download_foldername+File.separator;
				//1: Creat and connect the data stream
				System.out.println(folder_str+File_Name);
				File f = new File(folder_str+File_Name);
				
				//Grant read file permission
				if(!f.canRead())
				{
					f.setReadable(true);
				}
				
				FileReader fr = new FileReader(f);
				
				FileInputStream fis = new FileInputStream(f);

				XWPFDocument document = new XWPFDocument(fis);

				List<XWPFParagraph> paragraphs = document.getParagraphs();

				for (XWPFParagraph para : paragraphs) {
					String content_str = para.getText();
					Content+=content_str;


				}
				document.close();
				fis.close();
				fr.close();
			}
			
			System.out.println("File Content: "+Content);
		} catch (Exception e) {
			TestConfigs.glb_TCFailedMessage+=e.getMessage()+".\n";
			Assert.fail(TestConfigs.glb_TCFailedMessage+"When trying to read the file\n");
		}

	

	}
	
	public void ReadPDFFile_func(String file_name)
	{
		//1: Creat and connect the data stream
		String folder_str = TestConfigs.local_home+File.separator+ TestConfigs.Download_foldername+File.separator;
		//1: Creat and connect the data stream
		
		File f = new File(folder_str+file_name);
		
		 PDDocument document;
		try {
			document = PDDocument.load(f);
			
			 if (!document.isEncrypted()) {

	                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
	                stripper.setSortByPosition(true);

	                PDFTextStripper tStripper = new PDFTextStripper();

	                String pdfFileInText = tStripper.getText(document);
	                //System.out.println("Text:" + st);

					// split by whitespace
	                String lines[] = pdfFileInText.split("\\r?\\n");
	                
	                for (String line : lines) {
	                	
	                	Content+=line;
	                	Content+="\n";
	                   System.out.println(Content);
	                }//end for
	  
	      }//end if
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	     

}//end void
	
	
	
	public  void CreateWordFile_func(String file_name,String content_str)
	{
		try {
		//Blank Document

		String[] lines = content_str.split("\n");

		int count = lines.length;

		XWPFDocument document = new XWPFDocument();
		//Write the Document in file system
		FileOutputStream out;
		
		out = new FileOutputStream(new File(TestConfigs.test_data_full_path+file_name));
		
		XWPFParagraph paragraph = document.createParagraph();

		//create Paragraph
		for(int i = 0;i<count;i++)
		{
			XWPFRun run = paragraph.createRun();
			run.setText(lines[i]);
			run.addBreak();

		}

		document.write(out);
		//Close document
		document.close();
		out.close();
		System.out.println(file_name + " written successfully");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	 //METHOD: SAVE FILE FROM URL GET FROM ELEMENT HYPERLINK / DOWNLOAD BUTTON
	 public  void SaveFileFromURL_func(String filename, String url)
	 {
		 try {
		 File new_file = new File(TestConfigs.test_data_full_path+filename);
	 
	 URL new_url;
	
		new_url = new URL(url);
	
	 
	 System.setProperty("http.agent", "Chrome");
	 
	 HttpURLConnection http_connect = (HttpURLConnection) new_url.openConnection();
	 
	 http_connect.connect();
	 
	 InputStream input_stream = (InputStream) new_url.openStream();
	 //new_url.get
	
	 System.out.println(new_url.getContent());
	
	 Base64InputStream data_buff = new Base64InputStream(input_stream)	;
	 
	 DataOutputStream output = new DataOutputStream( new BufferedOutputStream(new FileOutputStream(new_file)));
	 
	 byte[] buffer = new byte[1024];
	 
	 int bytesRead;
	
	 while ((bytesRead = data_buff.read(buffer)) != -1) {
	
	System.out.println(bytesRead);
	
	output.write(buffer, 0, bytesRead);
	 }//end while
	
	 output.close();
	 http_connect.disconnect();
	 
	 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }//end void
	 
	 
	 /*
	 public void SavefileTxt(String filename, String url)
	 {
	 File new_file = new File(this.file_path+filename);
	 
	 URL new_url = new URL(url);
	
	 System.setProperty("http.agent", "Chrome");
	 
	 HttpURLConnection http_connect = (HttpURLConnection) new_url.openConnection();
	 
	 http_connect.connect();
	 
	 InputStream input_stream = (InputStream) new_url.openStream();
	 
	 //FileUtils.Copy(input_stream, new_file)
	
	 FileUtils.copyURLToFile(new_url, new_file);
	 
	 http_connect.disconnect();
	
	 WebDriver webDriver = TestConfigs.glb_webdriver;
	 
	 webDriver.get("about:config");
	
	 String name  = "browser.download.useDownloadDir";
	 
	 String value = "false";
	 //webDriver.execute_script()
	 
	 ((JavascriptExecutor) webDriver).executeScript("""var prefs = Components.classes["@mozilla.org/preferences-service;1"].getService(Components.interfaces.nsIPrefBranch);prefs.setBoolPref(arguments[0], arguments[1]);""", name, value);
	
	 PageObjects.Optimize_ElementClick(null);
	
	 }
	 */
	
	
	public void VerifyFileContent_func(String filename,String expect_content)
	{
		ReadFileContent_func(filename);

		if(Content.equals(expect_content))
		{
			TestConfigs.glb_TCStatus = false;
			TestConfigs.glb_TCFailedMessage += "Incorrect Content[Observed:"+Content+"Expect:"+expect_content+"].\n";
		}
	}
	

	
	public  void DownloadFile_func(WebElement element_tmp, String file_name)
	{
		try {
			element_tmp.click();
			
			Thread.sleep(3000);
					
			if(Custom_Func.GetOS_func().contains("mac")) {
	
				//MacUploadFile_func(test_data_full_path+file_name);
	
				/*
				 System.out.println ("Start to upload image in MAC")
				 Runtime.getRuntime().exec("osascript "+"/Users/bigin1/Documents/macupload_valid.scpt")
				 Thread.sleep(2000)
				 System.out.println ("Completed to upload image in MAC")
				 */
		}
		else{
			
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			
			StringSelection string_slect = new StringSelection(TestConfigs.test_data_full_path+file_name);
	
			clipboard.setContents(string_slect, string_slect);
			
			Robot rbot;
				
			rbot = new Robot();
				
				
			//SELECT OPTION SAVE FILE
			rbot.keyPress(KeyEvent.VK_ALT);
			rbot.keyPress(KeyEvent.VK_S);
			Thread.sleep(2000);

			rbot.keyRelease(KeyEvent.VK_S);
			rbot.keyRelease(KeyEvent.VK_ALT);

			Thread.sleep(2000);
			rbot.keyPress(KeyEvent.VK_ENTER);
			rbot.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(1000);
	
				/*
				rbot.keyPress(KeyEvent.VK_CONTROL);
				rbot.keyPress(KeyEvent.VK_V);
				Thread.sleep(1000);
				rbot.keyRelease(KeyEvent.VK_V);
				rbot.keyRelease(KeyEvent.VK_CONTROL);
				Thread.sleep(2000);
	
				rbot.keyPress(KeyEvent.VK_ENTER);
				rbot.keyRelease(KeyEvent.VK_ENTER);
				Thread.sleep(2000);
				*/
			}//else
		}//try
		catch (AWTException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//catch
		
			
		}//void
	
	
		
	private  void MacUploadFile_func(String file_path) {

			try {
		
			
				
			String exec_str ="";

		//	exec_str += "tell application \"Firefox\"\n";
		//	exec_str += "activate \"Firefox\"\n";
			//exec_str += "set current tab to tab 2\n";
		//	exec_str +="end tell\n";
			//exec_str +="delay 2\n";
			exec_str +="tell application \"System Events\"\n";
			exec_str += "activate \"System Events\"\n";
			exec_str +="delay 2\n";
			exec_str +="keystroke \"G\" using {command down, shift down}\n";
			exec_str +="delay 2\n";
			exec_str +="keystroke \""+file_path+"\"\n";
			//exec_str +="keystroke \"/Users/bigin1/Documents/Agoyu-Automation/AgoyuProj/Data Files/logo4M.jpg\"\n"

			exec_str +="keystroke return\n";
			exec_str +="delay 2\n";
			exec_str +="keystroke return\n";
			exec_str +="delay 2\n";
			exec_str +="end tell\n";

			System.out.println(exec_str);

			String []Run_str = new String[3];

			Run_str[0] = "osascript";
			Run_str[1] = "-e";
			Run_str[2] = exec_str;

			System.out.println ("Start to upload image in MAC");

			Runtime rtime = Runtime.getRuntime();
		
			Process prcess = rtime.exec(Run_str);
			
			prcess.destroy();
			

			System.out.println ("Completed to upload image in MAC");
			}//end Try
			catch(IOException  e)
			{
				e.printStackTrace();
			}

		}//end void
	
	
	
	
	public  void UploadFile_func(WebElement upload_btn, String file_name){
		
		try
		{
		WebElement element_tmp = upload_btn;

		String file = file_name;

	//	String container = File.separator+ folder+File.separator;

		String workingDirectory = TestConfigs.test_data_full_path+file;
		
		System.out.println(workingDirectory);

		//PageObjects.Wait_For_ElementDisplay(element_tmp);
		
		Thread.sleep(3000);
		
		element_tmp.click();
				
		Thread.sleep(5000);
		
		//WebDriver driver = TestConfigs.glb_webdriver;

		if(Custom_Func.GetOS_func().contains("mac")) {

			Thread.sleep(5000);
			
			MacUploadFile_func(workingDirectory);

			/*
			 System.out.println ("Start to upload image in MAC")
			 Runtime.getRuntime().exec("osascript "+"/Users/bigin1/Documents/macupload_valid.scpt")
			 Thread.sleep(2000)
			 System.out.println ("Completed to upload image in MAC")
			 */
		}
		else{
			
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

			StringSelection string_slect = new StringSelection( workingDirectory );

			clipboard.setContents(string_slect, string_slect);
			Robot rbot = new Robot();
			
			rbot.keyPress(KeyEvent.VK_ALT);
			
			rbot.keyPress(KeyEvent.VK_N);
			
			Thread.sleep(1000);
			
			rbot.keyRelease(KeyEvent.VK_N);
			
			rbot.keyRelease(KeyEvent.VK_ALT);
			
			
			rbot.keyPress(KeyEvent.VK_CONTROL);
			
			rbot.keyPress(KeyEvent.VK_V);
			
			Thread.sleep(1000);
			
			rbot.keyRelease(KeyEvent.VK_V);
			
			rbot.keyRelease(KeyEvent.VK_CONTROL);
			
			Thread.sleep(3000);

			rbot.keyPress(KeyEvent.VK_ENTER);
			
			rbot.keyRelease(KeyEvent.VK_ENTER);
			
			/*
			if(TestConfigs.IsFF_blen ==false)
			{
				rbot.keyPress(KeyEvent.VK_ENTER);
				
				rbot.keyRelease(KeyEvent.VK_ENTER);
			}
			
			else
			{
				rbot.keyPress(KeyEvent.VK_ALT);
				
				rbot.keyPress(KeyEvent.VK_O);
				
				Thread.sleep(1000);
				
				rbot.keyRelease(KeyEvent.VK_O);
				
				rbot.keyRelease(KeyEvent.VK_ALT);
			}
			*/
			
			Thread.sleep(2000);
			}
		}//if
		catch(Exception  e)
		{
			e.printStackTrace();
		}
			
		//KeyStroke keystroke = KeyStroke.getKeyStroke(KeyEvent.VK_V)


	}//end void
	
	public  void Delete_File_func(String path_folder_str, String file_name){
	
		String full_path_file = path_folder_str+File.separator+file_name;
		
		File myObj = new File(full_path_file); 
	    
		if (myObj.delete()) { 
	      System.out.println("Deleted the file: " + myObj.getName());
	   
		} else {
	      System.out.println("Failed to delete the file.");
	    } 
	}//end void
	
	public  void Delete_Downloaded_File_func(String file_name)
	{
		Delete_File_func(TestConfigs.Download_full_folder_path,file_name);
	}

	
	public Element Parse_XMLFile(String file_name)
	{
		Element classElement =null;
		
		try {
			
		File inputFile = new File(file_name);
	        
		SAXBuilder saxBuilder = new SAXBuilder();
	        
		Document document;
			
		document = saxBuilder.build(inputFile);
			
		System.out.println("Root element :" + document.getRootElement().getName());
	        
		classElement = document.getRootElement();
			
		
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
			
		return classElement;
	
	}
	
	public List<Element> Get_XML_ChildElement(Element parent_element)
	{
		List<Element> childElements = parent_element.getChildren();
		System.out.println("Children elements :" + childElements.toString());
		return childElements;
	}
	
	
	  public static JSONObject ReadJsonFile(String jsonLocation) throws Exception {
	        JSONParser jsonParser = new JSONParser();
	        return (JSONObject) jsonParser.parse(new FileReader(jsonLocation));
	    }
	
	
	
}
