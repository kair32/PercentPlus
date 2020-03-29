package com.procentplus.retrofit.interfaces;

import com.procentplus.retrofit.models.Bonus;
import com.procentplus.retrofit.models.BonusRequest;
import com.procentplus.retrofit.models.SaleRecordsRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SaleRecords {
    @Headers("Content-Type: application/json")
    @POST("sale_records")
    Call<ResponseBody> setSaleRecords(@Header("Authorization") String authorization,
                                      @Body SaleRecordsRequest recordsRequest);
}
