package com.procentplus.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.procentplus.R;
import com.procentplus.databinding.ActivityCalculateBinding;

import java.util.Objects;

public class ActivityCalculate extends AppCompatActivity {
    public String userName;
    public ObservableBoolean isInputSum = new ObservableBoolean(true);
    public ObservableField<String> sumText = new ObservableField<String>("");

    ActivityCalculateBinding binding;
    String userId;
    String userBonus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calculate);
        userName = getIntent().getStringExtra("userName");
        userId = getIntent().getStringExtra("userId");
        userBonus = getIntent().getStringExtra("userBonus");
        binding.setActivity(this);
    }

    public void onEnter(){
        String s = Objects.requireNonNull(sumText.get());
        if (s.equals("")) s = "0";
        if (Integer.parseInt(s)<=0) return;
        if (!isInputSum.get())  {
            setAmount();
            return;
        }
        isInputSum.set(!isInputSum.get());
        if (isInputSum.get())   binding.btAmount.setText(R.string.calculate_amount);
        else                    binding.btAmount.setText(R.string.complete);
        binding.tvAmount.setText(String.format("%s %s", sumText.get(), getString(R.string.rub)));
    }

    private void setAmount(){
        finish();
    }
}

