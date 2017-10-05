package com.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

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
	
	private static int PRETTY_PRINT_INDENT_FACTOR = 4;
	private static File[] files;
	private static String indexName;
	private static String syncTimer = "sync.timer";

	public void addToES() {

		long interval = (long) getInterval(syncTimer);
		Timer timer = new Timer();
		timer.schedule(new RemindTask(timer, interval), interval);

	}

	class RemindTask extends TimerTask {

		Timer timer;
		long interval;

		RemindTask(Timer currentTimer, long sec) {
			timer = currentTimer;
			interval = sec;
		}

		@Override
		public void run() {

			System.out.println("Timer task started at:" + new Date());

			loadFiles();
			addToElasticSearch(files);

			System.out.println("Timer task finished at:" + new Date());

			interval = (long) getInterval(syncTimer);

			timer.schedule(new RemindTask(timer, interval), interval);
		}

	}

	public static int getInterval(String timer) {

		EntityManager manager = Persistence.createEntityManagerFactory("JavaHelps").createEntityManager();

		Config config = manager.find(Config.class, timer);

		return config.getTime();
	}

	public static void loadFiles() {

		Properties p = new Properties();

		InputStream input = null;
		try {
			input = new FileInputStream("D:\\dataConfig.properties");
			p.load(input);
			String path = p.getProperty("pathToFiles");
			indexName = p.getProperty("index");
			files = new File(path).listFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

			try {
				JSONObject xmlJSONObj = XML.toJSONObject(xml);

				jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);

				id = Long.toString((Long) xmlJSONObj.get("id"));

				System.out.println(id);

				System.out.println(jsonPrettyPrintString);

			} catch (JSONException je) {
				System.out.println(je.toString());
			}
			
			
			Test.addToElasticSearch(type, id, jsonPrettyPrintString, indexName);
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