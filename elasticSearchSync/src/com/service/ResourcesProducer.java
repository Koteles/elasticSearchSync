package com.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.enterprise.inject.Produces;

import com.qualifiers.HostName;
import com.qualifiers.IndexName;
import com.qualifiers.TypeName;

public class ResourcesProducer {


	private static Properties p;

	static {
		
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();

		p = new Properties();
		
		InputStream inputStream = loader.getResourceAsStream("config.properties");
	
		try {
			p.load(inputStream);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	@Produces
	@IndexName
	private String produceIndexName() {
		
		return p.getProperty("index");
	}

	@Produces
	@HostName
	private String produceHostName() {

		return p.getProperty("elasticSearchHost").trim();

	}

	@Produces
	@TypeName
	private String produceTypeName() {
	
		return p.getProperty("type");
	}

	@Produces
	private int producePortNumber() {
		
		return Integer.parseInt(p.getProperty("elasticSearchPort").trim());
	}
	
	@Produces
	private File[] produceFilesPath() {
		final String path = p.getProperty("pathToFiles");
		return new File(path).listFiles();
	}

	

	/*private void produceFilesPath() {

		final Properties p = new Properties();

		final ClassLoader loader = Thread.currentThread().getContextClassLoader();

		InputStream input = null;
		String indexName = null;
		String host = null;
		int portNumber = 0;
		String type = null;

		try {

			input = loader.getResourceAsStream("config.properties");

			p.load(input);

			final String path = p.getProperty("pathToFiles");

			indexName = p.getProperty("index");
			// produceIndexName(indexName);

			host = p.getProperty("elasticSearchHost").trim();
			// produceHostName(host);

			type = p.getProperty("type");
			// produceTypeName(type);

			String port = p.getProperty("elasticSearchPort").trim();

			portNumber = Integer.parseInt(port);
			// producePort(portNumber);

			files = new File(path).listFiles();

		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return files;
	}*/

}
