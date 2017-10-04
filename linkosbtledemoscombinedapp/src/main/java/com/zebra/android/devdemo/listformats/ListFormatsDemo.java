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

package com.zebra.android.devdemo.listformats;

import com.zebra.android.devdemo.ConnectionScreen;
import com.zebra.android.devdemoble.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class ListFormatsDemo extends ConnectionScreen {
    private boolean retrieveFormats = true;
    CheckBox b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = new CheckBox(this);
        b.setText("Retrieve Files");

        LinearLayout connection_screen_layout = (LinearLayout) findViewById(R.id.connection_screen_layout);
        connection_screen_layout.addView(b, connection_screen_layout.indexOfChild(discoverPrintersButton) + 1);

    }

    @Override
    public void performTest() {
        Intent intent;
        intent = new Intent(this, ListFormatsScreen.class);
        intent.putExtra("mac address", macAddress);
        if (!b.isChecked()) {
            retrieveFormats = true;
            intent.putExtra("retrieveFormats", retrieveFormats);
        } else {
            retrieveFormats = false;
            intent.putExtra("retrieveFormats", retrieveFormats);
        }
        startActivity(intent);
    }

}
