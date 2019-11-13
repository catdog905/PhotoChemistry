package com.example.myapplication.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.helpers.DataBaseHelper;

public class DbCursors {
    SQLiteDatabase db;

    public DbCursors(Context context) {
        DataBaseHelper DbHelper = new DataBaseHelper(context);
        db = DbHelper.getReadableDatabase();
    }

    public Cursor getCursor (String name) {
        return db.query(name, null, null, null, null, null, null);
    }
}
