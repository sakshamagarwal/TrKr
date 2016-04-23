package com.saksham.trkr;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by saksham on 23/4/16.
 */
public class DBHelper {

    public static final String DATABASE_NAME = "trkr.sqlite";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "words";
    public static final String COL_DATA = "col_words";
    public static final String _ID = "_id";
    public static final String CREATE_WORD_TABLE = "create table if not exists " + TABLE_NAME + " (" + _ID + " integer," +  COL_DATA + " text);";

    private Context context;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DBHelper(Context c) {
        context = c;
        helper = new DatabaseHelper(c);
    }


    private class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_WORD_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Do nothing for now
        }
    }

    public DBHelper open() throws  SQLException {
        db = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        helper.close();
    }

    public void insert (String filename) {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream;
        try {
            inputStream = assetManager.open(filename);
        } catch (IOException e) {
            Toast toast = Toast.makeText(context, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        db.beginTransaction();
        try {
            while ((line = in.readLine()) != null) {
                String word = line.trim();
                ContentValues values = new ContentValues();
                values.put(COL_DATA, word);
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (IOException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.endTransaction();
    }

    public Cursor getData() {
        String query = "select * from " + TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public void deleteData() {
        String query = "delete from " + TABLE_NAME;
        db.execSQL(query);
    }

    public Cursor getData(String searchKey) {
        if (searchKey != null) {
            String query = "select * from " + TABLE_NAME + " where " + COL_DATA + " like '" + searchKey + "%'";
            return db.rawQuery(query, null);
        } else {
            return null;
        }
    }

}
