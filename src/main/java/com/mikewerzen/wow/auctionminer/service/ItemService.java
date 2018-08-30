package com.mikewerzen.wow.auctionminer.service;

import com.mikewerzen.wow.auctionminer.entity.Item;
import com.mikewerzen.wow.auctionminer.entity.Suggestion;
import com.mikewerzen.wow.auctionminer.repository.ItemRepository;
import com.mikewerzen.wow.auctionminer.rest.WowApiClient;
import com.mikewerzen.wow.auctionminer.rest.container.ApiItem;
import com.mikewerzen.wow.auctionminer.web.SuggestionItemPairs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService
{
	private static final Logger logger = LogManager.getLogger(ItemService.class);

	@Autowired
	private WowApiClient client;

	@Autowired
	private ItemRepository itemRepository;

	public void fetchItemDetails(long itemId)
	{
		ApiItem apiItem = client.getItem(itemId);
		Item item = mapItemFromApiResponse(apiItem);
		itemRepository.save(item);
	}

	public Item mapItemFromApiResponse(ApiItem apiItem)
	{
		Item item = new Item();
		item.setItemId(apiItem.id);
		item.setItemName(apiItem.name);
		item.setItemLevel(apiItem.itemLevel);
		item.setItemQuality(apiItem.quality);
		item.setRequiredLevel(apiItem.requiredLevel);
		item.setBuyPrice(apiItem.buyPrice);
		item.setSellPrice(apiItem.sellPrice);
		return item;
	}

	public List<SuggestionItemPairs> mergeItemsToSuggestions(List<Suggestion> suggestions)
	{
		List<SuggestionItemPairs> suggestionsWithItems = new ArrayList<>();

		for(Suggestion suggestion : suggestions)
		{
			Optional<Item> item = itemRepository.findById(suggestion.itemId);

			if(item.isPresent())
			{
				suggestionsWithItems.add(new SuggestionItemPairs(suggestion, item.get()));
			}
			else
			{
				suggestionsWithItems.add(new SuggestionItemPairs(suggestion, null));
			}
		}

		return suggestionsWithItems;
	}
}
