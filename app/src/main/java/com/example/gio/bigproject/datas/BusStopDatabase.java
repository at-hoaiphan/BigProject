package com.example.gio.bigproject.datas;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.gio.bigproject.models.bus_stops.PlaceStop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright by Gio.
 * Created on 4/24/2017.
 */

public class BusStopDatabase extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "busstop.sqlite";
    private static String TABLE_PLACES = "busplaces";
    private final static String DATABASE_PATH = "/data/data/com.example.gio.bigproject/databases/";
    private static final int DATABASE_VERSION = 2;

    private Context mContext;
    private SQLiteDatabase mSqLiteDatabase;

    public BusStopDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        // checking database and open it if exists
        if (checkDataBase()) {
            openDataBase();
        } else {
            try {
                this.getReadableDatabase();
                copyDataBase();
                this.close();
                openDataBase();

            } catch (IOException e) {
                throw new Error("Error copying database");
            }
            Toast.makeText(context, "Initial database is created", Toast.LENGTH_LONG).show();
        }
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = mContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private void openDataBase() throws SQLException {
        String dbPath = DATABASE_PATH + DATABASE_NAME;
        SQLiteDatabase dataBase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private boolean checkDataBase() {
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    public List<PlaceStop> getAllPlaces() {
        mSqLiteDatabase = getWritableDatabase();
        List<PlaceStop> listPlaces = new ArrayList<>();
        Cursor cursor = mSqLiteDatabase.rawQuery("select * from " + TABLE_PLACES, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            listPlaces.add(new PlaceStop(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3),
                    cursor.getString(4), cursor.getString(5)));
            cursor.moveToNext();
        }
        cursor.close();
        return listPlaces;
    }

    public List<PlaceStop> getPlacesByIdCarriage(String idCarriage) {
        mSqLiteDatabase = getWritableDatabase();
        List<PlaceStop> listPlaces = new ArrayList<>();
        Cursor cursor = mSqLiteDatabase.rawQuery("select * from " + TABLE_PLACES + " where idCarriage like '%" + idCarriage + "%'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            listPlaces.add(new PlaceStop(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3),
                    cursor.getString(4), cursor.getString(5)));
            cursor.moveToNext();
        }
        cursor.close();
        return listPlaces;
    }

//    public int getCountPlaces(String idCarriage) {
//        SQLiteDatabase mSqLiteDatabase = getWritableDatabase();
//        ArrayList<PlaceStop> listPlaces = new ArrayList<>();
//        Cursor cursor = mSqLiteDatabase.rawQuery("select * from " + TABLE_PLACES + " where idCarriage like '%" + idCarriage + "%'", null);
//        return cursor.getCount();
//    }
//
//    public PlaceStop getPlaceById(int id) {
//        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_PLACES + " where " + PlaceStop._ID + " = " + id, null);
//        if (cursor.moveToNext()) {
//            return new PlaceStop(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3),
//                    cursor.getString(4), cursor.getString(5));
//        }
//        return null;
//    }

    // Unused implemented-functions
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
