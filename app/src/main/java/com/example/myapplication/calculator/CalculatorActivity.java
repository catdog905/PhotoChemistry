package com.example.myapplication.calculator;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.camera.CameraActivity;
import com.example.myapplication.camera.ImageProcessing;
import com.example.myapplication.helpers.DbCursors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CalculatorActivity extends AppCompatActivity {
    String TAG_EQUATION = "100";
    static{ System.loadLibrary("opencv_java3"); }
    static final int CAMERA = 10;
    EditText input_text;
    TextView output_text;
    Button solve_button;
    //Button image_button;
    String input;
    String str = "";
    String mFile;
    DbCursors db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        setPermissions();
        input_text = findViewById(R.id.input);
        output_text = findViewById(R.id.output);
        solve_button = findViewById(R.id.solve_button);
        db = new DbCursors(getApplicationContext());
        //image_button = findViewById(R.id.image_button);

        Bundle arguments = getIntent().getExtras();
        str = arguments.get("str").toString();
        input_text.setText(str);

        View.OnClickListener solveTask = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                input = input_text.getText().toString(); //ввод
                SolutionChemicalEquations test = new SolutionChemicalEquations(input.split("\\+"), getApplicationContext(), db); // соединения
                String output = "";
                for (Equation eq : test.getSolutionEquations()) {
                    //output += eq.id + ": ";
                    List<Integer> el = eq.getLeft();
                    for (int i = 0; i < el.size(); i++) {
                        try {
                            output += eq.balance_right.get(i) + test.IntToCompound(el.get(i), db.getCursor("compound"), db) + " ";
                        }catch(Exception e){
                            output += test.IntToCompound(el.get(i), db.getCursor("compound"), db) + " ";
                        }
                        //Log.i(TAG, el + " " + "+");
                    }
                    output += "---> ";
                    el = eq.getRight();
                    for (int i = 0; i < el.size(); i++) {
                        try {
                            output += eq.balance_right.get(i) + test.IntToCompound(el.get(i), db.getCursor("compound"), db) + " ";
                        }catch(Exception e){
                            output += test.IntToCompound(el.get(i), db.getCursor("compound"), db) + " ";
                        }
                        //Log.i(TAG, el + " " + "+");
                    }
                    output += " :" + eq.frequency;
                    output += "\n";
                    //Log.i(TAG, (eq.getRight()).size() + "");
                    //break;
                }
                output_text.setText(output);
            }
        };

        View.OnClickListener takePhoto = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalculatorActivity.this, CameraActivity.class);
                startActivityForResult(intent, CAMERA);
            }
        };

        solve_button.setOnClickListener(solveTask);
        //image_button.setOnClickListener(takePhoto);
    }

    private void setPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        }, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(TAG_EQUATION);
                input_text.setText(str);
            }else {
                input_text.setText(""); // стираем текст
            }
        }
    }
}