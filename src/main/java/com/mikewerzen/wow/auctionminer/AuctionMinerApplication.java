package com.mikewerzen.wow.auctionminer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mikewerzen", "org.tessatech" })
public class AuctionMinerApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(AuctionMinerApplication.class, args);
	}
}
