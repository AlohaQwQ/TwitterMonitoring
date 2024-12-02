package com.web3.twitter.monitorBeans;


import java.util.List;

/**
 * 推特数据解析bean
 */
public class MonitorCoin {

    /**
     * 代币名称
     */
    private String coinName;

    /**
     * 代币ca
     */
    private String coinCa;

    /**
     * 代币被用户提及列表
     */
    private List<String> mentionUserList;

    /**
     * 代币创建时间
     */
    private String createDate;

    /**
     * 代币全称
     */
    private String coinFullName;

    /**
     * 代币市值
     */
    private String marketValue;

    /**
     * 代币进度
     */
    private String coinLaunchpad;

    /**
     * 代币信息
     */
    private String coinDetail;

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinCa() {
        return coinCa;
    }

    public void setCoinCa(String coinCa) {
        this.coinCa = coinCa;
    }

    public List<String> getMentionUserList() {
        return mentionUserList;
    }

    public void setMentionUserList(List<String> mentionUserList) {
        this.mentionUserList = mentionUserList;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCoinFullName() {
        return coinFullName;
    }

    public void setCoinFullName(String coinFullName) {
        this.coinFullName = coinFullName;
    }

    public String getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(String marketValue) {
        this.marketValue = marketValue;
    }

    public String getCoinLaunchpad() {
        return coinLaunchpad;
    }

    public void setCoinLaunchpad(String coinLaunchpad) {
        this.coinLaunchpad = coinLaunchpad;
    }

    public String getCoinDetail() {
        return coinDetail;
    }

    public void setCoinDetail(String coinDetail) {
        this.coinDetail = coinDetail;
    }

    @Override
    public String toString() {
        return "MonitorCoin{" +
                "coinName='" + coinName + '\'' +
                ", coinCa='" + coinCa + '\'' +
                ", mentionUserList=" + mentionUserList +
                ", createDate='" + createDate + '\'' +
                ", coinFullName='" + coinFullName + '\'' +
                ", marketValue='" + marketValue + '\'' +
                ", coinLaunchpad='" + coinLaunchpad + '\'' +
                ", coinDetail='" + coinDetail + '\'' +
                '}';
    }
}
