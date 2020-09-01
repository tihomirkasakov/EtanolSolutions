/* to create icons use https://romannurik.github.io/AndroidAssetStudio/index.html */

package com.example.etanolsolutions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSolution = findViewById(R.id.btnSolution);
        Button btnCorrection = findViewById(R.id.btnCorrection);

        //Define and attach click listeners
        btnSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSolutionActivity();
            }
        });
        btnCorrection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCorrectionActivity();
            }
        });
    }

    private void startSolutionActivity() {
        Intent solutionIntent = new Intent(this, SolutionActivity.class);
        solutionIntent.putExtra(Constants.EXTRA_RETURN_TO_CALLER, false);
        startActivity(solutionIntent);
    }

    private void startCorrectionActivity() {
        Intent correctionIntent = new Intent(this, CorrectionActivity.class);
        correctionIntent.putExtra(Constants.EXTRA_RETURN_TO_CALLER, false);
        startActivity(correctionIntent);
    }
}
