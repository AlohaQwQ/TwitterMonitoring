package com.web3.twitter.monitorBeans;


import java.util.List;
import java.util.Set;

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
     * 用户等级
     */
    private String userLevel;

    /**
     * 共同关注用户名集合List
     */
    //private List<String> followersYouKnowList;

    /**
     * 共同关注用户名命中备注列表集合List
     */
    private Set<String> followersYouKnowRemarkSet;

    /**
     * ca发布历史记录
     */
    private Set<String> pumpHistorySet;

    /**
     * 黑名单
     */
    private String isBan;

    /**
     * 改名次数
     */
    private Integer numberNameChanges;

    /**
     * pump发推次数
     */
    private Integer numberPumpLaunch;

    /**
     * 删除推特次数
     */
    private Integer numberTwitterDelete;

    /**
     * 检索推特链接
     */
    private String twitterLinkUrl;

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

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public Set<String> getFollowersYouKnowRemarkSet() {
        return followersYouKnowRemarkSet;
    }

    public void setFollowersYouKnowRemarkSet(Set<String> followersYouKnowRemarkSet) {
        this.followersYouKnowRemarkSet = followersYouKnowRemarkSet;
    }

    public Set<String> getPumpHistorySet() {
        return pumpHistorySet;
    }

    public void setPumpHistorySet(Set<String> pumpHistorySet) {
        this.pumpHistorySet = pumpHistorySet;
    }

    public Integer getNumberNameChanges() {
        return numberNameChanges;
    }

    public void setNumberNameChanges(Integer numberNameChanges) {
        this.numberNameChanges = numberNameChanges;
    }

    public Integer getNumberPumpLaunch() {
        return numberPumpLaunch;
    }

    public void setNumberPumpLaunch(Integer numberPumpLaunch) {
        this.numberPumpLaunch = numberPumpLaunch;
    }

    public Integer getNumberTwitterDelete() {
        return numberTwitterDelete;
    }

    public void setNumberTwitterDelete(Integer numberTwitterDelete) {
        this.numberTwitterDelete = numberTwitterDelete;
    }

    public String getTwitterLinkUrl() {
        return twitterLinkUrl;
    }

    public void setTwitterLinkUrl(String twitterLinkUrl) {
        this.twitterLinkUrl = twitterLinkUrl;
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
                ", userLevel='" + userLevel + '\'' +
//                ", followersYouKnowList=" + followersYouKnowList +
                ", followersYouKnowRemarkSet=" + followersYouKnowRemarkSet +
                ", isBan='" + isBan + '\'' +
                ", numberNameChanges='" + numberNameChanges + '\'' +
                ", numberPumpLaunch='" + numberPumpLaunch + '\'' +
                ", numberTwitterDelete='" + numberTwitterDelete + '\'' +
                ", twitterLinkUrl='" + twitterLinkUrl + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
