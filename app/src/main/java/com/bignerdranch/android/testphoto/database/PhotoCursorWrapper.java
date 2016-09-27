package com.bignerdranch.android.testphoto.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.testphoto.Photo;
import com.bignerdranch.android.testphoto.database.PhotoDbSchema.PhotoTable;

import java.util.UUID;

/**
 * Created by admin on 19.08.2016.
 */
public class PhotoCursorWrapper extends CursorWrapper {
    public PhotoCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Photo getPhoto() {
        String url = getString(getColumnIndex(PhotoTable.Cols.URL));
        String title = getString(getColumnIndex(PhotoTable.Cols.TITLE));


        Photo photo = new Photo(url);
        photo.setTitle(title);
        photo.setUrl(url);

        return photo;
        //return null;
    }
}
