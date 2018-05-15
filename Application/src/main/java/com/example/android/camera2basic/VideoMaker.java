package com.example.android.camera2basic;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 5/15/2018.
 */

public class VideoMaker {

    private List<Bitmap> bitmaps;

    private Context context;

    public VideoMaker(Context context){
        this.context = context;
        bitmaps = new ArrayList<Bitmap>();
    }

    public VideoMaker(){
        bitmaps = new ArrayList<Bitmap>();
    }

    public void addBitmap(Bitmap b){
        bitmaps.add(b);
    }

    public List<Bitmap> getBitmaps(){
        return bitmaps;
    }

    public Bitmap getBitmap(int i){
        return bitmaps.get(i);
    }

    public void setContext(Context c){
        this.context = c;
    }

    public Context getContext(){
        return context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void makeVideo() throws IOException {

        SeekableByteChannel out = null;
        try {
            String path = context.getExternalFilesDir(null).getPath().toString();
            Boolean makePath = new File(path + "/videos").mkdirs();
            out = (SeekableByteChannel) NIOUtils.writableFileChannel(path + "/videos/output.mp4");
            // for Android use: AndroidSequenceEncoder
            AndroidSequenceEncoder encoder = new AndroidSequenceEncoder((org.jcodec.common.io.SeekableByteChannel) out, Rational.R(25, 1));

            for (Bitmap b : bitmaps) {
                // Generate the image, for Android use Bitmap
                //BufferedImage image = ...;
                // Encode the image
                encoder.encodeImage(b);
            }
            // Finalize the encoding, i.e. clear the buffers, write the header, etc.
            encoder.finish();

        } finally {
            NIOUtils.closeQuietly(out);
        }
    }

}
