package com.procentplus.retrofit.interfaces;

import com.procentplus.retrofit.models.User;
import com.procentplus.retrofit.models.response_bubble.RestResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface GetUser {
    @Headers("Content-Type: application/json")
    @GET("mobile_users/{id}")
    Call<RestResponse<User>> getUser(
            @Path("id") Integer id,
            @Header("Authorization") String authorization);
}
