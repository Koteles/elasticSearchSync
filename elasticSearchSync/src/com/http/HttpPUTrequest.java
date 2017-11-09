package com.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpPUTrequest {

	public String sendPUT(String putUrl) throws IOException {
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		HttpPut put = new HttpPut(putUrl);
		
		put.setHeader("Accept", "application/json");
				
		put.setHeader("Content-type", "application/json");
		
		HttpResponse response = httpClient.execute(put);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line); }
		return result.toString(); 		
		
	}

}

