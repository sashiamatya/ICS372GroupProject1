package edu.metrostate.ics372groupproject1.scientificDataCollectionApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class IOInterface {
	private File outputFile;
	private Gson myGson;
	public ArrayList <Item> readings;
	
	//IOInterface constructor, initialize class members
	public IOInterface() {
		myGson = new Gson();
		readings = new ArrayList<>();
	}
	
	/*
	 * ReadJSON method takes a file and reads the content into Objects
	 */	
	public void ReadJson(File input) throws Exception{
		//Instantiates a BufferReader object that takes the input file as an argument 
		BufferedReader reader = new BufferedReader(new FileReader(input));
		Site mySite = myGson.fromJson(reader, Site.class);
		
		//If mySite is not null, point readings to the list of items in file
		if(mySite != null) {
			readings = mySite.getItems();
		}
		reader.close();
	}
	
	/*
	 * WriteToFile method takes as a parameters a list of sites
	 * and a file name. It write the sites in the list to a file on the disk
	 */
	public void writeToFile(ArrayList<Site> site, String outputFileName) throws Exception{
		//path and construct of the output file
		outputFile = new File(System.getProperty("user.dir")+"/src/"+ outputFileName + ".json");
		//Instantiate a PrintWriter object
		PrintWriter writer = new PrintWriter(outputFile);
		//Write JSON object to the specified file on the disk
		myGson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		String jsonString = myGson.toJson(site);
		writer.write(jsonString);
		
		writer.close();
		
	}
	/*
	 * Read the xml file
	 */
	public void readXMLFile(File file) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	try {
		SAXParser saxParser=saxParserFactory.newSAXParser();
		XMLSAXParserHandler handler=new XMLSAXParserHandler();
		saxParser.parse(file, handler);
		
		//Get Item List
		Study study = handler.getStudy();
		readings = handler.getItemList();
		//Need to create study 
		
//		//print Study information
//		System.out.println(study.toString());
//		//print Item information
//		for(Item item:items) {
//			System.out.println("------------");
//			System.out.println(item);
//			System.out.println("Unit:" + item.getUnit());
//			
//		}		
	}
	catch (ParserConfigurationException | SAXException | IOException e) {
        e.printStackTrace();
    }
		
	}
//	
//	
//	
//	
	//get specified site reading from the list of all site readings 
	public void getSiteReadings(String siteID, Site pickedSite) {
		for(Item item : readings) {
			//Only the item with matching Site ID are add 
			if(item.getSiteID().equals(siteID) && pickedSite.isRecording()) {
				pickedSite.addItem(item);
			}
		}
	}
	
	//get all the site collections
	public String getReadings() {
		String reading = "";
		for(Item i : readings) {
			reading += i.toString()+ "\n\n"; 
		}
		return reading;
	}
	
	// The method set up the path and name of the output file to write to
	public String getOutputFile () {
		return outputFile.getAbsolutePath();
	}
}
