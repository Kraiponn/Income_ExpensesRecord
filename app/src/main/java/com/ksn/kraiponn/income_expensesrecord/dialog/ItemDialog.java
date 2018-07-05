package com.ksn.kraiponn.income_expensesrecord.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class ItemDialog extends DialogFragment {
    private int mSelectIndex = 0;
    private boolean c;

    public static ItemDialog newInstance(
            String title, String[] items) {
        ItemDialog dialog = new ItemDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putStringArray("items", items);
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
        String[] items = getArguments().getStringArray("items");

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setSingleChoiceItems(items, mSelectIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectIndex = which;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallBack.onFinishDialog(mSelectIndex);
                    }
                });

        return dialog.create();
    }


}
