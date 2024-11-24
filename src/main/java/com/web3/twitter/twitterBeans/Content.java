/**
  * Copyright 2024 bejson.com 
  */
package com.web3.twitter.twitterBeans;

/**
 * Auto-generated: 2024-11-24 18:56:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Content {

    private String entryType;
    private String __typename;
    private ItemContent itemContent;
    private ClientEventInfo clientEventInfo;
    public void setEntryType(String entryType) {
         this.entryType = entryType;
     }
     public String getEntryType() {
         return entryType;
     }

    public void set__typename(String __typename) {
         this.__typename = __typename;
     }
     public String get__typename() {
         return __typename;
     }

    public void setItemContent(ItemContent itemContent) {
         this.itemContent = itemContent;
     }
     public ItemContent getItemContent() {
         return itemContent;
     }

    public void setClientEventInfo(ClientEventInfo clientEventInfo) {
         this.clientEventInfo = clientEventInfo;
     }
     public ClientEventInfo getClientEventInfo() {
         return clientEventInfo;
     }

}