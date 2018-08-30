package com.mikewerzen.wow.auctionminer.entity;

import com.mikewerzen.wow.auctionminer.util.AuctionUtil;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Suggestion implements Comparable<Suggestion>
{

	@Id
	public long itemId;

	public String text;
	public boolean buyout;
	public long estimatedGoldUsage;
	public long estimatedSalePrice;
	public long estimatedSaleTimeInHours;
	public long dataPoints;
	public long profit;
	public double hourlyROR;
	public long risk;

	public void updateHourlyROR()
	{
		hourlyROR = (((double) (estimatedSalePrice - estimatedGoldUsage) / (double) estimatedGoldUsage) / (double)
				estimatedSaleTimeInHours) * 100.0;
	}


	@Override public String toString()
	{
		return "Suggestion{" +
				"itemId=" + itemId +
				", buyout=" + buyout +
				", estimatedGoldUsage=" + estimatedGoldUsage +
				", estimatedSalePrice=" + estimatedSalePrice +
				", estimatedSaleTimeInHours=" + estimatedSaleTimeInHours +
				", hourlyROR=" + hourlyROR +
				", risk=" + risk +
				", text='" + text + '\'' +
				'}';
	}

	@Override public int compareTo(Suggestion o)
	{
		return Double.compare(hourlyROR, o.hourlyROR);
	}

	public String getSalePrice()
	{
		return AuctionUtil.getWowPriceFormat(estimatedSalePrice);
	}

	public String getCost()
	{
		return AuctionUtil.getWowPriceFormat(estimatedGoldUsage);
	}

	public String getHourlyROR()
	{
		return String.format("%.2f", hourlyROR) + "%";
	}

}
