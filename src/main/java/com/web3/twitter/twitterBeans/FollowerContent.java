package com.web3.twitter.twitterBeans;

public class FollowerContent {

    private String __typename;
    private String entryType;
    private String cursorType;
    private String value;

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

    public String getCursorType() {
        return cursorType;
    }

    public void setCursorType(String cursorType) {
        this.cursorType = cursorType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FollowerContent{" +
                "__typename='" + __typename + '\'' +
                ", entryType='" + entryType + '\'' +
                ", cursorType='" + cursorType + '\'' +
                ", value='" + value + '\'' +
                ", itemContent=" + itemContent +
                '}';
    }
}
