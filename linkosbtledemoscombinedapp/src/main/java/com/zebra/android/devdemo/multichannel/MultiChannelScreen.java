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

package com.zebra.android.devdemo.multichannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zebra.android.devdemo.util.DemoSleeper;
import com.zebra.android.devdemo.util.UIHelper;
import com.zebra.android.devdemoble.R;
import com.zebra.sdk.btleComm.MultichannelBluetoothLeConnection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.device.ZebraIllegalArgumentException;
import com.zebra.sdk.printer.LinkOsInformation;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.SGD;
import com.zebra.sdk.settings.SettingsValues;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MultiChannelScreen extends Activity {
    private String macAddress;
    private boolean hasPrintJobFinished;
    private UIHelper helper = new UIHelper(this);
    private ArrayAdapter<String> multichannelListAdapter;
    private List<String> multichannelMessageList = new ArrayList<String>();

    // private static final List<String> settingNames = Arrays.asList("media.type", "media.speed", "device.languages",
    // "ezpl.media_type", "zpl.label_length", "ezpl.print_width", "zpl.print_orientation", "media.sense_mode",
    // "ezpl.print_method", "media.printmode", "ezpl.head_close_action", "ezpl.power_up_action",
    // "device.printhead.resolution", "print.tone_zpl");
    private final static String beginningOfLabel = "^XA";
    private final static String endOfLabel = "^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_channel_activity);
        Bundle b = getIntent().getExtras();
        macAddress = b.getString("macAddress");
        multichannelListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, multichannelMessageList);
        ListView multichannelList = (ListView) this.findViewById(R.id.statusListView);
        multichannelList.setAdapter(multichannelListAdapter);
        new Thread(new Runnable() {

            public void run() {
                // runMultiChannelTest();
                updateGui(null, null, 0, 0);
                runMultichannelDemo();
            }
        }).start();

    }

    // private void runMultiChannelTest() {
    // runOnUiThread(new Runnable() {
    // public void run() {
    // statusListAdapter.clear();
    // statusMessageList.clear();
    // statusMessageList.add("Printing test label");
    // statusMessageList.add("Waiting for Status from printer");
    // statusListAdapter.notifyDataSetChanged();
    // }
    // });
    // helper.showLoadingDialog("Creating Multi Channel Connection to printer.");
    // MultichannelConnection multichannelConnection = new MultichannelBluetoothLeConnection(macAddress, this);
    // try {
    // multichannelConnection.open();
    //
    // helper.dismissLoadingDialog();
    // helper.showLoadingDialog("Sending test label over label channel.");
    //
    // String zplForBox = "~HI^XA^LL200^FO0,0^GB200,200,10^FS";
    // String zplForTestLabel = "^FO10,10^A0N,50,50^FDTest^XZ~HI";
    // String jsonToPrintReports = "{}{\"appl.name\":null, \"appl.name\":null}";
    //
    // final byte[] printerConnectionBoxResponse = multichannelConnection.sendAndWaitForResponse(zplForBox.getBytes(),
    // 15000, 5000, null);
    //
    // byte[] jsonResponse =
    // multichannelConnection.getStatusChannel().sendAndWaitForResponse(jsonToPrintReports.getBytes(), 15000, 5000,
    // null);
    //
    // final byte[] printerConnectionLabelBoxResponse =
    // multichannelConnection.sendAndWaitForResponse(zplForTestLabel.getBytes(), 15000, 5000, null);
    //
    // System.out.println("box response:" + new String(printerConnectionBoxResponse));
    // System.out.println("json response:" + new String(jsonResponse));
    // System.out.println("test label response:" + new String(printerConnectionLabelBoxResponse));
    //
    // final String RangeResponse = new String(jsonResponse);
    //
    // runOnUiThread(new Runnable() {
    // public void run() {
    // statusListAdapter.clear();
    // statusMessageList.clear();
    // statusMessageList.add("Response from statusChannel:\n " + RangeResponse);
    // statusMessageList.add("Response from printing Channel:\n " + new String(printerConnectionBoxResponse) + "\n" +
    // new String(printerConnectionLabelBoxResponse));
    // statusListAdapter.notifyDataSetChanged();
    // }
    // });
    // helper.dismissLoadingDialog();
    //
    // } catch (ConnectionException e) {
    // helper.dismissLoadingDialog();
    // helper.showErrorDialogOnGuiThread(e.getMessage());
    // } catch (Exception e) {
    // helper.dismissLoadingDialog();
    // helper.showErrorDialogOnGuiThread(e.getMessage());
    // } finally {
    // try {
    // if (multichannelConnection != null) {
    // multichannelConnection.close();
    // Sleeper.sleep(1500);
    // }
    // } catch (ConnectionException e) {
    // helper.showErrorDialogOnGuiThread(e.getMessage());
    // }
    // }
    // }

    private void runMultichannelDemo() {
        hasPrintJobFinished = false;
        final MultichannelBluetoothLeConnection multichannelConnection = new MultichannelBluetoothLeConnection(macAddress, this);

        try {
            multichannelConnection.open();

            new Thread(new Runnable() {
                public void run() {
                    int statusQueryCount = 1;
                    List<String> odometerSettings = Arrays.asList("odometer.total_label_count", "odometer.total_print_length");
                    PrinterLanguage pl = null;
                    LinkOsInformation linkOsVersion = null;
                    try {
                        pl = PrinterLanguage.getLanguage(SGD.GET("device.languages", multichannelConnection));
                        linkOsVersion = new LinkOsInformation(SGD.GET("appl.link_os_version", multichannelConnection));
                    } catch (Exception e1) {
                    }
                    if (pl != null && linkOsVersion != null) {
                        try {
                            while (multichannelConnection.isConnected() && !hasPrintJobFinished) {

                                long startTime = System.currentTimeMillis();
                                final Map<String, String> odometerValues = new SettingsValues().getValues(odometerSettings, multichannelConnection.getStatusChannel(), pl, linkOsVersion);
                                final long totalTime = System.currentTimeMillis() - startTime;
                                final int count = statusQueryCount++;
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        updateGui(odometerValues, null, totalTime, count);
                                    }

                                });
                            }
                        } catch (ZebraIllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (ConnectionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            new Thread(new Runnable() {

                public void run() {

                    try {
                        // Send the "^XA" to open the channel and sleep to hold the channel open while querying the
                        // status.
                        // Sleep for 2 seconds after sending the end of the label to let the user see the print job
                        // finish while querying the status.
                        multichannelConnection.getPrintingChannel().write(beginningOfLabel.getBytes());
                        DemoSleeper.sleep(5000);
                        multichannelConnection.getPrintingChannel().write(endOfLabel.getBytes());
                        DemoSleeper.sleep(2000);
                        hasPrintJobFinished = true;
                    } catch (ConnectionException e) {
                        e.printStackTrace();
                        hasPrintJobFinished = true;
                    }

                }
            }).start();

        } catch (ConnectionException e) {
            helper.showErrorDialogOnGuiThread(e.getMessage());
        } catch (Exception e) {
            helper.showErrorDialogOnGuiThread(e.getMessage());
        } finally {
            while (!hasPrintJobFinished) {
                DemoSleeper.sleep(100);
            }
            try {
                if (multichannelConnection != null) {
                    multichannelConnection.close();
                }
            } catch (ConnectionException e) {
                helper.showErrorDialogOnGuiThread(e.getMessage());
            }
        }
    }

    protected void updateGui(final Map<String, String> odometerValues, final PrinterStatus myPrinterStatus, final long totalTime, final int count) {
        multichannelListAdapter.clear();
        multichannelMessageList.clear();
        multichannelMessageList.add("Total number of queries: " + count);
        multichannelMessageList.add("Total time to query settings: " + totalTime + "ms");
        if (null != odometerValues) {
            multichannelMessageList.add("Total label count: " + odometerValues.get("odometer.total_label_count"));
            multichannelMessageList.add("Total print length: " + odometerValues.get("odometer.total_print_length"));
        }
        if (null != myPrinterStatus) {
            multichannelMessageList.add("Is Printer Ready: " + myPrinterStatus.isReadyToPrint);
            multichannelMessageList.add("Is Head Open: " + myPrinterStatus.isHeadOpen);
            multichannelMessageList.add("Is Paper Out: " + myPrinterStatus.isPaperOut);
            multichannelMessageList.add("Is Printer Paused: " + myPrinterStatus.isPaused);
            multichannelMessageList.add("Batch Labels Remaining: " + myPrinterStatus.labelsRemainingInBatch);
        }
        multichannelListAdapter.notifyDataSetChanged();
    }
}
