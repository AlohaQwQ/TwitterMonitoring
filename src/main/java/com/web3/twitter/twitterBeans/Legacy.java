/**
  * Copyright 2024 bejson.com 
  */
package com.web3.twitter.twitterBeans;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.Date;
import java.util.List;

/**
 * Auto-generated: 2024-11-24 18:56:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Legacy {

    private int bookmark_count;
    private boolean bookmarked;

    @JSONField(format = "yy MM dd HH:mm:ss +ZZZZ yyyy")
    private Date created_at;

    private String conversation_id_str;
    private List<Integer> display_text_range;
    private Entities entities;
    private int favorite_count;
    private boolean favorited;
    private String full_text;
    private boolean is_quote_status;
    private String lang;
    private boolean possibly_sensitive;
    private boolean possibly_sensitive_editable;
    private int quote_count;
    private String quoted_status_id_str;
    private Quoted_status_permalink quoted_status_permalink;
    private int reply_count;
    private int retweet_count;
    private boolean retweeted;
    private String user_id_str;
    private String id_str;
    public void setBookmark_count(int bookmark_count) {
         this.bookmark_count = bookmark_count;
     }
     public int getBookmark_count() {
         return bookmark_count;
     }

    public void setBookmarked(boolean bookmarked) {
         this.bookmarked = bookmarked;
     }
     public boolean getBookmarked() {
         return bookmarked;
     }

    public void setCreated_at(Date created_at) {
         this.created_at = created_at;
     }
     public Date getCreated_at() {
         return created_at;
     }

    public void setConversation_id_str(String conversation_id_str) {
         this.conversation_id_str = conversation_id_str;
     }
     public String getConversation_id_str() {
         return conversation_id_str;
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

    public void setFavorite_count(int favorite_count) {
         this.favorite_count = favorite_count;
     }
     public int getFavorite_count() {
         return favorite_count;
     }

    public void setFavorited(boolean favorited) {
         this.favorited = favorited;
     }
     public boolean getFavorited() {
         return favorited;
     }

    public void setFull_text(String full_text) {
         this.full_text = full_text;
     }
     public String getFull_text() {
         return full_text;
     }

    public void setIs_quote_status(boolean is_quote_status) {
         this.is_quote_status = is_quote_status;
     }
     public boolean getIs_quote_status() {
         return is_quote_status;
     }

    public void setLang(String lang) {
         this.lang = lang;
     }
     public String getLang() {
         return lang;
     }

    public void setPossibly_sensitive(boolean possibly_sensitive) {
         this.possibly_sensitive = possibly_sensitive;
     }
     public boolean getPossibly_sensitive() {
         return possibly_sensitive;
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

    public void setQuoted_status_id_str(String quoted_status_id_str) {
         this.quoted_status_id_str = quoted_status_id_str;
     }
     public String getQuoted_status_id_str() {
         return quoted_status_id_str;
     }

    public void setQuoted_status_permalink(Quoted_status_permalink quoted_status_permalink) {
         this.quoted_status_permalink = quoted_status_permalink;
     }
     public Quoted_status_permalink getQuoted_status_permalink() {
         return quoted_status_permalink;
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

    public void setRetweeted(boolean retweeted) {
         this.retweeted = retweeted;
     }
     public boolean getRetweeted() {
         return retweeted;
     }

    public void setUser_id_str(String user_id_str) {
         this.user_id_str = user_id_str;
     }
     public String getUser_id_str() {
         return user_id_str;
     }

    public void setId_str(String id_str) {
         this.id_str = id_str;
     }
     public String getId_str() {
         return id_str;
     }

}