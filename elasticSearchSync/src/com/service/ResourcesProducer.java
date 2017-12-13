package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.model.Config;
import com.qualifiers.HostName;
import com.qualifiers.IndexName;
import com.qualifiers.PortNumber;
import com.qualifiers.TypeName;

public class ResourcesProducer {

	private static Properties properties;

	static {

		final ClassLoader loader = Thread.currentThread().getContextClassLoader();

		properties = new Properties();

		final InputStream inputStream = loader.getResourceAsStream("config.properties");

		try {
			properties.load(inputStream);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Produces
	private Properties produceProperties() {
		return properties;
	}
	
	@Produces
	private long produceTimer() {
		final String syncTimer = "sync.timer";
		final EntityManager manager = Persistence.createEntityManagerFactory(properties.getProperty("database")).createEntityManager();
		final Config config = manager.find(Config.class, syncTimer);
		return config.getTime();
	}
	
	@Produces
	@IndexName
	private String produceIndexName() {

		return properties.getProperty("index");
	}

	@Produces
	@HostName
	private String produceHostName() {

		return properties.getProperty("elasticSearchHost").trim();

	}

	@Produces
	@TypeName
	private String produceTypeName() {

		return properties.getProperty("type");
	}

	@Produces
	@PortNumber
	private int producePortNumber() {

		return Integer.parseInt(properties.getProperty("elasticSearchPort").trim());
	}

	@Produces
	private Client produceClient() {

		Client client = null;
		String host = produceHostName();
		int portNumber = producePortNumber();

		try {
			client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), portNumber));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return client;
	}

}
