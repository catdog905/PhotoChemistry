package com.example.myapplication.calculator;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.myapplication.helpers.DbCursors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SolutionChemicalEquations {
    private static final String TAG = "MyApp";
    List<Equation> SolutionEquations;

    public SolutionChemicalEquations(String[] input, Context context, DbCursors db) {
        List<Integer> inputInt = CompoundToInt(input, db.getCursor("compound")); // id соединений
        List<Compound> inputCompounds = new ArrayList<Compound>(); // объекты соединений
        for (Integer s: inputInt) {
            inputCompounds.add(new Compound(s, context, db));
         }
        List<Equation> possibleEquations = new ArrayList<>();
        SolutionEquations = possibleEquations;
        Cursor cursor = db.getFindEqCursor("equations", inputCompounds);
        cursor.moveToFirst();
        do {
            List<Integer> intLeft = new ArrayList<>();
            List<Integer> intRight = new ArrayList<>();
            List<Integer> balance_left = new ArrayList<>();
            List<Integer> balance_right = new ArrayList<>();
            for (String s : cursor.getString(cursor.getColumnIndex("left")).split("-")) {
                intLeft.add(Integer.valueOf(s));
            }
            for (String s : cursor.getString(cursor.getColumnIndex("right")).split("-")) {
                intRight.add(Integer.valueOf(s));
            }
            try {
                //Log.d("help", cursor.getString(cursor.getColumnIndex("_id")));
                for (String s : cursor.getString(cursor.getColumnIndex("balance_left")).split("-")) {
                    balance_left.add(Integer.valueOf(s));
                }
                for (String s : cursor.getString(cursor.getColumnIndex("balance_right")).split("-")) {
                    balance_right.add(Integer.valueOf(s));
                }
            } catch (Exception e){};

            Equation tempEquation = new Equation(cursor.getInt(cursor.getColumnIndex("_id")), cursor.getInt(cursor.getColumnIndex("frequency")),
                    intLeft, intRight, balance_left, balance_right);
            possibleEquations.add(tempEquation);
        } while (cursor.moveToNext());
    }

    class FrequencyComp implements Comparator<Equation> {

        @Override
        public int compare(Equation e1, Equation e2) {
            if(e1.getFrequency() < e2.getFrequency()){
                return 1;
            } else {
                return -1;
            }
        }
    }

    public List<Equation> getSolutionEquations() {
        return SolutionEquations;
    }

    private List<Integer> CompoundToInt (String[] input, Cursor cursor) {
        List<Integer> inputInt= new ArrayList<>();
        for (String s:input) {
            s = s.replace(" ", "");
            cursor.moveToFirst();
            do {
                if (cursor.getInt(cursor.getColumnIndex("_id")) == 100) {
                    Log.i(TAG, cursor.getString(cursor.getColumnIndex("formula")).toLowerCase() + " + " + s.toLowerCase() + " + " + cursor.getString(cursor.getColumnIndex("formula")).equalsIgnoreCase(s));
                }
                String str = cursor.getString(cursor.getColumnIndex("formula")).toLowerCase() + " + " + s.toLowerCase() + " + " + cursor.getString(cursor.getColumnIndex("formula")).equalsIgnoreCase(s);
                if (cursor.getString(cursor.getColumnIndex("formula")).equalsIgnoreCase(s)) {
                    inputInt.add(cursor.getInt(cursor.getColumnIndex("_id")));
                    break;
                }
            } while (cursor.moveToNext());
        }
        return inputInt;
    }

    public String IntToCompound (Integer id, Cursor cursor, DbCursors cursors) {

        Cursor c = cursors.getIdCursor("compound", id);
        if (c != null) {
            c.moveToFirst();
            return c.getString(c.getColumnIndex("formula"));
        }
        return null;
    }
}
