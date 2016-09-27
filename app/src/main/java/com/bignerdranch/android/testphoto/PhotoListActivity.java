package com.bignerdranch.android.testphoto;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class PhotoListActivity extends AppCompatActivity {
    private ArrayList<SoftinventPhoto> mPhotos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        LoadPhotos showTask = new LoadPhotos();
        showTask.execute();




    }
    private void addfragment(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new PhotoListFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }


    //Реализация AsyncTask

    private class LoadPhotos extends AsyncTask<String,String,Long> {
        @Override
        protected void onPreExecute() {
            // mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Long doInBackground(String... params) {

            HttpURLConnection connection = null;
            try {
                URL dataUrl = new URL("http://softinvent.ru/job/photo.json");
                connection = (HttpURLConnection) dataUrl.openConnection();
                //connection.setRequestMethod("GET");//С сайта
                connection.connect();
                int status = connection.getResponseCode();
                Log.d("connection", "status " + status);
                if (status == 200) {
                    InputStream is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String responseString;
                    StringBuilder sb = new StringBuilder();
                    while ((responseString = reader.readLine()) != null) {
                        sb = sb.append(responseString);
                    }
                    String photoData = sb.toString();
                    mPhotos = SoftinventPhoto.makePhotoList(photoData);
                    Log.d("connection", photoData);
                    return (0l);
                } else {
                    return (1l);
                }
            } catch (MalformedURLException e) {
                //connection.disconnect();

                e.printStackTrace();
                return (1l);
            } catch (IOException e) {

                //connection.disconnect();
                e.printStackTrace();
                return (1l);
            } catch (JSONException e) {
                Log.d("MalformedURLException", "Error");
                //connection.disconnect();
                return (1l);
            }
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            if (result == (0l)) {

                PhotoLab photoLab = PhotoLab.get(PhotoListActivity.this);
                for(SoftinventPhoto currentPhoto : mPhotos){
                    Photo existingPhoto = photoLab.getPhoto(currentPhoto.getUrl());
                    if(existingPhoto == null){
                        existingPhoto = new Photo();
                        existingPhoto.setTitle(currentPhoto.getTitle());
                        existingPhoto.setUrl(currentPhoto.getUrl());
                        PhotoLab.get(PhotoListActivity.this).addPhoto(existingPhoto);
                    }else{
                        // Photo photo = new Photo();
                        existingPhoto.setTitle(currentPhoto.getTitle());
                        existingPhoto.setUrl(currentPhoto.getUrl());
                        photoLab.updatePhoto(existingPhoto);
                    }
                }
                addfragment();

            } else {
                Toast.makeText(PhotoListActivity.this.getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            //        mProgressBar.setVisibility(View.GONE);
            addfragment();
        }
    }
    public ArrayList<SoftinventPhoto> getPhotos(){
        return mPhotos;
    }
}
