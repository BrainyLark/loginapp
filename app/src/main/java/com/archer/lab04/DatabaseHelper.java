package com.archer.lab04;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by archer on 2017-10-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "crudbase.db";
    private static final int DATABASE_VERSION = 1;
    public  static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String DATE = "date";
    public static final String AGE = "age";
    public static final String GENDER = "gender";
    public static final String PHONE = "phone";
    public static final String _ID = "id";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE users(id integer primary key autoincrement, username text, password text, date text, age integer, gender text, phone text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
