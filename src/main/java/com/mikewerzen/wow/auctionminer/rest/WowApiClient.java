package com.mikewerzen.wow.auctionminer.rest;

import com.mikewerzen.wow.auctionminer.rest.container.AuctionDataStatus;
import com.mikewerzen.wow.auctionminer.rest.container.AuctionList;
import com.mikewerzen.wow.auctionminer.rest.container.ApiItem;
import com.mikewerzen.wow.auctionminer.rest.container.WowError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.tessatech.tessa.framework.core.event.TessaEvent;
import org.tessatech.tessa.framework.core.exception.system.ExternalException;
import org.tessatech.tessa.framework.core.logging.external.ExternalCallAttributesBuilder;
import org.tessatech.tessa.framework.rest.client.AbstractRestClient;

@Service
public class WowApiClient extends AbstractRestClient<WowError>
{
	private static final Logger logger = LogManager.getLogger(WowApiClient.class);

	@Value("${wow.realm.id}")
	private String realmId;

	@Value("${wow.locale.id}")
	private String localeId;

	@Value("${wow.api.key}")
	private String apiKey;

	@Value("${wow.api.base.url}")
	private String baseApiUrl;

	@Value("${wow.api.item.url}")
	private String itemUrl;

	@Value("${wow.api.auction.url}")
	private String auctionUrl;

	public WowApiClient()
	{
		super("BattleNetAPIs", "WoW", "?", WowError.class);
	}

	@Override protected HttpHeaders buildHttpHeaders()
	{
		return new HttpHeaders();
	}

	@Override protected ExternalException convertErrorIntoException(WowError wowError)
	{
		logger.error("An error occurred");
		return new ExternalException("An Error Occurred");
	}

	@Override protected void addErrorAttributes(ExternalCallAttributesBuilder builder, WowError wowError)
	{

	}

	private String buildAuctionDataUrlQuery()
	{
		String url = baseApiUrl + auctionUrl;
		url = url.replace("{realm}", realmId);

		return addStandardParameters(url);
	}

	private String buildItemDataUrlQuery(long itemId)
	{
		String url = baseApiUrl + itemUrl;
		url = url.replace("{id}", String.valueOf(itemId));

		return addStandardParameters(url);
	}

	private String addStandardParameters(String url)
	{
		return UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("locale", localeId)
				.queryParam("apikey", apiKey)
				.toUriString();
	}

	@TessaEvent(eventName = "getAuctionData")
	public AuctionDataStatus getAuctionData()
	{
		logger.warn("Calling getAuctionData");
		return super.execute("getAuctionData", null, buildAuctionDataUrlQuery(), HttpMethod.GET, AuctionDataStatus.class);
	}

	@TessaEvent(eventName = "getAuctions")
	public AuctionList getAuctions(String auctionUrl)
	{
		logger.warn("Calling getAuctions: " + auctionUrl);
		return super.execute("getAuctions", null, auctionUrl, HttpMethod.GET, AuctionList.class);
	}

	@TessaEvent(eventName = "getItem")
	public ApiItem getItem(long itemId)
	{
		logger.warn("Calling getItem: " + itemId);
		return super.execute("getItem", null, buildItemDataUrlQuery(itemId), HttpMethod.GET, ApiItem.class);
	}

}
