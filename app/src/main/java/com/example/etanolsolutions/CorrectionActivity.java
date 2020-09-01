package com.example.etanolsolutions;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormatSymbols;
import java.util.Map;

public class CorrectionActivity extends AppCompatActivity {

    private Intent callerIntent;

    private EditText txtInputTemperature;
    private EditText txtInputDegree;
    private EditText txtResult;

    private CorrectionService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correction);

        service = new CorrectionService(this);

        txtInputTemperature = findViewById(R.id.txtInputTemperature);
        txtInputDegree = findViewById(R.id.txtInputDegree);
        txtResult = findViewById(R.id.txtResult);
        txtResult.setEnabled(false);

        char separator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
        txtInputTemperature.setKeyListener(DigitsKeyListener.getInstance("0123456789" + separator));
        txtInputDegree.setKeyListener(DigitsKeyListener.getInstance("0123456789" + separator));

        callerIntent = getIntent();
        if (callerIntent.hasExtra(Constants.EXTRA_INPUT_VALUE)) {
            String inputValue = callerIntent.getStringExtra(Constants.EXTRA_INPUT_VALUE);
            if (!inputValue.equals(""))
                txtInputDegree.setText(inputValue);
        }

        Button btnCorrection = findViewById(R.id.btnCorrection);

        //Define and attach click listeners
        btnCorrection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean success = correctDegree();
                if (success && callerIntent.hasExtra(Constants.EXTRA_RETURN_TO_CALLER) && callerIntent.getBooleanExtra(Constants.EXTRA_RETURN_TO_CALLER, false)) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(Constants.EXTRA_RESULT,txtResult.getText().toString());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    private Boolean correctDegree() {
        ErrorMessageDialogFragment dialog;

        Map.Entry<Boolean, String> setTemperatureResult = service.trySetTemperature(txtInputTemperature.getText().toString());
        if (!setTemperatureResult.getKey()) {
            dialog = ErrorMessageDialogFragment.newInstance(setTemperatureResult.getValue());
            dialog.show(getSupportFragmentManager(), "Error");

            txtInputTemperature.requestFocus();
            txtResult.setText("");
            return false;
        }
        Map.Entry<Boolean, String> setDegreeResult = service.trySetDegree(txtInputDegree.getText().toString());
        if (!setDegreeResult.getKey()) {
            dialog = ErrorMessageDialogFragment.newInstance(setDegreeResult.getValue());
            dialog.show(getSupportFragmentManager(), "Error");

            txtInputDegree.requestFocus();
            txtResult.setText("");
            return false;
        }
        float correctedDegree = service.getCorrectedValue();
        txtResult.setText(String.format("%.2f", correctedDegree));
        return true;
    }
}
