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

    private Fragment[] fragments = new Fragment[] {new QrScannerFragment(),new SearchFragment(),new CategoryFragment(),createBonusFragment()};
    private Fragment[] fragmentsSmall = new Fragment[] {new SearchFragment(),new CategoryFragment(),createBonusFragment()};
    private String[] name = new String[] {"Сканировать", "Поиск", "Категория", "Бонус"};
    private String[] nameSmall = new String[] {"Поиск", "Категория", "Бонус"};
    private Bundle data;
    private Boolean isOperator;

    public CustomPagerAdapter(FragmentManager fm, Bundle data, Boolean isOperator) {
        super(fm);
        this.data = data;
        this.isOperator = isOperator;
    }

    @Override
    public Fragment getItem(int position) {
        if (isOperator) return fragments[position];
        else return fragmentsSmall[position];
    }

    private Fragment createBonusFragment(){
        BonusFragment bonusFragment = new BonusFragment();
        bonusFragment.setArguments(this.data);
        return bonusFragment;
    }
    @Override
    public int getCount() {
        if (isOperator) return fragments.length;
        else            return fragmentsSmall.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (isOperator) return name[position];
        else return nameSmall[position];
    }
}
