package com.procentplus.retrofit.models.response_bubble;

import androidx.annotation.Nullable;

public interface ResponseCallback<Q>{
    void onSuccessfulRequest(Q response);
    void onErrorRequest(@Nullable String message, boolean isUnauthorized);
}
