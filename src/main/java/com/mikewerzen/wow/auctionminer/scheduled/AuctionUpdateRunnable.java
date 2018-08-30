package com.mikewerzen.wow.auctionminer.scheduled;

import com.mikewerzen.wow.auctionminer.service.AuctionMinerCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuctionUpdateRunnable implements Runnable
{
	private static final Logger logger = LogManager.getLogger(AuctionUpdateRunnable.class);

	private AuctionMinerCore core;

	@Autowired
	private AuctionUpdateRunnable self;

	private TaskScheduler scheduler;

	public AuctionUpdateRunnable(AuctionMinerCore core, TaskScheduler scheduler)
	{
		this.core = core;
		this.scheduler = scheduler;

		//scheduler.schedule(this, new Date(System.currentTimeMillis() + 5000L));
	}

	@Override
	@Scheduled(fixedRate = 3600000L)
	public void run()
	{
		logger.info("Executing Scheduler to Update Core");
		core.update();
	}
}
