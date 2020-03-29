package com.procentplus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserBonusRequest {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("partner_id")
    @Expose
    private Integer partnerId;

    public UserBonusRequest(Integer userId, Integer partnerId) {
        this.userId = userId;
        this.partnerId = partnerId;
    }

    public Integer getParentId() { return partnerId; }
    public Integer getUserId() { return userId; }
}
