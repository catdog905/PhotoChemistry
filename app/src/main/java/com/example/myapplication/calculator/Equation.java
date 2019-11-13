package com.example.myapplication.calculator;

import java.util.ArrayList;
import java.util.List;

public class Equation {
    int id, frequency;
    List<Integer> left;
    List<Integer> right;

    public Equation(int id, int frequency, List<Integer> left, List<Integer> right) {
        this.id = id;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    public int getFrequency() {
        return frequency;
    }

    public List<Integer> getRight() {
        return right;
    }
}


