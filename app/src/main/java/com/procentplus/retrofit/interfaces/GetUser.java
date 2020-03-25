package com.procentplus.retrofit.interfaces;

import com.procentplus.retrofit.models.AuthResponse;
import com.procentplus.retrofit.models.SignRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GetUser {
    @Headers("Content-Type: application/json")
    @GET("mobile_users/{id}")
    Call<AuthResponse> getUser(
            @Path("id") Integer id,
            @Header("Authorization") String authorization);
}
