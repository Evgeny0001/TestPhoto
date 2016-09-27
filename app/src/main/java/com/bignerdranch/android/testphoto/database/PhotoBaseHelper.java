package com.bignerdranch.android.testphoto.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.testphoto.database.PhotoDbSchema.PhotoTable;

public class PhotoBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "photoBase.db";

    public PhotoBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table " + PhotoDbSchema.CrimeTable.NAME); после alt+enter:
        db.execSQL("create table " + PhotoTable.NAME + "(" +
                /*" _id integer primary key autoincrement, " +
                        CrimeTable.Cols.UUID + ", " +
                        CrimeTable.Cols.TITLE + ", " +
                        CrimeTable.Cols.DATE + ", " +
                        CrimeTable.Cols.SOLVED +
                        ")"*/
                " _id integer primary key autoincrement, " +
                PhotoTable.Cols.TITLE + ", " +
                PhotoTable.Cols.URL +
                ")"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}