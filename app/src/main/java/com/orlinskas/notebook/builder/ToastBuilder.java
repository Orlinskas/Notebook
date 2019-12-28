package com.orlinskas.notebook.builder;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class ToastBuilder {

    public static void doToast(Context context, String message) {
        Toast toast = Toast.makeText(context,
                message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void createSnackBar (View view, String message) {
        try {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
