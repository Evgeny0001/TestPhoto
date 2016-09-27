package com.bignerdranch.android.testphoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PhotoListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private TextView mClearView;//1
    //Итак, адаптер готов; остается связать его с RecyclerView. Реализуйте метод updateUI, который настраивает
    //пользовательский интерфейс PhotoListFragment. Пока он создает объект CrimeAdapter и назначает его RecyclerView.
    private CrimeAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_photo_list, container, false);

        mClearView = (TextView) view.findViewById(R.id.is_empty_text_view);


        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updareStart();


        //if (savedInstanceState != null) {
        //    mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        //}

        updateUI();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        updareStart();
    }


    private void updateUI() {
        PhotoLab photoLab = PhotoLab.get(getActivity());
        List<Photo> photos = photoLab.getPhotos();
        //mAdapter = new CrimeAdapter(photos);
        //mCrimeRecyclerView.setAdapter(mAdapter);
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(photos);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
            mAdapter.setPhotos(photos);
        }
    }

    private void updareStart(){
        PhotoLab photoLab = PhotoLab.get(getActivity());
        int crimeCount = photoLab.getPhotos().size();
        if (crimeCount == 0) {
            mCrimeRecyclerView.setVisibility(View.GONE);
            mClearView.setVisibility(View.VISIBLE);
        } else {
            mCrimeRecyclerView.setVisibility(View.VISIBLE);
            mClearView.setVisibility(View.GONE);
        }
    }


    //Как должен выглядить объекты в списке:
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Обработка касания
            private TextView mTitleTextView;
            private TextView mUrlTextView;


            private Photo mPhoto;

            public CrimeHolder(View itemView) {
            super(itemView);
            //Обработк касания
            itemView.setOnClickListener(this);
            //...
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mUrlTextView = (TextView) itemView.findViewById(R.id.list_item_crime_url_text_view);
        }


        public void bindCrime(Photo photo) {
            mPhoto = photo;
            mTitleTextView.setText(mPhoto.getTitle());
            mUrlTextView.setText(mPhoto.getUrl());
        }


        @Override
        public void onClick(View v) {
            //Toast.makeText(getActivity(), mPhoto.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = PhotoPagerActivity.newIntent(getActivity(), mPhoto.getUrl());
           startActivity(intent);
           updareStart();
        }



    }

    //Создаем адаптер
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Photo> mPhotos;

        public CrimeAdapter(List<Photo> photos) {
            mPhotos = photos;
        }

        //Запрос на создание объекта в списке
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_photo, parent, false); //подключаем готовый макет элемента списка.
            return new CrimeHolder(view);
        }

        //Задаем позицию объекта в списке
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Photo photo = mPhotos.get(position);
            //holder.mTitleTextView.setText(photo.getTitle());
            holder.bindCrime(photo);
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }
        public void setPhotos(List<Photo> photos) {
            mPhotos = photos;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



}
