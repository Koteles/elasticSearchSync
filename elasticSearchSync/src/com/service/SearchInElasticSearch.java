package com.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Student;
import com.qualifiers.HostName;
import com.qualifiers.IndexName;
import com.qualifiers.TypeName;

public class SearchInElasticSearch {

	@Inject
	@IndexName
	private String index;

	@Inject
	@HostName
	private String host;

	@Inject
	@TypeName
	private String type;

	@Inject
	private Client client;

	private final ObjectMapper objectMapper = new ObjectMapper();
	private Student student;

	public void searchByBigDecimalHeight(double num1, double num2) {
			
		String stringNum1 = Double.toString(num1);
		String stringNum2 = Double.toString(num2);
		BigDecimal big = new BigDecimal(stringNum1).multiply(new BigDecimal(stringNum2));
		
		final int size = 1000;
		SearchResponse response = null;
		int i = 0;
		List<Student> students = new ArrayList<Student>();
			
		final SearchRequestBuilder search = client.prepareSearch(index).setTypes(type)
				.setQuery(QueryBuilders.matchAllQuery()).setPostFilter(QueryBuilders.rangeQuery("height").from(big).to(big))
				.setSize(size).addSort("id", SortOrder.ASC);
		
		while (response == null || response.getHits().hits().length != 0) {

			response = search.setFrom(i * size).get();

			for (SearchHit hit : response.getHits()) {

				try {
					student = objectMapper.readValue(hit.getSourceAsString(), Student.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				students.add(student);
		
			}
			i++;
		}
		System.out.println(students);
		
	}
	
	public void searchByHeight(double from, double to) {
		
		final int size = 1000;
		SearchResponse response = null;
		int i = 0;
		
		List<Student> students = new ArrayList<Student>();
		
		final SearchRequestBuilder search = client.prepareSearch(index).setTypes(type)
				.setQuery(QueryBuilders.matchAllQuery()).setPostFilter(QueryBuilders.rangeQuery("height").from(from).to(to))
				.setSize(size).addSort("id", SortOrder.ASC);
		
		while (response == null || response.getHits().hits().length != 0) {

			response = search.setFrom(i * size).get();

			for (SearchHit hit : response.getHits()) {

				try {
					student = objectMapper.readValue(hit.getSourceAsString(), Student.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				students.add(student);
		
			}
			i++;
		}
		System.out.println(students);
		
	}
		
}
