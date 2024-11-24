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
public class Professional {

    private String rest_id;
    private String professional_type;
    private List<Category> category;
    public void setRest_id(String rest_id) {
         this.rest_id = rest_id;
     }
     public String getRest_id() {
         return rest_id;
     }

    public void setProfessional_type(String professional_type) {
         this.professional_type = professional_type;
     }
     public String getProfessional_type() {
         return professional_type;
     }

    public void setCategory(List<Category> category) {
         this.category = category;
     }
     public List<Category> getCategory() {
         return category;
     }

}