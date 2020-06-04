package com.procentplus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bonus {
    @SerializedName("partner_id")
    @Expose
    private Integer partnerId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("current_discount")
    @Expose
    private String currentDiscount;
    @SerializedName("balance")
    @Expose
    private Double balance;
    @SerializedName("next_bonus_discount")
    @Expose
    private Integer nextBonusDiscount;
    @SerializedName("next_bonus_from")
    @Expose
    private Integer nextBonusFrom;
    @SerializedName("isMax")
    @Expose
    private boolean isMax;

    public Double getBalance() { return balance; }
    public String getCurrentDiscount() { return currentDiscount; }
    public Integer getNextBonusDiscount() { return nextBonusDiscount; }
    public Integer getNextBonusFrom() { return nextBonusFrom; }
    public Integer getPartnerId() { return partnerId; }
    public Integer getUserId() { return userId; }
    public boolean isMax() { return isMax; }
}
/*"partner_id": 0,
"user_id": 0,
“current_discount”: 0,
“balance”: 0,
"next_bonus_discount”: 0,
"next_bonus_from”: 0*/