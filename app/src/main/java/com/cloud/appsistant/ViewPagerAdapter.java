package com.cloud.appsistant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public static CardsFragment currentF;
    public static CalendarFragment currentCalendar;

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            CalendarFragment calendarFragment = new CalendarFragment();
            currentCalendar = calendarFragment;
            return calendarFragment;
        } else {
            CardsFragment cardsFragment = new CardsFragment();
            currentF = cardsFragment;
            return cardsFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
            return "calendar";
        else
            return "cards";
    }
}
