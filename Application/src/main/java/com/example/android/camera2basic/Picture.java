package com.example.android.camera2basic;

/**
 * Created by student on 4/10/2018.
 */

public class Picture {

    private Boolean selected;
    private String thumbnailPath;
    private String path;

    public Picture (){}

    public Picture(Boolean selected, String thumbnailPath, String path) {
        this.selected = selected;
        this.thumbnailPath = thumbnailPath;
        this.path = path;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
