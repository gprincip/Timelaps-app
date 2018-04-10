package com.example.android.camera2basic;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ReportActivity extends AppCompatActivity implements MediaScannerConnection.MediaScannerConnectionClient{

    private String SCAN_PATH ;
    private static final String FILE_TYPE = "image/*";

    private MediaScannerConnection conn;
    private Adapter adapter = null;
    GridView gridView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent intent = getIntent();
        int numberOfPicturesTaken = intent.getIntExtra("numberOfPictures", -1);

        TextView picturesTaken = findViewById(R.id.picturesTaken);
        picturesTaken.setText(""+numberOfPicturesTaken);

        gridView = (GridView)findViewById(R.id.gridViewGallery);
        gridView.setAdapter(new ImageAdapter(this, numberOfPicturesTaken));

        registerForContextMenu(gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String filename = (String)parent.getAdapter().getItem(position);

                Log.i("TL", filename);

                SCAN_PATH = filename;

                startScan();
            }
        });

//        Intent intent2 = new Intent();
//        intent2.setType("image/*");
//        intent2.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent2, "Select Picture"), 1);


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

        switch (item.getItemId()){
            case R.id.context_menu_report_activity_view :
                String path = gridView.getAdapter().getItem(info.position).toString();
                SCAN_PATH = path;
                startScan();

                break;
            case R.id.context_menu_report_activity_delete :
                deletePicture(info.id);
        }

        return super.onContextItemSelected(item);
    }

    private void deletePicture(long id) {

    }

    private void startScan()
    {
        Log.d("Connected","success"+conn);
        if(conn!=null)
        {
            conn.disconnect();
        }

        conn = new MediaScannerConnection(this,this);
        conn.connect();
    }
    @Override
    public void onMediaScannerConnected() {
        Log.d("onMediaScannerConnected","success"+conn);
        conn.scanFile(SCAN_PATH, FILE_TYPE);
    }
    @Override
    public void onScanCompleted(String path, Uri uri) {
        try {
            Log.d("onScanCompleted",uri + "success"+conn);
            if (uri != null)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
            }
        } finally
        {
            conn.disconnect();
            conn = null;
        }
    }
}
