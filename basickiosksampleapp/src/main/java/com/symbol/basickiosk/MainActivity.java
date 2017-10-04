package com.symbol.basickiosk;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "BasicKiosk";
    private static final int REQUEST_ENABLE = 1234;

    private static Activity mActivity = null;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;
    private String mPackageName;

    private TextView mStatusTextView;
    private String mStatusTextString = "";
    private Button mStartKioskMode;
    private Button mStopKioskMode;
    private boolean mIsInKioskMode = false;

    private Button mSetHome;
    private Button mResetHome;
    private Button mRemoveDeviceOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bk_activity_main);

        // Uncomment this if you want a full screen app
        /*
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LOW_PROFILE;

                // Add this one if you want to leave full screen
                // with a vertical swipe
                //| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(flags);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                {

                    @Override
                    public void onSystemUiVisibilityChange(int visibility)
                    {
                        if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                        {
                            decorView.setSystemUiVisibility(flags);
                        }
                    }
                });
         */

        mActivity = this;

        mStatusTextView = (TextView)findViewById(R.id.textView_status);

        // Retrieve the package name
        mPackageName = this.getPackageName();

        // Retrieve the device policy manager
        mDPM = (DevicePolicyManager)mActivity.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminName = new ComponentName(this, DeviceAdminRightsReceiver.class);

        // Check DevAdmin rights and ask for them if not available
        enableAdmin();
    }

    private void enableAdmin(){
        if (!mDPM.isAdminActive(mAdminName)) {
            logMessageAndShowStatus("Not in device administrator mode.\nRequesting DevAdmin rights.");
            // try to become active – must happen here in this activity, to get result

            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    mAdminName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "Please give me admin rights...");
            startActivityForResult(intent, REQUEST_ENABLE);

        } else {
            // Already is a device administrator, can do security operations now.
            logMessageAndShowStatus("Already in device administrator mode");
            appIsDeviceAdmin();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_ENABLE == requestCode)
        {
            if (resultCode == Activity.RESULT_OK) {
                // Has become the device administrator.
                logMessageAndShowStatus("Has become the device administrator");
                appIsDeviceAdmin();
            } else {
                //Canceled or failed.
                logMessageAndShowStatus("Device administrator canceled or failed.\nRestart application and grant DeviceAdmin rights.\nOr do it with the settings menu.");
            }
        }
    }

    /**
     * Do what you want to do once you know that you are DeviceAdministrator
     */
    private void appIsDeviceAdmin()
    {
        // Do something only if we are the device Owner
        // Or do nothing except showing a message
        if(checkIsDeviceOwner())
        {
            // Declare that the current package may enter lock task mode
            mDPM.setLockTaskPackages(mAdminName,
                    new String[] { getPackageName() });

            initButtons();
        }

    }

    private void initButtons()
    {
        mStartKioskMode = (Button)findViewById(R.id.button_startkiosk);
        mStartKioskMode.setVisibility(View.VISIBLE);
        mStartKioskMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterKioskMode();
            }
        });

        mStopKioskMode = (Button)findViewById(R.id.button_stopkioskmode);
        mStopKioskMode.setVisibility(View.VISIBLE);
        mStopKioskMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitKioskMode();
            }
        });

        mSetHome = (Button)findViewById(R.id.button_sethome);
        mSetHome.setVisibility(View.VISIBLE);
        mSetHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHomeApp();
            }
        });

        mResetHome = (Button)findViewById(R.id.button_resethome);
        mResetHome.setVisibility(View.VISIBLE);
        mResetHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetHomeApp();
            }
        });

        mRemoveDeviceOwner = (Button)findViewById(R.id.button_removedevowner);
        mRemoveDeviceOwner.setVisibility(View.VISIBLE);
        mRemoveDeviceOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDeviceOwner();
            }
        });
    }

    private void removeDeviceOwner()
    {
        if(checkIsDeviceOwner())
        {
            mDPM.clearDeviceOwnerApp(mPackageName);
            logMessageAndShowStatus("Device owner has been removed");
        }
    }

    private void enterKioskMode()
    {
        if (mDPM.isLockTaskPermitted(this.getPackageName()) == false)
        {
            logMessageAndShowStatus("Kiosk Mode is not permitted");
            return;
        }

        if(mIsInKioskMode == false)
        {
            // Lock the app
            logMessageAndShowStatus("Entering Kiosk Mode");
            startLockTask();
            mIsInKioskMode = true;
            logMessageAndShowStatus("Kiosk Mode has been activated");
        }
        else
        {
            logMessageAndShowStatus("Kiosk mode is already running");
        }


    }

    private void exitKioskMode()
    {
        if (mDPM.isLockTaskPermitted(this.getPackageName()) == false)
        {
            logMessageAndShowStatus("Kiosk Mode is not permitted");
            return;
        }

        if(mIsInKioskMode == true)
        {
            logMessageAndShowStatus("Exiting Kiosk Mode");
            stopLockTask();
            mIsInKioskMode = false;
            logMessageAndShowStatus("Kiosk Mode has been stopped");
        }
        else
        {
            logMessageAndShowStatus("Kiosk mode is not running");
        }
    }

    private void setHomeApp()
    {
        if(checkIsDeviceOwner())
        {
            // We create the intent filter according to the messages we want this
            // class to respond to.
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
            intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
            intentFilter.addCategory(Intent.CATEGORY_HOME);

            // We will pass a reference to this instance of MainActivity
            ComponentName activity = new ComponentName(this, MainActivity.class);

            // Current instance of MainActivity will be registered as the intent default handler
            // for the previously configured intent filter.
            // A reference to the DevAdminReceiver class is passed to the Device Policy Manager.
            mDPM.addPersistentPreferredActivity(mAdminName, intentFilter, activity);

            logMessageAndShowStatus("This app is now the Default Home App");
        }
    }

    private void resetHomeApp()
    {
        if(checkIsDeviceOwner())
        {
                // Remove the settings associated with our device admin receriver class and package name.
            mDPM.clearPackagePersistentPreferredActivities(mAdminName,
                    mPackageName);
            logMessageAndShowStatus("The Default Home App has been restored");
        }
    }

    private boolean checkIsDeviceOwner()
    {
        if (!mDPM.isDeviceOwnerApp(mPackageName)) {
            logMessageAndShowStatus("This app is not the device owner!\nIt won't be able to override home application.\nPlease execute this line with adb:\nadb shell dpm set-device-owner com.symbol.emdkallsamples/com.symbol.basickiosk.DeviceAdminRightsReceiver");
            return false;
        }

        logMessageAndShowStatus("This app is the device owner!");

        return true;
    }

    private void logMessageAndShowStatus(String message)
    {
        Log.d(TAG, message);

        mStatusTextString += "\n" + message;
        final String fStatusTextString = mStatusTextString;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatusTextView.setText("Status: " + fStatusTextString);
            }
        });

    }
}
