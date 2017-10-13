package com.symbol.emdkallsamples;

import com.wagnerandade.coollection.query.order.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import static com.wagnerandade.coollection.Coollection.*;
import static com.wagnerandade.coollection.Coollection.contains;
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
    public TreeMap<String, List<String>> mByAPI;

    // Ordered by Use-Cases
    public List<String> mUseCasesList;
    public List<String> mUseCasesListWithNumber;
    public TreeMap<String, List<String>> mByUseCases;

    // Ordered Alphabetically by title
    public List<String> mSampleTitlesAlphabetically;
    public TreeMap<String,SampleItem> mSampleAlphabetically;

    public SamplesDescription()
    {
        initSampleItems();

        saveToJSon();

        initLists();
    }

    public void initSampleItems()
    {
        mSamples = new ArrayList<>();
        addSampleDescription("Barcode Sample",                           com.symbol.barcodesample1.MainActivity.class,                               new String[]{"EMDK"},                                       new String[]{"Scanning"}                           ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Notification Sample",                      com.symbol.notificationsample1.MainActivity.class,                          new String[]{"EMDK"},                                       new String[]{"Other"}                              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Profile Application Mgr",                  com.symbol.profileappmgrsample1.MainActivity.class,                         new String[]{"Profile Manager"},                            new String[]{"Setup"}                              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Profile Clock",                            com.symbol.profileclocksample1.MainActivity.class,                          new String[]{"Profile Manager"},                            new String[]{"Setup"}                              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Profile Data Capture",                     com.symbol.profiledatacapturesample1.MainActivity.class,                    new String[]{"Profile Manager"},                            new String[]{"Scanning"}                           ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Profile GPRS",                             com.symbol.profilegprsmgrsample1.MainActivity.class,                        new String[]{"Profile Manager"},                            new String[]{"Network Configuration"}              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Profile Power",                            com.symbol.profilepowermgrsample1.MainActivity.class,                       new String[]{"Profile Manager"},                            new String[]{"Setup"}                              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Profile Wifi",                             com.symbol.profilewifisample1.MainActivity.class,                           new String[]{"Profile Manager"},                            new String[]{"Network Configuration"}              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Profile Wireless",                         com.symbol.profilewirelessmgrsample1.MainActivity.class,                    new String[]{"Profile Manager"},                            new String[]{"Network Configuration"}              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Profile Component",                        com.symbol.componentmanagersample.MainActivity.class,                       new String[]{"Profile Manager"},                            new String[]{"Network Configuration"}              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Profile Ethernet",                         com.symbol.ethernetmanagersample.MainActivity.class,                        new String[]{"Profile Manager"},                            new String[]{"Network Configuration"}              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Simulscan",                                com.symbol.emdk.simulscansample1.MainActivity.class,                        new String[]{"Simulscan"},                                  new String[]{"Scanning"}                           ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Scan and Pair",                            com.symbol.scanandpairsample1.MainActivity.class,                           new String[]{"EMDK"},                                       new String[]{"Setup"}                              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Secure NFC",                               com.symbol.securenfcsample1.MainActivity.class,                             new String[]{"EMDK"},                                       new String[]{"Other"}                              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Datawedge API",                            com.symbol.datacapturereceiver.MainActivity.class,                          new String[]{"DataWedge"},                                  new String[]{"Scanning"}                           ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Battery info",                             com.symbol.batteryinfosample.MainActivity.class,                            new String[]{"Android API"},                                new String[]{"Device Informations"}                ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Terminal info",                            com.symbol.terminalinfo.MainActivity.class,                                 new String[]{"Android API"},                                new String[]{"Device Informations"}                ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Lock Pin",                                 com.symbol.lockpinenabler.MainActivity.class,                               new String[]{"Android API"},                                new String[]{"Security"}                           ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Basic Kiosk / Device Admin",               com.symbol.basickiosk.MainActivity.class,                                   new String[]{"Android API"},                                new String[]{"Security"}                           ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Cellular Enable/Disable",                  com.symbol.cellulardataenabledisable.MainActivity.class,                    new String[]{"Android API"},                                new String[]{"Network Configuration"}              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Serial Communications",                    com.symbol.serialcommsample1.MainActivity.class,                            new String[]{"EMDK"},                                       new String[]{"Other"}                              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("UDI",                                      com.symbol.udisample1.MainActivity.class,                                   new String[]{"EMDK"},                                       new String[]{"Other"}                              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("Personnal Shopper",                        com.symbol.personalshoppersample1.MainActivity.class,                       new String[]{"EMDK"},                                       new String[]{"Other"}                              ,"Description","SourceCodeURL","DocURL"      );

        addSampleDescription("LinkOS BT Connectivity",                   com.zebra.connectivitydemo.MainActivity.class,                             new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Printer Connectivity"}               ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BT Default Printer",                com.zebra.defaultprinterdemo.MainActivity.class,                           new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Printer Connectivity"}               ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BT Printer Language and Status",    com.zebra.determineprinterlanguage.MainActivity.class,                     new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Printer Status"}                     ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BT List Formats",                   com.zebra.listformatsdemo.MainActivity.class,                              new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Printer Status"}                     ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BT PDF Print",                      com.zebra.pdfprint.MainActivity.class,                                     new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Print"}                              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BT Print Rotated Image",            com.zebra.printrotatedimage.MainActivity.class,                            new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Print"}                              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BT PrintStation Demo",              com.zebra.kdu.ChooseFormatScreen.class,                                    new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Print"}                              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BT SendFile",                       com.zebra.sendfiledemo.MainActivity.class,                                 new String[]{"LinkOS","BlueTooth"    },                      new String[]{"Printer Connectivity"}               ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BTLE Connectivity",                 com.zebra.android.devdemo.connectivity.ConnectivityDemo.class,             new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Connectivity"}               ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BTLE Discovered Printers",          com.zebra.android.devdemo.discovery.BluetoothLeDiscovery.class,            new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Connectivity"}               ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BTLE Image Print",                  com.zebra.android.devdemo.imageprint.ImagePrintDemo.class,                 new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Print"}                              ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BTLE List Formats",                 com.zebra.android.devdemo.listformats.ListFormatsDemo.class,               new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Status"}                     ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BTLE Printer Status",               com.zebra.android.devdemo.status.PrintStatusDemo.class,                    new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Status"}                     ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BTLE Send File",                    com.zebra.android.devdemo.sendfile.SendFileDemo.class,                     new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Connectivity"}               ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BTLE Stored Format",                com.zebra.android.devdemo.storedformat.StoredFormatDemo.class,             new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Status"}                     ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BTLE Status Channel",               com.zebra.android.devdemo.statuschannel.StatusChannelDemo.class,           new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Status"}                     ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BTLE Multi Channel",                com.zebra.android.devdemo.receipt.ReceiptDemo.class,                       new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Printer Status"}                     ,"Description","SourceCodeURL","DocURL"      );
        addSampleDescription("LinkOS BTLE Receipt",                      com.zebra.android.devdemo.multichannel.MultiChannelDemo.class,             new String[]{"LinkOS","BlueTooth Low Energy"  },             new String[]{"Print"}                              ,"Description","SourceCodeURL","DocURL"      );

    }

    private void addSampleDescription(String title, Class<?> assclass, String[] api, String[] usecases, String description, String sourceCodeURL, String documentationURL)
    {
        SampleItem item = new SampleItem();
        item.mTitle = title;
        item.mAPIs = Arrays.asList(api);
        item.mUseCases = Arrays.asList(usecases);
        item.mClass = assclass;
        item.mDescription = description;
        item.mSourceCodeLink = sourceCodeURL;
        item.mDocumentationLink = documentationURL;
        item.mResources = "";
        mSamples.add(item);
    }

    private void saveToJSon()
    {
        
    }


    private void initLists()
    {
        // Order Samples by APIs
        mAPIsList = from(mSamples).<List<String>>select("mAPIs").<String>flatten().unique().order(Order.ASC).all();
        mAPIsListWithNumber = new ArrayList<>();
        mByAPI = new TreeMap<>();
        for(String api : mAPIsList)
        {
            List<String> itemforapi = from(mSamples).where("mAPIs", eqIgnoreCase(api)).orderBy("mTitle", Order.ASC).<String>select("mTitle").all();
            mByAPI.put(api, itemforapi);
            mAPIsListWithNumber.add(api + " (" + itemforapi.size() + ")");
        }

        // Order Samples by UseCases
        mUseCasesList = from(mSamples).<List<String>>select("mUseCases").<String>flatten().unique().order(Order.ASC).all();
        mUseCasesListWithNumber = new ArrayList<>();
        mByUseCases = new TreeMap<>();
        for(String usecase : mUseCasesList)
        {
            List<String> itemsforusecase = from(mSamples).where("mUseCases", eqIgnoreCase(usecase)).orderBy("mTitle", Order.ASC).<String>select("mTitle").all();
            mByUseCases.put(usecase, itemsforusecase);
            mUseCasesListWithNumber.add(usecase + " (" + itemsforusecase.size() + ")");
        }

        // Order Samples Alphabetically
        mSampleTitlesAlphabetically = from(mSamples).<String>select("mTitle").order(Order.ASC).all();
        mSampleAlphabetically = new TreeMap<>();
        for(String title : mSampleTitlesAlphabetically)
        {
            SampleItem itemfortitle = from(mSamples).where("mTitle", eqIgnoreCase(title)).first();
            mSampleAlphabetically.put(title, itemfortitle);
        }
    }

    public Class<?> getClass(String sampleTitle)
    {
        return mSampleAlphabetically.get(sampleTitle).mClass;
    }

}
