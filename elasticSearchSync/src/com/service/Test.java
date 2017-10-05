package com.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import javax.inject.Inject;
import org.elasticsearch.common.transport.InetSocketTransportAddress;


public class Test {

	/*@Inject 
	private static Client client;*/
	
    public static void addToElasticSearch (String type, String id, String json, String index) {
    	
    	
    	Client client = null;
		try {
			client = TransportClient.builder().build()
					   .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
		client.prepareIndex(index, type, id)
		        .setSource(json)
		        .get();
	
    }
}
