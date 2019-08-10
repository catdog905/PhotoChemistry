package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyApp";

    EditText editText;
    TextView textView;
    Button button;
    String input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = editText.getText().toString();
                SolutionChemicalEquations test = new SolutionChemicalEquations(input.split("\\+"), getApplicationContext());
                String output = "";
                for (Equation eq : test.getSolutionEquations()) {
                    output += eq.id + ": ";
                    for (Integer el: eq.getRight()) {
                        output += test.IntToCompound(el, (new DbCursors(getApplicationContext())).getCursor("compound")) + " ";
                        //Log.i(TAG, el + " " + "+");
                    }
                    output += " :" + eq.frequency;
                    output += "\n";
                    //Log.i(TAG, (eq.getRight()).size() + "");
                }
                textView.setText(output);
            }
        });


        //Toast toast = Toast.makeText(getApplicationContext() , cursor.getString(cursor.getColumnIndex("locale")),  Toast.LENGTH_SHORT).show();
    }
}
