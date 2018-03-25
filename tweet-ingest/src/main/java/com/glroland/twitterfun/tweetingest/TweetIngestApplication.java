package com.glroland.twitterfun.tweetingest;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Constants.FilterLevel;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

@SpringBootApplication
@EnableAutoConfiguration
public class TweetIngestApplication implements CommandLineRunner {

	@Autowired
	private IngestConfig config;

	public static void main(String[] args) {
		SpringApplication.run(TweetIngestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		/**
		 * Set up your blocking queues: Be sure to size these properly based on expected
		 * TPS of your stream
		 */
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
		BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

		/**
		 * Declare the host you want to connect to, the endpoint, and authentication
		 * (basic auth or oauth)
		 */
		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		// Optional: set up some followings and track terms
		List<Long> followings = Lists.newArrayList(1234L, 566788L);
		List<String> terms = Lists.newArrayList("red hat", "RHT", "redhat");
		hosebirdEndpoint.followings(followings);
		hosebirdEndpoint.trackTerms(terms);
		hosebirdEndpoint.filterLevel(FilterLevel.Low);
		hosebirdEndpoint.languages(Lists.newArrayList("en"));

		// These secrets should be read from a config file
		Authentication hosebirdAuth = new OAuth1(config.getConsumerKey(), config.getConsumerSecret(),
				config.getAccessToken(), config.getAccessTokenSecret());

		ClientBuilder builder = new ClientBuilder().name("Hosebird-Client-01") // optional: mainly for the logs
				.hosts(hosebirdHosts).authentication(hosebirdAuth).endpoint(hosebirdEndpoint)
				.processor(new StringDelimitedProcessor(msgQueue)).eventMessageQueue(eventQueue); // optional: use this
																									// if you want to
																									// process client
																									// events

		Client hosebirdClient = builder.build();
		// Attempts to establish a connection.
		hosebirdClient.connect();

		ObjectMapper objectMapper = new ObjectMapper();
		while (!hosebirdClient.isDone()) {
			String msg = msgQueue.take();
			Tweet tweet = objectMapper.readValue(msg, Tweet.class);
			System.out.println(tweet.getText());
		}

	}
}
