package com.glroland.twitterfun.tweetingest;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties
@Component
public class IngestConfig {

	private String twitterConsumerKey;
	private String twitterConsumerSecret;
	private String twitterAccessToken;
	private String twitterAccessTokenSecret;
	private String twitterClientName;
	private int twitterMessageQueueSize;
	private int twitterEventQueueSize;
	private String kafkaBootstrapServers;
	private String kafkaTopic;

	@Value("#{'${twitterTerms}'.split(',')}")
	private List<String> twitterTerms;

	@Value("#{'${twitterLanguages}'.split(',')}")
	private List<String> twitterLanguages;

	public String getTwitterConsumerKey() {
		return twitterConsumerKey;
	}

	public void setTwitterConsumerKey(String twitterConsumerKey) {
		this.twitterConsumerKey = twitterConsumerKey;
	}

	public String getTwitterConsumerSecret() {
		return twitterConsumerSecret;
	}

	public void setTwitterConsumerSecret(String twitterConsumerSecret) {
		this.twitterConsumerSecret = twitterConsumerSecret;
	}

	public String getTwitterAccessToken() {
		return twitterAccessToken;
	}

	public void setTwitterAccessToken(String twitterAccessToken) {
		this.twitterAccessToken = twitterAccessToken;
	}

	public String getTwitterAccessTokenSecret() {
		return twitterAccessTokenSecret;
	}

	public void setTwitterAccessTokenSecret(String twitterAccessTokenSecret) {
		this.twitterAccessTokenSecret = twitterAccessTokenSecret;
	}

	public List<String> getTwitterTerms() {
		return twitterTerms;
	}

	public void setTwitterTerms(List<String> twitterTerms) {
		this.twitterTerms = twitterTerms;
	}

	public List<String> getTwitterLanguages() {
		return twitterLanguages;
	}

	public void setTwitterLanguages(List<String> twitterLanguages) {
		this.twitterLanguages = twitterLanguages;
	}

	public String getTwitterClientName() {
		return twitterClientName;
	}

	public void setTwitterClientName(String twitterClientName) {
		this.twitterClientName = twitterClientName;
	}

	public int getTwitterMessageQueueSize() {
		return twitterMessageQueueSize;
	}

	public void setTwitterMessageQueueSize(int twitterMessageQueueSize) {
		this.twitterMessageQueueSize = twitterMessageQueueSize;
	}

	public int getTwitterEventQueueSize() {
		return twitterEventQueueSize;
	}

	public void setTwitterEventQueueSize(int twitterEventQueueSize) {
		this.twitterEventQueueSize = twitterEventQueueSize;
	}

	public String getKafkaBootstrapServers() {
		return kafkaBootstrapServers;
	}

	public void setKafkaBootstrapServers(String kafkaBootstrapServers) {
		this.kafkaBootstrapServers = kafkaBootstrapServers;
	}

	public String getKafkaTopic() {
		return kafkaTopic;
	}

	public void setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}

	
}
