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

public class CustomPagerAdapter extends FragmentPagerAdapter {

    private Bundle data;

    public CustomPagerAdapter(FragmentManager fm, Bundle data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new QrScannerFragment();
            case 1: return new SearchFragment();
            case 2: return new CategoryFragment();
            case 3:
                final BonusFragment bonusFragment = new BonusFragment();
                bonusFragment.setArguments(this.data);
                return bonusFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Сканировать";
            case 1: return "Поиск";
            case 2: return "Категория";
            case 3: return "Бонус";
            default: return null;
        }
    }
}
