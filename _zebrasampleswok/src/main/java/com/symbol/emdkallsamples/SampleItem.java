package com.symbol.emdkallsamples;

import java.util.List;

/**
 * Created by Trudu Laurent on 06/10/2017.
 */

public class SampleItem {
    /**
     * Friendly name title
     */
    public String mTitle;

    /**
     * List of APIs used in this sample
     */
    public List<String> mAPIs = null;

    /**
     * List of use cases related to this sample
     */
    public List<String> mUseCases = null;

    /**
     * Sample's Android class activity
     */
    public Class<?> mClass;

    /**
     * Additional informations (optional)
     */
    // Name of the associated html page without extension
    // ex: mTagID=barcodesampleapp refers to the html page barcodesampleapp.html
    // in the asset folder
    public String mTagID;

    // Indicates if the page stored in the asset folder should be processed
    // if yes, indicates the processing level
    // see CustomWebViewClient for more information
    public int mProcessLevel = CustomWebViewClient.PROCESS_LEVEL_NONE;

    // URL of the sample description and informations
    // Refers to a page inside the asset folder
    // if it is available
    public String mHTMLDescriptionURL = null;
}
