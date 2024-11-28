/**
  * Copyright 2024 bejson.com 
  */
package com.web3.twitter.twitterBeans;

/**
 * Auto-generated: 2024-11-29 3:31:45
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ItemContent {

    private String __typename;
    private String timeline_tweet_display_type;
    private Tweet_results tweet_results;

    public void set__typename(String __typename) {
         this.__typename = __typename;
     }

     public String get__typename() {
         return __typename;
     }

    public String getTimeline_tweet_display_type() {
        return timeline_tweet_display_type;
    }

    public void setTimeline_tweet_display_type(String timeline_tweet_display_type) {
        this.timeline_tweet_display_type = timeline_tweet_display_type;
    }

    public Tweet_results getTweet_results() {
        return tweet_results;
    }

    public void setTweet_results(Tweet_results tweet_results) {
        this.tweet_results = tweet_results;
    }
}