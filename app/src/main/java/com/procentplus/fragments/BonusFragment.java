package com.procentplus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.procentplus.CreateQr;
import com.procentplus.R;
import com.procentplus.databinding.FragmentBonusBinding;
import com.procentplus.retrofit.models.User;


public class BonusFragment extends Fragment {

    private User userDetail;
    FragmentBonusBinding binding;

    public BonusFragment(User userDetail){
        this.userDetail = userDetail;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBonusBinding.inflate(inflater, container, false);

        binding.setUser(userDetail);
        if (userDetail.getName()==null) binding.tvName.setText(R.string.empty_name);
        if (userDetail.getEmail()==null) binding.tvMail.setText(R.string.empty_email);

        new CreateQr(userDetail.getName(), userDetail.getId(), binding.ivQr);

        return binding.getRoot();
    }
}
