package com.procentplus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuthResponse {
    @SerializedName("errors_count")
    @Expose
    private Integer errorsCount;

    @SerializedName("access_token")
    @Expose
    private String accessToken;

    @SerializedName("token_type")
    @Expose
    private String tokenType;

    @SerializedName("expires_in")
    @Expose
    private Integer expiresIn;

    @SerializedName("user")
    @Expose
    private User user;



    @SerializedName("msg")
    @Expose
    private String msg;

    public String getMsg() { return msg; }

    public Integer getErrorsCount() { return errorsCount; }

    public String getAccessToken() { return accessToken; }

    public String getTokenType() { return tokenType; }

    public Integer getExpiresIn() { return expiresIn; }

    public User getUser() { return user; }


}
