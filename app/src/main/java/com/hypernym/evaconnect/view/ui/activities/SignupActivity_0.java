package com.hypernym.evaconnect.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.AccountCheck;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.bottomsheets.BottomSheetPictureSelection;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.onesignal.OneSignal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity_0 extends BaseActivity implements Validator.ValidationListener {


    @BindView(R.id.tv_company)
    TextView tv_company;

    @BindView(R.id.tv_individual)
    TextView tv_individual;

    @BindView(R.id.btn_next)
    Button btn_next;

    @NotEmpty
    @BindView(R.id.edt_displayname)
    EditText edt_displayname;

    private String userType = "user";


    private Validator validator;
    String email, password, photourl, activity_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_0);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        tv_company.setBackground(getDrawable(R.drawable.rounded_button_border));
        tv_individual.setBackground(getDrawable(R.drawable.rounded_button_selected));
        validator = new Validator(this);
        validator.setValidationListener(this);
        if ("LinkedinActivity".equals(getIntent().getStringExtra(Constants.ACTIVITY_NAME))) {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            activity_type = "LinkedinActivity";

        } else {
            email = getIntent().getStringExtra("Email");
            password = getIntent().getStringExtra("Password");
            activity_type = "normal_type";
        }

        btn_next.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                validator.validate();
            }
        });
        tv_individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_company.setBackground(getDrawable(R.drawable.rounded_button_border));
                tv_individual.setBackground(getDrawable(R.drawable.rounded_button_selected));
                tv_company.setTextColor(getResources().getColor(R.color.gray));
                tv_individual.setTextColor(getResources().getColor(R.color.white));
                userType = "user";
            }
        });

        tv_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_company.setBackground(getDrawable(R.drawable.rounded_button_selected));
                tv_company.setTextColor(getResources().getColor(R.color.white));
                tv_individual.setBackground(getDrawable(R.drawable.rounded_button_border));
                tv_individual.setTextColor(getResources().getColor(R.color.gray));
                userType = "company";
            }
        });

    }


    @Override
    public void onValidationSucceeded() {

        if (activity_type.equals("LinkedinActivity")) {
            Intent intent = new Intent(SignupActivity_0.this, SignupActivity_1.class);
            intent.putExtra("Email", email);
            intent.putExtra("Photo", photourl);
            intent.putExtra("userType", userType);
            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
            intent.putExtra("username", edt_displayname.getText().toString());
            startActivity(intent);

        } else {
            Intent intent = new Intent(SignupActivity_0.this, SignupActivity_1.class);
            intent.putExtra("Email", email);
            intent.putExtra("Password", password);
            intent.putExtra("userType", userType);
            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
            intent.putExtra("username", edt_displayname.getText().toString());
            startActivity(intent);

        }


    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
