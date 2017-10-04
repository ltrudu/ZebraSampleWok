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

package com.zebra.android.devdemo.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zebra.android.devdemo.util.DemoSleeper;
import com.zebra.android.devdemo.util.UIHelper;
import com.zebra.android.devdemoble.R;
import com.zebra.sdk.btleComm.BluetoothLeConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.PrinterStatusMessages;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PrinterStatusScreen extends Activity {

    private String macAddress;
    private Connection Connection;
    private ZebraPrinter printer;
    private ArrayAdapter<String> statusListAdapter;
    private List<String> statusMessageList = new ArrayList<String>();
    private UIHelper helper = new UIHelper(this);
    private boolean activityIsActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIsActive = true;
        setContentView(R.layout.print_status_activity);
        Bundle b = getIntent().getExtras();
        macAddress = b.getString("mac address");
        statusListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, statusMessageList);
        ListView statusList = (ListView) this.findViewById(R.id.statusList);
        statusList.setAdapter(statusListAdapter);
        new Thread(new Runnable() {

            public void run() {
                Looper.prepare();
                pollForStatus();
                Looper.myLooper().quit();
            }
        }).start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        activityIsActive = false;
        if (Connection != null && Connection.isConnected()) {
            disconnect();
        }
        if (helper.isDialogActive()) {
            helper.dismissLoadingDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityIsActive = true;
        if (Connection != null) {
            helper.showLoadingDialog("Updating Status...");
            connect();
        }
    }

    public ZebraPrinter connect() {

        try {
            Connection = new BluetoothLeConnection(macAddress, this);
            Connection.open();
        } catch (ConnectionException e) {
            displayConnectionError(e.getMessage());
            disconnect();
        }

        ZebraPrinter printer = null;

        if (Connection != null && Connection.isConnected()) {
            try {
                printer = ZebraPrinterFactory.getInstance(Connection);
                printer.getPrinterControlLanguage();
            } catch (ConnectionException e) {
                displayConnectionError(e.getMessage());
                printer = null;
                disconnect();
            } catch (ZebraPrinterLanguageUnknownException e) {
                displayConnectionError(e.getMessage());
                printer = null;
                disconnect();
            }
        }
        return printer;
    }

    public void disconnect() {
        try {
            if (Connection != null) {
                Connection.close();
            }
        } catch (ConnectionException e) {
        }
    }

    private void displayConnectionError(final String message) {
        if (activityIsActive == true) {
            runOnUiThread(new Runnable() {
                public void run() {
                    new AlertDialog.Builder(PrinterStatusScreen.this).setMessage(message).setTitle("Error").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
                }
            });
        }
    }

    private void pollForStatus() {
        helper.showLoadingDialog("Updating Status...");
        printer = connect();
        while (Connection != null && Connection.isConnected()) {
            try {
                updatePrinterStatus();
            } catch (ConnectionException e) {
                displayConnectionError(e.getMessage());
                e.printStackTrace();
            }
            DemoSleeper.sleep(3000);
        }

        helper.dismissLoadingDialog();
    }

    private void updatePrinterStatus() throws ConnectionException {
        if (Connection != null && Connection.isConnected()) {

            PrinterStatus printerStatus = printer.getCurrentStatus();

            final String[] printerStatusString = new PrinterStatusMessages(printerStatus).getStatusMessage();
            final String[] printerStatusPrefix = getPrinterStatusPrefix(printerStatus);
            runOnUiThread(new Runnable() {
                public void run() {
                    statusListAdapter.clear();
                    statusMessageList.clear();
                    statusMessageList.addAll(Arrays.asList(printerStatusPrefix));
                    statusMessageList.addAll(Arrays.asList(printerStatusString));
                    statusListAdapter.notifyDataSetChanged();
                }
            });
            disconnect();
        } else {
            displayConnectionError("No printer connection");
        }

    }

    private String[] getPrinterStatusPrefix(PrinterStatus printerStatus) {
        boolean ready = printerStatus != null ? printerStatus.isReadyToPrint : false;
        String readyString = "Printer " + (ready ? "ready" : "not ready");
        String labelsInBatch = "Labels in batch: " + String.valueOf(printerStatus.labelsRemainingInBatch);
        String labelsInRecvBuffer = "Labels in buffer: " + String.valueOf(printerStatus.numberOfFormatsInReceiveBuffer);
        return new String[] { readyString, labelsInBatch, labelsInRecvBuffer };
    }

}
