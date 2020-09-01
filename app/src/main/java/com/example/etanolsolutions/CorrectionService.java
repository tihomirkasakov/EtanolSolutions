package com.example.etanolsolutions;

import android.content.Context;
import android.content.res.Resources;
import java.text.NumberFormat;
import java.text.ParseException;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Locale;
import java.util.Map;

public class CorrectionService {

    private final Context context;
    private float inputTemperature;
    private float inputDegree;
    private float outputDegree;
    private final NumberFormat format;

    public CorrectionService(Context con) {
        this.context = con;
        this.inputTemperature = 20;
        this.inputDegree = 100;
        this.outputDegree = 100;
        this.format = NumberFormat.getInstance(Locale.getDefault());
    }

    public Map.Entry<Boolean, String> trySetTemperature(String value) {
        //;
        if (value.equals(""))
            return new AbstractMap.SimpleEntry<>(false, context.getResources().getString(R.string.error_correctionService_setTemperature_empty));
        try {
            Number number = format.parse(value);
            float v = Float.parseFloat(String.valueOf(number));
            if (v >= 0.0 && v <= 30.0) {
                inputTemperature = Math.round(v);
                return new AbstractMap.SimpleEntry<>(true, "");
            }
            else
                return new AbstractMap.SimpleEntry<>(false, context.getResources().getString(R.string.error_correctionService_setTemperature_outOfRange));
        }
        catch (NullPointerException | NumberFormatException | ParseException ex) {
            inputTemperature = 20f;
            return new AbstractMap.SimpleEntry<>(false, ex.getMessage());
        }
    }

    public Map.Entry<Boolean, String> trySetDegree(String value) {
        if (value.equals(""))
            return new AbstractMap.SimpleEntry<>(false, context.getResources().getString(R.string.error_correctionService_setDegree_empty));
        try {
            Number number = format.parse(value);
            float v = Float.parseFloat(String.valueOf(number));
            if (v >= 5.5 && v <= 100.0) {
                inputDegree = v;
                return new AbstractMap.SimpleEntry<>(true, "");
            }
            else
                return new AbstractMap.SimpleEntry<>(false, context.getResources().getString(R.string.error_correctionService_setDegree_outOfRange));
        }
        catch (NullPointerException | NumberFormatException | ParseException ex) {
            inputDegree = 100f;
            return new AbstractMap.SimpleEntry<>(false, ex.getMessage());
        }
    }

    public float getCorrectedValue() {
        try {
            correctDegree();
        }
        catch (Resources.NotFoundException | IOException | NumberFormatException | ParseException ignored) {
            outputDegree = 100f;
        }
        return outputDegree;
    }

    private void correctDegree() throws Resources.NotFoundException, IOException, NumberFormatException, ParseException {
        CSVParser parser = new CSVParser();
        if (parser.parse(context)) {
            NumberFormat csvFormat = NumberFormat.getInstance(Locale.ENGLISH);
            int colsCount = parser.getColsCount();
            int rowsCount = parser.getRowsCount();
            int targetColIndex = 0;
            int targetRowIndex = 0;
            for (int i = 0; i < colsCount; i++){
                String el = parser.getElement(0, i);
                if (!el.equals("")) {
                    Number number = csvFormat.parse(el);
                    if ((Math.abs(Float.parseFloat(String.valueOf(number)) - inputTemperature) < 0.001f)) {
                        targetColIndex = i;
                        break;
                    }
                }
            }
            for (int j = 0; j < rowsCount; j++){
                String el = parser.getElement(j, 0);
                if (!el.equals("")) {
                    Number number = csvFormat.parse(el);
                    if ((Math.abs(Float.parseFloat(String.valueOf(number)) - inputDegree) < 0.001f)) {
                        targetRowIndex = j;
                        break;
                    }
                }
            }
            String el = parser.getElement(targetRowIndex, targetColIndex);
            if (!el.equals("")) {
                Number number = csvFormat.parse(el);
                outputDegree = Float.parseFloat(String.valueOf(number));
            }
        }
    }
}
