package com.mikewerzen.wow.auctionminer.util;

public class AuctionUtil
{

	public static long estimateFinishTime(String timeLeft, long currentPrediction)
	{
		long estimatedFinishTime = System.currentTimeMillis();

		if (timeLeft.equalsIgnoreCase("VERY_LONG"))
		{
			estimatedFinishTime += 48 * 60 * 60 * 1000;
		}
		else if (timeLeft.equalsIgnoreCase("LONG"))
		{
			estimatedFinishTime += 24 * 60 * 60 * 1000;
		}
		else if (timeLeft.equalsIgnoreCase("MEDIUM"))
		{
			estimatedFinishTime += 12 * 60 * 60 * 1000;
		}
		else
		{
			estimatedFinishTime += 30 * 60 * 1000;
		}

		return estimatedFinishTime;
	}

	public static String getWowPriceFormat(long price)
	{
		long gold = price / 10000;
		long silver = (price % 10000) / 100;
		long copper = (price % 100);

		String formatted = "";

		if(gold != 0)
		{
			formatted += gold + "g ";
		}

		if(silver != 0)
		{
			formatted += silver + "s ";
		}

		if(copper != 0)
		{
			formatted += copper + "c ";
		}


		return formatted;
	}
}
