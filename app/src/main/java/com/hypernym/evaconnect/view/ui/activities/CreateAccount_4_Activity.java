package com.hypernym.evaconnect.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.utils.Constants;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAccount_4_Activity extends BaseActivity implements Validator.ValidationListener, View.OnClickListener {

    String email, password, photourl, activity_type, user_type,path,
            aviation_type, JobSector, username, firstname, surname, city, country, filepath,otherJobSector;


    @NotEmpty
    @BindView(R.id.edt_companyname)
    EditText edt_companyname;

    @NotEmpty
    @BindView(R.id.edt_jobtitle)
    EditText edt_jobtitle;


    @BindView(R.id.btn_next)
    Button btn_next;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.img_cross)
    ImageView img_cross;


    @BindView(R.id.tv_already_account)
    TextView tv_already_account;

    @BindView(R.id.job_title)
    LinearLayout job_title;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_4);
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        img_cross.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);
        tv_already_account.setOnClickListener(this);
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
            intent.putExtra("Path", path);
            intent.putExtra("userType", user_type);
            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
            intent.putExtra("aviation_type", aviation_type);
            intent.putExtra("job_sector", JobSector);
            intent.putExtra("other_job_sector", otherJobSector);
            intent.putExtra("username", username);
            intent.putExtra("FirstName", firstname);
            intent.putExtra("SurName", surname);
            intent.putExtra("city", city);
            intent.putExtra("country", country);
            intent.putExtra("jobtitle", edt_jobtitle.getText().toString());
            intent.putExtra("companyname", edt_companyname.getText().toString());
            startActivity(intent);

        }
        else if (activity_type.equals(AppConstants.FACEBOOK_LOGIN_TYPE)){
            Intent intent = new Intent(CreateAccount_4_Activity.this, PasswordActivity.class);
            intent.putExtra("Email", email);
            intent.putExtra("Photo", photourl);
            intent.putExtra("Path", path);
            intent.putExtra("userType", user_type);
            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
            intent.putExtra("aviation_type", aviation_type);
            intent.putExtra("job_sector", JobSector);
            intent.putExtra("other_job_sector", otherJobSector);
            intent.putExtra("username", username);
            intent.putExtra("FirstName", firstname);
            intent.putExtra("SurName", surname);
            intent.putExtra("city", city);
            intent.putExtra("country", country);
            intent.putExtra("jobtitle", edt_jobtitle.getText().toString());
            intent.putExtra("companyname", edt_companyname.getText().toString());
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(CreateAccount_4_Activity.this, PasswordActivity.class);
            if (filepath != null) {

                intent.putExtra("Email", email);
                intent.putExtra("userType", user_type);
                intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                intent.putExtra("aviation_type", aviation_type);
                intent.putExtra("job_sector", JobSector);
                intent.putExtra("other_job_sector", otherJobSector);
                intent.putExtra("username", username);
                intent.putExtra("FirstName", firstname);
                intent.putExtra("SurName", surname);
                intent.putExtra("city", city);
                intent.putExtra("country", country);
                intent.putExtra("FilePath", filepath);
                intent.putExtra("jobtitle", edt_jobtitle.getText().toString());
                intent.putExtra("companyname", edt_companyname.getText().toString());
                startActivity(intent);
            } else {
                intent.putExtra("Email", email);
                intent.putExtra("userType", user_type);
                intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                intent.putExtra("aviation_type", aviation_type);
                intent.putExtra("job_sector", JobSector);
                intent.putExtra("other_job_sector", otherJobSector);
                intent.putExtra("username", username);
                intent.putExtra("FirstName", firstname);
                intent.putExtra("SurName", surname);
                intent.putExtra("city", city);
                intent.putExtra("country", country);
                intent.putExtra("jobtitle", edt_jobtitle.getText().toString());
                intent.putExtra("companyname", edt_companyname.getText().toString());
                startActivity(intent);
            }
        }


    }

    private void init() {
        String type = getIntent().getStringExtra(Constants.ACTIVITY_NAME);

        if ("LinkedinActivity".equals(getIntent().getStringExtra(Constants.ACTIVITY_NAME))) {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            path = getIntent().getStringExtra("Path");
            user_type = getIntent().getStringExtra("userType");
            username = getIntent().getStringExtra("username");
            activity_type = "LinkedinActivity";
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
            city = getIntent().getStringExtra("city");
            country = getIntent().getStringExtra("country");
            aviation_type = getIntent().getStringExtra("aviation_type");
            JobSector = getIntent().getStringExtra("job_sector");
            otherJobSector=getIntent().getStringExtra("other_job_sector");
        }
        else if (!TextUtils.isEmpty(type) && type.equals(AppConstants.FACEBOOK_LOGIN_TYPE))
        {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            path = getIntent().getStringExtra("Path");
            user_type = getIntent().getStringExtra("userType");
            username = getIntent().getStringExtra("username");
            activity_type = AppConstants.FACEBOOK_LOGIN_TYPE;
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
            city = getIntent().getStringExtra("city");
            country = getIntent().getStringExtra("country");
            aviation_type = getIntent().getStringExtra("aviation_type");
            JobSector = getIntent().getStringExtra("job_sector");
            otherJobSector=getIntent().getStringExtra("other_job_sector");
        }
        else {
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
            otherJobSector=getIntent().getStringExtra("other_job_sector");

            activity_type = "normal_type";
        }

        if(user_type.equalsIgnoreCase(AppConstants.COMPANY_USER_TYPE))
        {
            job_title.setVisibility(View.GONE);
        }
        else
        {
            job_title.setVisibility(View.VISIBLE);
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


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(CreateAccount_4_Activity.this, LoginActivity.class);
        switch (v.getId()) {
            case R.id.img_backarrow:
                this.finish();
                break;

            case R.id.img_cross:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.tv_already_account:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}
