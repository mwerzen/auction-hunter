package com.mikewerzen.wow.auctionminer.repository;

import com.mikewerzen.wow.auctionminer.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long>
{

	@Modifying
	@Query("UPDATE Auction a SET a.processedThisTick = false WHERE a.completed = false")
	void markIncomplete();

	List<Auction> findAllByProcessedThisTickAndCompleted(boolean processedThisTick, boolean completed);

	@Query("SELECT DISTINCT itemId FROM Auction WHERE completed = false")
	List<Long> findAllCurrentItemIds();

	List<Auction> findAllByItemIdAndEstimatedFinishDateGreaterThanAndCompletedTrue(long itemId, long estimatedFinishDate);
	List<Auction> findAllByItemIdAndCompletedIsFalse(long itemId);

	@Query("SELECT MIN (estimatedFinishDate) FROM Auction WHERE completed = true")
	Long getFirstTimestamp();
}
