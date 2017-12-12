package com.symbol.emdkallsamples;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by laure on 11/12/2017.
 */

public class SBSampleItemsArrayAdapter extends ArrayAdapter<SampleItem> {
    private Context mContext;
    private int mLayoutRessourceId;
    private List<SampleItem> mData;

    public SBSampleItemsArrayAdapter(Context context, int layoutresourceId, List<SampleItem> data) {
        super(context, layoutresourceId, data);
        this.mContext = context;
        this.mLayoutRessourceId = layoutresourceId;
        this.mData = data;
    }

    public int getCount() {
        return mData.size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        SampleItemHolder holder = null;

        if (rowView == null) {

            LayoutInflater vi = ((Activity)mContext).getLayoutInflater();
            rowView = vi.inflate(mLayoutRessourceId, parent, false);

            holder = new SampleItemHolder();
            holder.infoButton = (Button)rowView.findViewById(R.id.bt_info);
            holder.txtTitle = (TextView)rowView.findViewById(R.id.txt_sampletitle);
            rowView.setTag(holder);
        }
        else
        {
            holder = (SampleItemHolder)rowView.getTag();
        }

        SampleItem item = mData.get(position);

        holder.txtTitle.setText(item.mTitle);
        if(item.mHTMLDescriptionURL != null && !item.mHTMLDescriptionURL.isEmpty())
        {
            holder.infoButton.setVisibility(View.VISIBLE);
            final SampleItem fItem = item;
            holder.infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Display information webview here
                    startInfoViewerActivity(fItem);
                    //String pageToShow = fItem.mHTMLDescriptionURL;
                    //Toast.makeText(mContext, pageToShow, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            holder.infoButton.setVisibility(View.GONE);
        }
        return rowView;
    }

    private void startInfoViewerActivity(SampleItem item)
    {
        Intent intent = new Intent(mContext,
                InfoViewer.class);

        Bundle extras = new Bundle();
        extras.putString(Constants.BUNDLE_EXTRA_SAMPLE_TITLE, item.mTitle);
        extras.putString(Constants.BUNDLE_EXTRA_SAMPLE_TAGID, item.mTagID);
        extras.putInt(Constants.BUNDLE_EXTRA_SAMPLE_PROCESSINGLEVEL, item.mProcessLevel);
        extras.putString(Constants.BUNDLE_EXTRA_ASSET_URL, item.mHTMLDescriptionURL);
        intent.putExtras(extras);

        mContext.startActivity(intent);
    }


    static class SampleItemHolder
    {
        TextView txtTitle;
        Button  infoButton;
    }
}