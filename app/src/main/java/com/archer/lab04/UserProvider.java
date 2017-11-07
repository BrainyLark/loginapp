package com.archer.lab04;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class UserProvider extends ContentProvider {
    public static final String PROVIDER_NAME = "com.archer.Lab04.UserProvider";
    private static final String URL = "content://" + PROVIDER_NAME + "/users";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    public static final String USER_TABLE_NAME = "users";

    private static UriMatcher userUriMatcher = null;
    private static final int USERS = 1;
    private static final int USERS_ID = 2;
    static {
        userUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        userUriMatcher.addURI(PROVIDER_NAME, USER_TABLE_NAME, USERS);
        userUriMatcher.addURI(PROVIDER_NAME, USER_TABLE_NAME + "/#", USERS_ID);
    }

    // MIME types
    public static final String CONTENT_TYPE = "vnd.sdsu.cursor.dir/vnd.sdsu.name";
    public static final String CONTENT_ITEM_TYPE = "vnd.sdsu.cursor.item/vnd.sdsu.name";

    private SQLiteDatabase db;
    private DatabaseHelper dbhelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbhelper = new DatabaseHelper(context);
        db = dbhelper.getWritableDatabase();
        return (db == null) ? false:true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String sqlSelection = null;

        switch (userUriMatcher.match(uri)) {

            case USERS:
                sqlSelection = selection;
                break;
            case USERS_ID:
                sqlSelection = USER_TABLE_NAME + "." + dbhelper._ID + "=" + uri.getPathSegments().get(1);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        return db.query(USER_TABLE_NAME, projection, sqlSelection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = userUriMatcher.match(uri);
        switch (match) {
            case USERS:
                return CONTENT_TYPE;

            case USERS_ID:
                return CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if(userUriMatcher.match(uri) != USERS) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        long rowId = db.insert(USER_TABLE_NAME, null, contentValues);

        if(rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        int count;

        switch (userUriMatcher.match(uri)) {

            case USERS:
                count = db.delete(USER_TABLE_NAME, where, whereArgs);
                break;

            case USERS_ID:
                String user_id = uri.getPathSegments().get(1);
                count = db.delete(USER_TABLE_NAME,
                        dbhelper._ID + "=" + user_id
                        + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {
        int count;

        switch (userUriMatcher.match(uri)) {

            case USERS:
                count = db.update(USER_TABLE_NAME, contentValues, where, whereArgs);
                break;

            case USERS_ID:
                String user_id = uri.getPathSegments().get(1);
                count = db.update(USER_TABLE_NAME, contentValues, dbhelper._ID + "=" + user_id + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
