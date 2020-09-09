package com.example.myapplication.calculator;

import java.util.ArrayList;
import java.util.List;

public class Equation {
    int id, frequency;
    List<Integer> left;
    List<Integer> right;
    List<Integer> balance_left;
    List<Integer> balance_right;

    public Equation(int id, int frequency, List<Integer> left, List<Integer> right, List<Integer> balance_left, List<Integer> balance_right) {
        this.id = id;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
        this.balance_left = balance_left;
        this.balance_right= balance_right;
    }

    public int getFrequency() {
        return frequency;
    }

    public List<Integer> getRight() {
        return right;
    }

    public List<Integer> getLeft() {
        return left;
    }

    public List<Integer> getRightBalance() {
        return balance_right;
    }
}


