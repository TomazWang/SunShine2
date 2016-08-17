package me.tomazwang.app.sunshine2;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

public class SettingFragment extends PreferenceFragment {

    private static final String TAG = SettingFragment.class.getSimpleName();

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: addPreferences");
        addPreferencesFromResource(R.xml.pref_general);
    }

}
