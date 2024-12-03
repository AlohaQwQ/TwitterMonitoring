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
public class FollowerItemContent {

    private String __typename;
    private String itemType;
    private String userDisplayType;

    private FollowerUserResults user_results;

    public String get__typename() {
        return __typename;
    }

    public void set__typename(String __typename) {
        this.__typename = __typename;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getUserDisplayType() {
        return userDisplayType;
    }

    public void setUserDisplayType(String userDisplayType) {
        this.userDisplayType = userDisplayType;
    }

    public FollowerUserResults getUser_results() {
        return user_results;
    }

    public void setUser_results(FollowerUserResults user_results) {
        this.user_results = user_results;
    }

    @Override
    public String toString() {
        return "FollowerItemContent{" +
                "__typename='" + __typename + '\'' +
                ", itemType='" + itemType + '\'' +
                ", userDisplayType='" + userDisplayType + '\'' +
                ", user_results=" + user_results +
                '}';
    }
}