package com.web3.twitter.monitorBeans;

/**
 * Frontend数据解析bean
 * {
 *     "mint": "ETeG6w8q7YVc11KvE9KEPXdX4JVgSfVCmHvWGgThpump",
 *     "name": "Just Buy $5 Of This Coin",
 *     "symbol": "JB5",
 *     "description": "Just buy $5 and you will be rich. Step 1: Buy. Step 2: Forget.",
 *     "image_uri": "https://ipfs.io/ipfs/QmXEdB9PYThjgsQDp5iWFq8oXijZ8HnCCZePRSRhXfQJ8j",
 *     "metadata_uri": "https://ipfs.io/ipfs/QmdYsYonLaiwK8gyp9YcDBkh4KwWwS9qmCdqPJYq3gYThR",
 *     "twitter": null,
 *     "telegram": null,
 *     "bonding_curve": "GSoN8NUpsWLuZbr5iaUun6XyMbXbUisBA6f9GQzDTSPY",
 *     "associated_bonding_curve": "Awo9tdZAPezqat921JvBkunuSBJw5MKaZ6AoWrPG3598",
 *     "creator": "2h5mXo6yGstspNbZA4yFtsqw41z1KSBj8zbczyq1rAus",
 *     "created_timestamp": 1739683051855,
 *     "raydium_pool": null,
 *     "complete": false,
 *     "virtual_sol_reserves": 30936250001,
 *     "virtual_token_reserves": 1040526889976290,
 *     "hidden": null,
 *     "total_supply": 1000000000000000,
 *     "website": null,
 *     "show_name": true,
 *     "last_trade_timestamp": 1739683051000,
 *     "king_of_the_hill_timestamp": null,
 *     "market_cap": 29.731331596,
 *     "reply_count": 0,
 *     "last_reply": null,
 *     "nsfw": false,
 *     "market_id": null,
 *     "inverted": null,
 *     "is_currently_live": false,
 *     "username": "EvanSid",
 *     "profile_image": null,
 *     "usd_market_cap": 5786.90638184544
 * }
 */
public class MonitorFrontendPumpCoin {

    /**
     * 代币地址
     */
    private String mint;

    /**
     * 代币全称
     */
    private String name;

    /**
     * 代币简称
     */
    private String symbol;

    /**
     * 代币描述
     */
    private String description;

    /**
     * 代币Logo链接
     */
    private String image_uri;

    private String video_uri;

    /**
     * 代币元数据链接
     */
    private String metadata_uri;

    /**
     * 代币推特链接
     */
    private String twitter;

    /**
     * 代币telegram链接
     */
    private String telegram;

    /**
     * 代币Dev地址
     */
    private String creator;

    /**
     * 代币Pump创建者名称
     */
    private String username;

    /**
     * 代币创建时间
     */
    private long created_timestamp;

    /**
     * 发射与否
     */
    private boolean complete;

    /**
     * 代币总供应量
     */
    private long total_supply;

    /**
     * 美元市值
     */
    private double usd_market_cap;

    /**
     * 市值
     */
    private double market_cap;

    private String bonding_curve;
    private String associated_bonding_curve;
    private String raydium_pool;
    private long virtual_sol_reserves;
    private long virtual_token_reserves;
    private String website;
    private boolean show_name;
    private String king_of_the_hill_timestamp;
    /**
     * 回复数
     */
    private int reply_count;
    private String last_reply;
    private boolean nsfw;
    private String market_id;
    private String inverted;
    private boolean is_currently_live;
    private String profile_image;



    public String getMint() {
        return mint;
    }

    public void setMint(String mint) {
        this.mint = mint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }

    public String getMetadata_uri() {
        return metadata_uri;
    }

    public void setMetadata_uri(String metadata_uri) {
        this.metadata_uri = metadata_uri;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getBonding_curve() {
        return bonding_curve;
    }

    public void setBonding_curve(String bonding_curve) {
        this.bonding_curve = bonding_curve;
    }

    public String getAssociated_bonding_curve() {
        return associated_bonding_curve;
    }

    public void setAssociated_bonding_curve(String associated_bonding_curve) {
        this.associated_bonding_curve = associated_bonding_curve;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getCreated_timestamp() {
        return created_timestamp;
    }

    public void setCreated_timestamp(long created_timestamp) {
        this.created_timestamp = created_timestamp;
    }

    public String getRaydium_pool() {
        return raydium_pool;
    }

    public void setRaydium_pool(String raydium_pool) {
        this.raydium_pool = raydium_pool;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public long getVirtual_sol_reserves() {
        return virtual_sol_reserves;
    }

    public void setVirtual_sol_reserves(long virtual_sol_reserves) {
        this.virtual_sol_reserves = virtual_sol_reserves;
    }

    public long getVirtual_token_reserves() {
        return virtual_token_reserves;
    }

    public void setVirtual_token_reserves(long virtual_token_reserves) {
        this.virtual_token_reserves = virtual_token_reserves;
    }

    public long getTotal_supply() {
        return total_supply;
    }

    public void setTotal_supply(long total_supply) {
        this.total_supply = total_supply;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isShow_name() {
        return show_name;
    }

    public void setShow_name(boolean show_name) {
        this.show_name = show_name;
    }

    public String getKing_of_the_hill_timestamp() {
        return king_of_the_hill_timestamp;
    }

    public void setKing_of_the_hill_timestamp(String king_of_the_hill_timestamp) {
        this.king_of_the_hill_timestamp = king_of_the_hill_timestamp;
    }

    public double getMarket_cap() {
        return market_cap;
    }

    public void setMarket_cap(double market_cap) {
        this.market_cap = market_cap;
    }

    public int getReply_count() {
        return reply_count;
    }

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
    }

    public String getLast_reply() {
        return last_reply;
    }

    public void setLast_reply(String last_reply) {
        this.last_reply = last_reply;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    public String getMarket_id() {
        return market_id;
    }

    public void setMarket_id(String market_id) {
        this.market_id = market_id;
    }

    public String getInverted() {
        return inverted;
    }

    public void setInverted(String inverted) {
        this.inverted = inverted;
    }

    public boolean isIs_currently_live() {
        return is_currently_live;
    }

    public void setIs_currently_live(boolean is_currently_live) {
        this.is_currently_live = is_currently_live;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public double getUsd_market_cap() {
        return usd_market_cap;
    }

    public void setUsd_market_cap(double usd_market_cap) {
        this.usd_market_cap = usd_market_cap;
    }

    @Override
    public String toString() {
        return "MonitorFrontendPumpCoin{" +
                "mint='" + mint + '\'' +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", description='" + description + '\'' +
                ", image_uri='" + image_uri + '\'' +
                ", video_uri='" + video_uri + '\'' +
                ", metadata_uri='" + metadata_uri + '\'' +
                ", twitter='" + twitter + '\'' +
                ", telegram='" + telegram + '\'' +
                ", bonding_curve='" + bonding_curve + '\'' +
                ", associated_bonding_curve='" + associated_bonding_curve + '\'' +
                ", creator='" + creator + '\'' +
                ", created_timestamp=" + created_timestamp +
                ", raydium_pool='" + raydium_pool + '\'' +
                ", complete=" + complete +
                ", virtual_sol_reserves=" + virtual_sol_reserves +
                ", virtual_token_reserves=" + virtual_token_reserves +
                ", total_supply=" + total_supply +
                ", website='" + website + '\'' +
                ", show_name=" + show_name +
                ", king_of_the_hill_timestamp='" + king_of_the_hill_timestamp + '\'' +
                ", market_cap=" + market_cap +
                ", reply_count=" + reply_count +
                ", last_reply='" + last_reply + '\'' +
                ", nsfw=" + nsfw +
                ", market_id='" + market_id + '\'' +
                ", inverted='" + inverted + '\'' +
                ", is_currently_live=" + is_currently_live +
                ", username='" + username + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", usd_market_cap=" + usd_market_cap +
                '}';
    }
}
