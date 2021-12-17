package com.hypernym.evaconnect.view.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.view.ui.fragments.SearchResultFragment;

public class ApplicationSuccess_dialog extends Dialog implements View.OnClickListener {

    private Context context;
    private Activity activity;
    TextView tv_ok;


    public ApplicationSuccess_dialog(Activity activity,Context context) {
        super(context);
        this.context = context;
        this.activity =activity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_application_success);

        tv_ok = findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                    activity.onBackPressed();
                dismiss();
                break;
        }
    }
}
