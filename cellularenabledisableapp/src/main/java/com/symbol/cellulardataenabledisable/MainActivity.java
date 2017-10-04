/*
* Copyright (C) 2015-2016 Symbol Technologies LLC
* All rights reserved.
* Author: Trudu Laurent
* Source: http://stackoverflow.com/questions/11555366/enable-disable-data-connection-in-android-programmatically#11555457
*/
package com.symbol.cellulardataenabledisable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static MainActivity mMainActivity;

    private TextView statusTextView = null;
    private RadioGroup wirelessRadioGroup = null;

    private boolean value = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainActivity = this;

        setContentView(R.layout.cd_activity_main);

        statusTextView = (TextView)findViewById(R.id.textViewStatus);
        wirelessRadioGroup = (RadioGroup)findViewById(R.id.radioGroupWireless);

        addSetButtonListener();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void addSetButtonListener() {

        Button setButton = (Button)findViewById(R.id.buttonSet);

        setButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                int radioid = wirelessRadioGroup.getCheckedRadioButtonId();

                if(radioid == R.id.radioEnable ) {
                        value = true; // 1 - Enable Cellular Data
                }
                else if(radioid == R.id.radioDisable)
                {
                        value = false; // 2 - Disable Cellular Data
                }

                boolean success = true;
                try
                {
                    setMobileDataEnabled(mMainActivity, value);
                }
                catch(Exception excp)
                {
                    success = false;
                }

                if(success)
                {
                    statusTextView.setText("Success");
                }
                else
                {
                    statusTextView.setText("Failed");
                }

            }
        });
    }

    private void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ConnectivityManager conman = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
        connectivityManagerField.setAccessible(true);
        final Object connectivityManager = connectivityManagerField.get(conman);
        final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
    }
}
