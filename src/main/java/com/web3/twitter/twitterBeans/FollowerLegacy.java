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
public class FollowerLegacy {

    private boolean canDm;
    private boolean canMediaTag;
    private String createdAt;
    private boolean defaultProfile;
    private boolean defaultProfileImage;
    private String description;
    private int fastFollowersCount;
    private int favouritesCount;
    private int followersCount;
    private boolean following;
    private int friendsCount;
    private boolean hasCustomTimelines;
    private boolean isTranslator;
    private int listedCount;
    private String location;
    private int mediaCount;
    private String name;
    private int normalFollowersCount;
    private List<String> pinnedTweetIdsStr;
    private boolean possiblySensitive;
    private String profileBannerUrl;
    private String profileImageUrlHttps;
    private String profileInterstitialType;

    /**
     * 用户名
     */
    private String screenName;
    private int statusesCount;
    private String translatorType;
    private String url;
    private boolean verified;
    private boolean wantRetweets;
    private List<String> withheldInCountries;

    public boolean isCanDm() {
        return canDm;
    }

    public void setCanDm(boolean canDm) {
        this.canDm = canDm;
    }

    public boolean isCanMediaTag() {
        return canMediaTag;
    }

    public void setCanMediaTag(boolean canMediaTag) {
        this.canMediaTag = canMediaTag;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDefaultProfile() {
        return defaultProfile;
    }

    public void setDefaultProfile(boolean defaultProfile) {
        this.defaultProfile = defaultProfile;
    }

    public boolean isDefaultProfileImage() {
        return defaultProfileImage;
    }

    public void setDefaultProfileImage(boolean defaultProfileImage) {
        this.defaultProfileImage = defaultProfileImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFastFollowersCount() {
        return fastFollowersCount;
    }

    public void setFastFollowersCount(int fastFollowersCount) {
        this.fastFollowersCount = fastFollowersCount;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(int favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public boolean isHasCustomTimelines() {
        return hasCustomTimelines;
    }

    public void setHasCustomTimelines(boolean hasCustomTimelines) {
        this.hasCustomTimelines = hasCustomTimelines;
    }

    public boolean isTranslator() {
        return isTranslator;
    }

    public void setTranslator(boolean translator) {
        isTranslator = translator;
    }

    public int getListedCount() {
        return listedCount;
    }

    public void setListedCount(int listedCount) {
        this.listedCount = listedCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMediaCount() {
        return mediaCount;
    }

    public void setMediaCount(int mediaCount) {
        this.mediaCount = mediaCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNormalFollowersCount() {
        return normalFollowersCount;
    }

    public void setNormalFollowersCount(int normalFollowersCount) {
        this.normalFollowersCount = normalFollowersCount;
    }

    public List<String> getPinnedTweetIdsStr() {
        return pinnedTweetIdsStr;
    }

    public void setPinnedTweetIdsStr(List<String> pinnedTweetIdsStr) {
        this.pinnedTweetIdsStr = pinnedTweetIdsStr;
    }

    public boolean isPossiblySensitive() {
        return possiblySensitive;
    }

    public void setPossiblySensitive(boolean possiblySensitive) {
        this.possiblySensitive = possiblySensitive;
    }

    public String getProfileBannerUrl() {
        return profileBannerUrl;
    }

    public void setProfileBannerUrl(String profileBannerUrl) {
        this.profileBannerUrl = profileBannerUrl;
    }

    public String getProfileImageUrlHttps() {
        return profileImageUrlHttps;
    }

    public void setProfileImageUrlHttps(String profileImageUrlHttps) {
        this.profileImageUrlHttps = profileImageUrlHttps;
    }

    public String getProfileInterstitialType() {
        return profileInterstitialType;
    }

    public void setProfileInterstitialType(String profileInterstitialType) {
        this.profileInterstitialType = profileInterstitialType;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    public void setStatusesCount(int statusesCount) {
        this.statusesCount = statusesCount;
    }

    public String getTranslatorType() {
        return translatorType;
    }

    public void setTranslatorType(String translatorType) {
        this.translatorType = translatorType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isWantRetweets() {
        return wantRetweets;
    }

    public void setWantRetweets(boolean wantRetweets) {
        this.wantRetweets = wantRetweets;
    }

    public List<String> getWithheldInCountries() {
        return withheldInCountries;
    }

    public void setWithheldInCountries(List<String> withheldInCountries) {
        this.withheldInCountries = withheldInCountries;
    }

    @Override
    public String toString() {
        return "FollowerLegacy{" +
                "canDm=" + canDm +
                ", canMediaTag=" + canMediaTag +
                ", createdAt='" + createdAt + '\'' +
                ", defaultProfile=" + defaultProfile +
                ", defaultProfileImage=" + defaultProfileImage +
                ", description='" + description + '\'' +
                ", fastFollowersCount=" + fastFollowersCount +
                ", favouritesCount=" + favouritesCount +
                ", followersCount=" + followersCount +
                ", following=" + following +
                ", friendsCount=" + friendsCount +
                ", hasCustomTimelines=" + hasCustomTimelines +
                ", isTranslator=" + isTranslator +
                ", listedCount=" + listedCount +
                ", location='" + location + '\'' +
                ", mediaCount=" + mediaCount +
                ", name='" + name + '\'' +
                ", normalFollowersCount=" + normalFollowersCount +
                ", pinnedTweetIdsStr=" + pinnedTweetIdsStr +
                ", possiblySensitive=" + possiblySensitive +
                ", profileBannerUrl='" + profileBannerUrl + '\'' +
                ", profileImageUrlHttps='" + profileImageUrlHttps + '\'' +
                ", profileInterstitialType='" + profileInterstitialType + '\'' +
                ", screenName='" + screenName + '\'' +
                ", statusesCount=" + statusesCount +
                ", translatorType='" + translatorType + '\'' +
                ", url='" + url + '\'' +
                ", verified=" + verified +
                ", wantRetweets=" + wantRetweets +
                ", withheldInCountries=" + withheldInCountries +
                '}';
    }
}