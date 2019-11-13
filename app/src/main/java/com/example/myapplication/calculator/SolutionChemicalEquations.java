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

    public SolutionChemicalEquations(String[] input, Context context) {
        //for (int i = 0; i < input.length; i++){
            //Log.i(TAG, "!" + input[i]);
        //}
        List<Integer> inputInt = CompoundToInt(input, (new DbCursors(context)).getCursor("compound")); // id соединений
        List<Compound> inputCompounds = new ArrayList<Compound>(); // объекты соединений
        for (Integer s: inputInt) {
            inputCompounds.add(new Compound(s, context));
         }
        List<Equation> possibleEquations = PossibleEquations(inputCompounds, context);
        //Log.i(TAG,  possibleEquations.size() + "");
        for (Equation eq : possibleEquations) {
            for (Integer el: eq.getRight()) {
                //Log.i(TAG,  Integer.toString(el));
            }
            //Log.i(TAG,  "+");
        }
        SolutionEquations = possibleEquations;
    }

    public List<Equation> PossibleEquations (List<Compound> left, Context context) {
        //Log.i(TAG,  left.size() + "");
        //for (Compound el:left) {
        //    Log.i(TAG,  el.id + "");
        //}
        List<Integer> leftId = new ArrayList<>();
        for (Compound s : left) {
            leftId.add(s.id);
        }
        Cursor cursor = (new DbCursors(context)).getCursor("equation");

        List<Equation> possibleEquations = new ArrayList<>();

        cursor.moveToFirst();
        do {
            List<Integer> leftHelp = new ArrayList<>(leftId);
            List<String> listTempStr = Arrays.asList((cursor.getString(cursor.getColumnIndex("left"))).split("-")); // _id
            List<Integer> listTempInt = new ArrayList<>();
            Integer TempInt;
            for (String s : listTempStr) {
                Log.i(TAG, leftId.get(0) + "");
                TempInt = Integer.parseInt(s);
                //if (cursor.getInt(cursor.getColumnIndex("_id")) < 10) Log.i(TAG, TempInt + "+" + leftHelp.get(0) + "+" + cursor.getString(cursor.getColumnIndex("_id")));
                if (leftHelp.contains(TempInt)) {
                    //Log.i(TAG, "help");
                    leftHelp.remove(leftHelp.indexOf(TempInt));
                }
                listTempInt.add(TempInt);
            }

            if (leftHelp.isEmpty()) {
                //Log.i(TAG, "+");
                List<String> listTempStrRight = Arrays.asList((cursor.getString(cursor.getColumnIndex("right"))).split("-")); // _id
                List<Integer> listTempIntRight = new ArrayList<>();
                for (String s : listTempStrRight) {
                    listTempIntRight.add(Integer.parseInt(s));
                    //Log.i(TAG, s);
                }
                Equation tempEquation = new Equation(cursor.getInt(cursor.getColumnIndex("_id")), cursor.getInt(cursor.getColumnIndex("frequency")),
                        leftId, listTempIntRight);
                possibleEquations.add(tempEquation);
            }

        } while (cursor.moveToNext());
        Collections.sort(possibleEquations, new FrequencyComp());
        Log.i(TAG,  possibleEquations.size() + "");
        return possibleEquations;
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
            cursor.moveToFirst();
            do {
                if (cursor.getInt(cursor.getColumnIndex("_id")) < 10) {
                    Log.i(TAG, cursor.getString(cursor.getColumnIndex("formula")).toLowerCase() + " + " + s.toLowerCase() + " + " + cursor.getString(cursor.getColumnIndex("formula")).equalsIgnoreCase(s));
                }
                if (cursor.getString(cursor.getColumnIndex("formula")).equalsIgnoreCase(s)) {
                    inputInt.add(cursor.getInt(cursor.getColumnIndex("_id")));
                    break;
                }
            } while (cursor.moveToNext());
        }
        return inputInt;
    }

    public String IntToCompound (Integer id, Cursor cursor) {
        cursor.moveToFirst();
        do {
            if (cursor.getInt(cursor.getColumnIndex("_id")) == id) {
                return cursor.getString(cursor.getColumnIndex("formula"));
            }
        } while (cursor.moveToNext());
        return null;
    }
}
