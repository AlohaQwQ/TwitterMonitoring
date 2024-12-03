package com.web3.twitter.twitterBeans;

public class FollowerUserResults {

    private FollowerResults result;

    public FollowerResults getResult() {
        return result;
    }

    public void setResult(FollowerResults result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "FollowerUserResults{" +
                "result=" + result +
                '}';
    }
}
