package com.procentplus.retrofit.models.response_bubble;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestResponse<T> {
    @SerializedName("errors_count") @Expose
    private Integer errorsCount;

    @SerializedName("msg") @Expose
    private String msg;

    @SerializedName("data") @Expose
    private T data;

    public T getData() { return data; }
    public Integer getErrorsCount() { return errorsCount; }
    public String getMsg() { return msg; }
}
