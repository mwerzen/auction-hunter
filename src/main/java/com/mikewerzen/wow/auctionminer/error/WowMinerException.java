package com.mikewerzen.wow.auctionminer.error;

public class WowMinerException extends RuntimeException
{
	public WowMinerException()
	{
	}

	public WowMinerException(String message)
	{
		super(message);
	}

	public WowMinerException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public WowMinerException(Throwable cause)
	{
		super(cause);
	}

	public WowMinerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
