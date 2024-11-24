/**
  * Copyright 2024 bejson.com 
  */
package com.web3.twitter.twitterBeans;
import java.util.List;

/**
 * Auto-generated: 2024-11-24 18:56:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Edit_control {

    private List<String> edit_tweet_ids;
    private String editable_until_msecs;
    private boolean is_edit_eligible;
    private String edits_remaining;
    public void setEdit_tweet_ids(List<String> edit_tweet_ids) {
         this.edit_tweet_ids = edit_tweet_ids;
     }
     public List<String> getEdit_tweet_ids() {
         return edit_tweet_ids;
     }

    public void setEditable_until_msecs(String editable_until_msecs) {
         this.editable_until_msecs = editable_until_msecs;
     }
     public String getEditable_until_msecs() {
         return editable_until_msecs;
     }

    public void setIs_edit_eligible(boolean is_edit_eligible) {
         this.is_edit_eligible = is_edit_eligible;
     }
     public boolean getIs_edit_eligible() {
         return is_edit_eligible;
     }

    public void setEdits_remaining(String edits_remaining) {
         this.edits_remaining = edits_remaining;
     }
     public String getEdits_remaining() {
         return edits_remaining;
     }

}