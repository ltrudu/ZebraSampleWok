package com.symbol.emdkallsamples;

import android.content.Context;
import android.content.res.AssetManager;

import com.wagnerandade.coollection.query.Query;
import com.wagnerandade.coollection.query.order.Order;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import static com.wagnerandade.coollection.Coollection.*;
import static com.wagnerandade.coollection.Coollection.from;

/**
 * Created by Trudu Laurent on 06/10/2017.
 */

public class SamplesDescription {
    // All Samples
    public List<SampleItem> mSamples;

    // Ordered by APIs
    public List<String> mAPIsList;
    public List<String> mAPIsListWithNumber;
    public TreeMap<String, List<SampleItem>> mSampleItemsByAPI;

    // Ordered by Use-Cases
    public List<String> mUseCasesList;
    public List<String> mUseCasesListWithNumber;
    public TreeMap<String, List<SampleItem>> mSampleItemsByUseCases;

    // Ordered Alphabetically by title
    public List<String> mSampleTitlesAlphabetically;
    public List<SampleItem> mSampleItemsAlphabetically;
    public TreeMap<String,SampleItem> mSampleAlphabetically;

    // Context to get access to asset folder
    private Context mContext = null;

    // Base folders for assets
    private String mSampleDescriptionsFolder = "samplesinfo";

    public SamplesDescription(Context mycontext)
    {
        mContext = mycontext;

        initSampleItems();

        //saveToJSon();

        initLists();
    }

    public void initSampleItems()
    {
        mSamples = new ArrayList<>();
        addSampleDescription("Barcode Sample",                           com.symbol.barcodesample1.MainActivity.class,                               new String[]{"EMDK"},                                       new String[]{"Scanning"}                           ,"com.symbol.barcodesample1.MainActivity"                   , CustomWebViewClient.PROCESS_LEVEL_EMDKJAVASAMPLES                );
        addSampleDescription("Notification Sample",                      com.symbol.notificationsample1.MainActivity.class,                          new String[]{"EMDK"},                                       new String[]{"Other"}                              ,"com.symbol.notificationsample1.MainActivity"              , CustomWebViewClient.PROCESS_LEVEL_EMDKJAVASAMPLES                );
        addSampleDescription("Profile Application Mgr",                  com.symbol.profileappmgrsample1.MainActivity.class,                         new String[]{"Profile Manager"},                            new String[]{"Setup"}                              ,"com.symbol.profileappmgrsample1.MainActivity"             , CustomWebViewClient.PROCESS_LEVEL_EMDKJAVASAMPLES                );
        addSampleDescription("Profile Clock",                            com.symbol.profileclocksample1.MainActivity.class,                          new String[]{"Profile Manager"},                            new String[]{"Setup"}                              ,"com.symbol.profileclocksample1.MainActivity"              , CustomWebViewClient.PROCESS_LEVEL_EMDKJAVASAMPLES                );
        addSampleDescription("Profile Data Capture",                     com.symbol.profiledatacapturesample1.MainActivity.class,                    new String[]{"Profile Manager"},                            new String[]{"Scanning"}                           ,"com.symbol.profiledatacapturesample1.MainActivity"        , CustomWebViewClient.PROCESS_LEVEL_EMDKJAVASAMPLES                );
        addSampleDescription("Profile GPRS",                             com.symbol.profilegprsmgrsample1.MainActivity.class,                        new String[]{"Profile Manager"},                            new String[]{"Network Configuration"}              ,"com.symbol.profilegprsmgrsample1.MainActivity"            , CustomWebViewClient.PROCESS_LEVEL_EMDKJAVASAMPLES                );
        addSampleDescription("Profile Power",                            com.symbol.profilepowermgrsample1.MainActivity.class,                       new String[]{"Profile Manager"},                            new String[]{"Setup"}                              ,"com.symbol.profilepowermgrsample1.MainActivity"           , CustomWebViewClient.PROCESS_LEVEL_EMDKJAVASAMPLES                );
        addSampleDescription("Profile Wifi",                             com.symbol.profilewifisample1.MainActivity.class,                           new String[]{"Profile Manager"},                            new String[]{"Network Configuration"}              ,"com.symbol.profilewifisample1.MainActivity"               , CustomWebViewClient.PROCESS_LEVEL_EMDKJAVASAMPLES                );
        addSampleDescription("Profile Wireless",                         com.symbol.profilewirelessmgrsample1.MainActivity.class,                    new String[]{"Profile Manager"},                            new String[]{"Network Configuration"}              ,"com.symbol.profilewirelessmgrsample1.MainActivity"        , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("Profile Component",                        com.symbol.componentmanagersample.MainActivity.class,                       new String[]{"Profile Manager"},                            new String[]{"Network Configuration"}              ,"com.symbol.componentmanagersample.MainActivity"           , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("Profile Ethernet",                         com.symbol.ethernetmanagersample.MainActivity.class,                        new String[]{"Profile Manager"},                            new String[]{"Network Configuration"}              ,"com.symbol.ethernetmanagersample.MainActivity"            , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("Simulscan",                                com.symbol.emdk.simulscansample1.MainActivity.class,                        new String[]{"Simulscan"},                                  new String[]{"Scanning"}                           ,"com.symbol.emdk.simulscansample1.MainActivity"            , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("Scan and Pair",                            com.symbol.scanandpairsample1.MainActivity.class,                           new String[]{"EMDK"},                                       new String[]{"Setup"}                              ,"com.symbol.scanandpairsample1.MainActivity"               , CustomWebViewClient.PROCESS_LEVEL_EMDKJAVASAMPLES                );
        addSampleDescription("Secure NFC",                               com.symbol.securenfcsample1.MainActivity.class,                             new String[]{"EMDK"},                                       new String[]{"Other"}                              ,"com.symbol.securenfcsample1.MainActivity"                 , CustomWebViewClient.PROCESS_LEVEL_EMDKJAVASAMPLES                );
        addSampleDescription("Datawedge API",                            com.symbol.datacapturereceiver.MainActivity.class,                          new String[]{"DataWedge"},                                  new String[]{"Scanning"}                           ,"com.symbol.datacapturereceiver.MainActivity"              , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("Battery info",                             com.symbol.batteryinfosample.MainActivity.class,                            new String[]{"Android API"},                                new String[]{"Device Informations"}                ,"com.symbol.batteryinfosample.MainActivity"                , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("Terminal info",                            com.symbol.terminalinfo.MainActivity.class,                                 new String[]{"Android API"},                                new String[]{"Device Informations"}                ,"com.symbol.terminalinfo.MainActivity"                     , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("Lock Pin",                                 com.symbol.lockpinenabler.MainActivity.class,                               new String[]{"Android API"},                                new String[]{"Security"}                           ,"com.symbol.lockpinenabler.MainActivity"                   , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("Basic Kiosk / Device Admin",               com.symbol.basickiosk.MainActivity.class,                                   new String[]{"Android API"},                                new String[]{"Security"}                           ,"com.symbol.basickiosk.MainActivity"                       , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("Cellular Enable/Disable",                  com.symbol.cellulardataenabledisable.MainActivity.class,                    new String[]{"Android API"},                                new String[]{"Network Configuration"}              ,"com.symbol.cellulardataenabledisable.MainActivity"        , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("Serial Communications",                    com.symbol.serialcommsample1.MainActivity.class,                            new String[]{"EMDK"},                                       new String[]{"Other"}                              ,"com.symbol.serialcommsample1.MainActivity"                , CustomWebViewClient.PROCESS_LEVEL_EMDKJAVASAMPLES                );
        addSampleDescription("UDI",                                      com.symbol.udisample1.MainActivity.class,                                   new String[]{"EMDK"},                                       new String[]{"Other"}                              ,"com.symbol.udisample1.MainActivity"                       , CustomWebViewClient.PROCESS_LEVEL_EMDKJAVASAMPLES                );
        addSampleDescription("Personnal Shopper",                        com.symbol.personalshoppersample1.MainActivity.class,                       new String[]{"EMDK"},                                       new String[]{"Other"}                              ,"com.symbol.personalshoppersample1.MainActivity"           , CustomWebViewClient.PROCESS_LEVEL_EMDKJAVASAMPLES                );
        addSampleDescription("LinkOS BT Connectivity",                   com.zebra.connectivitydemo.MainActivity.class,                             new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Printer Connectivity"}               ,"com.zebra.connectivitydemo.MainActivity"                  , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BT Default Printer",                com.zebra.defaultprinterdemo.MainActivity.class,                           new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Printer Connectivity"}               ,"com.zebra.defaultprinterdemo.MainActivity"                , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BT Printer Language and Status",    com.zebra.determineprinterlanguage.MainActivity.class,                     new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Printer Status"}                     ,"com.zebra.determineprinterlanguage.MainActivity"          , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BT List Formats",                   com.zebra.listformatsdemo.MainActivity.class,                              new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Printer Status"}                     ,"com.zebra.listformatsdemo.MainActivity"                   , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BT PDF Print",                      com.zebra.pdfprint.MainActivity.class,                                     new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Print"}                              ,"com.zebra.pdfprint.MainActivity"                          , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BT Print Rotated Image",            com.zebra.printrotatedimage.MainActivity.class,                            new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Print"}                              ,"com.zebra.printrotatedimage.MainActivity"                 , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BT PrintStation Demo",              com.zebra.kdu.ChooseFormatScreen.class,                                    new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Print"}                              ,"com.zebra.kdu.ChooseFormatScreen"                         , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BT SendFile",                       com.zebra.sendfiledemo.MainActivity.class,                                 new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Printer Connectivity"}               ,"com.zebra.sendfiledemo.MainActivity"                      , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BTLE Connectivity",                 com.zebra.android.devdemo.connectivity.ConnectivityDemo.class,             new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Connectivity"}               ,"com.zebra.android.devdemo.connectivity.ConnectivityDemo"  , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BTLE Discovered Printers",          com.zebra.android.devdemo.discovery.BluetoothLeDiscovery.class,            new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Connectivity"}               ,"com.zebra.android.devdemo.discovery.BluetoothLeDiscovery" , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BTLE Image Print",                  com.zebra.android.devdemo.imageprint.ImagePrintDemo.class,                 new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Print"}                              ,"com.zebra.android.devdemo.imageprint.ImagePrintDemo"      , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BTLE List Formats",                 com.zebra.android.devdemo.listformats.ListFormatsDemo.class,               new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Status"}                     ,"com.zebra.android.devdemo.listformats.ListFormatsDemo"    , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BTLE Printer Status",               com.zebra.android.devdemo.status.PrintStatusDemo.class,                    new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Status"}                     ,"com.zebra.android.devdemo.status.PrintStatusDemo"         , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BTLE Send File",                    com.zebra.android.devdemo.sendfile.SendFileDemo.class,                     new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Connectivity"}               ,"com.zebra.android.devdemo.sendfile.SendFileDemo"          , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BTLE Stored Format",                com.zebra.android.devdemo.storedformat.StoredFormatDemo.class,             new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Status"}                     ,"com.zebra.android.devdemo.storedformat.StoredFormatDemo"  , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BTLE Status Channel",               com.zebra.android.devdemo.statuschannel.StatusChannelDemo.class,           new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Status"}                     ,"com.zebra.android.devdemo.statuschannel.StatusChannelDemo", CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BTLE Multi Channel",                com.zebra.android.devdemo.receipt.ReceiptDemo.class,                       new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Status"}                     ,"com.zebra.android.devdemo.receipt.ReceiptDemo"            , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
        addSampleDescription("LinkOS BTLE Receipt",                      com.zebra.android.devdemo.multichannel.MultiChannelDemo.class,             new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Print"}                              ,"com.zebra.android.devdemo.multichannel.MultiChannelDemo"  , CustomWebViewClient.PROCESS_LEVEL_NONE                           );
    }

    private void addSampleDescription(String title, Class<?> assclass, String[] api, String[] usecases, String tagid, int processlevel)
    {
        SampleItem item = new SampleItem();
        item.mTitle = title;
        item.mAPIs = Arrays.asList(api);
        item.mUseCases = Arrays.asList(usecases);
        item.mClass = assclass;
        item.mTagID = tagid;
        item.mHTMLDescriptionURL = "";
        item.mProcessLevel = processlevel;
        checkIfAssetFolderContainsSampleDescription(item);
        mSamples.add(item);
    }

    private void saveToJSon()
    {
        
    }

    private void checkIfAssetFolderContainsSampleDescription(SampleItem item)
    {
        AssetManager mg = mContext.getResources().getAssets();
        InputStream is = null;
        try {
            is = mg.open(mSampleDescriptionsFolder + "/" + item.mTagID + ".html");
            //File exists so do something with it
            if(is != null)
            {
                // We have detail information
                item.mHTMLDescriptionURL = mSampleDescriptionsFolder + "/" + item.mTagID + ".html";
            }
        } catch (IOException ex) {
            //file does not exist
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void initLists()
    {
        // Order Samples by APIs
        mAPIsList = from(mSamples).<List<String>>select("mAPIs").<String>flatten().unique().order(Order.ASC).all();
        mAPIsListWithNumber = new ArrayList<>();
        mSampleItemsByAPI = new TreeMap<>();
        for(String api : mAPIsList)
        {
            Query<SampleItem> orderedSampleItems = from(mSamples).where("mAPIs", eqIgnoreCase(api)).orderBy("mTitle", Order.ASC);

            List<SampleItem> sampleitemsforapi = orderedSampleItems.all();
            mSampleItemsByAPI.put(api, sampleitemsforapi);

            mAPIsListWithNumber.add(api + " (" + sampleitemsforapi.size() + ")");
        }

        // Order Samples by UseCases
        mUseCasesList = from(mSamples).<List<String>>select("mUseCases").<String>flatten().unique().order(Order.ASC).all();
        mUseCasesListWithNumber = new ArrayList<>();
        mSampleItemsByUseCases = new TreeMap<>();
        for(String usecase : mUseCasesList)
        {
            Query<SampleItem> orderedSampleItems = from(mSamples).where("mUseCases", eqIgnoreCase(usecase)).orderBy("mTitle", Order.ASC);

            List<SampleItem> sampleitemsforusecase = orderedSampleItems.all();
            mSampleItemsByUseCases.put(usecase, sampleitemsforusecase);

            mUseCasesListWithNumber.add(usecase + " (" + sampleitemsforusecase.size() + ")");
        }

        // Order Samples Alphabetically
        mSampleTitlesAlphabetically = from(mSamples).<String>select("mTitle").order(Order.ASC).all();
        mSampleAlphabetically = new TreeMap<String, SampleItem>();
        mSampleItemsAlphabetically = new ArrayList<SampleItem>(mSampleTitlesAlphabetically.size());
        for(String title : mSampleTitlesAlphabetically)
        {
            SampleItem itemfortitle = from(mSamples).where("mTitle", eqIgnoreCase(title)).first();
            mSampleAlphabetically.put(title, itemfortitle);
            mSampleItemsAlphabetically.add(itemfortitle);
        }
    }

    public Class<?> getClass(String sampleTitle)
    {
        return mSampleAlphabetically.get(sampleTitle).mClass;
    }

}
