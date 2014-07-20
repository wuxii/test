package org.moon.test.timer.spring.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class SimpleQuartzJob implements Job {

	static Logger LOG = LoggerFactory.getLogger(SimpleQuartzJob.class);

	@Autowired
	private ApplicationContext context;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOG.info("has execute, context is " + context);
	}

}
