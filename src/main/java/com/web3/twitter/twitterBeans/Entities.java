/**
  * Copyright 2024 bejson.com 
  */
package com.web3.twitter.twitterBeans;
import java.util.List;

/**
 * Auto-generated: 2024-11-29 3:31:45
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Entities {

    private List<Symbols> symbols;
    private List<Urls> urls;
    private List<User_mentions> user_mentions;
    public void setSymbols(List<Symbols> symbols) {
         this.symbols = symbols;
     }
     public List<Symbols> getSymbols() {
         return symbols;
     }

    public void setUrls(List<Urls> urls) {
         this.urls = urls;
     }
     public List<Urls> getUrls() {
         return urls;
     }

    public void setUser_mentions(List<User_mentions> user_mentions) {
         this.user_mentions = user_mentions;
     }
     public List<User_mentions> getUser_mentions() {
         return user_mentions;
     }

}