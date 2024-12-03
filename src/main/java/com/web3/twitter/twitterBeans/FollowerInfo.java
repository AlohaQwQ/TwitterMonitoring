package com.web3.twitter.twitterBeans;

public class FollowerInfo {

    private String entryId;

    private String sortIndex;

    private FollowerContent content;

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(String sortIndex) {
        this.sortIndex = sortIndex;
    }

    public FollowerContent getContent() {
        return content;
    }

    public void setContent(FollowerContent content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "FollowerInfo{" +
                "entryId='" + entryId + '\'' +
                ", sortIndex='" + sortIndex + '\'' +
                ", content=" + content +
                '}';
    }
}
