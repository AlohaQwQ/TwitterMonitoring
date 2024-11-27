package com.web3.twitter.monitorBeans;


/**
 * 监控任务Bean
 */
public class MonitorTask {

    /**
     * 名称
     */
    private String name;

    /**
     * 监控列表地址
     */
    private String listUrl;

    /**
     * 任务类型
     */
    private String taskType;

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

    public String getListUrl() {
        return listUrl;
    }

    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "MonitorTask{" +
                "name='" + name + '\'' +
                ", listUrl='" + listUrl + '\'' +
                ", taskType='" + taskType + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
