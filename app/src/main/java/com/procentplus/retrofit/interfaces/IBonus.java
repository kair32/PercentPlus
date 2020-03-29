package com.procentplus.retrofit.interfaces;

import com.procentplus.retrofit.models.BonusData;
import com.procentplus.retrofit.models.BonusRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IBonus {

    @Headers("Content-Type: application/json")
    @POST("bonuses/current_bonus")
    Call<BonusData> getBonus(@Header("Authorization") String authorization,
                             @Body BonusRequest partner_id);

}
