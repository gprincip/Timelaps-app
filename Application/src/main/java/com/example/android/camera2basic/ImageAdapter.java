package com.example.android.camera2basic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private int mCount;

    private ArrayList<Integer> selectedPositions;

    private String thumbnailsPaths[];
    private String filePaths[];

    private ArrayList<Picture> pictures;

    public ImageAdapter(Context c, int mCount) {
        mContext = c;
        this.mCount = mCount;
        selectedPositions = new ArrayList<Integer>();
        pictures = new ArrayList<Picture>();

        filePaths = new String[mCount];
        thumbnailsPaths = new String[mCount];

        for(int i=0; i< mCount; ++i){
            filePaths[i] = mContext.getExternalFilesDir(null).getPath()+"/picture"+i+".jpg";
            thumbnailsPaths[i] = mContext.getExternalFilesDir(null).getPath()+"/picture"+i+"_thumb.jpg";

            String filePath = mContext.getExternalFilesDir(null).getPath()+"/picture"+i+".jpg";
            String thumbnailPath = mContext.getExternalFilesDir(null).getPath()+"/picture"+i+"_thumb.jpg";

            pictures.add(new Picture(false, thumbnailPath, filePath));
        }

    }

    public ArrayList<Integer> getSelectedPositions(){
        return selectedPositions;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public Picture getItem(int position) {

        return pictures.get(position);

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

       // byte imageData[];

        try {

            Bitmap bmThumbnail = BitmapFactory.decodeFile(getItem(position).getThumbnailPath());

//            Bitmap bmThumbnail = Bitmap.createScaledBitmap(bm, bm.getWidth() / 10, bm.getHeight() / 10, false);

//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
           // imageData = baos.toByteArray();

            imageView.setImageBitmap(bmThumbnail);

        } catch(Exception ex){

        }

        return imageView;
    }


}
