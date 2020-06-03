package com.procentplus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BonusList {
    @SerializedName("errors_count") @Expose
    private Integer errorsCount;

    @SerializedName("msg") @Expose
    private String msg;

    @SerializedName("partner") @Expose
    private List<Partner> partnerList;

    public Integer getErrorsCount() { return errorsCount; }
    public List<Partner> getPartnerList() { return partnerList; }
    public String getMsg() { return msg; }
}
