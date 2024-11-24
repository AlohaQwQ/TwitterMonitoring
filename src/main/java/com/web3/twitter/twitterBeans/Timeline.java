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
public class Timeline {

    private List<Instructions> instructions;
    private Metadata metadata;
    public void setInstructions(List<Instructions> instructions) {
         this.instructions = instructions;
     }
     public List<Instructions> getInstructions() {
         return instructions;
     }

    public void setMetadata(Metadata metadata) {
         this.metadata = metadata;
     }
     public Metadata getMetadata() {
         return metadata;
     }

}