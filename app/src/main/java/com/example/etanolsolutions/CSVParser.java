package com.example.etanolsolutions;

import android.content.Context;
import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class CSVParser {
    private final Vector<Vector<String>> data;
    private int colsCount;
    private int rowsCount;

    public CSVParser() {
        this.data = new Vector<>();
        this.colsCount = 0;
        this.rowsCount = 0;
    }

    public int getColsCount() {
        return this.colsCount;
    }

    public int getRowsCount() {
        return this.rowsCount;
    }

    public String getElement(int rowIndex, int colIndex) {
        if (this.data.isEmpty()) return "";
        if (this.data.size() <= rowIndex) return "";
        Vector<String> row = this.data.elementAt(rowIndex);
        if (row.size() <= colIndex) return "";
        return row.elementAt(colIndex);
    }

    public boolean parse(Context con) throws Resources.NotFoundException, IOException {
        InputStream inStream;
        try {
            inStream = con.getResources().openRawResource(R.raw.data);//Resources.getSystem().openRawResource(R.raw.data);
        }
        catch (Resources.NotFoundException ex) {
            throw new Resources.NotFoundException("failed to make a connection with the file" + ex.getMessage());
        }
        int inputInt;
        char c;
        StringBuilder text = new StringBuilder();
        Vector<String> line = new Vector<>();
        int maxColsCount = 0;
        try {
            while ((inputInt = inStream.read()) != -1) {
                // processing code goes here
                c = (char)inputInt;
                switch(c) {
                    // cases goes here
                    case '\r':
                        continue;
                    case ';':// end of block in a line
                        line.addElement(text.toString());
                        // reset block
                        text = new StringBuilder();
                        break;
                    case '\n':// end of line
                        // add last block
                        line.addElement(text.toString());
                        if (maxColsCount < line.size()) maxColsCount = line.size();
                        // add line
                        this.data.addElement(line);
                        // reset line and block
                        text = new StringBuilder();
                        line = new Vector<>();
                        break;
                    default:
                        text.append(c);
                        break;
                }
            }
        }
        catch (IOException ex) {
            throw new IOException("failed to proccess the file" + ex.getMessage());
        }
        line.addElement(text.toString());
        this.data.addElement(line);
        this.rowsCount = this.data.size();
        this.colsCount = maxColsCount;

        return true;
    }
}
