package com.example.android.camera2basic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity implements MediaScannerConnection.MediaScannerConnectionClient {

    private String SCAN_PATH;
    private static final String FILE_TYPE = "image/*";

    private MediaScannerConnection conn;
    private ImageAdapter adapter = null;
    int numberOfPicturesTaken = -1;

    GridView gridView = null;
    TextView picturesTaken = null;
    Button deleteSelectedItemsButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent intent = getIntent();
        numberOfPicturesTaken = intent.getIntExtra("numberOfPictures", -1);

        /* Initializations of views */

        picturesTaken = findViewById(R.id.picturesTaken);
        gridView = (GridView) findViewById(R.id.gridViewGallery);
        deleteSelectedItemsButton = findViewById(R.id.deleteSelectedItemsButton);

        /********************************************/

        deleteSelectedItemsButton.setVisibility(View.INVISIBLE);

        picturesTaken.setText("" + numberOfPicturesTaken);

        adapter = new ImageAdapter(this, numberOfPicturesTaken);
        gridView.setAdapter(adapter);

        registerForContextMenu(gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                adapter.getItem(position).setSelected(!adapter.getItem(position).getSelected());

                if (adapter.getItem(position).getSelected())
                    gridView.getChildAt(position).setBackgroundColor(Color.BLACK);
                else gridView.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);


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

    }

    private void deletePictures(ArrayList<Picture> toBeDeleted) {

        adapter.removePictures(toBeDeleted);

        if(!someItemIsSelected()) deleteSelectedItemsButton.setVisibility(View.INVISIBLE);

    }

    private boolean someItemIsSelected() {

        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).getSelected()) {
                return true;
            }
        }
        return false;
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

        if(!someItemIsSelected()) deleteSelectedItemsButton.setVisibility(View.INVISIBLE);

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
}
