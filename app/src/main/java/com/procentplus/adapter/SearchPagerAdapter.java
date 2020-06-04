package com.procentplus.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.procentplus.fragments.searchFragments.ByAddressFragment;
import com.procentplus.fragments.searchFragments.ByCodeFragment;
import com.procentplus.fragments.searchFragments.ByNameFragment;

public class SearchPagerAdapter extends FragmentPagerAdapter {

    public SearchPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new ByNameFragment();
            case 1: return new ByCodeFragment();
            //case 2: return new ByAddressFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "По названию";
            case 1: return "По коду объекта";
            //case 2: return "По адресу";
            default: return null;
        }
    }
}
