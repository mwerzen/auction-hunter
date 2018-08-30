package com.mikewerzen.wow.auctionminer.repository;

import com.mikewerzen.wow.auctionminer.entity.AggregateData;
import com.mikewerzen.wow.auctionminer.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AggregateRepository extends JpaRepository<AggregateData, Long>
{

}
