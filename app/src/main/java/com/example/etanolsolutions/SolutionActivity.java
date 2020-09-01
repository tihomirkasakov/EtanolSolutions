package com.example.etanolsolutions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormatSymbols;
import java.util.Map;

public class SolutionActivity extends AppCompatActivity {

    private EditText txtSourceDegree;
    private EditText txtSourceVolume;
    private EditText txtDiluentDegree;
    private EditText txtTargetDegree;
    private EditText txtResult;

    private SolutionService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        service = new SolutionService(this);

        txtSourceDegree = findViewById(R.id.txtSourceDegree);
        txtSourceVolume = findViewById(R.id.txtSourceVolume);
        txtDiluentDegree = findViewById(R.id.txtDiluentDegree);
        txtTargetDegree = findViewById(R.id.txtTargetDegree);
        txtResult = findViewById(R.id.txtResult);
        txtResult.setEnabled(false);

        char separator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
        txtSourceDegree.setKeyListener(DigitsKeyListener.getInstance("0123456789" + separator));
        txtSourceVolume.setKeyListener(DigitsKeyListener.getInstance("0123456789" + separator));
        txtDiluentDegree.setKeyListener(DigitsKeyListener.getInstance("0123456789" + separator));
        txtTargetDegree.setKeyListener(DigitsKeyListener.getInstance("0123456789" + separator));

        Button btnSolutionOK = findViewById(R.id.btnSolutionOK);
        Button btnCorrectSourceDegree = findViewById(R.id.btnCorrectSourceDegree);
        Button btnCorrectDiluentDegree = findViewById(R.id.btnCorrectDiluentDegree);

        //Define and attach click listeners
        btnSolutionOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateSolution();
            }
        });
        btnCorrectSourceDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to do ... save to bundle
                startCorrectionActivity(Constants.REQUEST_CODE_CORRECTION_ACTIVITY_FOR_SOURCE_DEGREE);
            }
        });
        btnCorrectDiluentDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to do ... save to bundle
                startCorrectionActivity(Constants.REQUEST_CODE_CORRECTION_ACTIVITY_FOR_DILUENT_DEGREE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_CORRECTION_ACTIVITY_FOR_SOURCE_DEGREE && resultCode == Activity.RESULT_OK) {
            // to do ... save to bundle
            txtSourceDegree.setText(data.getStringExtra(Constants.EXTRA_RESULT));
        }
        else if (requestCode == Constants.REQUEST_CODE_CORRECTION_ACTIVITY_FOR_DILUENT_DEGREE && resultCode == Activity.RESULT_OK) {
            // to do ... save to bundle
            txtDiluentDegree.setText(data.getStringExtra(Constants.EXTRA_RESULT));
        }
    }

    private void calculateSolution() {
        ErrorMessageDialogFragment dialog;

        Map.Entry<Boolean, String> setSourceDegreeResult = service.trySetSourceDegree(txtSourceDegree.getText().toString());
        if (!setSourceDegreeResult.getKey()) {
            dialog = ErrorMessageDialogFragment.newInstance(setSourceDegreeResult.getValue());
            dialog.show(getSupportFragmentManager(), "Error");

            txtResult.setText("");
            txtSourceDegree.requestFocus();
            return;
        }
        Map.Entry<Boolean, String> setSourceVolumeResult = service.trySetSourceVolume(txtSourceVolume.getText().toString());
        if (!setSourceVolumeResult.getKey()) {
            dialog = ErrorMessageDialogFragment.newInstance(setSourceVolumeResult.getValue());
            dialog.show(getSupportFragmentManager(), "Error");

            txtResult.setText("");
            txtSourceVolume.requestFocus();
            return;
        }
        Map.Entry<Boolean, String> setDiluentDegreeResult = service.trySetDiluentDegree(txtDiluentDegree.getText().toString());
        if (!setDiluentDegreeResult.getKey()) {
            dialog = ErrorMessageDialogFragment.newInstance(setDiluentDegreeResult.getValue());
            dialog.show(getSupportFragmentManager(), "Error");

            txtResult.setText("");
            txtDiluentDegree.requestFocus();
            return;
        }
        Map.Entry<Boolean, String> setTargetDegreeResult = service.trySetTargetDegree(txtTargetDegree.getText().toString());
        if (!setTargetDegreeResult.getKey()) {
            dialog = ErrorMessageDialogFragment.newInstance(setTargetDegreeResult.getValue());
            dialog.show(getSupportFragmentManager(), "Error");

            txtResult.setText("");
            txtTargetDegree.requestFocus();
            return;
        }

        float diluentVolume = service.getDiluentVolume();
        txtResult.setText(String.format("%.2f", diluentVolume));
    }

    private void startCorrectionActivity(int requestCode) {
        Intent correctionIntent = new Intent(this, CorrectionActivity.class);
        correctionIntent.putExtra(Constants.EXTRA_RETURN_TO_CALLER, true);
        if (requestCode == Constants.REQUEST_CODE_CORRECTION_ACTIVITY_FOR_SOURCE_DEGREE)
            correctionIntent.putExtra(Constants.EXTRA_INPUT_VALUE, txtSourceDegree.getText().toString());
        else if (requestCode == Constants.REQUEST_CODE_CORRECTION_ACTIVITY_FOR_DILUENT_DEGREE)
            correctionIntent.putExtra(Constants.EXTRA_INPUT_VALUE, txtDiluentDegree.getText().toString());
        startActivityForResult(correctionIntent, requestCode);
    }
}
