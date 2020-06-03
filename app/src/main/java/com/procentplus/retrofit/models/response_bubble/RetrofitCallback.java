package com.procentplus.retrofit.models.response_bubble;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCallback<T> implements Callback<T> {

    private ResponseCallback responseCallback;

    public RetrofitCallback(ResponseCallback responseCallback){
        this.responseCallback = responseCallback;
    }
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.code() == 401) {
            responseCallback.onErrorRequest(null, true);
            return;
        }
        if (response.code() == 200) {
            if (response.body() != null && ((RestResponse)response.body()).getErrorsCount() > 0) {
                responseCallback.onErrorRequest(((RestResponse)response.body()).getMsg(), false);
                return;
            }
            responseCallback.onSuccessfulRequest(((RestResponse)response.body()).getData());
        } else {
            try {
                responseCallback.onErrorRequest(new JSONObject(response.errorBody().string()).getString("error_message"), false);
            } catch (JSONException | IOException e) {
                responseCallback.onErrorRequest("Неизвестная ошибка. Попробуйте повторить запрос позже", false);
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) { responseCallback.onErrorRequest(t.getMessage(), false); }
}