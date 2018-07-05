package com.ksn.kraiponn.income_expensesrecord.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class ConfirmDialog extends DialogFragment {
    private int mSelectIndex = -1;

    public static ConfirmDialog newInstance(
            String title, String textNeg, String textPos) {
        ConfirmDialog dialog = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("textNeg", textNeg);
        args.putString("textPos", textPos);
        dialog.setArguments(args);
        return dialog;
    }

    public interface onFinishDialogListener{
        void onFinishDialog(int index);
    }

    private onFinishDialogListener mCallBack;

    public void setOnFinishDialogListener(onFinishDialogListener listener) {
        mCallBack = listener;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String textNeg = getArguments().getString("textNeg");
        String textPos = getArguments().getString("textPos");

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setNegativeButton(textNeg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallBack.onFinishDialog(1);
                    }
                })
                .setPositiveButton(textPos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallBack.onFinishDialog(2);
                    }
                });

        return dialog.create();
    }


}
