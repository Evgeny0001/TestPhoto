package com.bignerdranch.android.testphoto;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;


//Синтаксический разбор формата JSON
public class SoftinventPhoto extends Object {

    String mTitle;
    String mURL;

    public SoftinventPhoto(JSONObject jsonPhoto) throws JSONException{

        this.mTitle = (String) jsonPhoto.optString("title");
        this.mURL = (String) jsonPhoto.optString("url");
            }



    //При передаче этому методу строки в формате json получаем массив  SoftinventPhoto
    public static ArrayList<SoftinventPhoto> makePhotoList(String photoData)
        throws JSONException, NullPointerException{
        int i;
        ArrayList<SoftinventPhoto> softinventPhotos = new ArrayList<SoftinventPhoto>();
        //JSONObject data = new JSONObject(photoData);
        //JSONObject photos = data.optJSONObject("items");
        //JSONArray photoArray = photos.optJSONArray("required");//***
        JSONArray photoArray = new JSONArray(photoData);

        for(i = 0;i < photoArray.length();i++){
            JSONObject photo=photoArray.getJSONObject(i);
            //String value1=photo.optString("title");    ***

            //JSONObject photo = (JSONObject) photoArray.get(i);//***
           // Log.d("JSON",photo.getTitle() );
            SoftinventPhoto currentPhoto = new SoftinventPhoto(photo);
            softinventPhotos.add(currentPhoto);
        }
        return softinventPhotos;
    }

    public String getTitle() {
        if(mTitle != "null") {
            return mTitle;
        }else {
            return "photo";
        }
    }

    public String getUrl() {
        return mURL;
    }

}
