/**
  * Copyright 2024 bejson.com 
  */
package com.web3.twitter.twitterBeans;

/**
 * Auto-generated: 2024-11-24 18:56:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ItemContent {

    private String itemType;
    private String __typename;
    private Tweet_results tweet_results;
    private String tweetDisplayType;
    public void setItemType(String itemType) {
         this.itemType = itemType;
     }
     public String getItemType() {
         return itemType;
     }

    public void set__typename(String __typename) {
         this.__typename = __typename;
     }
     public String get__typename() {
         return __typename;
     }

    public void setTweet_results(Tweet_results tweet_results) {
         this.tweet_results = tweet_results;
     }
     public Tweet_results getTweet_results() {
         return tweet_results;
     }

    public void setTweetDisplayType(String tweetDisplayType) {
         this.tweetDisplayType = tweetDisplayType;
     }
     public String getTweetDisplayType() {
         return tweetDisplayType;
     }

}