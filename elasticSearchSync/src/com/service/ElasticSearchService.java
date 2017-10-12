package com.service;

import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import javax.xml.bind.Unmarshaller;

import java.util.Date;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Student;

/**
 * 
 * This class parses xml files to JSON
 * 
 */

public class ElasticSearchService {

	public void addToElasticSearch() {

		System.out.println("Timer task started at:" + new Date());

		loadFiles();

		System.out.println("Timer task finished at:" + new Date());
	}

	public static void loadFiles() {

		File[] files = null;

		Properties p = new Properties();

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		InputStream input = null;
		String indexName = null;
		String host = null;
		int portNumber = 0;
		String type = null;
		try {

			input = loader.getResourceAsStream("config.properties");

			p.load(input);

			String path = p.getProperty("pathToFiles");

			indexName = p.getProperty("index");
			host = p.getProperty("elasticSearchHost").trim();
			type = p.getProperty("type");
			String port = p.getProperty("elasticSearchPort").trim();

			portNumber = Integer.parseInt(port);

			files = new File(path).listFiles();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addToElasticSearch(files, host, indexName, type, portNumber);
	}

	public static void addToElasticSearch(File[] files, String host, String indexName, String type, int portNumber) {

		Client client = null;
		Unmarshaller jaxbUnmarshaller = null;
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(Student.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), portNumber));

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		for (File file : files) { // iterate through all the xml files

			Student student = null;

			try {
				student = (Student) jaxbUnmarshaller.unmarshal(file);

			} catch (JAXBException e) {
				e.printStackTrace();
			}
			String json = null;
			
			try {
				json = objectMapper.writeValueAsString(student);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(json);
			String id = student.getId();
			client.prepareIndex(indexName, type, id).setSource(json).get();
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