package com.web3.twitter.twitterBeans;

public class FollowerContent {

    private String __typename;
    private String entryType;
    private FollowerItemContent itemContent;

    public String get__typename() {
        return __typename;
    }

    public void set__typename(String __typename) {
        this.__typename = __typename;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public FollowerItemContent getItemContent() {
        return itemContent;
    }

    public void setItemContent(FollowerItemContent itemContent) {
        this.itemContent = itemContent;
    }
}
