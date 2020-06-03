package com.procentplus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Objects {
    @SerializedName("errors_count") @Expose
    private Integer errorsCount;

    @SerializedName("msg") @Expose
    private String msg;

    @SerializedName("activity_type") @Expose
    private List<ActivityType> activityType;

    public Integer getErrorsCount() { return errorsCount; }
    public List<ActivityType> getActivityType() { return activityType; }
    public String getMsg() { return msg; }

    public class ActivityType {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("description")
        @Expose
        private Object description;
        @SerializedName("partners")
        @Expose
        private List<Partner> partners = null;

        public Integer getId() { return id; }
        public String getName() { return name; }
        public Object getDescription() { return description; }
        public List<Partner> getPartners() { return partners; }
    }
}
