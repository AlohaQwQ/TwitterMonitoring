package com.web3.twitter.monitorBeans;

/**
 * FourMeme数据解析bean
 * {
 * 	"code": 0,
 * 	"msg": "success",
 * 	"data": {
 * 		"id": 100099200,
 * 		"address": "0x8afc14fb1954f909c167bdf06c93ed4e1e51592b",
 * 		"image": "https://static.four.meme/market/aed0981a-7c69-4073-9fcc-c772f1ed97a72486581113748503645.jpg",
 * 		"name": "ZeroByte",
 * 		"shortName": "ZB",
 * 		"symbol": "BNB",
 * 		"descr": "ZeroByte is a new AI Agent created in Four.meme. It understands everything you say. The more you talk to him, the more his mind develops. This unique agent can answer you in any language.",
 * 		"webUrl": "https://www.zero-byte.net/",
 * 		"telegramUrl": "https://t.me/zerobyte_portal",
 * 		"twitterUrl": "https://x.com/ZeroByte_X",
 * 		"totalAmount": "1000000000",
 * 		"saleAmount": "800000000",
 * 		"b0": "8.219178082191780821917808219179",
 * 		"t0": "1073972602.739726027397168628310622210046",
 * 		"launchTime": 1739777602000,
 * 		"minBuy": "0",
 * 		"maxBuy": "0",
 * 		"userId": 100286521,
 * 		"userAddress": "0xfbae4c06169b8c8a5e1b111b17d8c76cfd2280eb",
 * 		"userName": "ZeroByte",
 * 		"userAvatar": "",
 * 		"status": "TRADE",
 * 		"showStatus": "SHOW",
 * 		"tradeUrl": "https://pancakeswap.finance/swap?chain=BSC&outputCurrency=BNB&inputCurrency=0x8afc14fb1954f909c167bdf06c93ed4e1e51592b",
 * 		"tokenPrice": {
 * 			"price": "0.0005487650293134444",
 * 			"maxPrice": "0.0000001176",
 * 			"increase": "14.3663999996",
 * 			"amount": "800000000",
 * 			"marketCap": "548765.0293134444",
 * 			"trading": "91.955486806883472016",
 * 			"dayIncrease": "580.92",
 * 			"dayTrading": "1836994.620435",
 * 			"raisedAmount": "23.999999999439999992",
 * 			"progress": "1",
 * 			"liquidity": "122571.687454",
 * 			"tamount": "1073972602.739726027397260273972602739727",
 * 			"bamount": "8.219178082191780821917808219179"
 * 		        },
 * 		"oscarStatus": "Hide",
 * 		"progressTag": true,
 * 		"ctoTag": false,
 * 		"version": "V3",
 * 		"clickFunCheck": false,
 * 		"reserveAmount": "0",
 * 		"raisedAmount": "24",
 * 		"networkCode": "BSC",
 * 		"label": "AI",
 * 		"createDate": "1739777705000",
 * 		"isFollow": false    * 	}
 * }
 */
public class MonitorFourMemeCoin {

    private long id;
    /**
     * 合约地址
     */
    private String address;
    /**
     * 代币Logo
     */
    private String image;
    /**
     * 代币全名
     */
    private String name;
    /**
     * 代币名称简称
     */
    private String shortName;

    /**
     * 代币名称
     */
    private String symbol;
    /**
     * 代币描述
     */
    private String descr;
    /**
     * 官网
     */
    private String webUrl;
    /**
     * telegram链接
     */
    private String telegramUrl;
    /**
     * 推特链接
     */
    private String twitterUrl;

    private String totalAmount;
    private String saleAmount;
    /**
     * 创建时间
     */
    private String createDate;
    /**
     * 发射时间
     */
    private long launchTime;
    /**
     * Dev 用户id
     */
    private long userId;
    /**
     * Dev地址
     */
    private String userAddress;
    /**
     * 进度tag
     */
    private boolean progressTag;
    /**
     * Dev昵称
     */
    private String userName;

    /**
     * 标签
     */
    private String label;
    /**
     * pancake交易链接
     */
    private String tradeUrl;

    private String b0;
    private String t0;
    private String minBuy;
    private String maxBuy;
    private String userAvatar;
    private String status;
    private String showStatus;
    private TokenPrice tokenPrice;
    private String oscarStatus;
    private boolean ctoTag;
    private String version;
    private boolean clickFunCheck;
    private String reserveAmount;
    private String raisedAmount;
    private String networkCode;
    private boolean isFollow;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getTelegramUrl() {
        return telegramUrl;
    }

    public void setTelegramUrl(String telegramUrl) {
        this.telegramUrl = telegramUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(String saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getB0() {
        return b0;
    }

    public void setB0(String b0) {
        this.b0 = b0;
    }

    public String getT0() {
        return t0;
    }

    public void setT0(String t0) {
        this.t0 = t0;
    }

    public long getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(long launchTime) {
        this.launchTime = launchTime;
    }

    public String getMinBuy() {
        return minBuy;
    }

    public void setMinBuy(String minBuy) {
        this.minBuy = minBuy;
    }

    public String getMaxBuy() {
        return maxBuy;
    }

    public void setMaxBuy(String maxBuy) {
        this.maxBuy = maxBuy;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(String showStatus) {
        this.showStatus = showStatus;
    }

    public String getTradeUrl() {
        return tradeUrl;
    }

    public void setTradeUrl(String tradeUrl) {
        this.tradeUrl = tradeUrl;
    }

    public TokenPrice getTokenPrice() {
        return tokenPrice;
    }

    public void setTokenPrice(TokenPrice tokenPrice) {
        this.tokenPrice = tokenPrice;
    }

    public String getOscarStatus() {
        return oscarStatus;
    }

    public void setOscarStatus(String oscarStatus) {
        this.oscarStatus = oscarStatus;
    }

    public boolean isProgressTag() {
        return progressTag;
    }

    public void setProgressTag(boolean progressTag) {
        this.progressTag = progressTag;
    }

    public boolean isCtoTag() {
        return ctoTag;
    }

    public void setCtoTag(boolean ctoTag) {
        this.ctoTag = ctoTag;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isClickFunCheck() {
        return clickFunCheck;
    }

    public void setClickFunCheck(boolean clickFunCheck) {
        this.clickFunCheck = clickFunCheck;
    }

    public String getReserveAmount() {
        return reserveAmount;
    }

    public void setReserveAmount(String reserveAmount) {
        this.reserveAmount = reserveAmount;
    }

    public String getRaisedAmount() {
        return raisedAmount;
    }

    public void setRaisedAmount(String raisedAmount) {
        this.raisedAmount = raisedAmount;
    }

    public String getNetworkCode() {
        return networkCode;
    }

    public void setNetworkCode(String networkCode) {
        this.networkCode = networkCode;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    @Override
    public String toString() {
        return "MonitorFourMemeCoin{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", symbol='" + symbol + '\'' +
                ", descr='" + descr + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", telegramUrl='" + telegramUrl + '\'' +
                ", twitterUrl='" + twitterUrl + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", saleAmount='" + saleAmount + '\'' +
                ", b0='" + b0 + '\'' +
                ", t0='" + t0 + '\'' +
                ", launchTime=" + launchTime +
                ", minBuy='" + minBuy + '\'' +
                ", maxBuy='" + maxBuy + '\'' +
                ", userId=" + userId +
                ", userAddress='" + userAddress + '\'' +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", status='" + status + '\'' +
                ", showStatus='" + showStatus + '\'' +
                ", tradeUrl='" + tradeUrl + '\'' +
                ", tokenPrice=" + tokenPrice +
                ", oscarStatus='" + oscarStatus + '\'' +
                ", progressTag=" + progressTag +
                ", ctoTag=" + ctoTag +
                ", version='" + version + '\'' +
                ", clickFunCheck=" + clickFunCheck +
                ", reserveAmount='" + reserveAmount + '\'' +
                ", raisedAmount='" + raisedAmount + '\'' +
                ", networkCode='" + networkCode + '\'' +
                ", label='" + label + '\'' +
                ", createDate='" + createDate + '\'' +
                ", isFollow=" + isFollow +
                '}';
    }

    public class TokenPrice {

        /**
         * 价格
         */
        private String price;
        private String maxPrice;
        private String increase;
        private String amount;
        /**
         * 市值
         */
        private String marketCap;
        private String trading;
        private String dayIncrease;
        private String dayTrading;
        private String raisedAmount;
        /**
         * 进度
         */
        private String progress;
        /**
         * 流动性
         */
        private String liquidity;
        private String tamount;
        private String bamount;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(String maxPrice) {
            this.maxPrice = maxPrice;
        }

        public String getIncrease() {
            return increase;
        }

        public void setIncrease(String increase) {
            this.increase = increase;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getMarketCap() {
            return marketCap;
        }

        public void setMarketCap(String marketCap) {
            this.marketCap = marketCap;
        }

        public String getTrading() {
            return trading;
        }

        public void setTrading(String trading) {
            this.trading = trading;
        }

        public String getDayIncrease() {
            return dayIncrease;
        }

        public void setDayIncrease(String dayIncrease) {
            this.dayIncrease = dayIncrease;
        }

        public String getDayTrading() {
            return dayTrading;
        }

        public void setDayTrading(String dayTrading) {
            this.dayTrading = dayTrading;
        }

        public String getRaisedAmount() {
            return raisedAmount;
        }

        public void setRaisedAmount(String raisedAmount) {
            this.raisedAmount = raisedAmount;
        }

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public String getLiquidity() {
            return liquidity;
        }

        public void setLiquidity(String liquidity) {
            this.liquidity = liquidity;
        }

        public String getTamount() {
            return tamount;
        }

        public void setTamount(String tamount) {
            this.tamount = tamount;
        }

        public String getBamount() {
            return bamount;
        }

        public void setBamount(String bamount) {
            this.bamount = bamount;
        }

        @Override
        public String toString() {
            return "TokenPrice{" +
                    "price='" + price + '\'' +
                    ", maxPrice='" + maxPrice + '\'' +
                    ", increase='" + increase + '\'' +
                    ", amount='" + amount + '\'' +
                    ", marketCap='" + marketCap + '\'' +
                    ", trading='" + trading + '\'' +
                    ", dayIncrease='" + dayIncrease + '\'' +
                    ", dayTrading='" + dayTrading + '\'' +
                    ", raisedAmount='" + raisedAmount + '\'' +
                    ", progress='" + progress + '\'' +
                    ", liquidity='" + liquidity + '\'' +
                    ", tamount='" + tamount + '\'' +
                    ", bamount='" + bamount + '\'' +
                    '}';
        }
    }
}
