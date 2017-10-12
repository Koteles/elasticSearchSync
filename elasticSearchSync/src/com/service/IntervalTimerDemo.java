package com.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.model.Config;


/**
 * Session Bean implementation class IntervalTimerDemo
 */
@Singleton
@LocalBean
@Startup
public class IntervalTimerDemo {

	@Resource
	private TimerService timerService;

	@PostConstruct
	private void init() {
		
		final String syncTimer = "sync.timer";
		
		final long interval = (long) getInterval(syncTimer);
		
		timerService.createTimer(0, interval, "IntervalTimerDemo_Info");
	}

	@Timeout
	public void execute(Timer timer) {

		ElasticSearchService es = new ElasticSearchService();
		 es.addToElasticSearch();
	}
	
	public static int getInterval(String timer) {

		final EntityManager manager = Persistence.createEntityManagerFactory("JavaHelps").createEntityManager();

		final Config config = manager.find(Config.class, timer);

		return config.getTime();
	}

}
