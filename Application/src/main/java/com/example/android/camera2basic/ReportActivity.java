package com.example.android.camera2basic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    }



}
