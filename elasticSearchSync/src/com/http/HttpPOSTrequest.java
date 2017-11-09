package com.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpPOSTrequest {


	public String sendPOST(String postUrl, String body) throws IOException {
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		HttpPost post = new HttpPost(postUrl);
		
		post.setHeader("Accept", "application/json");
		
		StringEntity postingString = new StringEntity(body);	
		
		post.setEntity(postingString);
		
		post.setHeader("Content-type", "application/json");
		
		HttpResponse response = httpClient.execute(post);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line); }
		return result.toString(); 		
		
	}

}