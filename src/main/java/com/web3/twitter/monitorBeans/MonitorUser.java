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
     * 用户名称
     */
    private String userName;

    /**
     * 用户自定义昵称
     */
    private String userShowName;

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

    /**
     * 用户分组
     */
    private String userGroup;

    /**
     * 黑名单
     */
    private String isBan;

    /**
     * 更新用户信息时间-时间戳
     */
    private String updateTime;

    /**
     * 创建用户信息时间-时间戳
     */
    private String createTime;

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

    public String getUserShowName() {
        return userShowName;
    }

    public void setUserShowName(String userShowName) {
        this.userShowName = userShowName;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getIsBan() {
        return isBan;
    }

    public void setIsBan(String isBan) {
        this.isBan = isBan;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MonitorUser{" +
                "userID='" + userID + '\'' +
                ", userType='" + userType + '\'' +
                ", userName='" + userName + '\'' +
                ", userShowName='" + userShowName + '\'' +
                ", fansNumber='" + fansNumber + '\'' +
                ", isCertified='" + isCertified + '\'' +
                ", compareAccount='" + compareAccount + '\'' +
                ", userRemark='" + userRemark + '\'' +
                ", userGroup='" + userGroup + '\'' +
                ", isBan='" + isBan + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
