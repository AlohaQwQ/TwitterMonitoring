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
public class Core_Result {

    private String __typename;
    private String rest_id;
    private Core core;
    private Card card;
    private Unmention_data unmention_data;
    private Edit_control edit_control;
    private boolean is_translatable;
    private Views views;
    private String source;
    private Quoted_status_result quoted_status_result;
    private UserLegacy legacy;
    public void set__typename(String __typename) {
         this.__typename = __typename;
     }
     public String get__typename() {
         return __typename;
     }

    public void setRest_id(String rest_id) {
         this.rest_id = rest_id;
     }
     public String getRest_id() {
         return rest_id;
     }

    public void setCore(Core core) {
         this.core = core;
     }
     public Core getCore() {
         return core;
     }

    public void setCard(Card card) {
         this.card = card;
     }
     public Card getCard() {
         return card;
     }

    public void setUnmention_data(Unmention_data unmention_data) {
         this.unmention_data = unmention_data;
     }
     public Unmention_data getUnmention_data() {
         return unmention_data;
     }

    public void setEdit_control(Edit_control edit_control) {
         this.edit_control = edit_control;
     }
     public Edit_control getEdit_control() {
         return edit_control;
     }

    public void setIs_translatable(boolean is_translatable) {
         this.is_translatable = is_translatable;
     }
     public boolean getIs_translatable() {
         return is_translatable;
     }

    public void setViews(Views views) {
         this.views = views;
     }
     public Views getViews() {
         return views;
     }

    public void setSource(String source) {
         this.source = source;
     }
     public String getSource() {
         return source;
     }

    public void setQuoted_status_result(Quoted_status_result quoted_status_result) {
         this.quoted_status_result = quoted_status_result;
     }
     public Quoted_status_result getQuoted_status_result() {
         return quoted_status_result;
     }

    public void setLegacy(UserLegacy legacy) {
         this.legacy = legacy;
     }
     public UserLegacy getLegacy() {
         return legacy;
     }

}