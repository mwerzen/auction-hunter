package com.mikewerzen.wow.auctionminer.web;

import com.mikewerzen.wow.auctionminer.entity.Item;
import com.mikewerzen.wow.auctionminer.entity.Suggestion;

public class SuggestionItemPairs
{
	public Suggestion suggestion;
	public Item item;

	public SuggestionItemPairs(Suggestion suggestion, Item item)
	{
		this.suggestion = suggestion;
		this.item = item;
	}

	public Suggestion getSuggestion()
	{
		return suggestion;
	}

	public void setSuggestion(Suggestion suggestion)
	{
		this.suggestion = suggestion;
	}

	public Item getItem()
	{
		return item;
	}

	public void setItem(Item item)
	{
		this.item = item;
	}
}
