package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.http.HttpPOSTrequest;
import com.http.HttpPUTrequest;
import com.model.PojoClass;

public class StartClosingBell {

	@Inject
	private HttpPOSTrequest request;

	@Inject
	private HttpPUTrequest putRequest;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public void start() {
		
		PojoClass obj = new PojoClass();
		final Properties p = new Properties();
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream input = null;
		String body = null;
		String url = null;
		String putUrl = null;
		String response = null;
		String logId = null;
		try {
			input = loader.getResourceAsStream("config.properties");
			p.load(input);
			body = p.getProperty("body");
			url = p.getProperty("url");
			putUrl = p.getProperty("putUrl");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			response = request.sendPOST(url, body);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(response);

		try {
			obj = objectMapper.readValue(response, PojoClass.class);
			logId = obj.getPayload().getUser().getLogId();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String put = putUrl + logId + "/start";
		try {
			System.out.println(putRequest.sendPUT(put));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
