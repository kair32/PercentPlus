package com.procentplus.adapter;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.procentplus.fragments.BonusFragment;
import com.procentplus.fragments.CategoryFragment;
import com.procentplus.fragments.QrScannerFragment;
import com.procentplus.fragments.SearchFragment;
import com.procentplus.retrofit.models.AuthResponse;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;
    private String[] name;
    private Bundle data;

    public CustomPagerAdapter(FragmentManager fm, Bundle data, AuthResponse userDetail) {
        super(fm);
        this.data = data;
        if (userDetail.getIsOperator()) {
            fragments = new Fragment[] {new QrScannerFragment(),new SearchFragment(),new CategoryFragment(), createBonusFragment(userDetail)};
            name = new String[] {"Сканировать", "Поиск", "Категория", "Бонус"};
        }
        else {
            fragments = new Fragment[]{new SearchFragment(), new CategoryFragment(), createBonusFragment(userDetail)};
            name = new String[] {"Поиск", "Категория", "Бонус"};
        }
    }

    @Override
    public Fragment getItem(int position) { return fragments[position]; }

    private Fragment createBonusFragment(AuthResponse userDetail){
        BonusFragment bonusFragment = new BonusFragment(userDetail);
        bonusFragment.setArguments(this.data);
        return bonusFragment;
    }
    @Override
    public int getCount() {return fragments.length; }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {return name[position]; }
}
