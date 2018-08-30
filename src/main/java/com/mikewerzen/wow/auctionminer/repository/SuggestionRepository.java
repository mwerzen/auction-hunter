package com.mikewerzen.wow.auctionminer.repository;

import com.mikewerzen.wow.auctionminer.entity.AggregateData;
import com.mikewerzen.wow.auctionminer.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long>
{

}
