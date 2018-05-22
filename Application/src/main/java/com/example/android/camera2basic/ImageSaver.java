package com.example.android.camera2basic;

/**
 * Created by student on 3/20/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.ByteArrayOutputStream;
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

    private byte[] createImageForVideo(byte[] bytes){

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        int width = bitmap.getWidth();

        int ratio = width / 50;

        Bitmap resizedBitmap = getResizedBitmap(bitmap, 350);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);

        return baos.toByteArray();
    }

    private byte[] createThumbnail(byte[] bytes)
    {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        int width = bitmap.getWidth();

        int ratio = width / 50;

        Bitmap thumb = Bitmap.createScaledBitmap(bitmap, 50, bitmap.getHeight() / ratio + 1, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumb.compress(Bitmap.CompressFormat.JPEG, 60, baos);

        return baos.toByteArray();
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();  // image.getHeight()

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void run() {
        ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        FileOutputStream output = null;

        FileOutputStream thumbnailOutput = null;

        try {
            output = new FileOutputStream(new File(mContext.getExternalFilesDir(null), "picture" + mPictureNumber +".jpg"));
            output.write(createImageForVideo(bytes));

            thumbnailOutput = new FileOutputStream(new File(mContext.getExternalFilesDir(null), "picture" + mPictureNumber +"_thumb.jpg"));
            thumbnailOutput.write(createThumbnail(bytes));

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
            if (thumbnailOutput != null) {
                try {
                    thumbnailOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}