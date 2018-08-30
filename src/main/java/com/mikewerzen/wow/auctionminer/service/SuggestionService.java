package com.mikewerzen.wow.auctionminer.service;

import com.mikewerzen.wow.auctionminer.entity.AggregateData;
import com.mikewerzen.wow.auctionminer.entity.Auction;
import com.mikewerzen.wow.auctionminer.entity.Suggestion;
import com.mikewerzen.wow.auctionminer.repository.AggregateRepository;
import com.mikewerzen.wow.auctionminer.repository.AuctionRepository;
import com.mikewerzen.wow.auctionminer.repository.SuggestionRepository;
import com.mikewerzen.wow.auctionminer.util.AuctionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SuggestionService
{
	private static final Logger logger = LogManager.getLogger(SuggestionService.class);

	@Autowired
	private AuctionRepository auctionRepository;

	@Autowired
	private AggregateRepository aggregateRepository;

	@Autowired
	private SuggestionRepository suggestionRepository;

	public void getSuggestions()
	{
		List<Long> allCurrentItemIds = auctionRepository.findAllCurrentItemIds();
		logger.warn("=================================================================");
		logger.warn("Ids Found: " + allCurrentItemIds.size());

		List<Suggestion> allSuggestions = new ArrayList<>();
		for (Long itemId : allCurrentItemIds)
		{

			Optional<AggregateData> aggregateData = aggregateRepository.findById(itemId);

			if (!aggregateData.isPresent())
			{
				continue;
			}

			List<Auction> currentAuctionsForItem = auctionRepository.findAllByItemIdAndCompletedIsFalse(itemId);

			Suggestion suggestion = calculateSuggestion(currentAuctionsForItem, aggregateData);
			if (suggestion != null)
			{
				suggestion.itemId = itemId;
				allSuggestions.add(suggestion);
				logger.info(suggestion);
			}

		}

		suggestionRepository.deleteAll();
		suggestionRepository.saveAll(allSuggestions);
	}

	private Suggestion calculateProfitFromFirstXSales(int numberToBuy, List<Auction> currentAuctionsForItem,
			AggregateData aggregateData)
	{
		long totalQuantity = 0;
		long totalPrice = 0;

		String sellers = "";

		for (int i = 0; i < numberToBuy; i++)
		{
			if(currentAuctionsForItem.get(i).getBuyoutPrice() <=0)
			{
				continue;
			}

			totalQuantity += currentAuctionsForItem.get(i).getQuantity();
			totalPrice += currentAuctionsForItem.get(i).getBuyoutPrice();
			sellers += currentAuctionsForItem.get(i).getOwner() + " ";
		}

		if(sellers.length() > 20)
			sellers = sellers.substring(0, 20);

		long salePricePerUnit = currentAuctionsForItem.get(currentAuctionsForItem.size() - 1).getBuyoutPricePerUnit();

		if (numberToBuy < currentAuctionsForItem.size())
		{
			salePricePerUnit = (long) (currentAuctionsForItem.get(numberToBuy).getBuyoutPricePerUnit() * .975);
		}

		if (salePricePerUnit > aggregateData.p75Sold)
		{
			salePricePerUnit = aggregateData.p75Sold;
		}

		if(salePricePerUnit <= 0 || totalPrice <= 0)
		{
			return null;
		}

		Suggestion suggestion = new Suggestion();
		suggestion.text = "Buy: " + numberToBuy + " stacks/" + totalQuantity + " units" + "<br \\>" + "From: " + sellers + aggregateData.getPricingText();
		suggestion.buyout = true;
		suggestion.dataPoints = aggregateData.amountSold;
		suggestion.estimatedGoldUsage = totalPrice;
		suggestion.estimatedSalePrice = salePricePerUnit * totalQuantity;
		suggestion.estimatedSaleTimeInHours = (long) Math.ceil(totalQuantity / aggregateData.salesPerHour);
		suggestion.updateHourlyROR();

		return suggestion;
	}

	private Suggestion calculateSuggestion(List<Auction> currentAuctionsForItem, Optional<AggregateData> aggregateData)
	{
		if (currentAuctionsForItem == null || currentAuctionsForItem.size() == 0 || !aggregateData.isPresent())
		{
			//There are no auctions, therefore nothing to suggest
			return null;
		}

		AggregateData data = aggregateData.get();

		if (currentAuctionsForItem.size() == 1)
		{
			Auction auction = currentAuctionsForItem.get(0);

			if(auction.getBuyoutPrice() <= 0)
			{
				return null;
			}

			Suggestion suggestion = new Suggestion();
			suggestion.text = "Buy: 1 stacks/"+ auction.getQuantity() + " units" + "<br \\>" + "From: " + auction.getOwner() + data.getPricingText();
			suggestion.buyout = true;
			suggestion.dataPoints = data.amountSold;
			suggestion.estimatedGoldUsage = auction.getBuyoutPrice();
			suggestion.estimatedSalePrice = data.averageSold * auction.getQuantity();
			suggestion.estimatedSaleTimeInHours = (long) Math.ceil(1 / data.salesPerHour);
			suggestion.updateHourlyROR();

			return suggestion;
		}

		Collections.sort(currentAuctionsForItem);

		Suggestion best = null;
		for (int i = 1; i < currentAuctionsForItem.size(); i++)
		{
			Suggestion test = calculateProfitFromFirstXSales(i, currentAuctionsForItem, data);

			if(test == null)
				continue;

			if (best == null || best.hourlyROR < test.hourlyROR)
			{
				best = test;
			}
			else if (best.hourlyROR > test.hourlyROR)
			{
				break;
			}
		}

		return best;
	}

}
