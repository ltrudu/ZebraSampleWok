package com.symbol.emdkallsamples;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class SampleListActivity extends AppCompatActivity {

    private ListView mSampleAppListView = null;
    private ArrayList<String> mListItems = null;

    private String[] mSampleNames = new String[]{
            "Barcode Sample",
            "Notification Sample",
            "Profile Application Mgr",
            "Profile Clock",
            "Profile Data Capture",
            "Profile GPRS",
            "Profile Power",
            "Profile Wifi",
            "Profile Wireless",
            "Profile Component",
            "Profile Ethernet",
            "Simulscan",
            "Scan and Pair",
            "Secure NFC",
            "Datawedge API",
            "Battery info",
            "Terminal info",
            "Lock Pin",
            "Basic Kiosk / Device Admin",
            "Cellular Enable/Disable",
            "Serial Communications",
            "UDI",
            "Personnal Shopper",
            "LinkOS BT Connectivity",
            "LinkOS BT Default Printer",
            "LinkOS BT Printer Language and Status",
            "LinkOS BT List Formats",
            "LinkOS BT PDF Print",
            "LinkOS BT Print Rotated Image",
            "LinkOS BT PrintStation Demo",
            "LinkOS BT SendFile",
            "LinkOS BTLE Connectivity",
            "LinkOS BTLE Discovered Printers",
            "LinkOS BTLE Image Print",
            "LinkOS BTLE List Formats",
            "LinkOS BTLE Printer Status",
            "LinkOS BTLE Send File",
            "LinkOS BTLE Stored Format",
            "LinkOS BTLE Status Channel",
            "LinkOS BTLE Multi Channel",
            "LinkOS BTLE Receipt"
    };

    private Class<?>[] mSampleClasses = new Class<?>[]{
            com.symbol.barcodesample1.MainActivity.class,
            com.symbol.notificationsample1.MainActivity.class,
            com.symbol.profileappmgrsample1.MainActivity.class,
            com.symbol.profileclocksample1.MainActivity.class,
            com.symbol.profiledatacapturesample1.MainActivity.class,
            com.symbol.profilegprsmgrsample1.MainActivity.class,
            com.symbol.profilepowermgrsample1.MainActivity.class,
            com.symbol.profilewifisample1.MainActivity.class,
            com.symbol.profilewirelessmgrsample1.MainActivity.class,
            com.symbol.componentmanagersample.MainActivity.class,
            com.symbol.ethernetmanagersample.MainActivity.class,
            com.symbol.emdk.simulscansample1.MainActivity.class,
            com.symbol.scanandpairsample1.MainActivity.class,
            com.symbol.securenfcsample1.MainActivity.class,
            com.symbol.datacapturereceiver.MainActivity.class,
            com.symbol.batteryinfosample.MainActivity.class,
            com.symbol.terminalinfo.MainActivity.class,
            com.symbol.lockpinenabler.MainActivity.class,
            com.symbol.basickiosk.MainActivity.class,
            com.symbol.cellulardataenabledisable.MainActivity.class,
            com.symbol.serialcommsample1.MainActivity.class,
            com.symbol.udisample1.MainActivity.class,
            com.symbol.personalshoppersample1.MainActivity.class,

            com.zebra.connectivitydemo.MainActivity.class,
            com.zebra.defaultprinterdemo.MainActivity.class,
            com.zebra.determineprinterlanguage.MainActivity.class,
            com.zebra.listformatsdemo.MainActivity.class,
            com.zebra.pdfprint.MainActivity.class,
            com.zebra.printrotatedimage.MainActivity.class,
            com.zebra.kdu.ChooseFormatScreen.class,
            com.zebra.sendfiledemo.MainActivity.class,

            com.zebra.android.devdemo.connectivity.ConnectivityDemo.class,
            com.zebra.android.devdemo.discovery.BluetoothLeDiscovery.class,
            com.zebra.android.devdemo.imageprint.ImagePrintDemo.class,
            com.zebra.android.devdemo.listformats.ListFormatsDemo.class,
            com.zebra.android.devdemo.status.PrintStatusDemo.class,
            com.zebra.android.devdemo.sendfile.SendFileDemo.class,
            com.zebra.android.devdemo.storedformat.StoredFormatDemo.class,
            com.zebra.android.devdemo.statuschannel.StatusChannelDemo.class,
            com.zebra.android.devdemo.receipt.ReceiptDemo.class,
            com.zebra.android.devdemo.multichannel.MultiChannelDemo.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_list);

        setTitle("Select a sample");

        mSampleAppListView = (ListView) findViewById(R.id.lv_samplelist);

        mListItems = new ArrayList<>();
        mListItems.addAll(Arrays.asList(mSampleNames));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mListItems);

        mSampleAppListView.setAdapter(adapter);

        mSampleAppListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(SampleListActivity.this, mSampleClasses[position]);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Prevent a return to the splash screen
        moveTaskToBack(true);
    }
}
