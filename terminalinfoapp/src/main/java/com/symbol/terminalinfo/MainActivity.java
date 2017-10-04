package com.symbol.terminalinfo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.glxn.qrgen.android.QRCode;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Copyright: Zebra 2016
 * Sources:
 * - QRCode: https://github.com/kenglxn/QRGen
 * - Serial Number: https://developer.zebra.com/community/android/android-forums/android-blogs/blog/2014/09/02/getting-device-info-from-msi-android-devices
 * - IMEI Number and telephony: http://stackoverflow.com/questions/1972381/how-to-get-the-devices-imei-esn-programmatically-in-android
 * - Mac Adress: http://stackoverflow.com/questions/33103798/ddg#35830358
 * - Wifi Enable/Disable: http://stackoverflow.com/questions/3930990/android-how-to-enable-disable-wifi-or-internet-connection-programmatically
 */


public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1;
    private static final int MY_PERMISSIONS_REQUEST_CHANGE_WIFI_STATE = 2;

    private String imei_number = "unknown";
    private String phoneNumber = "unknown";
    private String simSerialNumber = "unknown";
    private String serialNumber = "unknown";
    private String macAddress = "unknown";
    private String ethMacAddress = "unknown";
    private String allcodes = "unknown";
    private String allcodescontent = "unknown";

    TextView txt_serial;
    ImageView iv_serial;
    RelativeLayout rl_serial;
    TextView txt_imei;
    ImageView iv_imei;
    RelativeLayout rl_imei;
    TextView    txt_numtel;
    ImageView   iv_numtel;
    RelativeLayout rl_numtel;
    TextView    txt_simserial;
    ImageView   iv_simserial;
    RelativeLayout rl_simserial;
    TextView    txt_mac;
    ImageView   iv_mac;
    RelativeLayout rl_mac;
    TextView    txt_ethmac;
    ImageView   iv_ethmac;
    RelativeLayout rl_ethmac;

    RelativeLayout rl_allcodes;
    ImageView iv_allcodes;
    TextView txt_allcodes_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ti_activity_main);

        // Retrieve the target controls
        txt_serial     = (TextView)findViewById(R.id.txt_serialnumber);
        iv_serial     = (ImageView)findViewById(R.id.iv_serialnumber);
        rl_serial      = (RelativeLayout)findViewById(R.id.rl_serialnumber);

        txt_imei       = (TextView)findViewById(R.id.txt_imei);
        iv_imei       = (ImageView)findViewById(R.id.iv_imei);
        rl_imei      = (RelativeLayout)findViewById(R.id.rl_imei);

        txt_numtel  = (TextView)findViewById(R.id.txt_numtel);
        iv_numtel   = (ImageView)findViewById(R.id.iv_numtel);
        rl_numtel      = (RelativeLayout)findViewById(R.id.rl_numtel);

        txt_simserial  = (TextView)findViewById(R.id.txt_simserial);
        iv_simserial   = (ImageView)findViewById(R.id.iv_simserial);
        rl_simserial      = (RelativeLayout)findViewById(R.id.rl_simserial);

        txt_mac     = (TextView)findViewById(R.id.txt_macadress);
        iv_mac      = (ImageView)findViewById(R.id.iv_macadress);
        rl_mac      = (RelativeLayout)findViewById(R.id.rl_macadress);

        txt_ethmac     = (TextView)findViewById(R.id.txt_ethmacadress);
        iv_ethmac      = (ImageView)findViewById(R.id.iv_ethmacadress);
        rl_ethmac      = (RelativeLayout)findViewById(R.id.rl_ethmacadress);

        rl_allcodes    = (RelativeLayout)findViewById(R.id.rl_allcodes);
        iv_allcodes = (ImageView)findViewById(R.id.iv_allcodes);
        txt_allcodes_content = (TextView)findViewById(R.id.txt_allcodes_content);

        Button refreshButton = (Button)findViewById(R.id.bt_refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetValues();
                updateRLVisibility();
                updateValues();
            }
        });

        // Petit bout de code qui ne sert à rien ;)
        String manufacturer = android.os.Build.MANUFACTURER;
        if(manufacturer.contains("Zebra Technologies") || manufacturer.contains("Motorola Solutions"))
        {
            // C'est un device Zebra
            Log.d("MonTag", "Manufacturer = " + manufacturer);
        }

        updateValues();
        //setAllNifNames();
    }

    private void resetValues()
    {
        imei_number = "unknown";
        phoneNumber = "unknown";
        simSerialNumber = "unknown";
        serialNumber = "unknown";
        macAddress = "unknown";
        ethMacAddress = "unknown";
        allcodes = "unknown";
        allcodescontent = "unknown";
    }

    private void updateValues()
    {
        // Retrieve device informations
        getSerialNumber();
        getTelephonyInfo();
        enableWifi();
        getMacAddress();
        disableWifi();
        updateAllCodes();
        updateRLVisibility();
    }

    private void updateRLVisibility()
    {
        rl_serial.setVisibility(serialNumber.equals("unknown") ? View.GONE : View.VISIBLE);
        rl_imei.setVisibility(imei_number.equals("unknown") ? View.GONE : View.VISIBLE);
        rl_numtel.setVisibility(phoneNumber.equals("unknown") ? View.GONE : View.VISIBLE);
        rl_simserial.setVisibility(simSerialNumber.equals("unknown") ? View.GONE : View.VISIBLE);
        rl_mac.setVisibility(macAddress.equals("unknown") ? View.GONE : View.VISIBLE);
        rl_ethmac.setVisibility(ethMacAddress.equals("unknown") ? View.GONE : View.VISIBLE);
        rl_allcodes.setVisibility(allcodes.length() == 0 ? View.GONE : View.VISIBLE);
    }

    private void updateAllCodes()
    {
        allcodes = addIfNotUnknow("", serialNumber, serialNumber, "\u0009");
        allcodescontent = addIfNotUnknow("", serialNumber, "Serial number", " | ");

        allcodes = addIfNotUnknow(allcodes, imei_number, imei_number, "\u0009");
        allcodescontent = addIfNotUnknow(allcodescontent, imei_number, "Imei", " | ");

        allcodes = addIfNotUnknow(allcodes, phoneNumber, phoneNumber, "\u0009");
        allcodescontent = addIfNotUnknow(allcodescontent, phoneNumber, "Phone number", " | ");

        allcodes = addIfNotUnknow(allcodes, simSerialNumber, simSerialNumber, "\u0009");
        allcodescontent = addIfNotUnknow(allcodescontent, simSerialNumber, "Sim serial number", " | ");

        allcodes = addIfNotUnknow(allcodes, macAddress, macAddress, "\u0009");
        allcodescontent = addIfNotUnknow(allcodescontent, macAddress, "Wifi mac address", " | ");

        allcodes = addIfNotUnknow(allcodes, ethMacAddress, ethMacAddress, "\u0009");
        allcodescontent = addIfNotUnknow(allcodescontent, ethMacAddress, "Ethernet mac", " | ");

        iv_allcodes.setImageBitmap(getQRCodeBitmap(allcodes));
        txt_allcodes_content.setText(allcodescontent);
    }

    private String addIfNotUnknow(String baseString, String toCheck, String toAdd, String separator)
    {
        String returnString = baseString;
        if(toCheck.equals("unknown") == false)
        {
            if(baseString.length() > 0)
            {
                returnString += separator;
            }
            returnString += toAdd;
        }
        return returnString;
    }

    private void getSerialNumber()
    {
        serialNumber = checkString(Build.SERIAL);

        // Fill the controls
        txt_serial.setText("Serial Number = " + serialNumber);
        iv_serial.setImageBitmap(getQRCodeBitmap(serialNumber));

    }

    private void getTelephonyInfo()
    {
        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_PHONE_STATE permission has not been granted.
            requestReadPhoneStatePermission();
        }
        else
        {
            // READ_PHONE_STATE permission is already been granted.
            getTelephonyManagerInfos();
        }
    }

    private void enableWifi()
    {
        // Check if the CHANGE_WIFI_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // CHANGE_WIFI_STATE permission has not been granted.
            requestChangeWifiStatePermission();
        }
        else
        {
            // CHANGE_WIFI_STATE permission is already been granted.
            setWifiState(true);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void disableWifi()
    {
        setWifiState(false);
    }

    private void getMacAddress()
    {
        // Check if the INTERNET permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // INTERNET permission has not been granted.
            requestInternetPermission();
        }
        else
        {
            // INTERNET permission is already been granted.
            getMacAddr();
        }
    }

    /**
     * Requests the READ_PHONE_STATE permission.
     * If the permission has been denied previously, a dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission Request")
                    .setMessage("Please give me access to your phone informations")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                        }
                    })
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    private void requestChangeWifiStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CHANGE_WIFI_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission Request")
                    .setMessage("Please allow me to change the wifi state")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CHANGE_WIFI_STATE},
                                    MY_PERMISSIONS_REQUEST_CHANGE_WIFI_STATE);
                        }
                    })
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CHANGE_WIFI_STATE},
                    MY_PERMISSIONS_REQUEST_CHANGE_WIFI_STATE);
        }
    }

    private void requestInternetPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.INTERNET)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission Request")
                    .setMessage("Please give me access to your internet informations")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.INTERNET},
                                    MY_PERMISSIONS_REQUEST_INTERNET);
                        }
                    })
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_INTERNET);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            // Received permission result for READ_PHONE_STATE permission.est.");
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // READ_PHONE_STATE permission has been granted, proceed with displaying IMEI Number, phone number, sim serial number
                getTelephonyManagerInfos();
            } else {
                alertAlert("Permission to access phone informations not granted");
            }
        }
        else if(requestCode == MY_PERMISSIONS_REQUEST_INTERNET){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // INTERNET permission has been granted, proceed with displaying MAC address
                getMacAddr();
            } else {
                alertAlert("Permission to access internet informations not granted");
            }
        }
        else if(requestCode == MY_PERMISSIONS_REQUEST_CHANGE_WIFI_STATE){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // CHANGE_WIFI_STATE permission has been granted, proceed with enabling wifi
                setWifiState(true);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                alertAlert("Permission to access internet informations not granted");
            }
        }
    }

    private void alertAlert(String msg) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Permission Request")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do somthing here
                    }
                })
                .show();
    }

    private Bitmap getQRCodeBitmap(String inputString)
    {
        String toQRCodeString = checkString(inputString);
        return QRCode.from(toQRCodeString).bitmap();
    }

    private String checkString(String inputString)
    {
        if(inputString == null || inputString == "" || inputString.length() == 0)
        {
            return "unknown";
        }
        return inputString;
    }

    private void getTelephonyManagerInfos() {
        try
        {
            //Have an  object of TelephonyManager
            TelephonyManager tm =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

            if(tm != null)
            {
                imei_number= tm.getDeviceId();
                phoneNumber = tm.getLine1Number();
                simSerialNumber = tm.getSimSerialNumber();
            }
            else
            {
                imei_number= "unknown";
                phoneNumber = "unknown";
                simSerialNumber = "unknown";
            }
        }
        catch(Exception excp)
        {
            imei_number= "unknown";
            phoneNumber = "unknown";
            simSerialNumber = "unknown";
        }

        imei_number = checkString(imei_number);
        phoneNumber = checkString(phoneNumber);
        simSerialNumber = checkString(simSerialNumber);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                txt_imei.setText("Imei number = " + imei_number);
                iv_imei.setImageBitmap(getQRCodeBitmap(imei_number));

                txt_numtel.setText("Phone number = " + phoneNumber);
                iv_numtel.setImageBitmap(getQRCodeBitmap(phoneNumber));

                txt_simserial.setText("Sim serial = " + simSerialNumber);
                iv_simserial.setImageBitmap(getQRCodeBitmap(simSerialNumber));

            }
        });



        /************************************************
         * **********************************************
         * This is just an icing on the cake
         * the following are other children of TELEPHONY_SERVICE
         *
         //Get Subscriber ID
         String subscriberID=tm.getDeviceId();

         //Get SIM Serial Number
         String SIMSerialNumber=tm.getSimSerialNumber();

         //Get Network Country ISO Code
         String networkCountryISO=tm.getNetworkCountryIso();

         //Get SIM Country ISO Code
         String SIMCountryISO=tm.getSimCountryIso();

         //Get the device software version
         String softwareVersion=tm.getDeviceSoftwareVersion()

         //Get the Voice mail number
         String voiceMailNumber=tm.getVoiceMailNumber();


         //Get the Phone Type CDMA/GSM/NONE
         int phoneType=tm.getPhoneType();

         switch (phoneType)
         {
         case (TelephonyManager.PHONE_TYPE_CDMA):
         // your code
         break;
         case (TelephonyManager.PHONE_TYPE_GSM)
         // your code
         break;
         case (TelephonyManager.PHONE_TYPE_NONE):
         // your code
         break;
         }

         //Find whether the Phone is in Roaming, returns true if in roaming
         boolean isRoaming=tm.isNetworkRoaming();
         if(isRoaming)
         phoneDetails+="\nIs In Roaming : "+"YES";
         else
         phoneDetails+="\nIs In Roaming : "+"NO";


         //Get the SIM state
         int SIMState=tm.getSimState();
         switch(SIMState)
         {
         case TelephonyManager.SIM_STATE_ABSENT :
         // your code
         break;
         case TelephonyManager.SIM_STATE_NETWORK_LOCKED :
         // your code
         break;
         case TelephonyManager.SIM_STATE_PIN_REQUIRED :
         // your code
         break;
         case TelephonyManager.SIM_STATE_PUK_REQUIRED :
         // your code
         break;
         case TelephonyManager.SIM_STATE_READY :
         // your code
         break;
         case TelephonyManager.SIM_STATE_UNKNOWN :
         // your code
         break;

         }
         */
    }

    private void getMacAddr() {
        // Starting with M, the system return a fake mac address for security reasons
        //  A workaround consist in using the network interface to retrieve the info
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0"))
                {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        macAddress = "unknown";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    macAddress = res1.toString();
                }
                else if(nif.getName().equalsIgnoreCase("eth0"))
                {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        ethMacAddress = "unknown";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    ethMacAddress = res1.toString();
                }

            }
        } catch (Exception ex) {
            macAddress =  "unknown";
            ethMacAddress = "unknown";
        }

        macAddress = checkString(macAddress);
        ethMacAddress = checkString(ethMacAddress);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                txt_mac.setText("Wifi mac address = " + macAddress);
                iv_mac.setImageBitmap(getQRCodeBitmap(macAddress));

                txt_ethmac.setText("Eth mac address = " + ethMacAddress);
                iv_ethmac.setImageBitmap(getQRCodeBitmap(ethMacAddress));

            }
        });
    }

    private void setWifiState(boolean enabled)
    {
        try
        {
            WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifi.setWifiEnabled(enabled);
        }
        catch(Exception excp)
        {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        }
    }

}
