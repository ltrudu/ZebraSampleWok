package com.symbol.emdkallsamples;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static com.symbol.emdkallsamples.eAPIEnums.*;


/**
 * Created by Trudu Laurent on 18/09/2017.
 */

public class SampleItems {
    // All Samples
    public List<SampleItem> mItems;

    // Ordered by APIs
    public TreeMap<eAPIEnums, List<SampleItem>> mByAPI;
    public TreeMap<String, eAPIEnums> mAPIs;

    public SampleItems()
    {
        initSampleItems();
        initByAPI();
    }

    public void initSampleItems()
    {
        mItems = new ArrayList<>();
        addItem("Barcode Sample",                   com.symbol.barcodesample1.MainActivity.class,               new eAPIEnums[]{EMDK},                                null);
        addItem("Notification Sample",              com.symbol.notificationsample1.MainActivity.class,          new eAPIEnums[]{EMDK},                                null);
        addItem("Profile Application Mgr",          com.symbol.profileappmgrsample1.MainActivity.class,         new eAPIEnums[]{PROFILEMANAGER},                      null);
        addItem("Profile Clock",                    com.symbol.profileclocksample1.MainActivity.class,          new eAPIEnums[]{PROFILEMANAGER},                      null);
        addItem("Profile Data Capture",             com.symbol.profiledatacapturesample1.MainActivity.class,    new eAPIEnums[]{PROFILEMANAGER},                      null);
        addItem("Profile GPRS",                     com.symbol.profilegprsmgrsample1.MainActivity.class,        new eAPIEnums[]{PROFILEMANAGER},                      null);
        addItem("Profile Power",                    com.symbol.profilepowermgrsample1.MainActivity.class,       new eAPIEnums[]{PROFILEMANAGER},                      null);
        addItem("Profile Wifi",                     com.symbol.profilewifisample1.MainActivity.class,           new eAPIEnums[]{PROFILEMANAGER},                      null);
        addItem("Profile Wireless",                 com.symbol.profilewirelessmgrsample1.MainActivity.class,    new eAPIEnums[]{PROFILEMANAGER},                      null);
        addItem("Profile Component",                com.symbol.componentmanagersample.MainActivity.class,       new eAPIEnums[]{PROFILEMANAGER},                      null);
        addItem("Profile Ethernet",                 com.symbol.ethernetmanagersample.MainActivity.class,        new eAPIEnums[]{PROFILEMANAGER},                      null);
        addItem("Simulscan",                        com.symbol.emdk.simulscansample1.MainActivity.class,        new eAPIEnums[]{SIMULSCAN},                           null);
        addItem("Scan and Pair",                    com.symbol.scanandpairsample1.MainActivity.class,           new eAPIEnums[]{EMDK},                                null);
        addItem("Secure NFC",                       com.symbol.securenfcsample1.MainActivity.class,             new eAPIEnums[]{EMDK},                                null);
        addItem("Datawedge API",                    com.symbol.datacapturereceiver.MainActivity.class,          new eAPIEnums[]{DATAWEDGE},                           null);
        addItem("Battery info",                     com.symbol.batteryinfosample.MainActivity.class,            new eAPIEnums[]{ANDROID},                             null);
        addItem("Terminal info",                    com.symbol.terminalinfo.MainActivity.class,                 new eAPIEnums[]{ANDROID},                             null);
        addItem("Lock Pin",                         com.symbol.lockpinenabler.MainActivity.class,               new eAPIEnums[]{ANDROID},                             null);
        addItem("Basic Kiosk / Device Admin",       com.symbol.basickiosk.MainActivity.class,                   new eAPIEnums[]{ANDROID},                             null);
        addItem("Cellular Enable/Disable",          com.symbol.cellulardataenabledisable.MainActivity.class,    new eAPIEnums[]{ANDROID},                             null);
        addItem("Serial Communications",            com.symbol.serialcommsample1.MainActivity.class,            new eAPIEnums[]{EMDK},                                null);
        addItem("UDI",                              com.symbol.udisample1.MainActivity.class,                   new eAPIEnums[]{EMDK},                                null);
        addItem("Personnal Shopper",                com.symbol.personalshoppersample1.MainActivity.class,       new eAPIEnums[]{EMDK},                                null);
    }

    private void addItem(String title, Class<?> assclass, eAPIEnums[] api, int[] usecases)
    {
        SampleItem item = new SampleItem();

        item.mTitle = title;
        item.mAPI = api;
        item.mUseCase = usecases;
        item.mClass = assclass;

        mItems.add(item);
    }

    public void initByAPI()
    {
        mByAPI = new TreeMap<>();
        mAPIs = new TreeMap<>();
        for(SampleItem item : mItems)
        {
            for(eAPIEnums itemApi : item.mAPI)
            {
                if(mByAPI.containsKey(itemApi) == false)
                {
                    mByAPI.put(itemApi, new ArrayList<SampleItem>());
                    mAPIs.put(itemApi.getName(), itemApi);
                }
                List<SampleItem> destList = mByAPI.get(itemApi);
                destList.add(item);
            }
        }
    }
}
