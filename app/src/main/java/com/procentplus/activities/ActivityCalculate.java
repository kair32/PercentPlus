package com.procentplus.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.google.android.material.snackbar.Snackbar;
import com.procentplus.R;
import com.procentplus.databinding.ActivityCalculateBinding;
import com.procentplus.retrofit.RetrofitClient;
import com.procentplus.retrofit.interfaces.PartnerBonus;
import com.procentplus.retrofit.interfaces.SaleRecords;
import com.procentplus.retrofit.models.Bonus;
import com.procentplus.retrofit.models.SaleRecordsRequest;
import com.procentplus.retrofit.models.UserBonusRequest;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActivityCalculate extends AppCompatActivity {
    public String userName;
    public ObservableBoolean isInputSum = new ObservableBoolean(true);
    public ObservableField<String> sumText = new ObservableField<String>("");

    ActivityCalculateBinding binding;
    Integer userId, partnerId, operatorId;
    String userBonus;
    Integer percent = 0, originalPrice = 0;

    private Retrofit retrofit;
    private PartnerBonus partnerBonus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calculate);
        retrofit = RetrofitClient.getInstance();

        userName = getIntent().getStringExtra("userName");
        userId = getIntent().getIntExtra("userId", -1);
        partnerId = getIntent().getIntExtra("partnerId", -1);
        operatorId = getIntent().getIntExtra("operatorId", -1);
        userBonus = getIntent().getStringExtra("userBonus");
        binding.setActivity(this);
        response();
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

        String text = (Integer.parseInt(Objects.requireNonNull(sumText.get())) - Integer.parseInt(Objects.requireNonNull(sumText.get())) / 100 * percent) +"";
        originalPrice = Integer.parseInt(Objects.requireNonNull(sumText.get()));

        binding.tvAmount.setText(String.format("%s %s", text, getString(R.string.rub)));
    }

    private void response(){
        partnerBonus = retrofit.create(PartnerBonus.class);

        Call<Bonus> bonusCall = partnerBonus.getUserBonus(
                MainActivity.prefConfig.readToken(),
                new UserBonusRequest(userId, partnerId)
        );

        bonusCall.enqueue(new Callback<Bonus>() {
            @Override
            public void onResponse(Call<Bonus> call, Response<Bonus> response) {
                if (response.body()!=null) {
                    Bonus bonus = response.body();
                    if (bonus.getCurrentDiscount() != null) percent = Integer.parseInt(bonus.getCurrentDiscount());
                    String text = "Бонус: " + percent + "%";
                    binding.tvBonus.setText(text);
                }else snackBarView(false);
            }

            @Override public void onFailure(Call<Bonus> call, Throwable t) {
                snackBarView(false);
            }
        });
    }

    private void snackBarView(boolean isSaleRecords){
        Snackbar.make(binding.getRoot(), R.string.error, Snackbar.LENGTH_INDEFINITE)
                .setAction("Повторить?", v -> {
                    if (isSaleRecords)  setSumResponse();
                    else                response();
                })
                .show();
    }

    private void setAmount(){
        setSumResponse();
    }

    private void setSumResponse(){
        SaleRecords saleRecords = retrofit.create(SaleRecords.class);


        Call<ResponseBody> bonusCall = saleRecords.setSaleRecords(
                MainActivity.prefConfig.readToken(),
                new SaleRecordsRequest(new SaleRecordsRequest.SaleRecord(userId, percent, originalPrice))
        );

        bonusCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) finish();
                else                        snackBarView(true);
            }

            @Override public void onFailure(Call<ResponseBody> call, Throwable t) { snackBarView(true); }
        });

    }
}

