package com.mikewerzen.wow.auctionminer.service;

import com.mikewerzen.wow.auctionminer.entity.AggregateData;
import com.mikewerzen.wow.auctionminer.entity.Auction;
import com.mikewerzen.wow.auctionminer.repository.AggregateRepository;
import com.mikewerzen.wow.auctionminer.repository.AuctionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AggregateService
{
	private static final Logger logger = LogManager.getLogger(AggregateService.class);

	@Autowired
	private AuctionRepository auctionRepository;

	@Autowired
	private AggregateRepository aggregateRepository;

	private long numberOfHoursToAggregate = 7 * 24;
	private long numberOfMillisToAggregate = numberOfHoursToAggregate * 60 * 60 * 1000;

	public void buildAggregates()
	{
		List<Long> allCurrentItemIds = auctionRepository.findAllCurrentItemIds();
		logger.warn("=================================================================");
		logger.warn("Ids Found: " + allCurrentItemIds.size());

		for (Long itemId : allCurrentItemIds)
		{
			if (aggregateRepository.existsById(itemId))
			{
				aggregateRepository.deleteById(itemId);
			}

			long earliestDate = System.currentTimeMillis() - numberOfMillisToAggregate;
			List<Auction> completedAuctions = auctionRepository
					.findAllByItemIdAndEstimatedFinishDateGreaterThanAndCompletedTrue(
							itemId,
							earliestDate);

			if (completedAuctions.size() > 0)
			{
				logger.info(buildAggregate(completedAuctions));
			}

		}

	}

	public double getStandardDev(double average, List<Long> prices)
	{
		if (prices.size() == 0)
		{
			return 0;
		}

		double sumVariance = 0;
		for (Long price : prices)
		{
			sumVariance += Math.pow(price - average, 2);
		}
		double variance = sumVariance / prices.size();

		return Math.sqrt(variance);
	}

	private AggregateData buildAggregate(List<Auction> auctions)
	{
		AggregateData data = new AggregateData();

		List<Long> allPrices = new ArrayList<>();
		List<Long> soldPrices = new ArrayList<>();

		long amountOffered = 0;
		long averageOffered = 0;

		long amountSold = 0;
		long averageSold = 0;

		for (Auction auction : auctions)
		{
			amountOffered += auction.getQuantity();
			long price = Math.max(auction.getBuyoutPricePerUnit(), auction.getCurrentBidPerUnit());
			averageOffered += price * auction.getQuantity();

			for (int i = 0; i < auction.getQuantity(); i++)
			{
				allPrices.add(price);
			}

			if (auction.isSold())
			{
				amountSold += auction.getQuantity();
				averageSold += price * auction.getQuantity();

				for (int i = 0; i < auction.getQuantity(); i++)
				{
					soldPrices.add(price);
				}
			}
		}

		Collections.sort(allPrices);
		Collections.sort(soldPrices);

		data.itemId = auctions.get(0).getItemId();
		data.amountOffered = amountOffered;
		data.averageOffered = averageOffered / amountOffered;
		data.amountSold = amountSold;
		data.soldPercentage = (double) amountSold / amountOffered;

		if (amountSold > 0)
		{
			data.averageSold = averageSold / amountSold;
		}

		data.minOffered = allPrices.get(0);
		data.p25Offered = allPrices.get((int) Math.floor(allPrices.size() * .25));
		data.medianOffered = allPrices.get(allPrices.size() / 2);
		data.p75Offered = allPrices.get((int) Math.floor(allPrices.size() * .75));
		data.maxOffered = allPrices.get(allPrices.size() - 1);
		//data.standardDeviationOffered = (long) getStandardDev(data.averageOffered, allPrices);

		if (soldPrices.size() > 0)
		{
			data.minSold = soldPrices.get(0);
			data.p25Sold = soldPrices.get((int) Math.floor(soldPrices.size() * .25));
			data.medianSold = soldPrices.get(soldPrices.size() / 2);
			data.p75Sold = soldPrices.get((int) Math.floor(soldPrices.size() * .75));
			data.maxSold = soldPrices.get(soldPrices.size() - 1);
			//data.standardDeviationSold = (long) getStandardDev(data.averageSold, soldPrices);
		}

		data.salesPerHour = ((double) amountSold / (double) getNumberOfHours());

		aggregateRepository.save(data);

		return data;
	}

	public long getNumberOfHours()
	{
		long firstTimestamp = auctionRepository.getFirstTimestamp();
		long numberOfHoursSinceStart = (System.currentTimeMillis() - firstTimestamp) / 60 * 60 * 1000;
		return Math.min(numberOfHoursSinceStart, numberOfHoursToAggregate);
	}

}
