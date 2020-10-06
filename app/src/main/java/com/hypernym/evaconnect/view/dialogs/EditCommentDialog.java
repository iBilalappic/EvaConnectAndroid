package com.hypernym.evaconnect.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.hypernym.evaconnect.R;

public class EditCommentDialog extends Dialog {
    private String mTitle;
    private String mMessage;
    private String mPositiveButtonText;
    private String mNegativeButtonText;
    private EditText edt_comment;
    private View.OnClickListener mOnClickListener;

    public EditCommentDialog(Context context, String title, String negativeButtonText, String positiveButtonText, View.OnClickListener onClickListener) {
        super(context);
        this.mTitle = title;
        this.mPositiveButtonText = positiveButtonText;
        this.mNegativeButtonText = negativeButtonText;
        this.mOnClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_comments);
        setCancelable(false);

        EditText edt_comment = (EditText) findViewById(R.id.edt_comment);
      //  TextView messageText = (TextView) findViewById(R.id.text_message);
        Button negativeButton = (Button) findViewById(R.id.button_negative);
        Button positiveButton = (Button) findViewById(R.id.button_positive);
        negativeButton.setTransformationMethod(null);
        positiveButton.setTransformationMethod(null);

        if (mNegativeButtonText != null) {
            negativeButton.setText(mNegativeButtonText);
            negativeButton.setVisibility(View.VISIBLE);
            negativeButton.setOnClickListener(mOnClickListener);
        }
        if (mPositiveButtonText != null) {
            positiveButton.setText(mPositiveButtonText);
            positiveButton.setVisibility(View.VISIBLE);
            positiveButton.setOnClickListener(mOnClickListener);
        }
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        edt_comment.setText(mTitle);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}

