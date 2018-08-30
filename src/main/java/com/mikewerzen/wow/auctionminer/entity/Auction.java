package com.mikewerzen.wow.auctionminer.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Auction implements Comparable<Auction>
{

	@Id
	private long auctionId;

	private long itemId;
	private String owner;
	private String realm;
	private long startingBid;
	private long startingBidPerUnit;
	private long currentBid;
	private long currentBidPerUnit;
	private long numberOfBids;
	private long buyoutPrice;
	private long buyoutPricePerUnit;
	private long quantity;
	private long estimatedFinishDate;
	private boolean completed = false;
	private boolean sold = false;
	private boolean boughtOut = false;
	private long salePrice;
	private long salePricePerUnit;
	private boolean processedThisTick = false;

	public Auction()
	{

	}

	public long getAuctionId()
	{
		return auctionId;
	}

	public void setAuctionId(long auctionId)
	{
		this.auctionId = auctionId;
	}

	public long getItemId()
	{
		return itemId;
	}

	public void setItemId(long itemId)
	{
		this.itemId = itemId;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public String getRealm()
	{
		return realm;
	}

	public void setRealm(String realm)
	{
		this.realm = realm;
	}

	public long getStartingBid()
	{
		return startingBid;
	}

	public void setStartingBid(long startingBid)
	{
		this.startingBid = startingBid;
	}

	public long getStartingBidPerUnit()
	{
		return startingBidPerUnit;
	}

	public void setStartingBidPerUnit(long startingBidPerUnit)
	{
		this.startingBidPerUnit = startingBidPerUnit;
	}

	public long getCurrentBid()
	{
		return currentBid;
	}

	public void setCurrentBid(long currentBid)
	{
		this.currentBid = currentBid;
	}

	public long getCurrentBidPerUnit()
	{
		return currentBidPerUnit;
	}

	public void setCurrentBidPerUnit(long currentBidPerUnit)
	{
		this.currentBidPerUnit = currentBidPerUnit;
	}

	public long getNumberOfBids()
	{
		return numberOfBids;
	}

	public void setNumberOfBids(long numberOfBids)
	{
		this.numberOfBids = numberOfBids;
	}

	public long getBuyoutPrice()
	{
		return buyoutPrice;
	}

	public void setBuyoutPrice(long buyoutPrice)
	{
		this.buyoutPrice = buyoutPrice;
	}

	public long getBuyoutPricePerUnit()
	{
		return buyoutPricePerUnit;
	}

	public void setBuyoutPricePerUnit(long buyoutPricePerUnit)
	{
		this.buyoutPricePerUnit = buyoutPricePerUnit;
	}

	public long getQuantity()
	{
		return quantity;
	}

	public void setQuantity(long quantity)
	{
		this.quantity = quantity;
	}

	public long getEstimatedFinishDate()
	{
		return estimatedFinishDate;
	}

	public void setEstimatedFinishDate(long estimatedFinishDate)
	{
		this.estimatedFinishDate = estimatedFinishDate;
	}

	public boolean isSold()
	{
		return sold;
	}

	public void setSold(boolean sold)
	{
		this.sold = sold;
	}

	public boolean isBoughtOut()
	{
		return boughtOut;
	}

	public void setBoughtOut(boolean boughtOut)
	{
		this.boughtOut = boughtOut;
	}

	public long getSalePrice()
	{
		return salePrice;
	}

	public void setSalePrice(long salePrice)
	{
		this.salePrice = salePrice;
	}

	public long getSalePricePerUnit()
	{
		return salePricePerUnit;
	}

	public void setSalePricePerUnit(long salePricePerUnit)
	{
		this.salePricePerUnit = salePricePerUnit;
	}
	
	public boolean isCompleted()
	{
		return completed;
	}

	public void setCompleted(boolean completed)
	{
		this.completed = completed;
	}


	public boolean isProcessedThisTick()
	{
		return processedThisTick;
	}

	public void setProcessedThisTick(boolean processedThisTick)
	{
		this.processedThisTick = processedThisTick;
	}

	@Override public int compareTo(Auction o)
	{
		Long price = Math.max(currentBidPerUnit, buyoutPricePerUnit);
		Long otherPrice = Math.max(o.currentBidPerUnit, o.buyoutPricePerUnit);

		return price.compareTo(otherPrice);

	}
}
