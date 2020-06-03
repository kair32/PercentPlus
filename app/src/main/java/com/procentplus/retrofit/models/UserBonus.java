package com.procentplus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserBonus {
    @SerializedName("errors_count")
    @Expose
    private Integer errorsCount;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("user_bonus")
    @Expose
    private Bonus bonus;

    public Integer getErrorsCount() { return errorsCount; }
    public String getMsg() { return msg; }
    public Bonus getBonus() { return bonus; }
}
