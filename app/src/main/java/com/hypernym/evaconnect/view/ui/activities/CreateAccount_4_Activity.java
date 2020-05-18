package com.hypernym.evaconnect.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.bottomsheets.BottomSheetPictureSelection;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateAccount_4_Activity extends BaseActivity implements Validator.ValidationListener {

    String email, password, photourl, activity_type, user_type,
            aviation_type, JobSector, username, firstname, surname, city, country, filepath;


    @NotEmpty
    @BindView(R.id.edt_companyname)
    EditText edt_companyname;

    @NotEmpty
    @BindView(R.id.edt_jobtitle)
    EditText edt_jobtitle;


    @BindView(R.id.btn_next)
    Button btn_next;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_4);
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        init();
        btn_next.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                validator.validate();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {

        if (activity_type.equals("LinkedinActivity")) {
            Intent intent = new Intent(CreateAccount_4_Activity.this, PasswordActivity.class);
            intent.putExtra("Email", email);
            intent.putExtra("Photo", photourl);
            intent.putExtra("userType", user_type);
            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
            intent.putExtra("aviation_type", aviation_type);
            intent.putExtra("job_sector", JobSector);
            intent.putExtra("username", username);
            intent.putExtra("FirstName", firstname);
            intent.putExtra("SurName", surname);
            intent.putExtra("city", city);
            intent.putExtra("country", country);
            intent.putExtra("jobtitle", edt_jobtitle.getText().toString());
            intent.putExtra("companyname", edt_companyname.getText().toString());
            startActivity(intent);

        } else {
            Intent intent = new Intent(CreateAccount_4_Activity.this, PasswordActivity.class);
            intent.putExtra("Email", email);
            intent.putExtra("userType", user_type);
            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
            intent.putExtra("aviation_type", aviation_type);
            intent.putExtra("job_sector", JobSector);
            intent.putExtra("username", username);
            intent.putExtra("FirstName", firstname);
            intent.putExtra("SurName", surname);
            intent.putExtra("city", city);
            intent.putExtra("country", country);
            intent.putExtra("FilePath", filepath);
            intent.putExtra("jobtitle", edt_jobtitle.getText().toString());
            intent.putExtra("companyname", edt_companyname.getText().toString());
            startActivity(intent);
        }


    }

    private void init() {

        if ("LinkedinActivity".equals(getIntent().getStringExtra(Constants.ACTIVITY_NAME))) {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            user_type = getIntent().getStringExtra("userType");
            username = getIntent().getStringExtra("username");
            activity_type = "LinkedinActivity";
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
            city = getIntent().getStringExtra("city");
            country = getIntent().getStringExtra("country");
            aviation_type = getIntent().getStringExtra("aviation_type");
            JobSector = getIntent().getStringExtra("job_sector");


        } else {
            email = getIntent().getStringExtra("Email");
            password = getIntent().getStringExtra("Password");
            user_type = getIntent().getStringExtra("userType");
            username = getIntent().getStringExtra("username");
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
            city = getIntent().getStringExtra("city");
            country = getIntent().getStringExtra("country");
            filepath = getIntent().getStringExtra("FilePath");
            aviation_type = getIntent().getStringExtra("aviation_type");
            JobSector = getIntent().getStringExtra("job_sector");
            activity_type = "normal_type";
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
