package com.hypernym.evaconnect.view.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.utils.AppLogger;

public enum  ApplicationDialogs {
    INSTANCE;
    private ProgressDialog mDialog;
    private Dialog dialog;
    private SimpleDialog simpleDialog;

    public MaterialStyledDialog createErrorDialog(String title, Context context) {
        // this.mContext = context;
        MaterialStyledDialog dialog =new MaterialStyledDialog.Builder(context)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setHeaderDrawable(R.drawable.header)
                .withDarkerOverlay(true)
                .setTitle("Error")
                .setDescription(title)
                .setPositiveText("OK").show();

        return  dialog;

    }
    public void dismissLoader(Context context) {
        try {
            if (null != context && !((AppCompatActivity) context).isFinishing()) {
                if (null != mDialog && null != mDialog.getContext() && mDialog.isShowing())
                    mDialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
            AppLogger.get().logException(e);
        }
    }


}

