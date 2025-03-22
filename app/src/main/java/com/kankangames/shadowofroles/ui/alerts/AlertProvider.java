package com.kankangames.shadowofroles.ui.alerts;

import android.app.AlertDialog;
import android.content.Context;

import com.kankangames.shadowofroles.R;

public class AlertProvider {

    public static void showGeneralAlert(Context context, String title, String message, Runnable action) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.getText(R.string.ok), (dialog, which) -> {
                    action.run();
                    dialog.dismiss();
                })
                .show();
    }
}
