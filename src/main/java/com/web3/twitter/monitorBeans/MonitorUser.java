package com.web3.twitter.monitorBeans;


/**
 * 推特用户数据解析bean
 */
public class MonitorUser {

    /**
     * 用户名称
     */
    private String userID;

    /**
     * 监控用户类型
     */
    private String userType;

    /**
     * 监控列表id
     */
    private String userName;

    /**
     * 粉丝数
     */
    private String fansNumber;

    /**
     * 是否认证
     */
    private String isCertified;

    /**
     * 对比账号
     */
    private String compareAccount;

    /**
     * 用户备注
     */
    private String userRemark;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFansNumber() {
        return fansNumber;
    }

    public void setFansNumber(String fansNumber) {
        this.fansNumber = fansNumber;
    }

    public String getIsCertified() {
        return isCertified;
    }

    public void setIsCertified(String isCertified) {
        this.isCertified = isCertified;
    }

    public String getCompareAccount() {
        return compareAccount;
    }

    public void setCompareAccount(String compareAccount) {
        this.compareAccount = compareAccount;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    @Override
    public String toString() {
        return "MonitorUser{" +
                "userID='" + userID + '\'' +
                ", userType='" + userType + '\'' +
                ", userName='" + userName + '\'' +
                ", fansNumber='" + fansNumber + '\'' +
                ", isCertified='" + isCertified + '\'' +
                ", compareAccount='" + compareAccount + '\'' +
                ", userRemark='" + userRemark + '\'' +
                '}';
    }
}
