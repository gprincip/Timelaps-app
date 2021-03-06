/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.camera2basic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    public static int shootingInterval = -1;
    public static int videoDuration = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.main_menu_settings : openSettingsActivity(); break;
            case R.id.main_menu_galleries : openGallery(); break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openVideosFolder(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri videosDir = Uri.parse(getExternalFilesDir(null).getPath() + "/videos");
        intent.setDataAndType(videosDir,"application/*");
        startActivity(intent);
    }

    public void openSettingsActivity(){

        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);

    }

    private void openGallery() {
        //Check if gallery folder is empty, if it is don't start activity

        File[] files = getApplicationContext().getExternalFilesDir(null).listFiles();

        for(int i=0; i<files.length; i++) {
            if (files[i].isDirectory() && files[i].getName() != "videos") {
                Intent intent = new Intent(this, GalleryActivity.class);
                startActivity(intent);
                return;
            }
        }
        Toast.makeText(this,"Gallery is empty",Toast.LENGTH_SHORT).show();
    }
}
