package com.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpGETrequest {


	public void sendGET(String url) throws IOException {

		//String auth = "admin:admin";
		//byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("ISO-8859-1")));
		//String authHeader = "Basic " + new String(encodedAuth);
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		HttpGet get = new HttpGet(url);
		//post.setHeader("Accept", "application/json");
		//post.setHeader("Authorization", authHeader);

		
		//post.setHeader("Content-type", "application/json");
		HttpResponse response = httpClient.execute(get);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		
		/*BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);		}
		return result.toString(); */		
		
	}

}