package com.mikewerzen.wow.auctionminer.entity;

import com.mikewerzen.wow.auctionminer.util.AuctionUtil;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AggregateData
{
	@Id
	public long itemId;

	public long amountOffered;
	public long averageOffered;
	public long standardDeviationOffered;
	public long minOffered;
	public long p25Offered;
	public long medianOffered;
	public long p75Offered;
	public long maxOffered;


	public long amountSold;
	public double soldPercentage;
	public long averageSold;
	public long standardDeviationSold;
	public long minSold;
	public long p25Sold;
	public long medianSold;
	public long p75Sold;
	public long maxSold;


	public double salesPerHour;

	@Override public String toString()
	{
		return "AggregateData{" +
				"itemId=" + itemId +
				", amountOffered=" + amountOffered +
				", averageOffered=" + averageOffered +
				", standardDeviationOffered=" + standardDeviationOffered +
				", minOffered=" + minOffered +
				", p25Offered=" + p25Offered +
				", medianOffered=" + medianOffered +
				", p75Offered=" + p75Offered +
				", maxOffered=" + maxOffered +
				", amountSold=" + amountSold +
				", soldPercentage=" + soldPercentage +
				", averageSold=" + averageSold +
				", standardDeviationSold=" + standardDeviationSold +
				", minSold=" + minSold +
				", p25Sold=" + p25Sold +
				", medianSold=" + medianSold +
				", p75Sold=" + p75Sold +
				", maxSold=" + maxSold +
				", salesPerHour=" + salesPerHour +
				'}';
	}

	public String getPricingText()
	{
		return "<br \\>" + "min: " + AuctionUtil.getWowPriceFormat(minSold) + "<br \\>"
				+ "p25: " + AuctionUtil.getWowPriceFormat(p25Sold) + "<br \\>"
				+ "med: " + AuctionUtil.getWowPriceFormat(medianSold) + "<br \\>"
				+ "p75: " + AuctionUtil.getWowPriceFormat(p75Sold) + "<br \\>"
				+ "max: " + AuctionUtil.getWowPriceFormat(maxSold);
	}
}
