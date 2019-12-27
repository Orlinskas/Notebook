package com.orlinskas.notebook.mvvm.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.orlinskas.notebook.mvvm.model.Notification;

public class DeleteDialogFragment extends DialogFragment {
    private DayFragmentActions fragmentActions;
    private Notification notification;

    DeleteDialogFragment(Notification notification) {
        this.notification = notification;
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
        builder.setPositiveButton(positive, (dialog, id) -> fragmentActions.deleteNotification(notification));
        builder.setNegativeButton(negative, (dialog, id) -> dialog.cancel());
        builder.setCancelable(true);

        return builder.create();
    }
}
