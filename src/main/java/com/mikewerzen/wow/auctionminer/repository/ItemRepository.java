package com.mikewerzen.wow.auctionminer.repository;

import com.mikewerzen.wow.auctionminer.entity.Auction;
import com.mikewerzen.wow.auctionminer.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>
{
	
}
