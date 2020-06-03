package com.procentplus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Partner {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("legal_person")
    @Expose
    private boolean legalPerson;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("bonuses")
    @Expose
    private List<BonusData> bonuses;

    public List<BonusData> getBonus() { return bonuses; }
    public Integer getId() { return id; }
    public Integer getUserId() { return userId; }
    public List<BonusData> getBonuses() { return bonuses; }
    public String getCity() { return city; }
    public String getCompanyName() { return companyName; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
}