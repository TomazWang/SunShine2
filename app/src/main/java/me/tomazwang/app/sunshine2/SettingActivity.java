package me.tomazwang.app.sunshine2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SettingFragment fragment = SettingFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container,fragment)
                .commit();


    }




}
