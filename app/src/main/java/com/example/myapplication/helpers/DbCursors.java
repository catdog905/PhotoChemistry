package com.example.myapplication.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.calculator.Compound;
import com.example.myapplication.helpers.DataBaseHelper;

import java.io.IOException;
import java.util.List;

public class DbCursors {
    private DataBaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    public DbCursors(Context context) {

        mDBHelper = new DataBaseHelper(context);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }

    public Cursor getCursor (String name) {
        return mDb.rawQuery("select * from "+name, null);
    }

    public Cursor getIdCursor (String table, Integer _id) {
        return mDb.rawQuery("select * from "+ table + " WHERE _id = " + _id, null);
    }

    public Cursor getFindEqCursor (String table, List<Compound> left) {
        String str = "";
        for (int i = 1; i < left.size(); i++){
            str += " AND \"-\" || left || \"-\" like \"%-"+ left.get(i).id +"-%\"";
        }
        return mDb.rawQuery("select * from "+ table + " WHERE \"-\" || left || \"-\" like \"%-"+ left.get(0).id +"-%\"" + str, null);
    }
}
