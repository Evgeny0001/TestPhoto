package com.bignerdranch.android.testphoto;

import java.util.UUID;

public class Photo {
    String mTitle;
    String mUrl;

    public Photo(){
        //Присвоить id
        //this(UUID.randomUUID());
        //mId = UUID.randomUUID();
    }
    public Photo(String url) {
       // mId = id;
        mUrl = url;
    }




    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPhotoFilename() {
        return "IMG_" + ".jpg";
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}









