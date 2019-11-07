package com.orlinskas.notebook.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DeleteDialogFragment extends DialogFragment {
    private DayFragmentActions fragmentActions;
    private int deletedNotificationID;

    DeleteDialogFragment(int deletedNotificationID) {
        this.deletedNotificationID = deletedNotificationID;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActions = (DayFragmentActions) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Подтверждение";
        String message = "Удалить элемент?";
        String positive = "Удалить";
        String negative = "Отмена";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fragmentActions.deleteNotification(deletedNotificationID);
            }
        });
        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setCancelable(true);

        return builder.create();
    }
}
