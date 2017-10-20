package com.service;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Student;
import com.qualifiers.HostName;
import com.qualifiers.IndexName;
import com.qualifiers.TypeName;


/**
 * 
 * This class parses XML files to JSON
 * 
 */

public class ElasticSearchService {
	
	@Inject
	private File[] files;
	
	@Inject @IndexName
	private String index;
	
	@Inject @HostName
	private String host;
	
	@Inject @TypeName
	private String type;
	
	@Inject
	private int port;
	
	public void addToElasticSearch() {

		System.out.println("Timer task started at:" + new Date());

		addToElasticSearch(files,host,index,type,port);

		System.out.println("Timer task finished at:" + new Date());
	}


	public static void addToElasticSearch(File[] files, String host, String indexName, String type, int portNumber) {

		Client client = null;
		Unmarshaller jaxbUnmarshaller = null;
		
		final ObjectMapper objectMapper = new ObjectMapper();
		
		try {

			final JAXBContext jaxbContext = JAXBContext.newInstance(Student.class);
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

}