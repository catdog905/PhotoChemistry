package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Compound {
    int id, frequency;
    String formula;
    List<String> oxidationNumbers = new ArrayList();
    boolean verified;
    private static final String TAG = "MyApp";
    public Compound(Integer input, Context context) {
        Cursor cursor = (new DbCursors(context)).getCursor("compound");

        cursor.moveToFirst();
        do {
            //Log.i(TAG, cursor.getInt(cursor.getColumnIndex("_id")) + "==" + Integer.parseInt(input));
            if (cursor.getInt(cursor.getColumnIndex("_id")) == input) {
                this.id = cursor.getInt(cursor.getColumnIndex("_id"));
                this.frequency = cursor.getInt(cursor.getColumnIndex("frequency"));
                this.formula = cursor.getString(cursor.getColumnIndex("formula"));
                this.oxidationNumbers = Arrays.asList((cursor.getString(cursor.getColumnIndex("oxidation_numbers"))).split(","));
                this.verified = (cursor.getInt(cursor.getColumnIndex("verified")) == 1);
                //Log.i(TAG, cursor.getString(cursor.getColumnIndex("formula")));
                break;
            }
        } while (cursor.moveToNext());
    }


}
