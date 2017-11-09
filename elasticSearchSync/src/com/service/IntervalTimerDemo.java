package com.service;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Inject;
import com.google.common.base.Stopwatch;

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
	private static SearchInElasticSearch search;
	
	@Inject
	private static long timer;

	@Inject
	private static StartClosingBell closingBell;
	
	@Resource
	private TimerService timerService;

	@PostConstruct
	private void init() {
		
		ScheduleExpression schedule = new ScheduleExpression();
		schedule.dayOfWeek("*");
		schedule.minute("00");
		schedule.hour("9");
		timerService.createCalendarTimer(schedule);

		//timerService.createTimer(0, timer, "IntervalTimer");
	}

	@Timeout
	public void execute(Timer timer) {

		Stopwatch watch = Stopwatch.createStarted();
		//search.searchByHeight(5.0616/3.33, 1.72);
		closingBell.start();
		//search.searchByBigDecimalHeight(11, 1.89);
		
		//service.addToElasticSearch();		
		System.out.println("Method took: " + watch.stop());	
		
	}
	

}
