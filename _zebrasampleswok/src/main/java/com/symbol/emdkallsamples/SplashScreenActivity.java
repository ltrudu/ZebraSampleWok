package com.symbol.emdkallsamples;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zebra.kdu.SplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int DEMO_PERMISSION = 1;
    private static final String[] DEMO_PERMISSIONS_LIST = new String[]{
            Manifest.permission.NFC,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.VIBRATE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    private static long WAIT_MILLISECONDS = 700;

    private Class<?> mStartupClass = SampleBrowserActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreenWindow();

        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.activity_splash_screen, null, false);

        addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        ImageView imageView = (ImageView)findViewById(R.id.iv_logozebra);
        imageView.setImageResource(0);
        Drawable draw = ContextCompat.getDrawable(this, R.drawable.logozebra24);
        draw = resize(draw, 1.0f, 1.0f);
        imageView.setImageDrawable(draw);

        checkPermissions();

    }

    private void setFullScreenWindow()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void requestLaunchNextActivity()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {

                Intent intent = new Intent(SplashScreenActivity.this,
                        mStartupClass);
                startActivity(intent);

            }

        }, WAIT_MILLISECONDS);
    }

    private Drawable resize(Drawable image, float scaleX, float scaleY) {
        Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap,
                (int) (bitmap.getWidth() * scaleX), (int) (bitmap.getHeight() * scaleY), false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }


    private void checkPermissions()
    {
        boolean shouldNotRequestPermissions = true;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            for(String permission : DEMO_PERMISSIONS_LIST)
            {
                shouldNotRequestPermissions &= (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED);
            }
        }

        if (shouldNotRequestPermissions) {
            requestLaunchNextActivity();
        }
        else
        {
            ActivityCompat.requestPermissions(this,DEMO_PERMISSIONS_LIST, DEMO_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case DEMO_PERMISSION:
                boolean allPermissionGranted = true;
                for(int grantResult : grantResults)
                {
                    allPermissionGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
                }
                if (allPermissionGranted) {
                    requestLaunchNextActivity();
                } else {
                    ShowAlertDialog(SplashScreenActivity.this, "Error", "Please grant the necessary permission to launch the application.");
                }
                return;
        }
    }

    private void ShowAlertDialog(Context context, String title, String message)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        checkPermissions();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
