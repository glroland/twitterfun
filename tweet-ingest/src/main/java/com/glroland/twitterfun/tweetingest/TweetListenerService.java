package com.glroland.twitterfun.tweetingest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.Constants.FilterLevel;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

@Component
public class TweetListenerService {

	@Autowired
	private IngestConfig config;
	
	public void start(TweetHandler tweetHandler) throws Exception
	{
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(config.getTwitterMessageQueueSize());
		BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(config.getTwitterEventQueueSize());

		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		hosebirdEndpoint.trackTerms(config.getTwitterTerms());
		hosebirdEndpoint.filterLevel(FilterLevel.Low);
		hosebirdEndpoint.languages(config.getTwitterLanguages());

		// These secrets should be read from a config file
		Authentication hosebirdAuth = new OAuth1(config.getTwitterConsumerKey(), config.getTwitterConsumerSecret(),
				config.getTwitterAccessToken(), config.getTwitterAccessTokenSecret());

		ClientBuilder builder = new ClientBuilder().name(config.getTwitterClientName()).hosts(hosebirdHosts)
				.authentication(hosebirdAuth).endpoint(hosebirdEndpoint)
				.processor(new StringDelimitedProcessor(msgQueue)).eventMessageQueue(eventQueue);

		Client hosebirdClient = builder.build();
		hosebirdClient.connect();

		ObjectMapper objectMapper = new ObjectMapper();
		while (!hosebirdClient.isDone()) {
			String msg = msgQueue.take();
			Tweet tweet = objectMapper.readValue(msg, Tweet.class);
			tweetHandler.onTweet(tweet);
		}
	}
}
