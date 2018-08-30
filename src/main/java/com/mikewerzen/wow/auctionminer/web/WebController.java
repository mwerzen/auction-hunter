package com.mikewerzen.wow.auctionminer.web;

import com.mikewerzen.wow.auctionminer.entity.Suggestion;
import com.mikewerzen.wow.auctionminer.repository.SuggestionRepository;
import com.mikewerzen.wow.auctionminer.service.ItemService;
import com.mikewerzen.wow.auctionminer.service.SuggestionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class WebController
{
	private static final Logger logger = LogManager.getLogger(WebController.class);

	@Autowired
	private SuggestionRepository suggestionRepository;

	@Autowired
	private ItemService itemService;

	@RequestMapping("/")
	public String minerData(Map<String, Object> model)
	{
		List<Suggestion> allSuggestions = suggestionRepository.findAll();
		Collections.sort(allSuggestions);
		Collections.reverse(allSuggestions);
		List<SuggestionItemPairs> suggestionItemPairs = itemService.mergeItemsToSuggestions(allSuggestions);
		model.put("suggestions", suggestionItemPairs);
		logger.info("Found: " + allSuggestions.size() + " suggestions...");
		return "index";
	}

	@RequestMapping("/item/{itemId}")
	public String getItem(@PathVariable long itemId, Map<String, Object> model)
	{
		logger.info("Fetching Details for: " + itemId);
		itemService.fetchItemDetails(itemId);
		logger.info("Fetched");
		return minerData(model);
	}
}
