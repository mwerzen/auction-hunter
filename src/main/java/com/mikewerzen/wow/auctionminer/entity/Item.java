package com.mikewerzen.wow.auctionminer.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Item
{
	@Id
	private long itemId;

	private String itemName;
	private long itemLevel;
	private long itemQuality;
	private long requiredLevel;
	private long buyPrice;
	private long sellPrice;

	public long getItemId()
	{
		return itemId;
	}

	public void setItemId(long itemId)
	{
		this.itemId = itemId;
	}

	public String getItemName()
	{
		return itemName;
	}

	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}

	public long getItemLevel()
	{
		return itemLevel;
	}

	public void setItemLevel(long itemLevel)
	{
		this.itemLevel = itemLevel;
	}

	public long getItemQuality()
	{
		return itemQuality;
	}

	public void setItemQuality(long itemQuality)
	{
		this.itemQuality = itemQuality;
	}

	public long getRequiredLevel()
	{
		return requiredLevel;
	}

	public void setRequiredLevel(long requiredLevel)
	{
		this.requiredLevel = requiredLevel;
	}

	public long getBuyPrice()
	{
		return buyPrice;
	}

	public void setBuyPrice(long buyPrice)
	{
		this.buyPrice = buyPrice;
	}

	public long getSellPrice()
	{
		return sellPrice;
	}

	public void setSellPrice(long sellPrice)
	{
		this.sellPrice = sellPrice;
	}
}
