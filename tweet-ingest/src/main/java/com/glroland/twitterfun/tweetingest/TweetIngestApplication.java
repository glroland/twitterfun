package com.glroland.twitterfun.tweetingest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class TweetIngestApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(TweetIngestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
