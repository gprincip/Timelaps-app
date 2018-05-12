package com.example.android.camera2basic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;

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

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getBaseContext(), ReportActivity.class);
                intent.putExtra("activityName", THIS_ACTIVITY);
                intent.putExtra("galleryName", (String) listAdapter.getItem(position));

                startActivity(intent);
            }
        });

    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_gallery_activity, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.context_menu_gallery_activity_open:

                Intent intent = new Intent(getBaseContext(), ReportActivity.class);
                intent.putExtra("activityName", THIS_ACTIVITY);
                intent.putExtra("galleryName", (String)listAdapter.getItem(info.position));
                startActivity(intent);

                break;

            case R.id.context_menu_gallery_activity_delete:

                listAdapter.deleteGallery((String)listAdapter.getItem(info.position));

                break;
        }

        return super.onContextItemSelected(item);

    }
}
