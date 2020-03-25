package com.procentplus.retrofit.interfaces;

import com.procentplus.retrofit.models.Bonus;
import com.procentplus.retrofit.models.BonusRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PartnerBonus {

    @Headers("Content-Type: application/json")
    @POST("bonuses/user_bonus")
    Call<Bonus> getPartnerBonus(@Header("Authorization") String authorization,
                         @Body BonusRequest partner_id);

}
