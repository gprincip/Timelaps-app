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

    public ImageAdapter(Context c, String galleryName) {

        mContext = c;
        pictures = new ArrayList<Picture>();
        String galleryPath = mContext.getExternalFilesDir(null) + "/" + galleryName;
        File externalDir = new File(galleryPath);
        File files[] = externalDir.listFiles();

        for (int i = 0; i < files.length; i += 2) {

            String filePath = files[i].getPath();
            String thumbnailPath = files[i + 1].getPath();

            pictures.add(new Picture(false, thumbnailPath, filePath));

        }

        mCount = pictures.size();

    }

    public void removePicture(long id) {
        getItem((int) id).delete();
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

        } else imageView = (ImageView)convertView;

            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

            try {

                Bitmap bmThumbnail = BitmapFactory.decodeFile(getItem(position).getThumbnailPath());

//            Bitmap bmThumbnail = Bitmap.createScaledBitmap(bm, bm.getWidth() / 10, bm.getHeight() / 10, false);

//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                // imageData = baos.toByteArray();

                imageView.setImageBitmap(bmThumbnail);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        // byte imageData[];

        return imageView;
    }


    public void removePictures(ArrayList<Picture> toBeDeleted) {

        for (int i = 0; i < toBeDeleted.size(); i++) {
            toBeDeleted.get(i).delete();
            pictures.remove(toBeDeleted.get(i));
        }

    }

    public Picture getPicture(String path) {
        for (int i = 0; i < pictures.size(); i++) {
            if (pictures.get(i).getPath().compareTo(path) == 0)
                return pictures.get(i);
        }
        return null;
    }

    public ArrayList<Picture> getListOfPictures(){
        return pictures;
    }

}