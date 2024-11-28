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
public class Core_User_Result {

    private String screen_name;
    private String created_at;
    private String name;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreen_name(String screen_name) {
         this.screen_name = screen_name;
     }
     public String getScreen_name() {
         return screen_name;
     }

    @Override
    public String toString() {
        return "Core_User_Result{" +
                "screen_name='" + screen_name + '\'' +
                ", created_at='" + created_at + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}