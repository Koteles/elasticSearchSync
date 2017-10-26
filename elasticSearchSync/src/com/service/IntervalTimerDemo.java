package com.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Inject;


/**
 * Session Bean that is initialized upon application startup. 
 * In this class, a programmatic timer is implemented
 * 
 */

@Singleton
@LocalBean
@Startup
public class IntervalTimerDemo {

	@Inject
	private static ElasticSearchService service;
	
	@Inject
	private static long timer;
	
	@Resource
	private TimerService timerService;

	@PostConstruct
	private void init() {
		
		timerService.createTimer(0, timer, "IntervalTimer");
	}

	@Timeout
	public void execute(Timer timer) {

		 service.addToElasticSearch();
	}
	

}
