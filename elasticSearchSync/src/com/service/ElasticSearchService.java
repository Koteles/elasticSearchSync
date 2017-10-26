package com.service;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.elasticsearch.client.Client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Student;
import com.qualifiers.HostName;
import com.qualifiers.IndexName;
import com.qualifiers.TypeName;


/**
 * 
 * This class parses XML files to JSON and uses the Index API of ElasticSearch 
 * to add or update a JSON document in a specific index
 * 
 */

public class ElasticSearchService {
	
	@Inject
	private Properties properties;
	
	@Inject
	private Client client;
	
	@Inject
	private Logger logger;	
	
	@Inject @IndexName
	private String index;
	
	@Inject @HostName
	private String host;
	
	@Inject @TypeName
	private String type;
	
	@Inject
	private int port;
	
	public void addToElasticSearch() {
		
		logger.info("Adding to ElasticSearch");
		System.out.println("Timer task started at:" + new Date());

		addToElasticSearch(host,index,type,port);

		System.out.println("Timer task finished at:" + new Date());
	}


	public void addToElasticSearch(String host, String indexName, String type, int portNumber) {

		final String path = properties.getProperty("pathToFiles");
		File[] files = new File(path).listFiles();
		
		Unmarshaller jaxbUnmarshaller = null;
		
		final ObjectMapper objectMapper = new ObjectMapper();
		
		try {

			final JAXBContext jaxbContext = JAXBContext.newInstance(Student.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			

		}  catch (JAXBException e) {
			e.printStackTrace();
		}

		for (File file : files) { 		// iterate through all the XML files

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

}