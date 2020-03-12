package com.procentplus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.procentplus.databinding.FragmentScannerBinding;
import com.procentplus.retrofit.RetrofitClient;

import retrofit2.Retrofit;

public class QrScannerFragment extends Fragment {
    FragmentScannerBinding binding;
    private Retrofit retrofit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // init api
        retrofit = RetrofitClient.getInstance();

        binding = FragmentScannerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
