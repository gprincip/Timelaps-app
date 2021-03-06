package com.example.android.camera2basic;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity implements MediaScannerConnection.MediaScannerConnectionClient {

    private String SCAN_PATH;
    private static final String FILE_TYPE = "image/*";

    private MediaScannerConnection conn;
    private ImageAdapter adapter = null;
    int numberOfPicturesTaken = -1;
    boolean picturesSaved = false;

    GridView gridView = null;
    TextView picturesTaken = null;
    ImageButton deleteSelectedItemsButton = null;
    ImageButton savePhotosButton = null;
    ImageButton makeVideoButton = null;
    TextView galleryNameTextView = null;
    TextView progressInfoTextView = null;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        picturesSaved = false;

        Intent intent = getIntent();

        numberOfPicturesTaken = intent.getIntExtra("numberOfPictures", -1);
        String callingActivity = intent.getStringExtra("activityName");

        /* Initializations of views */

        picturesTaken = findViewById(R.id.picturesTaken);
        gridView = (GridView) findViewById(R.id.gridViewGallery);
        deleteSelectedItemsButton = findViewById(R.id.deleteSelectedItemsButton);
        savePhotosButton = findViewById(R.id.savePhotos);
        galleryNameTextView = findViewById(R.id.galleryNameTextView);
        progressInfoTextView = findViewById(R.id.progressInfoTextView);
        progressBar = findViewById(R.id.progress);
        makeVideoButton = findViewById(R.id.makeVideo);

        /********************************************/

        deleteSelectedItemsButton.setVisibility(View.INVISIBLE);

        String galleryName = "Untitled";

        if (callingActivity.compareTo(Camera2BasicFragment.THIS_ACTIVITY) == 0)
            adapter = new ImageAdapter(this, numberOfPicturesTaken);
        else if (callingActivity.compareTo(GalleryActivity.THIS_ACTIVITY) == 0) {
            galleryName = intent.getStringExtra("galleryName");
            adapter = new ImageAdapter(this, galleryName);
            picturesSaved = true;
        }
        gridView.setAdapter(adapter);

        picturesTaken.setText("" + adapter.getCount());

        galleryNameTextView.setText("Gallery name: " + galleryName);

        registerForContextMenu(gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                adapter.getItem(position).setSelected(!adapter.getItem(position).getSelected());

                if (adapter.getItem(position).getSelected()) {
                    gridView.getChildAt(position).setBackgroundColor(Color.BLACK);
                } else {
                    gridView.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
                }


                if (someItemIsSelected()) deleteSelectedItemsButton.setVisibility(View.VISIBLE);
                else deleteSelectedItemsButton.setVisibility(View.INVISIBLE);

            }
        });

        deleteSelectedItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Picture> toBeDeleted = new ArrayList<Picture>();

                for (int i = 0; i < adapter.getCount(); i++) {

                    if (adapter.getItem(i).getSelected())
                        toBeDeleted.add(adapter.getItem(i));
                }


                deletePictures(toBeDeleted);
                picturesTaken.setText("" + (adapter.getCount()));
                updateSelections();
                adapter.notifyDataSetChanged();

            }
        });

        if (callingActivity.compareTo(GalleryActivity.THIS_ACTIVITY) == 0) {
            savePhotosButton.setVisibility(View.INVISIBLE);
            picturesSaved = true;
        } else {
            savePhotosButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savePhotos();
                }
            });
        }

        makeVideoButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                picturesSaved = true;

                showVideoNameDialog();

            }
        });

    }

    private void deletePictures(ArrayList<Picture> toBeDeleted) {

        adapter.removePictures(toBeDeleted);

        if (!someItemIsSelected()) deleteSelectedItemsButton.setVisibility(View.INVISIBLE);

    }

    private boolean someItemIsSelected() {

        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).getSelected()) {
                return true;
            }
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void savePhotos() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter gallery name");


        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(editText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @TargetApi(24)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File[] files = getExternalFilesDir(null).listFiles();
                File storageLocationPath =
                        new File(getExternalFilesDir(null).getPath() + "/" + editText.getText().toString());
                storageLocationPath.mkdirs();

                for (int i = 0; i < files.length; i++) {

                    if (files[i].isFile() && files[i].getPath().endsWith(".jpg")) {

                        Picture p = adapter.getPicture(files[i].getPath());

                        String newPath = storageLocationPath + "/" + files[i].getName();
                        files[i].renameTo(new File(newPath));

                        if (p != null) {
                            p.setPath(newPath);
                        }
                    }

                }

                Toast.makeText(getApplicationContext(), "Photos saved at: " + storageLocationPath, Toast.LENGTH_SHORT).show();

                galleryNameTextView.setText("Gallery name: " + editText.getText().toString());

                picturesSaved = true;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showVideoNameDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter name of the video");


        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(editText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @TargetApi(24)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter name of the video", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    showVideoNameDialog();
                } else {
                    String videoName = editText.getText().toString();
                    final CreateVideoTask task = new CreateVideoTask(videoName);
                    task.execute(adapter.getListOfPictures().toArray(new Picture[0]));
                }
            }


        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_report_activity, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.context_menu_report_activity_view:
                Picture selectedPicture = (Picture) gridView.getAdapter().getItem(info.position);
                String path = selectedPicture.getPath();
                SCAN_PATH = path;
                startScan();

                break;
            case R.id.context_menu_report_activity_delete:
                deletePicture(info.id);
                picturesTaken.setText("" + (adapter.getCount()));
                updateSelections();
                adapter.notifyDataSetChanged();

        }

        return super.onContextItemSelected(item);
    }

    private void deletePicture(long id) {

        adapter.removePicture(id);

        if (!someItemIsSelected()) deleteSelectedItemsButton.setVisibility(View.INVISIBLE);

    }

    /* Check if items are selected and set up the background color of views */

    private void updateSelections() {

        for (int i = 0; i < adapter.getCount(); i++) {

            if (adapter.getItem(i).getSelected())
                gridView.getChildAt(i).setBackgroundColor(Color.BLACK);
            else gridView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

        }

    }

    private void startScan() {
        Log.d("Connected", "success" + conn);
        if (conn != null) {
            conn.disconnect();
        }

        conn = new MediaScannerConnection(this, this);
        conn.connect();
    }


    @Override
    public void onMediaScannerConnected() {
        Log.d("onMediaScannerConnected", "success" + conn);
        conn.scanFile(SCAN_PATH, FILE_TYPE);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        try {
            Log.d("onScanCompleted", uri + "success" + conn);
            if (uri != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
            }
        } finally {
            conn.disconnect();
            conn = null;
        }
    }

    @Override
    public void onBackPressed() {

        if (!picturesSaved) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Discard taken photos?");

            builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    File files[] = getApplicationContext().getExternalFilesDir(null).listFiles();

                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isFile()) {
                            files[i].delete();
                        }
                    }

                    ReportActivity.super.onBackPressed();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Do nothing
                }
            });

            builder.show();

        } else ReportActivity.super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public class CreateVideoTask extends AsyncTask<Picture, Integer, Void> {

        public static final String TAG = "SNIMANJE";
        private int totalPictures;
        private String videoName;

        public CreateVideoTask(String videoName) {
            this.videoName = videoName;
        }

        private void enableViews(){
            gridView.setEnabled(true);
            deleteSelectedItemsButton.setEnabled(true);
            savePhotosButton.setEnabled(true);
            makeVideoButton.setEnabled(true);
        }

        private void disableViews(){
            gridView.setEnabled(false);
            deleteSelectedItemsButton.setEnabled(false);
            savePhotosButton.setEnabled(false);
            makeVideoButton.setEnabled(false);
        }

        public CreateVideoTask() {
        }

        @Override
        protected Void doInBackground(Picture... pictures) {

            totalPictures = pictures.length;

            VideoMaker vm = new VideoMaker(getApplicationContext(), pictures);
            try {
                vm.makeVideo(this, videoName);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        public void doPublishProgress(int done) {
            if (done == 1)
                progressBar.setMax(totalPictures);
            publishProgress(done);
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressInfoTextView.setText(0 + "% done");
            disableViews();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int done = values[0];

            Log.i(TAG, "Done " + String.valueOf(done) + " of " + totalPictures);
            int percentage = (int) (((double) done / (double) totalPictures) * 100);
            progressInfoTextView.setText(percentage + "% done");

            progressBar.setProgress(done);

            if (done == totalPictures)
                Toast.makeText(getApplicationContext(), "Saved at " +
                        getExternalFilesDir(null).getPath().toString()+"/"+videoName, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i(TAG, "Finished.");
            progressBar.setVisibility(View.INVISIBLE);
            progressInfoTextView.setVisibility(View.INVISIBLE);
            enableViews();
        }
    }

}
