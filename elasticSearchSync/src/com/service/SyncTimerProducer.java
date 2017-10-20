package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.model.Config;

/**
 * 
 * This class has a method that produces the timer interval, by fetching it from the database
 * 
 */

public class SyncTimerProducer {

	
	@Produces
	private long produceSyncTimer() {
		
		final String syncTimer = "sync.timer";
		
		String databaseName = null;
		
		final Properties p = new Properties();
		
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();

		InputStream input = null;
		
		try {

			input = loader.getResourceAsStream("config.properties");

			p.load(input);

			databaseName = p.getProperty("database"); 
		
		}
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		final EntityManager manager = Persistence.createEntityManagerFactory(databaseName).createEntityManager();

		final Config config = manager.find(Config.class, syncTimer);

		return config.getTime();
	}
}
