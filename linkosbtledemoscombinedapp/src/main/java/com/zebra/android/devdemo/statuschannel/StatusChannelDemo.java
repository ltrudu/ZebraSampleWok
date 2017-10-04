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

import com.zebra.android.devdemo.ConnectionScreen;

import android.content.Intent;
import android.os.Bundle;

/**
 */
public class StatusChannelDemo extends ConnectionScreen {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /* (non-Javadoc)
     * @see com.zebra.android.devdemo.ConnectionScreen#performTest()
     */
    @Override
    public void performTest() {
        Intent intent = new Intent(this, StatusChannelScreen.class);
        intent.putExtra("macAddress", macAddress);
        startActivity(intent);
    }
}
