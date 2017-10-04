package com.symbol.batteryinfosample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "BatteryInfoSample";
    private String BATTERY_STATE_CHANGED_INTENT = "android.intent.action.BATTERY_CHANGED";
    private BatteryIntentReceiver mIntentReceiver;
    private IntentFilter mIntentFilter;
    private boolean mLayoutCreated = false;

    private HashMap<String, TextView> mTextViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBatteryReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterBatteryReceiver();
    }

    private void unregisterBatteryReceiver()
    {
        unregisterReceiver(mIntentReceiver);
    }

    private void registerBatteryReceiver()
    {
        mIntentReceiver = new BatteryIntentReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mIntentReceiver,mIntentFilter);
    }

    private void createLayout(Bundle aBundle)
    {
        mTextViews = new HashMap<String, TextView>();

        ScrollView hostView = new ScrollView(this);

        TableLayout table = new TableLayout(this);

        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("Battery Informations");

        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 3;
        rowTitle.addView(title, params);

        table.addView(rowTitle);

        TableRow columns = new TableRow(this);
        columns.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView columnKey = new TextView(this);
        columnKey.setText("Key");
        columns.addView(columnKey);

        TextView columnsValue = new TextView(this);
        columnsValue.setText("Value");
        columns.addView(columnsValue);

        TextView columnsType = new TextView(this);
        columnsType.setText("Type");
        columns.addView(columnsType);

        table.addView(columns);

        if (aBundle != null) {
            List<String> keySet = new ArrayList(aBundle.keySet());
            java.util.Collections.sort(keySet);
            for (String key : keySet) {
                Object value = aBundle.get(key);
                if(mTextViews.containsKey(key) == false)
                {
                    Log.d(TAG, "Creating new row with key: " + key);
                            // Create new row
                    TableRow newRow = new TableRow(this);

                    TextView titleLabel = new TextView(this);
                    titleLabel.setText(ellipsize(key,22));

                    newRow.addView(titleLabel);

                    TextView titleValue = new TextView(this);
                    titleValue.setText(value.toString());

                    newRow.addView(titleValue);

                    TextView titleType = new TextView(this);
                    titleType.setText(value.getClass().getSimpleName());

                    newRow.addView(titleType);

                    table.addView(newRow);

                    mTextViews.put(key, titleValue);

                    Log.d(TAG, "Row with key: " + key + " has been created.");
                }
                Log.d(TAG, String.format("%s %s (%s)", key,
                        value.toString(), value.getClass().getName()));
            }
        }

        hostView.addView(table);
        setContentView(hostView);
        Log.d(TAG, "Layout created");
        mLayoutCreated = true;
    }

    private void updateValues(Bundle aBundle)
    {
        if(mLayoutCreated == false)
        {
            createLayout(aBundle);
        }

        final Bundle fBundle = aBundle;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fBundle != null && mTextViews.size() > 0) {
                    for (String key : fBundle.keySet()) {
                        Object value = fBundle.get(key);
                        if(mTextViews.containsKey(key))
                        {
                            TextView target = (TextView)mTextViews.get(key);
                            target.setText(value.toString());
                        }
                        else
                        {
                            //Key not found
                            Log.d(TAG, String.format("KEY NOT FOUND: %s %s (%s)", key,
                                    value.toString(), value.getClass().getName()));
                        }
                        //Log.d(TAG, String.format("%s %s (%s)", key,
                        //        value.toString(), value.getClass().getName()));
                    }
                }
            }
        });
    }

    public class BatteryIntentReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (BATTERY_STATE_CHANGED_INTENT.equals(intent.getAction())) {
                Bundle bundle = intent.getExtras();
                updateValues(bundle);
            }
        }
    }


    String ellipsize(String input, int maxLength) {
        String ellip = "...";
        String returnString = "";
        if (input == null || input.length() <= maxLength
                || input.length() < ellip.length()) {
            return input;
        }
        return input.substring(0, maxLength - ellip.length()).concat(ellip);
    }


}
