package com.web3.twitter.monitorBeans;


/**
 * 监控来源Bean
 */
public class MonitorOrigin {

    /**
     * 名称
     */
    private String name;

    /**
     * 监控列表id
     */
    private String listId;

    /**
     * 创建时间
     */
    private String createDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "MonitorOrigin{" +
                "name='" + name + '\'' +
                ", listId='" + listId + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
