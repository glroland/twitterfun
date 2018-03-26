package com.glroland.twitterfun.tweetingest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class TweetIngestApplication implements CommandLineRunner {
	
	@Autowired
	private TweetListenerService tweetListenerService;
	
	@Autowired
	private TweetPublisherService tweetPublisherService;

	public static void main(String[] args) {
		SpringApplication.run(TweetIngestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		TweetIngestHandlerImpl handler = new TweetIngestHandlerImpl();
		tweetListenerService.start(handler);
	}
	
	public class TweetIngestHandlerImpl implements TweetHandler
	{

		@Override
		public void onTweet(Tweet tweet) {
			System.out.println(tweet.getText());
			tweetPublisherService.publish(tweet);
		}
		
	}
}
