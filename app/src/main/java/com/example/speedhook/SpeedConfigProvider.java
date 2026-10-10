package com.example.speedhook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

public class SpeedConfigProvider extends ContentProvider {
    public static final Uri CONTENT_URI =
            Uri.parse("content://com.example.speedhook.config/speed");
    private static final String KEY_SPEED = "fake_speed";

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        float speed = getContext().getSharedPreferences("speed_config", 0).getFloat(KEY_SPEED, 0f);
        MatrixCursor cursor = new MatrixCursor(new String[]{"speed"});
        cursor.addRow(new Object[]{speed});
        return cursor;
    }

    @Override
    public String getType(Uri uri) { return null; }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        float speed = values.getAsFloat(KEY_SPEED);
        getContext().getSharedPreferences("speed_config", 0).edit().putFloat(KEY_SPEED, speed).apply();
        return uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] as) { return 0; }
    @Override
    public int update(Uri uri, ContentValues v, String s, String[] as) { return 0; }
    @Override
    public boolean onCreate() { return true; }

    public static float getSpeed() { return 0f; }
}
