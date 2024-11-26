/**
  * Copyright 2024 bejson.com 
  */
package com.web3.twitter.twitterBeans;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.List;

/**
 * Auto-generated: 2024-11-24 18:56:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UserLegacy {

    private boolean following;
    private boolean can_dm;
    private boolean can_media_tag;
    private String created_at;
    private boolean default_profile;
    private boolean default_profile_image;
    private String description;

    @JSONField(name = "entities")
    private Entities entities;
    private int fast_followers_count;
    private long favourites_count;
    private long followers_count;
    private int friends_count;
    private boolean has_custom_timelines;
    private boolean is_translator;
    private int listed_count;
    private String location;
    private int media_count;
    private String name;
    private long normal_followers_count;
    private List<String> pinned_tweet_ids_str;
    private boolean possibly_sensitive;
    private String profile_image_url_https;
    private String profile_interstitial_type;
    private String screen_name;
    private int statuses_count;
    private String translator_type;
    private boolean verified;
    private boolean want_retweets;
    private List<String> withheld_in_countries;
    public void setFollowing(boolean following) {
        this.following = following;
    }
    public boolean getFollowing() {
        return following;
    }

    public void setCan_dm(boolean can_dm) {
        this.can_dm = can_dm;
    }
    public boolean getCan_dm() {
        return can_dm;
    }

    public void setCan_media_tag(boolean can_media_tag) {
        this.can_media_tag = can_media_tag;
    }
    public boolean getCan_media_tag() {
        return can_media_tag;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public String getCreated_at() {
        return created_at;
    }

    public void setDefault_profile(boolean default_profile) {
        this.default_profile = default_profile;
    }
    public boolean getDefault_profile() {
        return default_profile;
    }

    public void setDefault_profile_image(boolean default_profile_image) {
        this.default_profile_image = default_profile_image;
    }
    public boolean getDefault_profile_image() {
        return default_profile_image;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }
    public Entities getEntities() {
        return entities;
    }

    public void setFast_followers_count(int fast_followers_count) {
        this.fast_followers_count = fast_followers_count;
    }
    public int getFast_followers_count() {
        return fast_followers_count;
    }

    public void setFavourites_count(long favourites_count) {
        this.favourites_count = favourites_count;
    }
    public long getFavourites_count() {
        return favourites_count;
    }

    public void setFollowers_count(long followers_count) {
        this.followers_count = followers_count;
    }
    public long getFollowers_count() {
        return followers_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }
    public int getFriends_count() {
        return friends_count;
    }

    public void setHas_custom_timelines(boolean has_custom_timelines) {
        this.has_custom_timelines = has_custom_timelines;
    }
    public boolean getHas_custom_timelines() {
        return has_custom_timelines;
    }

    public void setIs_translator(boolean is_translator) {
        this.is_translator = is_translator;
    }
    public boolean getIs_translator() {
        return is_translator;
    }

    public void setListed_count(int listed_count) {
        this.listed_count = listed_count;
    }
    public int getListed_count() {
        return listed_count;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return location;
    }

    public void setMedia_count(int media_count) {
        this.media_count = media_count;
    }
    public int getMedia_count() {
        return media_count;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setNormal_followers_count(long normal_followers_count) {
        this.normal_followers_count = normal_followers_count;
    }
    public long getNormal_followers_count() {
        return normal_followers_count;
    }

    public void setPinned_tweet_ids_str(List<String> pinned_tweet_ids_str) {
        this.pinned_tweet_ids_str = pinned_tweet_ids_str;
    }
    public List<String> getPinned_tweet_ids_str() {
        return pinned_tweet_ids_str;
    }

    public void setPossibly_sensitive(boolean possibly_sensitive) {
        this.possibly_sensitive = possibly_sensitive;
    }
    public boolean getPossibly_sensitive() {
        return possibly_sensitive;
    }

    public void setProfile_image_url_https(String profile_image_url_https) {
        this.profile_image_url_https = profile_image_url_https;
    }
    public String getProfile_image_url_https() {
        return profile_image_url_https;
    }

    public void setProfile_interstitial_type(String profile_interstitial_type) {
        this.profile_interstitial_type = profile_interstitial_type;
    }
    public String getProfile_interstitial_type() {
        return profile_interstitial_type;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }
    public String getScreen_name() {
        return screen_name;
    }

    public void setStatuses_count(int statuses_count) {
        this.statuses_count = statuses_count;
    }
    public int getStatuses_count() {
        return statuses_count;
    }

    public void setTranslator_type(String translator_type) {
        this.translator_type = translator_type;
    }
    public String getTranslator_type() {
        return translator_type;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    public boolean getVerified() {
        return verified;
    }

    public void setWant_retweets(boolean want_retweets) {
        this.want_retweets = want_retweets;
    }
    public boolean getWant_retweets() {
        return want_retweets;
    }

    public void setWithheld_in_countries(List<String> withheld_in_countries) {
        this.withheld_in_countries = withheld_in_countries;
    }
    public List<String> getWithheld_in_countries() {
        return withheld_in_countries;
    }
}