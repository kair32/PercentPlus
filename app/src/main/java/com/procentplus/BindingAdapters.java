package com.procentplus;

import android.view.View;

import androidx.databinding.BindingAdapter;

/**
    login:      a022@ya.ru
    password:   qazwsxedc
 **/

public class BindingAdapters {
    @BindingAdapter("isVisible")
    public static void setVisibility(View view, boolean isVisible) {
        if (isVisible)  view.setVisibility(View.VISIBLE);
        else            view.setVisibility(View.GONE);
    }
}
