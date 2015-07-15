package com.example.root.listviewloader;

import android.widget.ImageView;

/**
 * Created by root on 15-7-15.
 */
public class MyItem {

    public String url;
    public String title;
    public String context;

    public MyItem(){

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
