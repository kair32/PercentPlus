package com.procentplus.retrofit.interfaces;

import com.procentplus.retrofit.models.Bonus;
import com.procentplus.retrofit.models.BonusData;
import com.procentplus.retrofit.models.BonusRequest;
import com.procentplus.retrofit.models.UserBonus;
import com.procentplus.retrofit.models.UserBonusRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PartnerBonus {

    @Headers("Content-Type: application/json")
    @POST("bonuses/user_bonus")
    Call<UserBonus> getPartnerBonus(@Header("Authorization") String authorization,
                                    @Body BonusRequest partner_id);

    @Headers("Content-Type: application/json")
    @POST("bonuses/user_bonus")
    Call<UserBonus> getUserBonus(@Header("Authorization") String authorization,
                                 @Body UserBonusRequest partner_id);

}
//bonuses/user_bonus

//id пользователя
//id партнера
//текущий бонус
//текущая сумма покупок
//следующий бонус
//слндующая сумма для бонуса
//примечание/описание (сюда текст)

