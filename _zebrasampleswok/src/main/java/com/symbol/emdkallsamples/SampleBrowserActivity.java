package com.symbol.emdkallsamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import java.util.List;

public class SampleBrowserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewAnimator mViewAnimator = null;

    private TextView mIntroText = null;
    private RelativeLayout mIntroRelativeLayout = null;

    private eNavigationState mNavigationState = eNavigationState.NONE;
    private int mNavigationLevel = -1;

    private SamplesDescription mSamplesDescription = null;

    private Animation mInFromLeftAnim;
    private Animation mInFromRightAnim;
    private Animation mOutToLeftAnim;
    private Animation mOutToRightAnim;

    private Animation mAlphaIn;
    private Animation mAlphaOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samplebrowser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mInFromLeftAnim = AnimationUtils.loadAnimation(this,R.anim.in_from_left);
        mInFromRightAnim = AnimationUtils.loadAnimation(this,R.anim.in_from_right);
        mOutToLeftAnim = AnimationUtils.loadAnimation(this,R.anim.out_to_left);
        mOutToRightAnim = AnimationUtils.loadAnimation(this,R.anim.out_to_right);
        mAlphaIn = AnimationUtils.loadAnimation(this,R.anim.alpha_in);
        mAlphaOut = AnimationUtils.loadAnimation(this,R.anim.alpha_out);

        // Setup view animator
        mViewAnimator = (ViewAnimator)findViewById(R.id.va_samplelist);

        mIntroText = (TextView)findViewById(R.id.tv_introtext);
        mIntroRelativeLayout = (RelativeLayout)findViewById(R.id.rl_introlayout);

        mSamplesDescription = new SamplesDescription(this);
        setupDetailView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // We are coming back from a sample
        // Do nothing
        if(mNavigationLevel > 0)
            return;
        setupDetailView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(mNavigationLevel == -1)
            {
                boolean sentAppToBackground = moveTaskToBack(true);
                if(!sentAppToBackground){
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_MAIN);
                    i.addCategory(Intent.CATEGORY_HOME);
                    this.startActivity(i);
                }
            }
            else
            {
                //enable backward animation
                enableListViewAnimations(true, false);
                mViewAnimator.showPrevious();
                mViewAnimator.removeViewAt(mNavigationLevel);
                mNavigationLevel--;
                if(mNavigationLevel == -1)
                {
                    mNavigationState = eNavigationState.NONE;
                    drawer.openDrawer(Gravity.LEFT);
                    setupDetailView();
                }
                updateTitle();
            }
        }
    }

    private void enableListViewAnimations(boolean enable, boolean forward)
    {
        if(enable)
        {
            if(forward)
            {
                mViewAnimator.setInAnimation(mInFromRightAnim);
                mViewAnimator.setOutAnimation(mOutToLeftAnim);
            }
            else
            {
                mViewAnimator.setInAnimation(mInFromLeftAnim);
                mViewAnimator.setOutAnimation(mOutToRightAnim);
            }
        }
        else
        {
            // Disable animations to force fast refresh with new list
            mViewAnimator.setInAnimation(mAlphaIn);
            mViewAnimator.setOutAnimation(mAlphaOut);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_api)
        {
            mNavigationState = eNavigationState.BYAPI;
        }
        else if (id == R.id.nav_usecase)
        {
            mNavigationState = eNavigationState.BYUSECASE;
        }
        else if(id == R.id.nav_alphabetical)
        {
            mNavigationState = eNavigationState.ALPHABETICALLY;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        setupDetailView();
        return true;
    }

    private void updateTitle()
    {
        switch(mNavigationState)
        {
            case NONE:
            {
                setTitle(R.string.title_activity_main);
                break;
            }
            case ALPHABETICALLY:
            {
                setTitle(R.string.title_activity_selectsample);
                break;
            }
            case BYAPI:
            {
                if(mNavigationLevel == 0)
                {
                    setTitle(R.string.title_activity_api);
                }
                else
                {
                    setTitle(R.string.title_activity_selectsample);
                }
                break;
            }
            case BYUSECASE:
            {
                if(mNavigationLevel == 0)
                {
                    setTitle(R.string.title_activity_usecase);
                }
                else
                {
                    setTitle(R.string.title_activity_selectsample);
                }
                break;
            }
        }
    }

    private void setupDetailView()
    {
        if(mNavigationState == eNavigationState.NONE)
        {
            mViewAnimator.setVisibility(View.GONE);
            mIntroRelativeLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            mViewAnimator.setVisibility(View.VISIBLE);
            mIntroRelativeLayout.setVisibility(View.GONE);
        }

        switch(mNavigationState)
        {
            case NONE:
            {
                mNavigationLevel = -1;
                // No animation when removing views
                enableListViewAnimations(false, true);
                mViewAnimator.removeAllViews();
                mViewAnimator.destroyDrawingCache();
                break;
            }
            case ALPHABETICALLY:
            {
                // No animation on first call
                enableListViewAnimations(false, true);
                mViewAnimator.removeAllViews();
                mViewAnimator.destroyDrawingCache();

                SBSampleItemsArrayAdapter adapter = new SBSampleItemsArrayAdapter(this, R.layout.samplebrowser_listview_item_row, mSamplesDescription.mSampleItemsAlphabetically);

                mNavigationLevel = 0;

                ListView sampleListView = new ListView(this);
                sampleListView.setAdapter(adapter);
                sampleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String sampleTitle = mSamplesDescription.mSampleTitlesAlphabetically.get(position);
                        Intent intent = new Intent(SampleBrowserActivity.this, mSamplesDescription.getClass(sampleTitle));
                        startActivity(intent);
                    }
                });
                mViewAnimator.addView(sampleListView);
                break;
            }
            case BYAPI:
            {
                // No animation on first call
                enableListViewAnimations(false, true);
                mViewAnimator.removeAllViews();
                mViewAnimator.destroyDrawingCache();

                ArrayAdapter<String> apiadapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, mSamplesDescription.mAPIsListWithNumber);

                mNavigationLevel = 0;

                ListView apiListView = new ListView(this);
                apiListView.setAdapter(apiadapter);
                apiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        // One api has been selected let's push a list of associated samples to the view animator
                        String apiTitle = mSamplesDescription.mAPIsList.get(position);

                        final List<SampleItem> sampleList = mSamplesDescription.mSampleItemsByAPI.get(apiTitle);
                        SBSampleItemsArrayAdapter adapter = new SBSampleItemsArrayAdapter(SampleBrowserActivity.this, R.layout.samplebrowser_listview_item_row, sampleList);

                        mNavigationLevel = 1;

                        ListView samplesListView = new ListView(SampleBrowserActivity.this);
                        samplesListView.setAdapter(adapter);
                        samplesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                SampleItem sampleitem = sampleList.get(position);
                                Intent intent = new Intent(SampleBrowserActivity.this, sampleitem.mClass);
                                startActivity(intent);
                            }
                        });
                        // Enable forward animation
                        enableListViewAnimations(true, true);
                        mViewAnimator.addView(samplesListView);
                        mViewAnimator.showNext();
                        updateTitle();
                    }
                });
                mViewAnimator.addView(apiListView);
                break;
            }
            case BYUSECASE:
            {
                // No animation on first call
                enableListViewAnimations(false, true);
                mViewAnimator.removeAllViews();
                mViewAnimator.destroyDrawingCache();

                ArrayAdapter<String> usecaseadapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, mSamplesDescription.mUseCasesListWithNumber);
                mNavigationLevel = 0;
                ListView usecaseListView = new ListView(this);
                usecaseListView.setAdapter(usecaseadapter);
                usecaseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        // One api has been selected let's push a list of associated samples to the view animator
                        String usecaseTitle = mSamplesDescription.mUseCasesList.get(position);

                        final List<SampleItem> useCaseList = mSamplesDescription.mSampleItemsByUseCases.get(usecaseTitle);
                        SBSampleItemsArrayAdapter adapter = new SBSampleItemsArrayAdapter(SampleBrowserActivity.this, R.layout.samplebrowser_listview_item_row, useCaseList);

                        mNavigationLevel = 1;
                        ListView useCaseSamplesListView = new ListView(SampleBrowserActivity.this);
                        useCaseSamplesListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        useCaseSamplesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                SampleItem sampleitem = useCaseList.get(position);
                                Intent intent = new Intent(SampleBrowserActivity.this, sampleitem.mClass);
                                startActivity(intent);
                            }
                        });
                        // Enable forward animation
                        enableListViewAnimations(true, true);
                        mViewAnimator.addView(useCaseSamplesListView);
                        mViewAnimator.showNext();
                        updateTitle();
                    }
                });

                mViewAnimator.addView(usecaseListView);
                break;
            }
        }
        updateTitle();
    }
}
