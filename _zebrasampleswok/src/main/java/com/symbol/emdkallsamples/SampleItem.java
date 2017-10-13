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
    // Description of the use-case
    public String mDescription = null;

    // Link on GitHub if a source code is available
    public String mSourceCodeLink = null;

    // Documentation links Pair<Description/title, URL>
    public String mDocumentationLink = null;

    // Resources
    public String mResources = null;
}
