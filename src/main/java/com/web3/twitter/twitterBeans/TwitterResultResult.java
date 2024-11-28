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
public class TwitterResultResult {

    private String __typename;
    private Core_Tweet_Result core;
    private boolean is_translatable;
    private Legacy legacy;
    private String rest_id;
    private Unmention_data unmention_data;
    private Edit_control edit_control;
    private View_count_info view_count_info;
    private Reply_to_user_results reply_to_user_results;
    public void set__typename(String __typename) {
         this.__typename = __typename;
     }
     public String get__typename() {
         return __typename;
     }

    public Core_Tweet_Result getCore() {
        return core;
    }

    public void setCore(Core_Tweet_Result core) {
        this.core = core;
    }

    public boolean isIs_translatable() {
        return is_translatable;
    }

    public void setIs_translatable(boolean is_translatable) {
         this.is_translatable = is_translatable;
     }
     public boolean getIs_translatable() {
         return is_translatable;
     }

    public void setLegacy(Legacy legacy) {
         this.legacy = legacy;
     }
     public Legacy getLegacy() {
         return legacy;
     }

    public void setRest_id(String rest_id) {
         this.rest_id = rest_id;
     }
     public String getRest_id() {
         return rest_id;
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

    public void setView_count_info(View_count_info view_count_info) {
         this.view_count_info = view_count_info;
     }
     public View_count_info getView_count_info() {
         return view_count_info;
     }

    public void setReply_to_user_results(Reply_to_user_results reply_to_user_results) {
         this.reply_to_user_results = reply_to_user_results;
     }
     public Reply_to_user_results getReply_to_user_results() {
         return reply_to_user_results;
     }

}