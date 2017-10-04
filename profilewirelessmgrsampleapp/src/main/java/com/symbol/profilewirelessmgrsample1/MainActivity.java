/*
* Copyright (C) 2015-2017 Zebra Technologies Corp
* All rights reserved.
*/
package com.symbol.profilewirelessmgrsample1;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;
import com.symbol.emdk.EMDKManager.EMDKListener;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity implements EMDKListener {

    //Assign the profile name used in EMDKConfig.xml
    private String profileName = "WirelessMgrProfile-1";

    //Declare a variable to store ProfileManager object
    private ProfileManager profileManager = null;

    //Declare a variable to store EMDKManager object
    private EMDKManager emdkManager = null;

    private TextView statusTextView = null;
    private RadioGroup BTRadioGroup = null;
    private RadioGroup BTStateRadioGroup = null;
    private RadioGroup GPSStateRadioGroup = null;
    private RadioGroup GPSModeRadioGroup = null;
    private RadioGroup NFCStateRadioGroup = null;
    private RadioGroup WWanStateRadioGroup = null;

    private int btChoice = 0;
    private int btStateChoice = 0;
    private int gpsStateChoice = 0;
    private int gpsModeChoice = 0;
    private int nfcStateChoice = 0;
    private int wwanStateChoice = 0;

    // Provides the error type for characteristic-error
    private String errorType = "";

    // Provides the parm name for parm-error
    private String parmName = "";

    // Provides error description
    private String errorDescription = "";

    // Provides error string with type/name + description
    private String errorString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwm_activity_main);

        statusTextView = (TextView)findViewById(R.id.textViewStatus);
        BTRadioGroup = (RadioGroup)findViewById(R.id.radioGroupBT);
        BTStateRadioGroup = (RadioGroup)findViewById(R.id.radioGroupBTState);
        GPSStateRadioGroup  = (RadioGroup)findViewById(R.id.radioGroupGPSState);
        GPSModeRadioGroup = (RadioGroup)findViewById(R.id.radioGroupGPSMode);
        NFCStateRadioGroup  = (RadioGroup)findViewById(R.id.radioGroupNFCState);
        WWanStateRadioGroup = (RadioGroup)findViewById(R.id.radioGroupWWANState);

        addSetButtonListener();

        //The EMDKManager object will be created and returned in the callback.
        EMDKResults results = EMDKManager.getEMDKManager(getApplicationContext(), this);

        //Check the return status of EMDKManager object creation.
        if(results.statusCode == EMDKResults.STATUS_CODE.SUCCESS) {
            //EMDKManager object creation success
        }else {
            //EMDKManager object creation failed
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        //Clean up the objects created by EMDK manager
        if (profileManager != null)
            profileManager = null;

        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClosed() {

        //This callback will be issued when the EMDK closes unexpectedly.
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }

        statusTextView.setText("Status: " + "EMDK closed unexpectedly! Please close and restart the application.");
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {

        //This callback will be issued when the EMDK is ready to use.
        statusTextView.setText("EMDK open success.");

        this.emdkManager = emdkManager;

        //Get the ProfileManager object to process the profiles
        profileManager = (ProfileManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.PROFILE);
    }

    private void addSetButtonListener() {

        Button setButton = (Button)findViewById(R.id.buttonSet);

        setButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int btradioid = BTRadioGroup.getCheckedRadioButtonId();
                if(btradioid == R.id.radioBTDNC) {
                    btChoice = 0; // 0 - Do nothing
                }
                else if(btradioid == R.id.radioBTEnable) {
                    btChoice = 1; // 1 - Enable Bluetooth
                }
                else if(btradioid == R.id.radioBTDisable){
                    btChoice = 2; // 2 - Disable Bluetooth
                }

                int btstateradioid = BTStateRadioGroup.getCheckedRadioButtonId();
                if(btstateradioid == R.id.radioBTStateDNC) {
                    btStateChoice = 0; // 0 - Do nothing
                }
                else if(btstateradioid == R.id.radioBTStateEnable) {
                    btStateChoice = 1; // 1 - Turn Bluetooth On
                }
                else if(btstateradioid == R.id.radioBTStateDisable){
                    btStateChoice = 2; // 2 - Turn Bluetooth Off
                }

                int gpsstateradioid = GPSStateRadioGroup.getCheckedRadioButtonId();
                if(gpsstateradioid == R.id.radioGPSStateDNC) {
                    gpsStateChoice = 0; // 0 - Do nothing
                }
                else if(gpsstateradioid == R.id.radioGPSStateEnable) {
                    gpsStateChoice = 1; // 1 - Turn GPS On
                }
                else if(gpsstateradioid == R.id.radioGPSStateDisable){
                    gpsStateChoice = 2; // 2 - Turn GPS Off
                }

                int gpsmoderadioid = GPSModeRadioGroup.getCheckedRadioButtonId();
                if(gpsmoderadioid == R.id.radioGPSModeDNC) {
                    gpsModeChoice = 0; // 0 - Do nothing
                }
                else if(gpsmoderadioid == R.id.radioGPSModeHA) {
                    gpsModeChoice = 1; // 1 - High Accuracy
                }
                else if(gpsmoderadioid == R.id.radioGPSModeBS){
                    gpsModeChoice = 2; // 2 - Battery saving
                }
                else if(gpsmoderadioid == R.id.radioGPSModeDO){
                    gpsModeChoice = 3; // 3 - Device only
                }

                int nfcstateradioid = NFCStateRadioGroup.getCheckedRadioButtonId();
                if(nfcstateradioid == R.id.radioNFCStateDNC) {
                    nfcStateChoice = 0; // 0 - Do nothing
                }
                else if(nfcstateradioid == R.id.radioNFCStateEnable) {
                    nfcStateChoice = 1; // 1 - Turn NFC On
                }
                else if(nfcstateradioid == R.id.radioNFCStateDisable){
                    nfcStateChoice = 2; // 2 - Turn NFC Off
                }

                int wwanstateradioid = WWanStateRadioGroup.getCheckedRadioButtonId();
                if(wwanstateradioid == R.id.radioWWANStateDNC) {
                    wwanStateChoice = 0; // 0 - Do nothing
                }
                else if(wwanstateradioid == R.id.radioWWANStateEnable) {
                    wwanStateChoice = 1; // 1 - Turn WWAN On
                }
                else if(wwanstateradioid == R.id.radioWWANStateDisable){
                    wwanStateChoice = 2; // 2 - Turn WWAN Off
                }

                modifyProfile_XMLString();
            }
        });
    }

    private void modifyProfile_XMLString() {

        statusTextView.setText("");
        errorType = "";
        parmName = "";
        errorDescription = "";
        errorString = "";

        //Prepare XML to modify the existing profile
        String[] modifyData = new String[1];
        modifyData[0]=
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<characteristic type=\"Profile\">" +
                        "<parm name=\"ProfileName\" value=\"WirelessMgrProfile-1\"/>" +
                        "<characteristic type=\"WirelessMgr\" version=\"6.1\">" +
                        "<parm name=\"Bluetooth\" value=\"" + btChoice + "\"/>" +
                        "<parm name=\"BluetoothState\" value=\"" + btStateChoice + "\"/>" +
                        "<parm name=\"NFCState\" value=\"" + nfcStateChoice + "\"/>" +
                        "<parm name=\"GPSState\" value=\"" + gpsStateChoice + "\"/>" +
                        "<parm name=\"WWANState\" value=\"" + wwanStateChoice + "\"/>" +
                        "<parm name=\"GPSLocationMode\" value=\"" + gpsModeChoice + "\"/>" +
                        "</characteristic>" +
                        "</characteristic>";

        new ProcessProfileTask().execute(modifyData[0]);
    }

    // Method to parse the XML response using XML Pull Parser
    public void parseXML(XmlPullParser myParser) {
        errorString = "";
        int event;
        try {
            // Retrieve error details if parm-error/characteristic-error in the response XML
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:

                        if (name.equals("parm-error")) {
                            parmName = myParser.getAttributeValue(null, "name");
                            errorDescription = myParser.getAttributeValue(null, "desc");
                            errorString += " (Name: " + parmName + ", Error Description: " + errorDescription + ")\n";
                        }

                        if (name.equals("characteristic-error")) {
                            errorType = myParser.getAttributeValue(null, "type");
                            errorDescription = myParser.getAttributeValue(null, "desc");
                            errorString += " (Type: " + errorType + ", Error Description: " + errorDescription + ")\n";
                        }

                        break;
                    case XmlPullParser.END_TAG:

                        break;
                }
                event = myParser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ProcessProfileTask extends AsyncTask<String, Void, EMDKResults> {

        @Override
        protected EMDKResults doInBackground(String... params) {

            //Call process profile to modify the profile of specified profile name
            EMDKResults results = profileManager.processProfile(profileName, ProfileManager.PROFILE_FLAG.SET, params);

            return results;
        }

        @Override
        protected void onPostExecute(EMDKResults results) {

            super.onPostExecute(results);

            String resultString = "";

            //Check the return status of processProfile
            if(results.statusCode == EMDKResults.STATUS_CODE.CHECK_XML) {

                // Get XML response as a String
                String statusXMLResponse = results.getStatusString();

                try {
                    // Create instance of XML Pull Parser to parse the response
                    XmlPullParser parser = Xml.newPullParser();
                    // Provide the string response to the String Reader that reads
                    // for the parser
                    parser.setInput(new StringReader(statusXMLResponse));
                    // Call method to parse the response
                    parseXML(parser);

                    if ( TextUtils.isEmpty(parmName) && TextUtils.isEmpty(errorType) && TextUtils.isEmpty(errorDescription) ) {

                        resultString = "Profile update success.";
                    }
                    else {

                        resultString = "Profile update failed." + errorString;
                    }

                } catch (XmlPullParserException e) {
                    resultString =  e.getMessage();
                }
            }

            statusTextView.setText(resultString);
        }
    }
}
