package com.example.myapplication.calculator;

import android.content.Context;
import android.database.Cursor;

import com.example.myapplication.helpers.DbCursors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Compound {
    public int id, frequency;
    String formula;
    List<String> oxidationNumbers = new ArrayList();
    boolean verified;
    private static final String TAG = "MyApp";
    public Compound(Integer input, Context context, DbCursors cursors) {
        Cursor cursor = cursors.getIdCursor("compound", input);

        cursor.moveToFirst();
        this.id = cursor.getInt(cursor.getColumnIndex("_id"));
        this.frequency = cursor.getInt(cursor.getColumnIndex("frequency"));
        this.formula = cursor.getString(cursor.getColumnIndex("formula"));
        this.oxidationNumbers = Arrays.asList((cursor.getString(cursor.getColumnIndex("oxidation_numbers"))).split(","));
        this.verified = (cursor.getInt(cursor.getColumnIndex("verified")) == 1);
        //Log.i(TAG, cursor.getString(cursor.getColumnIndex("formula")));
    }
}
