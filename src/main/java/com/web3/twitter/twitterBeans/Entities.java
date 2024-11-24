/**
  * Copyright 2024 bejson.com 
  */
package com.web3.twitter.twitterBeans;
import java.util.List;

/**
 * Auto-generated: 2024-11-24 18:56:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Entities {

    private List<String> hashtags;
    private List<Symbols> symbols;
    private List<String> timestamps;
    private List<Urls> urls;
    private List<String> user_mentions;
    public void setHashtags(List<String> hashtags) {
         this.hashtags = hashtags;
     }
     public List<String> getHashtags() {
         return hashtags;
     }

    public void setSymbols(List<Symbols> symbols) {
         this.symbols = symbols;
     }
     public List<Symbols> getSymbols() {
         return symbols;
     }

    public void setTimestamps(List<String> timestamps) {
         this.timestamps = timestamps;
     }
     public List<String> getTimestamps() {
         return timestamps;
     }

    public void setUrls(List<Urls> urls) {
         this.urls = urls;
     }
     public List<Urls> getUrls() {
         return urls;
     }

    public void setUser_mentions(List<String> user_mentions) {
         this.user_mentions = user_mentions;
     }
     public List<String> getUser_mentions() {
         return user_mentions;
     }

}