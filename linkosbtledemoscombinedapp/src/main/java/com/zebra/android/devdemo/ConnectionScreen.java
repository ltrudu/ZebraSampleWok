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

package com.zebra.android.devdemo;

import com.zebra.android.devdemoble.R;
import com.zebra.sdk.btleComm.BluetoothLeDiscoverer;
import com.zebra.sdk.btleComm.DiscoveredPrinterBluetoothLe;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
import com.zebra.sdk.printer.discovery.DiscoveryHandler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public abstract class ConnectionScreen extends Activity {

    protected Button discoverPrintersButton;
    private Context context;
    private ArrayAdapter<String> mArrayAdapter;
    protected ListView discoverPrintersList;
    protected String macAddress;
    protected OnItemClickListener discoveredPrintersClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            System.out.println("Clicked on " + position + " position\n");
            new Thread(new Runnable() {
                public void run() {
                    discoverPrintersList.setClickable(false);
                    String printerInformation = discoverPrintersList.getItemAtPosition(position).toString();
                    macAddress = printerInformation.split("\n")[1];
                    Looper.prepare();
                    performTest();
                    Looper.loop();
                    Looper.myLooper().quit();
                }
            }).start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_screen);

        context = this.getApplicationContext();
        mArrayAdapter = new ArrayAdapter<String>(context, R.layout.discovery_results_for_demos);

        discoverPrintersButton = (Button) this.findViewById(R.id.discoverPrintersButton);
        discoverPrintersButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                discoverPrintersButton.setClickable(false);
                mArrayAdapter.clear();
                mArrayAdapter.notifyDataSetChanged();
                try {
                    BluetoothLeDiscoverer.findPrinters(context, new DiscoveryHandler() {

                        public void foundPrinter(final DiscoveredPrinter discoveredPrinter) {
                            if (null != ((DiscoveredPrinterBluetoothLe) discoveredPrinter).friendlyName) {
                                System.out.print("Found " + ((DiscoveredPrinterBluetoothLe) discoveredPrinter).friendlyName + "\n");
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        mArrayAdapter.add(((DiscoveredPrinterBluetoothLe) discoveredPrinter).friendlyName + "\n" + ((DiscoveredPrinterBluetoothLe) discoveredPrinter).address);
                                        mArrayAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }

                        public void discoveryFinished() {
                            if (discoverPrintersList.isClickable()) {
                                enableDiscoveredPrinterButtonClick();
                            }
                        }

                        public void discoveryError(String message) {
                            if (discoverPrintersList.isClickable()) {
                                enableDiscoveredPrinterButtonClick();
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
    protected void onResume() {
        super.onResume();
        enableDiscoveredPrintersListClick();
        enableDiscoveredPrinterButtonClick();
    }

    public void enableDiscoveredPrintersListClick() {
        discoverPrintersList.setClickable(true);
    }

    public void enableDiscoveredPrinterButtonClick() {
        discoverPrintersButton.setClickable(true);
    }

    public abstract void performTest();

}
