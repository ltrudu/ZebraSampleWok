package com.symbol.emdkallsamples;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeMap;

public class OrderedSampleListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mSampleAppListView = null;
    private ArrayList<String> mListItems = null;

    private int mSelectedNavigation = -1;
    private TextView mIntroText = null;
    private RelativeLayout mIntroRelativeLayout = null;

    private SampleItems mSampleItems = new SampleItems();

    private eAPIEnums mSelectedAPI = eAPIEnums.NONE;
    private eNAVIGATION mCurrentNavigation = eNAVIGATION.NONE;

    private enum eNAVIGATION
    {
        NONE,
        BYAPI_LIST,
        BYAPI_SELECTED,
        BYUSECASE_LIST,
        BYUSECASE_SELECTED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_sample_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mIntroText = (TextView)findViewById(R.id.tv_introtext);
        mIntroRelativeLayout = (RelativeLayout)findViewById(R.id.rl_introlayout);
        mSampleAppListView = (ListView) findViewById(R.id.lv_selectedsamplelist);

        mSampleAppListView.setVisibility(View.GONE);
        mIntroRelativeLayout.setVisibility(View.VISIBLE);
    }

    private void setupSampleList()
    {
        if(mSelectedNavigation == -1)
        {
            mListItems = new ArrayList<>();
            mListItems.add("Swipe Left to Right");
            mListItems.add("to open the drawer and");
            mListItems.add("select a theme.");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, mListItems);

            mSampleAppListView.setAdapter(adapter);

            mSampleAppListView.setOnItemClickListener(null);
            return;
        }

        String navString = String.valueOf(mSelectedNavigation);

        mListItems = new ArrayList<>();
        mListItems.addAll(mSampleItems.mAPIs.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mListItems);

        mSampleAppListView.setAdapter(adapter);

        mSampleAppListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String navString = String.valueOf(mSelectedNavigation);

                /*
                TreeMap<String, Class<?>> samples = mSamplesMap.get(navString);
                Class<?> toLaunch = (Class<?>)samples.values().toArray()[position];
                Intent intent = new Intent(OrderedSampleListActivity.this, toLaunch);
                startActivity(intent);
                */
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // Minimise application
            moveTaskToBack(true);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (    id == R.id.nav_api) {
            mSampleAppListView.setVisibility(View.VISIBLE);
            mIntroRelativeLayout.setVisibility(View.GONE);
            mSelectedNavigation = id;
            mCurrentNavigation = eNAVIGATION.BYAPI_LIST;
            setupSampleList();
        }
        else if (id == R.id.nav_usecase ||
                id == R.id.nav_alphabetical) {
            mSampleAppListView.setVisibility(View.GONE);
            mIntroRelativeLayout.setVisibility(View.VISIBLE);
            mIntroText.setText("Coming SOOOOOON !!!");
        }

        /*
        if (id == R.id.nav_emdk) {
        }
        else if (id == R.id.nav_profilemanager) {
        }
        else if (id == R.id.nav_simulscan) {
        }
        else if (id == R.id.nav_datawedge) {
        }
        else if (id == R.id.nav_androidapi) {
        }
        else if (id == R.id.nav_printer) {
        }
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
