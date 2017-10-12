package com.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;


public class Test {

	/*@Inject 
	private static Client client;*/
	
    public static void addToElasticSearch (String type, String id, String json, String index, String host, int port) {
    	
    	Client client = null;
    	
		try {
			client = TransportClient.builder().build()
					   .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
		client.prepareIndex(index, type, id)
		        .setSource(json);
	
    }
}
