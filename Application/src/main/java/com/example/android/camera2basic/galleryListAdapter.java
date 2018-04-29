package com.example.android.camera2basic;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gavrilo on 29.4.18..
 */

public class galleryListAdapter extends BaseAdapter{

    List<String> items = new ArrayList<String>();
    Context mContext;

    public galleryListAdapter(Context context){
        this.mContext = context;

        File files[] = mContext.getExternalFilesDir(null).listFiles();

        for(int i=0; i<files.length; i++){
            if(files[i].isDirectory())
                items.add(files[i].getName());
        }

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView;
        if(convertView == null){
            textView = new TextView(mContext);
        }else{
            textView = (TextView)convertView;
        }

        textView.setText(items.get(position));
        textView.setTextSize(30);
        return textView;


    }
}
