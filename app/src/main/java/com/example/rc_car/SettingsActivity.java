package com.example.rc_car;


import android.content.Intent;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import java.util.List;

public class SettingsActivity extends PreferenceActivity
{
    @Override
    public void onBuildHeaders(List<Header> target)
    {
        loadHeadersFromResource(R.xml.headers_preference, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return SettingsFragment.class.getName().equals(fragmentName);
    }

}