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

public class galleryListAdapter extends BaseAdapter {

    List<String> items = new ArrayList<String>();
    List<Boolean> itemsSelected;
    Context mContext;

    public galleryListAdapter(Context context) {
        this.mContext = context;

        File files[] = mContext.getExternalFilesDir(null).listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory() && files[i].getName().compareTo("videos") != 0)
                items.add(files[i].getName());
        }

        itemsSelected  = new ArrayList<Boolean>();

        for(int i=0; i<getCount(); i++){
            itemsSelected.add(new Boolean(false));
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
        if (convertView == null) {
            textView = new TextView(mContext);
        } else {
            textView = (TextView) convertView;
        }

        textView.setText(items.get(position));
        textView.setTextSize(30);
        if(itemsSelected.get(position))
            textView.setBackgroundColor(Color.BLACK);
        else textView.setBackgroundColor(Color.TRANSPARENT);
        return textView;

    }

    public Boolean deleteDir(File dir) {

        File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile())
                if(!files[i].delete()) return false;
            else if (files[i].isDirectory())
                if(!deleteDir(files[i])) return false;
        }
        return dir.delete();

    }

    public Boolean deleteGallery(String galleryName) {

        for (int i = 0; i < items.size(); i++)
            if (items.get(i).compareTo(galleryName) == 0)
                items.remove(items.get(i));

        notifyDataSetChanged();

        File gallery = new File(mContext.getExternalFilesDir(null).getPath() + "/" + galleryName);
        return deleteDir(gallery);

    }


}
