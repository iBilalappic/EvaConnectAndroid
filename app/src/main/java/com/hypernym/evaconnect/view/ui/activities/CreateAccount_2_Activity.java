package com.hypernym.evaconnect.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAccount_2_Activity extends BaseActivity implements
         View.OnClickListener {

    @BindView(R.id.img_cross)
    ImageView img_cross;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.tv_already_account)
    TextView tv_already_account;

    @BindView(R.id.btn_next)
    TextView btn_next;

    @BindView(R.id.edit_about_yourself)
    EditText edit_about_yourself;

    @BindView(R.id.tv_main_title)
    TextView tv_main_title;

    String email, photourl, activity_type, user_type, firstname, surname, file_name, path, companyUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_2);
        ButterKnife.bind(this);
        img_cross.setOnClickListener(this);
        tv_already_account.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);
        init();
        btn_next.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {

                if (activity_type.equals("LinkedinActivity")) {
                    Intent intent = new Intent(CreateAccount_2_Activity.this, CreateAccount_3_Activity.class);
                    intent.putExtra("Email", email);
                    intent.putExtra("Photo", photourl);
                    intent.putExtra("Path", path);
                    intent.putExtra("userType", user_type);
                    intent.putExtra("FirstName", firstname);
                    intent.putExtra("SurName", surname);
                    intent.putExtra("companyUrl", companyUrl);
                    intent.putExtra("about", edit_about_yourself.getText().toString());
                    intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                    startActivity(intent);

                } else if (activity_type.equals(AppConstants.FACEBOOK_LOGIN_TYPE)) {
                    Intent intent = new Intent(CreateAccount_2_Activity.this, CreateAccount_3_Activity.class);
                    intent.putExtra("Email", email);
                    intent.putExtra("Photo", photourl);
                    intent.putExtra("Path", path);
                    intent.putExtra("userType", user_type);
                    intent.putExtra("FirstName", firstname);
                    intent.putExtra("SurName", surname);
                    intent.putExtra("companyUrl", companyUrl);
                    intent.putExtra("about", edit_about_yourself.getText().toString());
                    intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CreateAccount_2_Activity.this, CreateAccount_3_Activity.class);
                    if (file_name != null) {

                        intent.putExtra("Email", email);
                        intent.putExtra("FirstName", firstname);
                        intent.putExtra("SurName", surname);
                        intent.putExtra("FilePath", file_name);
                        intent.putExtra("userType", user_type);
                        intent.putExtra("companyUrl", companyUrl);
                        intent.putExtra("about", edit_about_yourself.getText().toString());
                        intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                        startActivity(intent);
                    } else {
                        intent.putExtra("Email", email);
                        intent.putExtra("FirstName", firstname);
                        intent.putExtra("SurName", surname);
                        intent.putExtra("userType", user_type);
                        intent.putExtra("companyUrl", companyUrl);
                        intent.putExtra("about", edit_about_yourself.getText().toString());
                        intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void init() {
        String type = getIntent().getStringExtra(Constants.ACTIVITY_NAME);

        if ("LinkedinActivity".equals(getIntent().getStringExtra(Constants.ACTIVITY_NAME))) {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            user_type = getIntent().getStringExtra("userType");
            path = getIntent().getStringExtra("Path");
            companyUrl = getIntent().getStringExtra("companyUrl");
            activity_type = "LinkedinActivity";
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");


        } else if (!TextUtils.isEmpty(type) && type.equals(AppConstants.FACEBOOK_LOGIN_TYPE)) {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            path = getIntent().getStringExtra("Path");
            companyUrl = getIntent().getStringExtra("companyUrl");
            user_type = getIntent().getStringExtra("userType");
            activity_type = AppConstants.FACEBOOK_LOGIN_TYPE;
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
        } else {
            email = getIntent().getStringExtra("Email");
            activity_type = "normal_type";
            companyUrl = getIntent().getStringExtra("companyUrl");
            file_name = getIntent().getStringExtra("FilePath");
            user_type = getIntent().getStringExtra("userType");
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
        }

        if(user_type!=null){
            switch (user_type){
                case "company":{
                    tv_main_title.setText(R.string.tell_us_about_comapny);
                }
                case "user":{
                    tv_main_title.setText(R.string.tell_us_about_yourself);
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(CreateAccount_2_Activity.this, LoginActivity.class);
        switch (v.getId()) {
            case R.id.img_backarrow:
                this.finish();
                break;

            case R.id.img_cross:

            case R.id.tv_already_account:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }


}