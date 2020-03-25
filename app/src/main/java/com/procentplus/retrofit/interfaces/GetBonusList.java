package com.procentplus.retrofit.interfaces;

import com.procentplus.retrofit.models.BonusList;
import com.procentplus.retrofit.models.BonusRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetBonusList {
    @Headers("Content-Type: application/json")
    @POST("bonuses/bonuses_list")
    Call<BonusList> getBonusList(
            @Header("Authorization") String authorization,
            @Body BonusRequest mobile_user);
}
