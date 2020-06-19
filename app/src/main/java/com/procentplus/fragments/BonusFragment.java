package com.procentplus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.procentplus.CreateQr;
import com.procentplus.R;
import com.procentplus.activities.AuthActivity;
import com.procentplus.activities.MainActivity;
import com.procentplus.databinding.FragmentBonusBinding;
import com.procentplus.retrofit.RetrofitClient;
import com.procentplus.retrofit.interfaces.ILogout;
import com.procentplus.retrofit.models.CategoriesResponse;
import com.procentplus.retrofit.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class BonusFragment extends Fragment {

    private User userDetail;
    FragmentBonusBinding binding;
    private Retrofit retrofit;

    public BonusFragment(User userDetail){
        this.userDetail = userDetail;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBonusBinding.inflate(inflater, container, false);

        // init api
        retrofit = RetrofitClient.getInstance();

        binding.setUser(userDetail);
        if (userDetail.getName()==null) binding.tvName.setText(R.string.empty_phone);

        new CreateQr(userDetail.getName(), binding.ivQr);

        binding.logout.setOnClickListener(view1 -> logout());
        return binding.getRoot();
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
