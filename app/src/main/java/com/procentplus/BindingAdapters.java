package com.procentplus;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;

import com.procentplus.activities.AuthActivity;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
    login:      a022@ya.ru
    password:   qazwsxedc

    Оператор
    login:      bwwb77bo@yasellerbot.xyz
    password:   qwertyuiop
 **/

public class BindingAdapters {
    @BindingAdapter("isVisible")
    public static void setVisibility(View view, boolean isVisible) {
        if (isVisible)  view.setVisibility(View.VISIBLE);
        else            view.setVisibility(View.GONE);
    }

    @BindingAdapter("editTextPhone")
    public static void initTextChangedListener(EditText editText, AuthActivity.AuthValue value) {
        MaskedTextChangedListener listener = new MaskedTextChangedListener("+7 ([000]) [000]-[0000]", false, editText, null, new MaskedTextChangedListener.ValueListener() {
            @Override//"+7 (965) 368-4111"
            public void onTextChanged(boolean maskFilled, @NotNull String extractedValue, @NotNull String s1) {
                String text = editText.getText().toString();
                if (extractedValue.equals("") && (text.equals("+7")))
                    editText.setText("+7 ");
                value.phone.set(editText.getText().toString());
                Log.d("TEGOZ", " " + value.phone);
            }
        }) {
            @Override
            public void onFocusChange(@Nullable View view, boolean hasFocus) {
                super.onFocusChange(view, hasFocus);
                String text = editText.getText().toString();
                if (hasFocus && text.equals("")) editText.setText("+7 ");
                else if (!hasFocus && text.equals("+7 ")) editText.setText("");
            }
        };
        editText.addTextChangedListener(listener);
        editText.setOnFocusChangeListener(listener);
        return;
    }
}
