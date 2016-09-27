package com.bignerdranch.android.testphoto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.bignerdranch.android.testphoto.database.PhotoBaseHelper;
import com.bignerdranch.android.testphoto.database.PhotoCursorWrapper;
import com.bignerdranch.android.testphoto.database.PhotoDbSchema.PhotoTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//Синглетный класс, отвечающий за хранение всех данные в TestPhoto
public class PhotoLab {
    private static PhotoLab sPhotoLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    //private File mPhotoFile;

    public static PhotoLab get(Context context) {
        if (sPhotoLab == null) {
            sPhotoLab = new PhotoLab(context);
        }
        return sPhotoLab;
    }
    private PhotoLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PhotoBaseHelper(mContext).getWritableDatabase();
    }
    public List<Photo> getPhotos() {
        List<Photo> photos = new ArrayList<>();
        PhotoCursorWrapper cursor = queryPhotos(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                photos.add(cursor.getPhoto());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return photos;
    }
    public Photo getPhoto(String url) {
        PhotoCursorWrapper cursor = queryPhotos(
                PhotoTable.Cols.URL + " = ?",
                new String[] { url.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getPhoto();
        } finally {
            cursor.close();
        }
    }
    public void updatePhoto(Photo photo) {
        String urlString = photo.getUrl().toString();

        ContentValues values = getContentValues(photo);
        mDatabase.update(PhotoTable.NAME, values, PhotoTable.Cols.URL + " = ?", new String[] { urlString });
    }

    private static ContentValues getContentValues(Photo photo) {
        ContentValues values = new ContentValues();
        values.put(PhotoTable.Cols.TITLE, photo.getTitle());
        values.put(PhotoTable.Cols.URL, photo.getUrl());
        return values;
    }
    //private Cursor queryPhotos(String whereClause, String[] whereArgs) {
    private PhotoCursorWrapper queryPhotos(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                PhotoTable.NAME,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        //return cursor;
        return new PhotoCursorWrapper(cursor);
    }

    public void addPhoto(Photo c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(PhotoTable.NAME, null, values);
    }
    public void DeletePhoto(UUID id){
        mDatabase.delete(PhotoTable.NAME,PhotoTable.Cols.URL + " = ?", new String[] { id.toString() });
    }
    public File getPhotoFile(Photo photo) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) {
            return null;
        }
        return new File(externalFilesDir, photo.getPhotoFilename());
    }
}