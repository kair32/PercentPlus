package com.procentplus.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.procentplus.CreateQr;
import com.procentplus.R;
import com.procentplus.databinding.ActivityBonusBinding;
import com.procentplus.legend.LegendActivity;
import com.procentplus.retrofit.RetrofitClient;
import com.procentplus.retrofit.interfaces.PartnerBonus;
import com.procentplus.retrofit.models.Bonus;
import com.procentplus.retrofit.models.BonusRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BonusActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityBonusBinding binding;
    private String object_name = "";
    private int object_id = -1;

    private Retrofit retrofit;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
    private SimpleDateFormat dateFormat3 = new SimpleDateFormat("dd MMMM", Locale.getDefault());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() !=null) {
            object_name = getIntent().getStringExtra("object_name");
            object_id = getIntent().getIntExtra("object_id", -1);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bonus);

        retrofit = RetrofitClient.getInstance();
        //setting time
        binding.currentDate.setText(dateFormat3.format(new Date()));
        binding.currentTime.setText(dateFormat.format(new Date()));

        // animate circle
        animateCircle(binding.getRoot());

        binding.bonusObjectName.setText(object_name);
        binding.iconInfo.setOnClickListener(this);

        TextView how_to_get_percent = binding.howToGetPercent;
        how_to_get_percent.setText("Как накопить процент");
        how_to_get_percent.setPaintFlags(how_to_get_percent.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        how_to_get_percent.setOnClickListener(this);

        binding.legendBackBtn.setOnClickListener(v -> onBackPressed());
        getBonus(binding.getRoot());
        new CreateQr(object_name, object_id, binding.ivQr);

    }

    private void animateCircle(View view) {
        Animation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.02f);
        mAnimation.setDuration(400);
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        binding.animatingCircle.setAnimation(mAnimation);
    }

    private void getBonus(final View view) {
        PartnerBonus iBonus = retrofit.create(PartnerBonus.class);

        Call<Bonus> bonusCall = iBonus.getPartnerBonus(
                MainActivity.prefConfig.readToken(),
                new BonusRequest(object_id)
        );

        bonusCall.enqueue(new Callback<Bonus>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Bonus> call, Response<Bonus> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body() ==  null)   return;
                    Bonus bonus = response.body();
                    binding.currentUserBonus.setText(bonus.getCurrentDiscount() + "%");
                    String text = "0";
                    if (bonus.getNextBonusFrom()!=null && bonus.getBalance() !=null)
                        text = (bonus.getNextBonusFrom() - bonus.getBalance()) + "";

                    if (bonus.getNextBonusFrom() == null || bonus.getNextBonusFrom() == 0)
                        binding.tvBonusText.setText("На данный момент у партнера нет никаких бонусов");
                    else binding.tvBonusText.setText("До скидки " +
                            bonus.getNextBonusDiscount() +
                            "% вам необходимо накопить еще " +
                            text +
                            " руб. \nПо данным на " +
                            new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));
                }
            }
            @Override
            public void onFailure(Call<Bonus> call, Throwable t) {
                MainActivity.prefConfig.displayToast("Произошла ошибка при получении бонусов");
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.icon_info:
            case R.id.how_to_get_percent:
                intent = new Intent(this, LegendActivity.class);
                intent.putExtra("partner_id", object_id);
                startActivity(intent);
                break;
        }
    }
}