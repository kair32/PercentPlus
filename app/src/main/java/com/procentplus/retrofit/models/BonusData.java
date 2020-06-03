package com.procentplus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BonusData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sum_from")
    @Expose
    private Integer sumFrom;
    @SerializedName("sum_to")
    @Expose
    private Integer sumTo;
    @SerializedName("percent")
    @Expose
    private String percent = "6";
    @SerializedName("partner_id")
    @Expose
    private Integer partnerId;

    public Integer getId() {
        return id;
    }
    public Integer getSumFrom() {
        return sumFrom;
    }
    public Integer getSumTo() {
        return sumTo;
    }
    public String getPercent() {
        return percent;
    }
    public Integer getPartnerId() { return partnerId; }
}
