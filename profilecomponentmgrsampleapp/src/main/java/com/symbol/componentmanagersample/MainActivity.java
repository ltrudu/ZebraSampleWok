/*
* Copyright (C) 2015-2016 Symbol Technologies LLC
* All rights reserved.
*/
package com.symbol.componentmanagersample;

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
    private String profileName = "ComponentMgrProfile-1";

    //Declare a variable to store ProfileManager object
    private ProfileManager profileManager = null;

    //Declare a variable to store EMDKManager object
    private EMDKManager emdkManager = null;

    private TextView statusTextView = null;
    private RadioGroup ethernetUsageRadioGroup = null;
    private RadioGroup ethernetStateRadioGroup = null;

    //private int value = 1;

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
        setContentView(R.layout.comp_activity_main);

        statusTextView = (TextView)findViewById(R.id.textViewStatus);
        ethernetUsageRadioGroup = (RadioGroup)findViewById(R.id.radioGroupEthernetUsage);
        ethernetStateRadioGroup = (RadioGroup)findViewById(R.id.radioGroupEthernetState);

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

                int ethernetUsageRadioGroupRadioId = ethernetUsageRadioGroup.getCheckedRadioButtonId();

                int ethernetUsage = 0;

                if(ethernetUsageRadioGroupRadioId == R.id.radioEthernetUsageDoNothing)
                {
                    ethernetUsage = 0; // 0 - Do nothing
                }
                else if(ethernetUsageRadioGroupRadioId == R.id.radioEthernetUsageEnable)
                {
                    ethernetUsage = 1; // 1 - Unlock Ethernet Option
                }
                else if(ethernetUsageRadioGroupRadioId == R.id.radioEthernetUsageDisable)
                {
                    ethernetUsage = 2; // 2 - Lock Ethernet Option
                }

                int ethernetStateRadioGroupRadioId = ethernetStateRadioGroup.getCheckedRadioButtonId();

                int ethernetState = 0;

                if(ethernetStateRadioGroupRadioId == R.id.radioEthernetStateDoNothing)
                {
                        ethernetState = 0; // 0 - Do nothing
                }
                else if(ethernetStateRadioGroupRadioId == R.id.radioEthernetStateTurnOn)
                {
                        ethernetState = 1; // 1 - Turn On Ethernet
                }
                else if(ethernetStateRadioGroupRadioId == R.id.radioEthernetStateTurnOff)
                {
                        ethernetState = 2; // 2 - Turn Off Ethernet
                }

                if(ethernetState != 0 && ethernetUsage != 1)
                {
                    statusTextView.setText("Profile processing has been canceled because of the following error:\nYou must ENABLE ethernet settings if you plan to change ethernet state.\n");
                }
                else
                    executeComponentManagerProfile(ethernetUsage, ethernetState);
            }
        });
    }

    private void executeComponentManagerProfile(int ethernetUsage, int ethernetState) {

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
                        "<parm name=\"ProfileName\" value=\"ComponentMgrProfile-1\"/>" +
                        "<parm name=\"ModifiedDate\" value=\"2016-11-18 16:49:02\"/>" +
                        "<parm name=\"TargetSystemVersion\" value=\"6.0\"/>" +
                        "<characteristic type=\"ComponentMgr\" version=\"4.4\">" +
                        "<parm name=\"emdk_name\" value=\"Component\"/>" +
                        "<parm name=\"EthernetUsage\" value=\"" + ethernetUsage + "\"/>" +
                        "<parm name=\"EthernetState\" value=\"" + ethernetState + "\"/>" +
                        "</characteristic>" +
                        "</characteristic>";

        new ProcessProfileTask().execute(modifyData[0]);
    }

    // Method to parse the XML response using XML Pull Parser
    public void parseXML(XmlPullParser myParser) {
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
                            errorString = " (Name: " + parmName + ", Error Description: " + errorDescription + ")";
                            return;
                        }

                        if (name.equals("characteristic-error")) {
                            errorType = myParser.getAttributeValue(null, "type");
                            errorDescription = myParser.getAttributeValue(null, "desc");
                            errorString = " (Type: " + errorType + ", Error Description: " + errorDescription + ")";
                            return;
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
