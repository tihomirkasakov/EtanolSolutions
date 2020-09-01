package com.example.etanolsolutions;

import android.content.Context;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.AbstractMap;
import java.util.Locale;
import java.util.Map;

public class SolutionService {
    private final Context context;
    private float c1;
    private float m1;
    private float c2;
    private float m2;
    private float cx;
    private float mx;
    private final NumberFormat format;

    public SolutionService(Context con) {
        this.context = con;
        this.c1 = 100;  //sourceDegree
        this.m1 = 1;    //sourceVolume
        this.c2 = 0;    //diluentDegree
        this.m2 = 0;    //diluentVolume
        this.cx = 100;  //targetDegree
        this.mx = 1;    //totalVolume
        this.format = NumberFormat.getInstance(Locale.getDefault());
    }

    public Map.Entry<Boolean, String> trySetSourceDegree(String value) {
        if (value.equals(""))
            return new AbstractMap.SimpleEntry<>(false, context.getResources().getString(R.string.error_solutionService_setSourceDegree_empty));
        try {
            Number number = format.parse(value);
            float v = Float.parseFloat(String.valueOf(number));
            if (v > 0.0 && v <= 100.0) {
                c1 = v;
                return new AbstractMap.SimpleEntry<>(true, "");
            }
            else
                return new AbstractMap.SimpleEntry<>(false, context.getResources().getString(R.string.error_solutionService_setSourceDegree_outOfRange));
        }
        catch (NullPointerException | NumberFormatException | ParseException ex) {
            return new AbstractMap.SimpleEntry<>(false, ex.getMessage());
        }
    }

    public Map.Entry<Boolean, String> trySetSourceVolume(String value) {
        if (value.equals(""))
            return new AbstractMap.SimpleEntry<>(false, context.getResources().getString(R.string.error_solutionService_setSourceVolume_empty));
        try {
            Number number = format.parse(value);
            float v = Float.parseFloat(String.valueOf(number));
            if (v > 0.0) {
                m1 = v;
                return new AbstractMap.SimpleEntry<>(true, "");
            }
            else
                return new AbstractMap.SimpleEntry<>(false, context.getResources().getString(R.string.error_solutionService_setSourceVolume_outOfRange));
        }
        catch (NullPointerException | NumberFormatException | ParseException ex) {
            return new AbstractMap.SimpleEntry<>(false, ex.getMessage());
        }
    }

    public Map.Entry<Boolean, String> trySetDiluentDegree(String value) {
        if (value.equals(""))
            return new AbstractMap.SimpleEntry<>(false, context.getResources().getString(R.string.error_solutionService_setDiluentDegree_empty));
        try {
            Number number = format.parse(value);
            float v = Float.parseFloat(String.valueOf(number));
            if (v >= 0.0 && v < c1) {
                c2 = v;
                return new AbstractMap.SimpleEntry<>(true, "");
            }
            else
                return new AbstractMap.SimpleEntry<>(false, context.getResources().getString(R.string.error_solutionService_setDiluentDegree_outOfRange));
        }
        catch (NullPointerException | NumberFormatException | ParseException ex) {
            return new AbstractMap.SimpleEntry<>(false, ex.getMessage());
        }
    }

    public Map.Entry<Boolean, String> trySetTargetDegree(String value) {
        if (value.equals(""))
            return new AbstractMap.SimpleEntry<>(false, context.getResources().getString(R.string.error_solutionService_setTargetDegree_empty));
        try {
            Number number = format.parse(value);
            float v = Float.parseFloat(String.valueOf(number));
            if (v > c2 && v < c1) {
                cx = v;
                return new AbstractMap.SimpleEntry<>(true, "");
            }
            else
                return new AbstractMap.SimpleEntry<>(false, context.getResources().getString(R.string.error_solutionService_setTargetDegree_outOfRange));
        }
        catch (NullPointerException | NumberFormatException | ParseException ex) {
            return new AbstractMap.SimpleEntry<>(false, ex.getMessage());
        }
    }

    public float getDiluentVolume() {
        calculateSolution();
        return m2;
    }

    public float getTotalVolume() {
        calculateSolution();
        return mx;
    }

    private void calculateSolution() {
        if (cx - c2 > 0) {
            m2 = (m1 * (c1 - cx)) / (cx - c2);
            mx = m1 + m2;
        }
    }
}
