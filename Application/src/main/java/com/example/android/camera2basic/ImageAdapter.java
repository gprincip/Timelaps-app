package com.example.android.camera2basic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private int mCount;

    private String filePaths[];

    public ImageAdapter(Context c, int mCount) {
        mContext = c;
        this.mCount = mCount;
        filePaths = new String[mCount];

        for(int i=0; i< mCount; ++i){
            filePaths[i] = mContext.getExternalFilesDir(null).getPath()+"/picture"+i+".jpg";
        }

    }


    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        Bitmap bm = BitmapFactory.decodeFile(filePaths[position]);

        imageView.setImageBitmap(bm);
        return imageView;
    }


}
