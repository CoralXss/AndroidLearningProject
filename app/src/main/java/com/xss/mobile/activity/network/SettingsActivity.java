package com.xss.mobile.activity.network;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;


/**
 * Created by xss on 2016/11/5.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
}
