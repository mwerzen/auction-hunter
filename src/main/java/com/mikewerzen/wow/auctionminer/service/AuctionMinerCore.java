package com.mikewerzen.wow.auctionminer.service;

import com.mikewerzen.wow.auctionminer.entity.Auction;
import com.mikewerzen.wow.auctionminer.rest.WowApiClient;
import com.mikewerzen.wow.auctionminer.rest.container.ApiAuction;
import com.mikewerzen.wow.auctionminer.rest.container.AuctionDataFile;
import com.mikewerzen.wow.auctionminer.rest.container.AuctionDataStatus;
import com.mikewerzen.wow.auctionminer.rest.container.AuctionList;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tessatech.tessa.framework.core.event.TessaEvent;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuctionMinerCore
{
	private static final Logger logger = LogManager.getLogger(AuctionMinerCore.class);

	@Value("${wow.realm.id}")
	private String realmId;

	@Autowired
	private WowApiClient apiClient;

	@Autowired
	private AuctionService auctionService;

	@Autowired
	private AggregateService aggregateService;

	@Autowired
	private SuggestionService suggestionService;

	@TessaEvent(eventName = "Update Core")
	public void update()
	{
		logger.info("Updating Core Data...");
		AuctionDataStatus status = apiClient.getAuctionData();

		List<ApiAuction> realmFilteredAuctions = new ArrayList<>();
		for(AuctionDataFile file : status.files)
		{
			if(file.lastModified > (System.currentTimeMillis() - 60 * 60 * 1000))
			{
				realmFilteredAuctions.addAll(parseFile(file));
			}
		}

		auctionService.processAuctionUpdates(realmFilteredAuctions);
		aggregateService.buildAggregates();
		suggestionService.getSuggestions();

	}

	public List<ApiAuction> parseFile(AuctionDataFile file)
	{
		logger.info("Parsing File: " + file.url);
		AuctionList list = apiClient.getAuctions(file.url);

		List<ApiAuction> realmFilteredAuctions = new ArrayList<>();
		for(ApiAuction auction : list.auctions)
		{
			if(realmId.equalsIgnoreCase(auction.ownerRealm))
				realmFilteredAuctions.add(auction);
		}

		return realmFilteredAuctions;
	}
}
