package com.hypernym.evaconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.view.ui.activities.BaseActivity;
import com.hypernym.evaconnect.view.ui.activities.EmailVerification;
import com.hypernym.evaconnect.view.ui.activities.LoginActivity;
import com.hypernym.evaconnect.view.ui.activities.PasswordActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EnterCodeActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.img_cross)
    ImageView img_cross;


    @BindView(R.id.tv_already_account)
    TextView tv_already_account;

    @BindView(R.id.btn_next)
    TextView btn_next;

    String email, password, photourl, activity_type, user_type,path, about, other_job_sector,companyname, language,
            aviation_type = "Commercial Aviation", JobSector,username,firstname,surname,city,country,filepath, companyUrl, jobtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        String type = getIntent().getStringExtra(Constants.ACTIVITY_NAME);

        if ("LinkedinActivity".equals(getIntent().getStringExtra(Constants.ACTIVITY_NAME))) {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            user_type = getIntent().getStringExtra("userType");
            path = getIntent().getStringExtra("Path");
            companyUrl = getIntent().getStringExtra("companyUrl");
            username = getIntent().getStringExtra("username");
            activity_type = "LinkedinActivity";
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
            city = getIntent().getStringExtra("city");
            country = getIntent().getStringExtra("country");
            about = getIntent().getStringExtra("about");
            language = getIntent().getStringExtra("language");
            jobtitle = getIntent().getStringExtra("jobtitle");
            JobSector = getIntent().getStringExtra("job_sector");
            other_job_sector = getIntent().getStringExtra("other_job_sector");
            companyname = getIntent().getStringExtra("companyname");

        }
        else if (!TextUtils.isEmpty(type) && type.equals(AppConstants.FACEBOOK_LOGIN_TYPE)){
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            path = getIntent().getStringExtra("Path");
            companyUrl = getIntent().getStringExtra("companyUrl");
            user_type = getIntent().getStringExtra("userType");
            username = getIntent().getStringExtra("username");
            activity_type = AppConstants.FACEBOOK_LOGIN_TYPE;
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
            city = getIntent().getStringExtra("city");
            country = getIntent().getStringExtra("country");
            about = getIntent().getStringExtra("about");
            language = getIntent().getStringExtra("language");
            jobtitle = getIntent().getStringExtra("jobtitle");
            JobSector = getIntent().getStringExtra("job_sector");
            other_job_sector = getIntent().getStringExtra("other_job_sector");
            companyname = getIntent().getStringExtra("companyname");
        }
        else {
            email = getIntent().getStringExtra("Email");
            user_type = getIntent().getStringExtra("userType");
            username = getIntent().getStringExtra("username");
            firstname = getIntent().getStringExtra("FirstName");
            companyUrl = getIntent().getStringExtra("companyUrl");
            surname = getIntent().getStringExtra("SurName");
            city = getIntent().getStringExtra("city");
            country = getIntent().getStringExtra("country");
            filepath = getIntent().getStringExtra("FilePath");
            about = getIntent().getStringExtra("about");
            language = getIntent().getStringExtra("language");
            jobtitle = getIntent().getStringExtra("jobtitle");
            JobSector = getIntent().getStringExtra("job_sector");
            other_job_sector = getIntent().getStringExtra("other_job_sector");
            companyname = getIntent().getStringExtra("companyname");
            activity_type = "normal_type";
        }


        btn_next.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {


                if (activity_type.equals("LinkedinActivity")) {
                    Intent intent = new Intent(EnterCodeActivity.this, PasswordActivity.class);
                    intent.putExtra("Email", email);
                    intent.putExtra("Photo", photourl);
                    intent.putExtra("userType", user_type);
                    intent.putExtra("Path", path);
                    intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                    intent.putExtra("aviation_type", aviation_type);
                    intent.putExtra("companyUrl", companyUrl);
                    intent.putExtra("job_sector", JobSector);
                    intent.putExtra("other_job_sector", other_job_sector);
                    intent.putExtra("username", username);
                    intent.putExtra("FirstName", firstname);
                    intent.putExtra("SurName", surname);
                    intent.putExtra("city", city);
                    intent.putExtra("country", country);
                    intent.putExtra("about", about);
                    intent.putExtra("language", language);
                    intent.putExtra("jobtitle", jobtitle);
                    intent.putExtra("companyname", companyname);
                    startActivity(intent);

                } else if (activity_type.equals(AppConstants.FACEBOOK_LOGIN_TYPE)) {
                    Intent intent = new Intent(EnterCodeActivity.this, PasswordActivity.class);
                    intent.putExtra("Email", email);
                    intent.putExtra("Photo", photourl);
                    intent.putExtra("Path", path);
                    intent.putExtra("userType", user_type);
                    intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                    intent.putExtra("aviation_type", aviation_type);
                    intent.putExtra("companyUrl", companyUrl);
                    intent.putExtra("job_sector", JobSector);
                    intent.putExtra("other_job_sector", other_job_sector);
                    intent.putExtra("username", username);
                    intent.putExtra("FirstName", firstname);
                    intent.putExtra("SurName", surname);
                    intent.putExtra("city", city);
                    intent.putExtra("country", country);
                    intent.putExtra("about", about);
                    intent.putExtra("language", language);
                    intent.putExtra("jobtitle", jobtitle);
                    intent.putExtra("companyname",companyname);
                    startActivity(intent);
                } else {

                    Intent intent = new Intent(EnterCodeActivity.this, PasswordActivity.class);
                    if (filepath != null) {

                        intent.putExtra("Email", email);
                        intent.putExtra("userType", user_type);
                        intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                        intent.putExtra("aviation_type", aviation_type);
                        intent.putExtra("job_sector", JobSector);
                        intent.putExtra("other_job_sector", other_job_sector);
                        intent.putExtra("username", username);
                        intent.putExtra("FirstName", firstname);
                        intent.putExtra("SurName", surname);
                        intent.putExtra("city", city);
                        intent.putExtra("companyUrl", companyUrl);
                        intent.putExtra("country", country);
                        intent.putExtra("FilePath", filepath);
                        intent.putExtra("about", about);
                        intent.putExtra("language", language);
                        intent.putExtra("jobtitle", jobtitle);
                        intent.putExtra("companyname", companyname);
                        startActivity(intent);
                    } else {
                        intent.putExtra("Email", email);
                        intent.putExtra("userType", user_type);
                        intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                        intent.putExtra("aviation_type", aviation_type);
                        intent.putExtra("job_sector", JobSector);
                        intent.putExtra("other_job_sector", other_job_sector);
                        intent.putExtra("username", username);
                        intent.putExtra("companyUrl", companyUrl);
                        intent.putExtra("FirstName", firstname);
                        intent.putExtra("SurName", surname);
                        intent.putExtra("city", city);
                        intent.putExtra("country", country);
                        intent.putExtra("about", about);
                        intent.putExtra("language", language);
                        intent.putExtra("jobtitle", jobtitle);
                        intent.putExtra("companyname", companyname);
                        startActivity(intent);
                    }

                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_backarrow:
                this.finish();
                break;

            case R.id.img_cross:
                this.finish();
                break;

            case R.id.tv_already_account:
                Intent intent = new Intent(EnterCodeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}