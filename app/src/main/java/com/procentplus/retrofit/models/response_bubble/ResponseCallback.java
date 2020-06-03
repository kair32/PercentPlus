package com.procentplus.retrofit.models.response_bubble;

public interface ResponseCallback<Q>{
    void onSuccessfulRequest(Q response);
    void onErrorRequest(String message);
}
