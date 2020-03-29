package com.procentplus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaleRecordsRequest {
    @SerializedName("sale_record")
    @Expose
    private SaleRecord saleRecord;

    public SaleRecordsRequest(SaleRecord saleRecord){ this.saleRecord = saleRecord; }

    public SaleRecord getSaleRecord() { return saleRecord; }
    public void setSaleRecord(SaleRecord saleRecord) { this.saleRecord = saleRecord; }

    public static class SaleRecord {
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("operator_id")
        @Expose
        private Integer operatorId;
        @SerializedName("discount")
        @Expose
        private Integer discount;
        @SerializedName("original_price")
        @Expose
        private Integer originalPrice;

        public SaleRecord(Integer userId, Integer operatorId, Integer discount, Integer originalPrice){
            this.userId = userId;
            this.operatorId = operatorId;
            this.discount = discount;
            this.originalPrice = originalPrice;
        }

        public Integer getMobileUserId() { return userId; }
        public void setMobileUserId(Integer userId) { this.userId = userId; }
        public Integer getDiscount() { return discount; }
        public void setDiscount(Integer discount) { this.discount = discount; }
        public Integer getOriginalPrice() { return originalPrice; }
        public void setOriginalPrice(Integer originalPrice) { this.originalPrice = originalPrice; }
    }
}
