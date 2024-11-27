package com.web3.twitter.monitorBeans;


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
     * 代币被提及次数
     */
    private String mentionNums;

    /**
     * 代币创建时间
     */
    private String createDate;

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

    public String getMentionNums() {
        return mentionNums;
    }

    public void setMentionNums(String mentionNums) {
        this.mentionNums = mentionNums;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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
                ", mentionNums='" + mentionNums + '\'' +
                ", createDate='" + createDate + '\'' +
                ", coinDetail='" + coinDetail + '\'' +
                '}';
    }
}