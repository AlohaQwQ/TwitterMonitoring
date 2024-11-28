/**
  * Copyright 2024 bejson.com 
  */
package com.web3.twitter.twitterBeans;
import java.util.Date;
import java.util.List;

/**
 * Auto-generated: 2024-11-29 3:31:45
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Legacy {

    private int bookmark_count;
    private String conversation_id_str;
    private String created_at;
    private List<Integer> display_text_range;
    private Entities entities;
    private Extended_entities extended_entities;
    private int favorite_count;
    private String full_text;
    private String in_reply_to_status_id_str;
    private String lang;
    private boolean possibly_sensitive_editable;
    private int quote_count;
    private int reply_count;
    private int retweet_count;
    private String user_id_str;
    public void setBookmark_count(int bookmark_count) {
         this.bookmark_count = bookmark_count;
     }
     public int getBookmark_count() {
         return bookmark_count;
     }

    public void setConversation_id_str(String conversation_id_str) {
         this.conversation_id_str = conversation_id_str;
     }
     public String getConversation_id_str() {
         return conversation_id_str;
     }

    public void setCreated_at(String created_at) {
         this.created_at = created_at;
     }
     public String getCreated_at() {
         return created_at;
     }

    public void setDisplay_text_range(List<Integer> display_text_range) {
         this.display_text_range = display_text_range;
     }
     public List<Integer> getDisplay_text_range() {
         return display_text_range;
     }

    public void setEntities(Entities entities) {
         this.entities = entities;
     }
     public Entities getEntities() {
         return entities;
     }

    public void setExtended_entities(Extended_entities extended_entities) {
         this.extended_entities = extended_entities;
     }
     public Extended_entities getExtended_entities() {
         return extended_entities;
     }

    public void setFavorite_count(int favorite_count) {
         this.favorite_count = favorite_count;
     }
     public int getFavorite_count() {
         return favorite_count;
     }

    public void setFull_text(String full_text) {
         this.full_text = full_text;
     }
     public String getFull_text() {
         return full_text;
     }

    public void setIn_reply_to_status_id_str(String in_reply_to_status_id_str) {
         this.in_reply_to_status_id_str = in_reply_to_status_id_str;
     }
     public String getIn_reply_to_status_id_str() {
         return in_reply_to_status_id_str;
     }

    public void setLang(String lang) {
         this.lang = lang;
     }
     public String getLang() {
         return lang;
     }

    public void setPossibly_sensitive_editable(boolean possibly_sensitive_editable) {
         this.possibly_sensitive_editable = possibly_sensitive_editable;
     }
     public boolean getPossibly_sensitive_editable() {
         return possibly_sensitive_editable;
     }

    public void setQuote_count(int quote_count) {
         this.quote_count = quote_count;
     }
     public int getQuote_count() {
         return quote_count;
     }

    public void setReply_count(int reply_count) {
         this.reply_count = reply_count;
     }
     public int getReply_count() {
         return reply_count;
     }

    public void setRetweet_count(int retweet_count) {
         this.retweet_count = retweet_count;
     }
     public int getRetweet_count() {
         return retweet_count;
     }

    public void setUser_id_str(String user_id_str) {
         this.user_id_str = user_id_str;
     }
     public String getUser_id_str() {
         return user_id_str;
     }

}