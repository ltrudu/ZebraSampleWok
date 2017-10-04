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

package com.zebra.android.devdemo.imageprint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.zebra.android.devdemo.util.UIHelper;
import com.zebra.android.devdemoble.R;
import com.zebra.sdk.btleComm.BluetoothLeConnection;
import com.zebra.sdk.btleComm.BluetoothLeDiscoverer;
import com.zebra.sdk.btleComm.DiscoveredPrinterBluetoothLe;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.device.ZebraIllegalArgumentException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
import com.zebra.sdk.printer.discovery.DiscoveryHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

public class ImagePrintDemo extends Activity {

    private Button discoverPrintersButton;
    private ListView discoverPrintersList;
    private ArrayAdapter<String> mArrayAdapter;
    private String macAddress;
    private Context context;
    private EditText printStoragePath;
    private UIHelper helper = new UIHelper(this);
    private static int PICTURE_FROM_GALLERY = 2;
    private static File file = null;

    private OnItemClickListener discoveredPrintersClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            System.out.println("Clicked on " + position + " position\n");
            new Thread(new Runnable() {
                public void run() {
                    discoverPrintersList.setOnItemClickListener(null);
                    String printerInformation = discoverPrintersList.getItemAtPosition(position).toString();
                    macAddress = printerInformation.split("\n")[1];
                    Looper.prepare();
                    getPhotosFromGallery();
                    Looper.loop();
                    Looper.myLooper().quit();
                }
            }).start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_print_demo);

        context = this.getApplicationContext();
        mArrayAdapter = new ArrayAdapter<String>(context, R.layout.discovery_results_for_demos);

        printStoragePath = (EditText) findViewById(R.id.printerStorePath);

        discoverPrintersButton = (Button) findViewById(R.id.discoverPrintersButton);
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
                            if (null != discoverPrintersList.getOnItemClickListener()) {
                                discoverPrintersButton.setClickable(true);
                            }
                        }

                        public void discoveryError(String message) {
                            if (null != discoverPrintersList.getOnItemClickListener()) {
                                discoverPrintersButton.setClickable(true);
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

        CheckBox cb = (CheckBox) findViewById(R.id.checkBox);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    printStoragePath.setVisibility(View.VISIBLE);
                } else {
                    printStoragePath.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void getPhotosFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICTURE_FROM_GALLERY);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri imgPath = data.getData();
            Bitmap myBitmap = null;
            try {
                myBitmap = Media.getBitmap(getContentResolver(), imgPath);
            } catch (FileNotFoundException e) {
                helper.showErrorDialog(e.getMessage());
            } catch (IOException e) {
                helper.showErrorDialog(e.getMessage());
            }
            printPhotoFromExternal(myBitmap);
        }
    }

    private void printPhotoFromExternal(final Bitmap bitmap) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Looper.prepare();
                    helper.showLoadingDialog("Sending image to printer");
                    Connection connection = getZebraPrinterConn();
                    connection.open();
                    ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);

                    if (((CheckBox) findViewById(R.id.checkBox)).isChecked()) {
                        printer.storeImage(printStoragePath.getText().toString(), new ZebraImageAndroid(bitmap), 550, 412);
                    } else {
                        printer.printImage(new ZebraImageAndroid(bitmap), 0, 0, 550, 412, false);
                    }
                    connection.close();

                    if (file != null) {
                        file.delete();
                        file = null;
                    }
                } catch (ConnectionException e) {
                    helper.showErrorDialogOnGuiThread(e.getMessage());
                } catch (ZebraPrinterLanguageUnknownException e) {
                    helper.showErrorDialogOnGuiThread(e.getMessage());
                } catch (ZebraIllegalArgumentException e) {
                    helper.showErrorDialogOnGuiThread(e.getMessage());
                } finally {
                    discoverPrintersList.setOnItemClickListener(discoveredPrintersClickListener);
                    discoverPrintersButton.setClickable(true);
                    bitmap.recycle();
                    helper.dismissLoadingDialog();
                    Looper.myLooper().quit();
                }
            }
        }).start();

    }

    private Connection getZebraPrinterConn() {
        return new BluetoothLeConnection(macAddress, this);
    }

}
