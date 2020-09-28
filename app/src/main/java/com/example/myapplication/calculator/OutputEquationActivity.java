package com.example.myapplication.calculator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.calculator.Equation;
import com.example.myapplication.calculator.SolutionChemicalEquations;
import com.example.myapplication.helpers.DbCursors;

import java.util.List;

public class OutputEquationActivity extends AppCompatActivity {


    TextView outputEquationTextView;
    TextView outputInfoTextView;
    DbCursors db;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_equation);

        outputEquationTextView = findViewById(R.id.output_equation);
        outputInfoTextView = findViewById(R.id.output_info);
        db = new DbCursors(getApplicationContext());

        Bundle arguments = getIntent().getExtras(); //TODO: Make asynchronously
        String outputCam = arguments.get("str").toString();
        SolutionChemicalEquations test = new SolutionChemicalEquations(outputCam.split("\\+"), getApplicationContext(), db); // соединения
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
                if (i != el.size() - 1){
                    output += "+ ";
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
                if (i != el.size() - 1){
                    output += "+ ";
                }
                //Log.i(TAG, el + " " + "+");
            }
            //output += " :" + eq.frequency;
            output += "\n";
            //Log.i(TAG, (eq.getRight()).size() + "");
            break;
        }
        outputEquationTextView.setText(output);

        recyclerView = (RecyclerView) findViewById(R.id.output_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        String[] myDataset = {"3 O2 + 4 NH3 → 6 H2O"};
        mAdapter = new RecyclerOutputAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);
    }
}
