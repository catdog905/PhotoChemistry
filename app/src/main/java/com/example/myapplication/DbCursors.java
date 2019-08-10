package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
