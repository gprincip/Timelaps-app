package com.example.android.camera2basic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private int mCount;

    private ArrayList<Picture> pictures;

    public ImageAdapter(Context c, int mCount) {

        mContext = c;
        this.mCount = mCount;
        pictures = new ArrayList<Picture>();


        for (int i = 0; i < mCount; ++i) {

            String filePath = mContext.getExternalFilesDir(null).getPath() + "/picture" + i + ".jpg";
            String thumbnailPath = mContext.getExternalFilesDir(null).getPath() + "/picture" + i + "_thumb.jpg";

            pictures.add(new Picture(false, thumbnailPath, filePath));
        }

    }

    public ImageAdapter(Context c, String galleryName){

        mContext = c;
        pictures = new ArrayList<Picture>();
        String galleryPath = mContext.getExternalFilesDir(null)+"/"+galleryName;
        File externalDir = new File(galleryPath);
        File files[] = externalDir.listFiles();

        for(int i=0; i<files.length; i+=2){

            String filePath = files[i].getPath();
            String thumbnailPath = files[i+1].getPath();

            pictures.add(new Picture(false, thumbnailPath, filePath));

        }

        mCount = pictures.size();

    }

    public void removePicture(long id) {

        pictures.remove(getItem((int) id));

    }

    @Override
    public int getCount() {

        return pictures.size();

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


        } catch (Exception ex) {

        }

        return imageView;
    }

    File findFileInArray(File[] files, String path) {

        for (int i = 0; i < files.length; i++) {
            if (files[i].getPath().compareTo(path) == 0)
                return files[i];
        }

        return null;
    }

    public void removePictures(ArrayList<Picture> toBeDeleted) {

        //Trenutno ne brise slike kada se galerija otvori iz Camera2BasicFragment aktivnosti

        File files[] = mContext.getExternalFilesDir(null).listFiles();

        for (int i = 0; i < toBeDeleted.size(); i++) {
            pictures.remove(toBeDeleted.get(i));
            File delete = findFileInArray(files, toBeDeleted.get(i).getPath());
            File deleteThumbnail = findFileInArray(files, toBeDeleted.get(i).getThumbnailPath());
            if(delete != null)
                delete.delete();

            if(deleteThumbnail != null)
                deleteThumbnail.delete();

        }

    }
}
