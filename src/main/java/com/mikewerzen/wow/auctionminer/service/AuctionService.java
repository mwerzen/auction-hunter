package com.mikewerzen.wow.auctionminer.service;

import com.mikewerzen.wow.auctionminer.entity.Auction;
import com.mikewerzen.wow.auctionminer.repository.AuctionRepository;
import com.mikewerzen.wow.auctionminer.rest.container.ApiAuction;
import com.mikewerzen.wow.auctionminer.util.AuctionUtil;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService
{
	private static final Logger logger = LogManager.getLogger(AuctionService.class);

	@Autowired
	private AuctionRepository repository;

	@Transactional
	public void processAuctionUpdates(List<ApiAuction> updates)
	{
		repository.markIncomplete();
		for(ApiAuction updatedAuction : updates)
		{
			logger.info("Updating Auction: " + updatedAuction.auc);
			processAuction(updatedAuction);
		}

		List<Auction> completedAuctions = repository.findAllByProcessedThisTickAndCompleted(false, false);
		for(Auction completedAuction : completedAuctions)
		{
			logger.info("Completing Auction: " + completedAuction.getAuctionId());
			completeAuction(completedAuction);
		}

	}

	private void processAuction(ApiAuction apiAuction)
	{
		Optional<Auction> auction = repository.findById(apiAuction.auc);
		if(auction.isPresent())
		{
			Auction updatedAuction = updateAuctionFromApiResponse(auction.get(), apiAuction);
			repository.save(updatedAuction);
		} else
		{
			Auction createdAuction = createAuctionFromAPIResponse(apiAuction);
			repository.save(createdAuction);
		}
	}

	private Auction createAuctionFromAPIResponse(ApiAuction apiAuction)
	{
		Auction auction = new Auction();
		auction.setAuctionId(apiAuction.auc);
		auction.setItemId(apiAuction.item);
		auction.setOwner(apiAuction.owner);
		auction.setRealm(apiAuction.ownerRealm);
		auction.setStartingBid(apiAuction.bid);
		auction.setCurrentBid(apiAuction.bid);
		auction.setNumberOfBids(0);
		auction.setBuyoutPrice(apiAuction.buyout);
		auction.setQuantity(apiAuction.quantity);
		auction.setEstimatedFinishDate(AuctionUtil.estimateFinishTime(apiAuction.timeLeft, Long.MAX_VALUE));
		updatePricePerUnit(auction);
		auction.setProcessedThisTick(true);
		return auction;
	}

	private Auction updateAuctionFromApiResponse(Auction auction, ApiAuction apiAuction)
	{
		if (auction.getCurrentBid() != apiAuction.bid)
		{
			auction.setNumberOfBids(auction.getNumberOfBids() + 1);
			auction.setCurrentBid(apiAuction.bid);
		}

		auction.setEstimatedFinishDate(AuctionUtil.estimateFinishTime(apiAuction.timeLeft, auction.getEstimatedFinishDate()));

		updatePricePerUnit(auction);
		auction.setProcessedThisTick(true);

		return auction;
	}

	private Auction completeAuction(Auction auction)
	{
		auction.setCompleted(true);
		if (System.currentTimeMillis() < auction.getEstimatedFinishDate())
		{
			auction.setSold(true);
			auction.setBoughtOut(true);
			auction.setSalePrice(auction.getBuyoutPrice());
			auction.setSalePricePerUnit(auction.getBuyoutPricePerUnit());
		}
		else if (auction.getNumberOfBids() > 0)
		{
			auction.setSold(true);
			auction.setSalePrice(auction.getCurrentBid());
			auction.setSalePricePerUnit(auction.getCurrentBidPerUnit());
		}

		return repository.save(auction);
	}

	private void updatePricePerUnit(Auction auction)
	{
		auction.setStartingBidPerUnit(auction.getStartingBid() / auction.getQuantity());
		auction.setCurrentBidPerUnit(auction.getCurrentBid() / auction.getQuantity());
		auction.setBuyoutPricePerUnit(auction.getBuyoutPrice() / auction.getQuantity());
	}
}
