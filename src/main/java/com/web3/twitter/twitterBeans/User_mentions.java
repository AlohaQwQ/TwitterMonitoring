/**
  * Copyright 2024 bejson.com 
  */
package com.web3.twitter.twitterBeans;
import java.util.List;

/**
 * Auto-generated: 2024-11-29 3:31:45
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class User_mentions {

    private String id_str;
    private List<Integer> indices;
    private String name;
    private String screen_name;
    public void setId_str(String id_str) {
         this.id_str = id_str;
     }
     public String getId_str() {
         return id_str;
     }

    public void setIndices(List<Integer> indices) {
         this.indices = indices;
     }
     public List<Integer> getIndices() {
         return indices;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setScreen_name(String screen_name) {
         this.screen_name = screen_name;
     }
     public String getScreen_name() {
         return screen_name;
     }

}