package com.procentplus.activities;

import android.content.Intent;
import android.graphics.PorterDuff;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.procentplus.ProgressDialog.DialogConfig;
import com.procentplus.R;
import com.procentplus.SharedPrefs.PrefConfig;
import com.procentplus.adapter.CustomPagerAdapter;
import com.procentplus.retrofit.RetrofitClient;
import com.procentplus.retrofit.interfaces.GetUser;
import com.procentplus.retrofit.interfaces.IAuthorization;
import com.procentplus.retrofit.models.AuthResponse;
import com.procentplus.retrofit.models.MobileUser;
import com.procentplus.retrofit.models.SignRequest;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends FragmentActivity {

    String tag = "FragmentActivityMain";
    @BindView(R.id.mainViewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.preloader)
    ConstraintLayout preloader_view;
    @BindView(R.id.main_logo)
    ImageView main_logo;
    private PagerAdapter pagerAdapter;
    private int tabPosition = 0;
    private String objectName;
    private Bundle objectNameBundle;

    public static PrefConfig prefConfig;
    public static DialogConfig dialogConfig;
    private Retrofit retrofit;
    private Boolean isOperator = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        prefConfig = new PrefConfig(this);
        dialogConfig = new DialogConfig(this, "Идет загрузка");
        retrofit = RetrofitClient.getInstance();
        getUser();
        if (savedInstanceState != null) {
            return;
        }

        // if user isn't logged in
        if (!prefConfig.readLoginStatus()) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {

                            Intent auth_intent = new Intent(MainActivity.this, AuthActivity.class);
                            startActivity(auth_intent);
                            finish();

                        }
                    }, 1800);
        }

        if (getIntent().getIntExtra("tab_id", -1) != -1) {
            tabPosition = getIntent().getExtras().getInt("tab_id");
            objectName = getIntent().getStringExtra("object_name");
            int object_id = getIntent().getExtras().getInt("object_id");


            objectNameBundle = new Bundle();
            objectNameBundle.putString("object_name", objectName);
            objectNameBundle.putInt("object_id", object_id);

            Log.d(tag, "tabLayout.setVisibility(View.VISIBLE) ");
        } else {
            Log.d(tag, "preloader ");
        }

    }

    private void animateLogo() {
        final Animation anim_out = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        try {
            anim_out.setAnimationListener(new Animation.AnimationListener()
            {
                @Override public void onAnimationStart(Animation animation) {}
                @Override public void onAnimationRepeat(Animation animation) {}
                @Override public void onAnimationEnd(Animation animation)
                {
                    anim_in.setAnimationListener(new Animation.AnimationListener() {
                        @Override public void onAnimationStart(Animation animation) {}
                        @Override public void onAnimationRepeat(Animation animation) {}
                        @Override public void onAnimationEnd(Animation animation) {}
                    });
                    main_logo.startAnimation(anim_in);

                }
            });
            main_logo.startAnimation(anim_out);
        } catch (Exception e) {
            Log.d("LOGGER MainActivity", "animateLogo: " + e.getMessage());
        }
    }

    private void initViewPager() {
        Log.d(tag, "initViewPager ");
        pagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), objectNameBundle, isOperator);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
        if (getIntent().getIntExtra("tab_id", -1) != -1)
            viewPager.setCurrentItem(tabPosition);
        setTabIcons();
    }

    private void setTabIcons() {
        if (isOperator){
            setColor(tabLayout.getTabAt(0),R.drawable.ic_qr);
            setColor(tabLayout.getTabAt(1),R.drawable.search);
            setColor(tabLayout.getTabAt(2),R.drawable.category);
            setColor(tabLayout.getTabAt(3),R.drawable.percent);
        }
        else{
            setColor(tabLayout.getTabAt(0),R.drawable.search);
            setColor(tabLayout.getTabAt(1),R.drawable.category);
            setColor(tabLayout.getTabAt(2),R.drawable.percent);
        }
    }

    private void setColor(@Nullable TabLayout.Tab tab, int drawable){
        if (tab!=null)
        Objects.requireNonNull(tab.setIcon(drawable).getIcon())
                .setColorFilter(new PorterDuffColorFilter(getResources()
                .getColor(R.color.tabIconColor), PorterDuff.Mode.SRC_IN));
    }

    private void getUser() {
        animateLogo();

        GetUser user = retrofit.create(GetUser.class);

        Call<AuthResponse> authResponseCall = user.getUser(prefConfig.readId(),prefConfig.readToken());

        authResponseCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                int statusCode = response.code();
                Log.d("LOGGER Auth", "statusCode: " + statusCode);
                if (statusCode == 200) {
                    if (response.body() != null && response.body().getIsOperator()!=null)
                    isOperator = response.body().getIsOperator();
                } else {
                    // hide dialog
                    MainActivity.prefConfig.displayToast("Email или пароль были введены неверно!");
                }
                preloader_view.setVisibility(View.GONE);
                tabLayout.setVisibility(View.VISIBLE);
                initViewPager();
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // hide dialog
                MainActivity.prefConfig.displayToast("Произошла ошибка при попытке авторизации, попытайтесь снова.");
                preloader_view.setVisibility(View.GONE);
                tabLayout.setVisibility(View.VISIBLE);
                initViewPager();
            }
        });
    }
}