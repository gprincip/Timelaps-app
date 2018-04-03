package com.example.android.camera2basic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent intent = getIntent();
        int numberOfPicturesTaken = intent.getIntExtra("numberOfPictures", -1);

        TextView picturesTaken = findViewById(R.id.picturesTaken);
        picturesTaken.setText(""+numberOfPicturesTaken);

        GridView gridView = (GridView)findViewById(R.id.gridViewGallery);
        gridView.setAdapter(new ImageAdapter(this, numberOfPicturesTaken));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String filename = (String)parent.getAdapter().getItem(position);

                Log.i("TL", filename);
            }
        });

//        Intent intent2 = new Intent();
//        intent2.setType("image/*");
//        intent2.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent2, "Select Picture"), 1);
    }



}
