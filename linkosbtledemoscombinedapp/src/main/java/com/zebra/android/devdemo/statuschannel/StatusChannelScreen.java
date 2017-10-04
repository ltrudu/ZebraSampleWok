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

package com.zebra.android.devdemo.statuschannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zebra.android.devdemo.util.UIHelper;
import com.zebra.android.devdemoble.R;
import com.zebra.sdk.btleComm.BluetoothLeStatusConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.LinkOsInformation;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.settings.SettingsRanges;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StatusChannelScreen extends Activity {
    private String macAddress;
    private UIHelper helper = new UIHelper(this);
    private ArrayAdapter<String> statusListAdapter;
    private List<String> statusMessageList = new ArrayList<String>();
    private static final List<String> settingNames = Arrays.asList("media.type", "media.speed", "device.languages", "ezpl.media_type", "zpl.label_length", "ezpl.print_width", "zpl.print_orientation", "media.sense_mode", "ezpl.print_method", "media.printmode", "ezpl.head_close_action", "ezpl.power_up_action", "device.printhead.resolution", "print.tone_zpl");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_channel_activity);
        Bundle b = getIntent().getExtras();
        macAddress = b.getString("macAddress");
        statusListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, statusMessageList);
        ListView statusList = (ListView) this.findViewById(R.id.statusListView);
        statusList.setAdapter(statusListAdapter);
        new Thread(new Runnable() {

            public void run() {
                getStatus();
            }
        }).start();

    }

    private void getStatus() {
        Connection connection = new BluetoothLeStatusConnection(macAddress, this);
        try {
            helper.showLoadingDialog("Getting settings over status channel");
            connection.open();

            final Map<String, String> rangeMap = SettingsRanges.getRanges(settingNames, connection, PrinterLanguage.ZPL, new LinkOsInformation(3, 2));

            runOnUiThread(new Runnable() {
                public void run() {
                    statusListAdapter.clear();
                    statusMessageList.clear();
                    if (rangeMap.size() > 0) {
                        for (String rangeKey : rangeMap.keySet()) {
                            statusMessageList.add(rangeKey + " range: " + (rangeMap.get(rangeKey)).replaceAll(",", ", "));
                        }
                    } else {
                        statusMessageList.add("Response from requesting ranges did not return any data.");
                    }
                    helper.dismissLoadingDialog();
                    statusListAdapter.notifyDataSetChanged();
                }
            });

        } catch (ConnectionException e) {
            helper.dismissLoadingDialog();
            helper.showErrorDialogOnGuiThread(e.getMessage());
        } catch (Exception e) {
            helper.dismissLoadingDialog();
            helper.showErrorDialogOnGuiThread(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (ConnectionException e) {
                helper.showErrorDialogOnGuiThread(e.getMessage());
            }
        }
    }

}
