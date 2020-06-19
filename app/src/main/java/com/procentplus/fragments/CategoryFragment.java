package com.procentplus.fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.procentplus.R;
import com.procentplus.activities.AuthActivity;
import com.procentplus.activities.MainActivity;
import com.procentplus.adapter.CategoryAdapter;
import com.procentplus.retrofit.RetrofitClient;
import com.procentplus.retrofit.interfaces.IAuthorization;
import com.procentplus.retrofit.interfaces.ICategories;
import com.procentplus.retrofit.interfaces.ILogout;
import com.procentplus.retrofit.models.AuthResponse;
import com.procentplus.retrofit.models.CategoriesResponse;
import com.procentplus.retrofit.models.MobileUser;
import com.procentplus.retrofit.models.SignRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

public class CategoryFragment extends Fragment {

    private RecyclerView categoryRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private ICategories iCategories;
    private IAuthorization iAuthorization;
    private Retrofit retrofit;

    private List<CategoriesResponse.ActivityType> categoriesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null);

        // init api
        retrofit = RetrofitClient.getInstance();

        // init recycler
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);

        getCategories();

        ImageView logout_btn = view.findViewById(R.id.logout);
        logout_btn.setOnClickListener(view1 -> logout());

        TextView tv_category_city = view.findViewById(R.id.tv_category_city);
        tv_category_city.setText("Курск");
        tv_category_city.setPaintFlags(tv_category_city.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        return view;
    }

    private void getCategories() {
        iCategories = retrofit.create(ICategories.class);

        Call<CategoriesResponse> categoriesResponseCall = iCategories.getCategories(
                MainActivity.prefConfig.readToken());

        categoriesResponseCall.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                int statusCode = response.code();
                Log.d("LOGGER Category", "statusCode: " + statusCode);
                if (statusCode == 200) {
                    if (categoriesList.size() > 0) categoriesList.clear();

                    categoriesList.addAll(response.body().getActivityTypes());
                    setUpRecyclerView();
                } else if (statusCode == 401) {
                    Log.d("LOGGER Category", "request new token");
                    getNewToken();
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {

            }
        });

    }

    private void getNewToken() {
        iAuthorization = retrofit.create(IAuthorization.class);
        Call<AuthResponse> authResponseCall = iAuthorization.getAccountData(
                new SignRequest(
                        new MobileUser(MainActivity.prefConfig.readPhone(),
                                MainActivity.prefConfig.readPassword()))
        );

        authResponseCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                int statusCode = response.code();
                Log.d("LOGGER CategoryToken", "statusCode: " + statusCode);
                if (statusCode == 200) {
                    String new_token = response.headers().get("Authorization");
                    MainActivity.prefConfig.writeToken(new_token);
                    getCategories();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                MainActivity.prefConfig.displayToast("Произошла ошибка при попытке авторизации, попытайтесь снова.");
            }
        });
    }

    private void setUpRecyclerView() {
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(getDrawable(getContext(), R.drawable.divider));

        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CategoryAdapter(categoriesList);
        categoryRecyclerView.addItemDecoration(itemDecorator);
        categoryRecyclerView.setAdapter(mAdapter);
    }

    private void logout() {
        ILogout iLogout = retrofit.create(ILogout.class);

        Call<CategoriesResponse> call = iLogout.logOut(MainActivity.prefConfig.readToken());

        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                int statusCode = response.code();
                Log.d("LOGGER Logout", "statusCode: " + statusCode);
                if (statusCode == 200 || statusCode == 204) {
                    MainActivity.prefConfig.writeLoginStatus(false);
                    Intent intent = new Intent(getContext(), AuthActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                MainActivity.prefConfig.displayToast("Произошла ошибка при попытке выхода из профиля");
            }
        });
    }
}
