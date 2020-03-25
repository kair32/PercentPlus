package com.procentplus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BonusList {
    @SerializedName("partner_id")
    @Expose
    private Integer partnerId;

    @SerializedName("bonuses")
    @Expose
    private List<Bonus.BonusData> bonuses;

    public List<Bonus.BonusData> getBonus() { return bonuses; }
    public Integer getPartnerId() { return partnerId; }
}
