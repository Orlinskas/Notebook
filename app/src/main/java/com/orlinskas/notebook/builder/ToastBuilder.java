package com.orlinskas.notebook.builder;

import android.content.Context;
import android.widget.Toast;

public class ToastBuilder {
    public static void doToast(Context context, String message) {
        Toast toast = Toast.makeText(context,
                message, Toast.LENGTH_LONG);
        toast.show();
    }
}
