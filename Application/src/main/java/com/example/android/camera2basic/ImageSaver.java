package com.example.android.camera2basic;

/**
 * Created by student on 3/20/2018.
 */

import android.content.Context;
import android.media.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Saves a JPEG {@link Image} into the specified {@link File}.
 */
public class ImageSaver implements Runnable {

    /**
     * The JPEG image
     */
    private final Image mImage;
    /**
     * The file we save the image into.
     */
    private final int mPictureNumber;

    private final Context mContext;

    public ImageSaver(Context context, Image image, int pictureNumber) {
        mImage = image;
        mPictureNumber = pictureNumber;
        mContext = context;
    }

    @Override
    public void run() {
        ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(new File(mContext.getExternalFilesDir(null), "picture" + mPictureNumber +".jpg"));
            output.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mImage.close();
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}