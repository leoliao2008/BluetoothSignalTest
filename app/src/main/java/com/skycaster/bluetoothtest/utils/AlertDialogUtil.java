package com.skycaster.bluetoothtest.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

/**
 * Created by 廖华凯 on 2017/8/10.
 */

public class AlertDialogUtil {

    private static AlertDialog alertDialog;

    public static void showDialog(Context context, String msg, @NonNull final Runnable onConfirm, @Nullable final Runnable onCancel,boolean isConfirmButtonOnly){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setTitle("温馨提示")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onConfirm.run();
                        alertDialog.dismiss();

                    }
                });
        if(!isConfirmButtonOnly){
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(onCancel!=null){
                        onCancel.run();
                    }
                    alertDialog.dismiss();
                }
            });
        }
        alertDialog=builder.create();
        alertDialog.show();
    }



}
