package com.example.etanolsolutions;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.Objects;

public class ErrorMessageDialogFragment extends DialogFragment {

    public static ErrorMessageDialogFragment newInstance(String message) {
        ErrorMessageDialogFragment fragment = new ErrorMessageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", message); // set message here
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle("Грешка!")
                .setMessage(Objects.requireNonNull(getArguments()).getString("message"))
                .setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
