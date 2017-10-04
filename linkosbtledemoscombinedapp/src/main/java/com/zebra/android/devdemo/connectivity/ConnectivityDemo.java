/***********************************************
 * CONFIDENTIAL AND PROPRIETARY 
 * 
 * The source code and other information contained herein is the confidential and the exclusive property of
 * ZIH Corp. and is subject to the terms and conditions in your end user license agreement.
 * This source code, and any other information contained herein, shall not be copied, reproduced, published, 
 * displayed or distributed, in whole or in part, in any medium, by any means, for any purpose except as
 * expressly permitted under such license agreement.
 * 
 * Copyright ZIH Corp. 2012
 * 
 * ALL RIGHTS RESERVED
 ***********************************************/

package com.zebra.android.devdemo.connectivity;

import java.util.ArrayList;

import com.zebra.android.devdemo.util.DemoSleeper;
import com.zebra.android.devdemoble.R;
import com.zebra.sdk.btleComm.BluetoothLeConnection;
import com.zebra.sdk.btleComm.BluetoothLeDiscoverer;
import com.zebra.sdk.btleComm.DiscoveredPrinterBluetoothLe;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
import com.zebra.sdk.printer.discovery.DiscoveryHandler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ConnectivityDemo extends Activity {

    private Connection printerConnection;
    private ZebraPrinter printer;
    private TextView statusField;
    private Button discoveryButton;
    private ArrayAdapter<String> mArrayAdapter;
    private ListView discoverPrintersList;
    private String macAddress;
    private Context context;

    private OnItemClickListener discoveredPrintersClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            System.out.println("Clicked on " + position + " position\n");
            new Thread(new Runnable() {
                public void run() {
                    discoverPrintersList.setOnItemClickListener(null);
                    String printerInformation = discoverPrintersList.getItemAtPosition(position).toString();
                    macAddress = printerInformation.split("\n")[1];
                    Looper.prepare();
                    doConnectionTest();
                    Looper.loop();
                    Looper.myLooper().quit();
                }
            }).start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_screen_with_status);

        context = this.getApplicationContext();
        mArrayAdapter = new ArrayAdapter<String>(context, R.layout.discovery_results_for_demos);

        statusField = (TextView) this.findViewById(R.id.statusText);
        discoveryButton = (Button) this.findViewById(R.id.discoverPrintersButton);
        discoveryButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                discoveryButton.setClickable(false);
                mArrayAdapter.clear();
                mArrayAdapter.notifyDataSetChanged();
                try {
                    BluetoothLeDiscoverer.findPrinters(context, new DiscoveryHandler() {
                        ArrayList<String> foundPrinters = new ArrayList<String>();

                        public void foundPrinter(final DiscoveredPrinter discoveredPrinter) {
                            if (null != ((DiscoveredPrinterBluetoothLe) discoveredPrinter).friendlyName && !foundPrinters.contains(((DiscoveredPrinterBluetoothLe) discoveredPrinter).friendlyName)) {
                                System.out.print("Found " + ((DiscoveredPrinterBluetoothLe) discoveredPrinter).friendlyName + "\n");
                                foundPrinters.add(((DiscoveredPrinterBluetoothLe) discoveredPrinter).friendlyName);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        mArrayAdapter.add(((DiscoveredPrinterBluetoothLe) discoveredPrinter).friendlyName + "\n" + ((DiscoveredPrinterBluetoothLe) discoveredPrinter).address);
                                        mArrayAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }

                        public void discoveryFinished() {
                            System.out.println("DISCOVERY COMPLETED");
                            if (null != discoverPrintersList.getOnItemClickListener()) {
                                discoveryButton.setClickable(true);
                            }
                        }

                        public void discoveryError(String message) {
                            System.out.println("DISCOVERY ERROR - " + message);
                            if (null != discoverPrintersList.getOnItemClickListener()) {
                                discoveryButton.setClickable(true);
                            }
                        }
                    });
                } catch (ConnectionException e) {
                    e.printStackTrace();
                }
            }

        });

        discoverPrintersList = (ListView) this.findViewById(R.id.discoveredPrintersList);
        discoverPrintersList.setAdapter(mArrayAdapter);
        discoverPrintersList.setOnItemClickListener(discoveredPrintersClickListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (printerConnection != null && printerConnection.isConnected()) {
            disconnect();
        }
    }

    public ZebraPrinter connect() {
        setStatus("Connecting...", Color.YELLOW);
        printerConnection = null;
        printerConnection = new BluetoothLeConnection(macAddress, this);

        try {
            printerConnection.open();
            setStatus("Connected", Color.GREEN);
        } catch (ConnectionException e) {
            setStatus("Comm Error! Disconnecting", Color.RED);
            DemoSleeper.sleep(1000);
            disconnect();
        }

        ZebraPrinter printer = null;

        if (printerConnection.isConnected()) {
            try {
                printer = ZebraPrinterFactory.getInstance(printerConnection);
                setStatus("Determining Printer Language", Color.YELLOW);
                PrinterLanguage pl = printer.getPrinterControlLanguage();
                setStatus("Printer Language " + pl, Color.BLUE);
            } catch (ConnectionException e) {
                setStatus("Unknown Printer Language", Color.RED);
                printer = null;
                DemoSleeper.sleep(1000);
                disconnect();
            } catch (ZebraPrinterLanguageUnknownException e) {
                setStatus("Unknown Printer Language", Color.RED);
                printer = null;
                DemoSleeper.sleep(1000);
                disconnect();
            }
        }

        return printer;
    }

    public void disconnect() {
        try {
            setStatus("Disconnecting", Color.RED);
            if (printerConnection != null) {
                printerConnection.close();
            }
            setStatus("Not Connected", Color.RED);
        } catch (ConnectionException e) {
            setStatus("COMM Error! Disconnected", Color.RED);
        } finally {
            discoverPrintersList.setOnItemClickListener(discoveredPrintersClickListener);
            discoveryButton.setClickable(true);
        }
    }

    private void setStatus(final String statusMessage, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                statusField.setBackgroundColor(color);
                statusField.setText(statusMessage);
            }
        });
        DemoSleeper.sleep(1000);
    }

    private void doConnectionTest() {
        printer = connect();
        if (printer != null) {
            sendTestLabel();
        } else {
            disconnect();
        }
    }

    private void sendTestLabel() {
        try {
            byte[] configLabel = getConfigLabel();
            printerConnection.write(configLabel);
            setStatus("Sending Data", Color.BLUE);
            DemoSleeper.sleep(1500);
            if (printerConnection instanceof BluetoothLeConnection) {
                String friendlyName = ((BluetoothLeConnection) printerConnection).getFriendlyName();
                setStatus(friendlyName, Color.MAGENTA);
                DemoSleeper.sleep(500);
            }
        } catch (ConnectionException e) {
            setStatus(e.getMessage(), Color.RED);
        } finally {
            disconnect();
        }
    }

    /*
    * Returns the command for a test label depending on the printer control language
    * The test label is a box with the word "TEST" inside of it
    * 
    * _________________________
    * |                       |
    * |                       |
    * |        TEST           |
    * |                       |
    * |                       |
    * |_______________________|
    * 
    * 
    */
    private byte[] getConfigLabel() {
        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();

        byte[] configLabel = null;
        if (printerLanguage == PrinterLanguage.ZPL) {
            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
        } else if (printerLanguage == PrinterLanguage.CPCL) {
            String cpclConfigLabel = "! 0 200 200 406 1\r\n" + "ON-FEED IGNORE\r\n" + "BOX 20 20 380 380 8\r\n" + "T 0 6 137 177 TEST\r\n" + "PRINT\r\n";
            configLabel = cpclConfigLabel.getBytes();
        }
        return configLabel;
    }

}
