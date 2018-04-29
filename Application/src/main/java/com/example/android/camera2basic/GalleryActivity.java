package com.example.android.camera2basic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class GalleryActivity extends AppCompatActivity {

    public static final String THIS_ACTIVITY = "GalleryActivity";
    ListView listView;
    galleryListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        listView = findViewById(R.id.galleryListView);
        listAdapter = new galleryListAdapter(this);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getBaseContext(), ReportActivity.class);
                intent.putExtra("activityName", THIS_ACTIVITY);
                intent.putExtra("galleryName", (String)listAdapter.getItem(position));

                startActivity(intent);
            }
        });

    }
}
