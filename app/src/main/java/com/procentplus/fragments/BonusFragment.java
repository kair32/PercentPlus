package com.procentplus.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.procentplus.BuildConfig;
import com.procentplus.R;
import com.procentplus.activities.LegendActivity;
import com.procentplus.activities.MainActivity;
import com.procentplus.databinding.FragmentBonusBinding;
import com.procentplus.retrofit.RetrofitClient;
import com.procentplus.retrofit.interfaces.IBonus;
import com.procentplus.retrofit.models.Bonus;
import com.procentplus.retrofit.models.BonusRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import kotlin.TuplesKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function2;
import kotlin.text.StringsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BonusFragment extends Fragment implements View.OnClickListener {
    FragmentBonusBinding binding;
    private String object_name = "";
    private int object_id = -1;

    private IBonus iBonus;
    private Retrofit retrofit;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
    private SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
    private SimpleDateFormat dateFormat3 = new SimpleDateFormat("dd MMMM", Locale.getDefault());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() !=null) {
            final Bundle args = getArguments();
            object_name = args.getString("object_name");
            object_id = args.getInt("object_id");
        }
        if (object_id == -1) {
            object_id = MainActivity.prefConfig.readId();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // init api
        retrofit = RetrofitClient.getInstance();

        binding = FragmentBonusBinding.inflate(inflater, container, false);

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

        getBonus(binding.getRoot());
        createQR(BuildConfig.APPLICATION_ID + "//" + "object_name" + "//" + object_id + "//15");
        return binding.getRoot();
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
        iBonus = retrofit.create(IBonus.class);

        Call<Bonus> bonusCall = iBonus.getBonus(
                MainActivity.prefConfig.readToken(),
                new BonusRequest(object_id)
        );

        bonusCall.enqueue(new Callback<Bonus>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Bonus> call, Response<Bonus> response) {
                int statusCode = response.code();
                Log.d("LOGGER Bonus", "statusCode: " + statusCode);
                if (statusCode == 200) {
                    try {
                        if (response.body() == null || response.body().getBonus() == null) return;
                        TextView current_user_bonus = binding.currentUserBonus;
                        String percent_full_num = response.body().getBonus().getPercent();
                        switch (percent_full_num) {
                            case "5.0": current_user_bonus.setText("0%");
                                break;
                            case "7.0": current_user_bonus.setText("5%");
                                break;
                            case "10.0": current_user_bonus.setText("7%");
                                break;
                            case "15.0": current_user_bonus.setText("10%");
                                break;
                        }
                        String percent = percent_full_num
                                .substring(0, percent_full_num.length()-2);
                        binding.tvBonusText.setText(
                                "До скидки " +
                                percent+
                                "% вам необходимо накопить еще " +
                                (response.body().getBonus().getSumTo()-response.body().getBonus().getSumFrom()) +
                                " руб. По данным на " +
                                getDate()
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                intent = new Intent(getContext(), LegendActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void createQR(String qrCode){
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(qrCode, BarcodeFormat.QR_CODE, 400, 400);
            binding.ivQr.setImageBitmap(bitmap);
        } catch(Exception e) { }
    }
    private String getDate() {
        return dateFormat2.format(new Date());
    }
}