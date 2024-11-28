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
public class Timeline {

    private String id;
    private List<Instructions> instructions;
    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setInstructions(List<Instructions> instructions) {
         this.instructions = instructions;
     }
     public List<Instructions> getInstructions() {
         return instructions;
     }

}