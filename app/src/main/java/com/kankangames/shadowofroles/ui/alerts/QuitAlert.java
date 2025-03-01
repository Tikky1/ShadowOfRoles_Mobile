package com.kankangames.shadowofroles.ui.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.kankangames.shadowofroles.R;

public class QuitAlert extends DialogFragment {
    public interface OnCloseApplication{
        void closeApplication();
    }

    private final OnCloseApplication onCloseApplication;
    public QuitAlert(OnCloseApplication onCloseApplication){
        this.onCloseApplication = onCloseApplication;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.quit_message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        onCloseApplication.closeApplication();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it.
        return builder.create();
    }
}