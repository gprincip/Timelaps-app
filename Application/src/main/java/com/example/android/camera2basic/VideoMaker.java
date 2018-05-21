package com.example.android.camera2basic;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.FileChannelWrapper;
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

    private List<Picture> pictures;

    private Context context;

    public VideoMaker(Context context, List<Picture> pictures){
        this.context = context;
        this.pictures = pictures;
    }

    public VideoMaker(){
        pictures = new ArrayList<>();
    }

    public void addPicture(Picture p){
        pictures.add(p);
    }

    public List<Picture> getPictures(){
        return pictures;
    }

    public Picture getPicture(int i){
        return pictures.get(i);
    }

    public void setContext(Context c){
        this.context = c;
    }

    public Context getContext(){
        return context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void makeVideo() throws IOException {

        FileChannelWrapper out = null;
        try {
            String path = context.getExternalFilesDir(null).getPath().toString();
            Boolean makePath = new File(path + "/videos").mkdirs();
            out = NIOUtils.writableFileChannel(path + "/videos/output.mp4");
            // for Android use: AndroidSequenceEncoder
            AndroidSequenceEncoder encoder = new AndroidSequenceEncoder(
                    (org.jcodec.common.io.SeekableByteChannel) out, Rational.R(25, 1));

            for (Picture p : pictures) {
                // Generate the image, for Android use Bitmap
                //BufferedImage image = ...;
                // Encode the image
            Bitmap b = BitmapFactory.decodeFile(p.getThumbnailPath());
                encoder.encodeImage(b);
            }
            // Finalize the encoding, i.e. clear the buffers, write the header, etc.
            encoder.finish();

        } finally {
            NIOUtils.closeQuietly(out);
        }
    }

}
