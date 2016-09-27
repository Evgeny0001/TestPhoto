package com.bignerdranch.android.testphoto;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.UUID;

import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

//контроллер, взаимодействующий с объектами модели и представления.
// Его задача — выдача подробной информации о конкретноq фотке (и  обновление при модификации пользователем)
public class PhotoFragment extends Fragment {
    private Photo mPhoto;
    private TextView mTitle;
    private ImageView mPhotoView;


    //Для передачи фрагменту параметров
    private static final String ARG_PHOTO_URL = "photo_url";

    public static PhotoFragment newInstance(String photoUrl) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO_URL, photoUrl);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //...
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String photoUrl = (String) getArguments().getSerializable(ARG_PHOTO_URL);
        mPhoto = PhotoLab.get(getActivity()).getPhoto(photoUrl);
        //mPhotoFile = PhotoLab.get(getActivity()).getPhotoFile(mPhoto);//???????????
        LoadImage showTask = new LoadImage(mPhotoView,(String) getArguments().getSerializable(ARG_PHOTO_URL));
        showTask.execute();

    }

    @Override
    public void onPause() {
        super.onPause();
        PhotoLab.get(getActivity()).updatePhoto(mPhoto);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo, container, false);
        //String photoUrl = (String) getArguments().getSerializable(ARG_PHOTO_URL);

        mPhotoView = (ImageView) v.findViewById(R.id.photo);
        LoadImage showTask = new LoadImage(mPhotoView,(String) getArguments().getSerializable(ARG_PHOTO_URL));
        showTask.execute();
        //updatePhotoView();
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private class LoadImage extends AsyncTask<String, String, Long> {
        private ImageView mImageView;
        private String mImageString;
        private Bitmap mBitmap;

        public LoadImage(ImageView v, String imageString) {
            mImageView = v;
            mImageString = imageString;
        }

        @Override
        protected void onPreExecute() {
            // mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Long doInBackground(String... params) {

            String imageFileName = mImageString.replace(":","");
            imageFileName = imageFileName.replace("/","");
            imageFileName = imageFileName.replace(".","");
            File imageFile = new File(getActivity().getCacheDir(),imageFileName);
            OutputStream imageOS;
            if (imageFile.exists()){
                mBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                return (0l);
            }
            try {
                imageFile.createNewFile();
                URL imageUrl = new URL(mImageString);
                URLConnection connection = imageUrl.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                mBitmap = BitmapFactory.decodeStream(is);
                imageOS = new BufferedOutputStream(new FileOutputStream(imageFile));
                mBitmap.compress(Bitmap.CompressFormat.JPEG,100,imageOS);
                imageOS.flush();
                imageOS.close();
                return (0l);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return (1l);
            } catch (IOException e) {
                e.printStackTrace();
                return (1l);
            }
        }


        @Override
        protected void onPostExecute(Long result) {
            if (result == (0l)) {
                mImageView.setImageBitmap(mBitmap);
            }else{
                Toast.makeText(getActivity().getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
