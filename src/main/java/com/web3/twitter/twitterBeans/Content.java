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
public class Content {

    private String __typename;
    private Client_event_info client_event_info;
    private ItemContent content;
    public void set__typename(String __typename) {
         this.__typename = __typename;
     }
     public String get__typename() {
         return __typename;
     }

    public void setClient_event_info(Client_event_info client_event_info) {
         this.client_event_info = client_event_info;
     }
     public Client_event_info getClient_event_info() {
         return client_event_info;
     }

    public void setContent(ItemContent content) {
         this.content = content;
     }
     public ItemContent getContent() {
         return content;
     }

}