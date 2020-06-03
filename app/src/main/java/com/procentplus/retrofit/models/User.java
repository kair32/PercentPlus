package com.procentplus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("is_active")
    @Expose
    private Boolean isActive;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("is_operator")
    @Expose
    private Boolean isOperator;

    public Boolean getIsOperator(){return isOperator;}

    public void setOperator(Boolean operator) { isOperator = operator; }

    public Boolean getActive() {
        return isActive;
    }

    public Integer getId() {
        return id;
    }

    public String getName() { return name; }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
