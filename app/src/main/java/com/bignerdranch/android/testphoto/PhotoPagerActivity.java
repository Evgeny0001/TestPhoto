package com.bignerdranch.android.testphoto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class PhotoPagerActivity extends AppCompatActivity{//FragmentActivity {
    private static final String EXTRA_PHOTO_URL = "com.bignerdranch.android.testphoto.photo_url";
    private ViewPager mViewPager;
    private List<Photo> mPhotos;

    public static Intent newIntent(Context packageContext, String photoURL) {
        Intent intent = new Intent(packageContext, PhotoPagerActivity.class);
        intent.putExtra(EXTRA_PHOTO_URL, photoURL);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pager);

        mViewPager = (ViewPager) findViewById(R.id.activity_photo_pager_view_pager);
        mPhotos = PhotoLab.get(this).getPhotos();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Photo photo = mPhotos.get(position);
                return PhotoFragment.newInstance(photo.getUrl());
            }
            @Override
            public int getCount() {
                return mPhotos.size();
            }
        });
        //UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);//Функуия NewInt... в CrimeActivity
        String photoId = (String) getIntent().getSerializableExtra(EXTRA_PHOTO_URL);
        for (int i = 0; i < mPhotos.size(); i++) {
            if (mPhotos.get(i).getUrl().equals(photoId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
