package com.web3.twitter.twitterBeans;

import java.util.List;

public class FollowerEntries {

    private List<FollowerInfo> entries;

    private String type;

    public List<FollowerInfo> getEntries() {
        return entries;
    }

    public void setEntries(List<FollowerInfo> entries) {
        this.entries = entries;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
