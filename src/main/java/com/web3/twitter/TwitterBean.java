package com.web3.twitter;

import com.web3.twitter.twitterBeans.TwitterList;

/**
 * @author Aloha
 * @date 2024-11-24 19:05
 * @description TwitterBean
 */
public class TwitterBean {

    private TwitterList twitterList;

    public void setList(TwitterList twitterList) {
        this.twitterList = twitterList;
    }

    public TwitterList getList() {
        return twitterList;
    }
}
