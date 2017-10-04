/*
* Copyright (C) 2015-2016 Symbol Technologies LLC
* All rights reserved.
*/
package com.symbol.ethernetmanagersample;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity implements EMDKListener {

    //Assign the profile name used in EMDKConfig.xml
    private String profileName = "EthernetMgrProfile-1";

    //Declare a variable to store ProfileManager object
    private ProfileManager profileManager = null;

    //Declare a variable to store EMDKManager object
    private EMDKManager emdkManager = null;

    private TextView statusTextView = null;

    private RadioGroup ethernetStateRadioGroup = null;
    private RadioGroup ethernetProxySettingsRadioGroup = null;

    private CheckBox configIpAddrCheckBox = null;
    private CheckBox useDHCPCheckBox = null;

    private EditText ipAddressEditText = null;
    private EditText gatewayEditText = null;
    private EditText netmaskEditText = null;
    private EditText dns1EditText = null;
    private EditText dns2EditText = null;
    private EditText proxyHostNameEditText = null;
    private EditText proxyPortEditText = null;
    private EditText bypassProxyEditText = null;

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
        setContentView(R.layout.ethm_activity_main);

        statusTextView = (TextView)findViewById(R.id.textViewStatus);

        ethernetStateRadioGroup = (RadioGroup)findViewById(R.id.radioGroupEthernetState);
        ethernetStateRadioGroup.check(R.id.radioEthernetStateTurnOn);

        ethernetProxySettingsRadioGroup = (RadioGroup)findViewById(R.id.radioGroupEthernetProxySettings);
        ethernetProxySettingsRadioGroup.check(R.id.radioEthernetProxySettingsManual);

        ethernetProxySettingsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeProxyDetailsEnabled(checkedId == R.id.radioEthernetProxySettingsManual);
            }
        });

        configIpAddrCheckBox = (CheckBox)findViewById(R.id.checkBoxConfigIpAddress);
        configIpAddrCheckBox.setChecked(true);

        useDHCPCheckBox = (CheckBox)findViewById(R.id.checkBoxUseDHCP);
        useDHCPCheckBox.setChecked(false);

        configIpAddrCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeIPDetailsEnabled(isChecked == true && useDHCPCheckBox.isChecked() == false);
            }
        });

        useDHCPCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeIPDetailsEnabled(configIpAddrCheckBox.isChecked() == true && isChecked == false);
            }
        });

        ipAddressEditText = (EditText)findViewById(R.id.editTextIpAddress);
        gatewayEditText = (EditText)findViewById(R.id.editTextGateway);
        netmaskEditText = (EditText)findViewById(R.id.editTextNetMask);
        dns1EditText = (EditText)findViewById(R.id.editTextDNS1);
        dns2EditText = (EditText)findViewById(R.id.editTextDNS2);

        proxyHostNameEditText = (EditText)findViewById(R.id.editTextProxyHostName);
        proxyPortEditText = (EditText)findViewById(R.id.editTextProxyPort);
        bypassProxyEditText = (EditText)findViewById(R.id.editTextBypassProxy);

        changeIPDetailsEnabled(true);
        changeProxyDetailsEnabled(true);

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

    private void changeIPDetailsEnabled(boolean enable)
    {
        ipAddressEditText.setEnabled(enable);
        gatewayEditText.setEnabled(enable);
        netmaskEditText.setEnabled(enable);
        dns1EditText.setEnabled(enable);
        dns2EditText.setEnabled(enable);
    }


    private void changeProxyDetailsEnabled(boolean enable)
    {
        proxyHostNameEditText.setEnabled(enable);
        proxyPortEditText.setEnabled(enable);
        bypassProxyEditText.setEnabled(enable);
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

                int ethernetStateRadioGroupRadioId = ethernetStateRadioGroup.getCheckedRadioButtonId();

                int ethernetState = 0;

                if(ethernetStateRadioGroupRadioId == R.id.radioEthernetStateDoNothing) {
                        ethernetState = 0; // 0 - Do nothing
                }
                else if(ethernetStateRadioGroupRadioId == R.id.radioEthernetStateTurnOn) {
                    ethernetState = 1; // 1 - Turn On Ethernet
                }
                else if(ethernetStateRadioGroupRadioId == R.id.radioEthernetStateTurnOff) {
                    ethernetState = 2; // 2 - Turn Off Ethernet
                }

                int ethernetProxySettingsRadioGroupRadioId = ethernetProxySettingsRadioGroup.getCheckedRadioButtonId();

                int setProxySettings = 0;

                if(ethernetStateRadioGroupRadioId == R.id.radioEthernetProxySettingsDoNothing) {
                    setProxySettings = 0; // 0 - Do nothing
                }
                else if(ethernetStateRadioGroupRadioId == R.id.radioEthernetProxySettingsDisable) {
                    setProxySettings = 1; // 1 - Disable proxy settings
                }
                else if(ethernetStateRadioGroupRadioId == R.id.radioEthernetProxySettingsManual) {
                    setProxySettings = 2; // 2 - Manual proxy settings
                }

                String proxyHostName = "";
                String proxyPort = "";
                String bypassProxy = "";
                if(setProxySettings == 2)
                {
                    proxyHostName = proxyHostNameEditText.getText().toString();
                    proxyPort = proxyPortEditText.getText().toString();
                    bypassProxy = bypassProxyEditText.getText().toString();
                }

                int configIpAddr = configIpAddrCheckBox.isChecked() ? 1 : 0;
                int useDHCP = useDHCPCheckBox.isChecked() ? 1 : 0;

                String ipAddress = "";
                String gateWay = "";
                String netMask = "";
                String DNS1 = "";
                String DNS2 = "";
                if(configIpAddr == 1 && useDHCP == 0)
                {
                    ipAddress = ipAddressEditText.getText().toString();
                    gateWay = gatewayEditText.getText().toString();
                    netMask = netmaskEditText.getText().toString();
                    DNS1 = dns1EditText.getText().toString();
                    DNS2 = dns2EditText.getText().toString();
                }

                executeEthernetManagerProfile(ethernetState, setProxySettings,
                proxyHostName,
                proxyPort,
                bypassProxy,
                configIpAddr,
                useDHCP,
                ipAddress,
                gateWay,
                netMask,
                DNS1,
                DNS2);
            }
        });
    }

    private void executeEthernetManagerProfile(int ethernetState,
                                               int setProxySettings,
                                               String proxyHostName,
                                               String proxyPort,
                                               String bypassProxy,
                                               int configIpAddr,
                                               int useDHCP,
                                               String ipAddress,
                                               String gateWay,
                                               String netMask,
                                               String DNS1,
                                               String DNS2) {

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
                        "<parm name=\"ProfileName\" value=\"EthernetMgrProfile-1\"/>" +
                        "<parm name=\"ModifiedDate\" value=\"2016-11-18 16:49:02\"/>" +
                        "<parm name=\"TargetSystemVersion\" value=\"6.2\"/>" +
                        "<characteristic type=\"EthernetMgr\" version=\"6.2\">" +
                        "<parm name=\"emdk_name\" value=\"Ethernet\"/>" +
                        "<parm name=\"EthernetState\" value=\"" + ethernetState + "\"/>";

        modifyData[0]+="<parm name=\"ConfigIpAddr\" value=\"" + configIpAddr + "\"/>";
        modifyData[0]+="<parm name=\"UseDHCP\" value=\"" + useDHCP + "\"/>";

        if(configIpAddr == 1 && useDHCP == 0)
        {
            modifyData[0]+="<characteristic type=\"ip-details\">";
            modifyData[0]+="<parm name=\"IpAddress\" value=\"" + ipAddress + "\"/>";
            modifyData[0]+="<parm name=\"Gateway\" value=\"" + gateWay + "\"/>";
            modifyData[0]+="<parm name=\"NetMask\" value=\"" + netMask + "\"/>";
            modifyData[0]+="<parm name=\"DNS1\" value=\"" + DNS1 + "\"/>";
            modifyData[0]+="<parm name=\"DNS2\" value=\"" + DNS2 + "\"/>";
            modifyData[0]+="</characteristic>";
        }

        modifyData[0]+="<parm name=\"SetEthernetProxySettings\" value=\"" + setProxySettings + "\"/>";

        if(setProxySettings == 2)
        {
            modifyData[0]+="<characteristic type=\"proxy-details\">";
            modifyData[0]+="<parm name=\"ProxyHostName\" value=\"" + proxyHostName + "\"/>";
            modifyData[0]+="<parm name=\"ProxyPort\" value=\"" + proxyPort + "\"/>";
            modifyData[0]+="<parm name=\"BypassProxy\" value=\"" + bypassProxy + "\"/>";
            modifyData[0]+="</characteristic>";
        }

        modifyData[0]+= "</characteristic>" + //EthernetMgr Characteristic
                        "</characteristic>"; //Profile Characteristic

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
            if(results.statusCode == EMDKResults.STATUS_CODE.CHECK_XML || results.statusCode == EMDKResults.STATUS_CODE.FAILURE) {

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
