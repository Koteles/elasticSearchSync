package com.service;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ElasticSearchService {
	
	public void addToElasticSearch() {
		Elasticsearch es = new Elasticsearch();
		 es.addToES();
	}
}
