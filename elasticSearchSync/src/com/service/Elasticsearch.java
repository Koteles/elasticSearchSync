package com.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 * 
 * This class parses xml files to JSON
 * 
 */

public class Elasticsearch {

	private static String indexName;
	private static int portNumber;
	private static String host;

	public void addToElasticSearch() {
		
		System.out.println("Timer task started at:" + new Date());
		
		File[] files = loadFiles();
		
		addToElasticSearch(files);
		
		System.out.println("Timer task finished at:" + new Date());
	}
	

	public static File[] loadFiles() {

		File[] files = null;
		
		Properties p = new Properties();
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		InputStream input = null;
		
		try {
			
			input = loader.getResourceAsStream("config.properties");
			
			p.load(input);
			
			String path = p.getProperty("pathToFiles");
			
			indexName = p.getProperty("index");
			host = p.getProperty("elasticSearchHost").trim();
			
			String port = p.getProperty("elasticSearchPort").trim();	
			
			portNumber = Integer.parseInt(port);
			
			files = new File(path).listFiles();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return files;
	}

	public static void addToElasticSearch(File[] files) {

		for (File file : files) { // iterate through all the xml files

			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String line;
			StringBuilder sb = new StringBuilder();
			int BUFFER_SIZE = 1000;

			String type = null;
			try {
				br.readLine();
				type = br.readLine();
				line = br.readLine();

				sb.append(line.trim());
				while ((line = br.readLine()) != null) {
					br.mark(BUFFER_SIZE); // in order to read next line without moving the pointer

					if (br.readLine() != null) {
						br.readLine();
						br.reset();
						sb.append(line.trim());
					}
				}				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			type = type.substring(1, type.length() - 1);
			System.out.println(type);

			String id = null;

			String xml = sb.toString();

			String jsonPrettyPrintString = null;

			final int PRETTY_PRINT_INDENT_FACTOR = 4;
			
			try {
				JSONObject xmlJSONObj = XML.toJSONObject(xml);

				jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);

				id = Long.toString((Long) xmlJSONObj.get("id"));

				System.out.println(id);

				System.out.println(jsonPrettyPrintString);

			} catch (JSONException je) {
				System.out.println(je.toString());
			}			
			
			Test.addToElasticSearch(type, id, jsonPrettyPrintString, indexName, host, portNumber);
		}
	}

	public static void showFiles(File[] files) {

		for (File file : files) {
			if (file.isDirectory()) {
				System.out.println("Directory: " + file.getName());
				showFiles(file.listFiles()); // Calls same method again.
			} else {
				System.out.println("File: " + file.getName());
			}
		}
	}
}