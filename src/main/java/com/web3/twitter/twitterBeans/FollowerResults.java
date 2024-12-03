package com.web3.twitter.twitterBeans;

public class FollowerResults {

    private String __typename;
    private boolean has_graduated_access;
    private String id;
    private String rest_id;

    private boolean is_blue_verified;
    private String profile_image_shape;

    /**
     * 关注者信息
     */
    private FollowerLegacy legacy;

    public String get__typename() {
        return __typename;
    }

    public void set__typename(String __typename) {
        this.__typename = __typename;
    }

    public boolean isHas_graduated_access() {
        return has_graduated_access;
    }

    public void setHas_graduated_access(boolean has_graduated_access) {
        this.has_graduated_access = has_graduated_access;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRest_id() {
        return rest_id;
    }

    public void setRest_id(String rest_id) {
        this.rest_id = rest_id;
    }

    public boolean isIs_blue_verified() {
        return is_blue_verified;
    }

    public void setIs_blue_verified(boolean is_blue_verified) {
        this.is_blue_verified = is_blue_verified;
    }

    public String getProfile_image_shape() {
        return profile_image_shape;
    }

    public void setProfile_image_shape(String profile_image_shape) {
        this.profile_image_shape = profile_image_shape;
    }

    public FollowerLegacy getLegacy() {
        return legacy;
    }

    public void setLegacy(FollowerLegacy legacy) {
        this.legacy = legacy;
    }

    @Override
    public String toString() {
        return "FollowerResults{" +
                "__typename='" + __typename + '\'' +
                ", has_graduated_access=" + has_graduated_access +
                ", id='" + id + '\'' +
                ", rest_id='" + rest_id + '\'' +
                ", is_blue_verified=" + is_blue_verified +
                ", profile_image_shape='" + profile_image_shape + '\'' +
                ", legacy=" + legacy +
                '}';
    }
}
