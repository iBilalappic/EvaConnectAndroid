package com.hypernym.evaconnect.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAccount_3_Activity extends BaseActivity implements View.OnClickListener {

    String email, password, photourl, activity_type, user_type,
            aviation_type = "Commercial Aviation", JobSector,username,firstname,surname,city,country,filepath;

    @BindView(R.id.tv_general_business)
    TextView tv_general_business;

    @BindView(R.id.tv_commercial_aviation)
    TextView tv_commercial_aviation;

    @BindView(R.id.spinner_sector)
    Spinner spinner_sector;

    @BindView(R.id.btn_next)
    Button btn_next;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.img_cross)
    ImageView img_cross;


    private List<String> jobsector = new ArrayList<>();
    ArrayAdapter<String> arraySectorAdapter;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_3);
        ButterKnife.bind(this);
        img_backarrow.setOnClickListener(this);
        img_cross.setOnClickListener(this);
        init();


    }

    private void init() {
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(this.getApplication())).get(UserViewModel.class);
        getSectorFromApi(aviation_type);
        tv_general_business.setBackground(getDrawable(R.drawable.rounded_button_border));
        tv_commercial_aviation.setBackground(getDrawable(R.drawable.rounded_button_selected));

        String type = getIntent().getStringExtra(Constants.ACTIVITY_NAME);

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

        }
        else if (!TextUtils.isEmpty(type) && type.equals(AppConstants.FACEBOOK_LOGIN_TYPE)){
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            user_type = getIntent().getStringExtra("userType");
            username = getIntent().getStringExtra("username");
            activity_type = AppConstants.FACEBOOK_LOGIN_TYPE;
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
            city = getIntent().getStringExtra("city");
            country = getIntent().getStringExtra("country");
        }
        else {
            email = getIntent().getStringExtra("Email");
            user_type = getIntent().getStringExtra("userType");
            username = getIntent().getStringExtra("username");
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
            city = getIntent().getStringExtra("city");
            country = getIntent().getStringExtra("country");
            filepath = getIntent().getStringExtra("FilePath");
            activity_type = "normal_type";
        }

        tv_general_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_commercial_aviation.setBackground(getDrawable(R.drawable.rounded_button_border));
                tv_general_business.setBackground(getDrawable(R.drawable.rounded_button_selected));
                tv_commercial_aviation.setTextColor(getResources().getColor(R.color.gray));
                tv_general_business.setTextColor(getResources().getColor(R.color.white));
                aviation_type = "General Aviation";
                jobsector.clear();
                getSectorFromApi(aviation_type);
            }
        });

        tv_commercial_aviation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_commercial_aviation.setBackground(getDrawable(R.drawable.rounded_button_selected));
                tv_commercial_aviation.setTextColor(getResources().getColor(R.color.white));
                tv_general_business.setBackground(getDrawable(R.drawable.rounded_button_border));
                tv_general_business.setTextColor(getResources().getColor(R.color.gray));
                aviation_type = "Commercial Aviation";
                jobsector.clear();
                getSectorFromApi(aviation_type);
            }
        });

        btn_next.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (activity_type.equals("LinkedinActivity")) {
                    Intent intent = new Intent(CreateAccount_3_Activity.this, CreateAccount_4_Activity.class);
                    intent.putExtra("Email", email);
                    intent.putExtra("Photo", photourl);
                    intent.putExtra("userType", user_type);
                    intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                    intent.putExtra("aviation_type", aviation_type);
                    intent.putExtra("job_sector", JobSector);
                    intent.putExtra("username", username);
                    intent.putExtra("FirstName",firstname);
                    intent.putExtra("SurName", surname);
                    intent.putExtra("city", city);
                    intent.putExtra("country", country);
                    startActivity(intent);

                }
                else if (activity_type.equals(AppConstants.FACEBOOK_LOGIN_TYPE)){
                    Intent intent = new Intent(CreateAccount_3_Activity.this, CreateAccount_4_Activity.class);
                    intent.putExtra("Email", email);
                    intent.putExtra("Photo", photourl);
                    intent.putExtra("userType", user_type);
                    intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                    intent.putExtra("aviation_type", aviation_type);
                    intent.putExtra("job_sector", JobSector);
                    intent.putExtra("username", username);
                    intent.putExtra("FirstName",firstname);
                    intent.putExtra("SurName", surname);
                    intent.putExtra("city", city);
                    intent.putExtra("country", country);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(CreateAccount_3_Activity.this, CreateAccount_4_Activity.class);
                    intent.putExtra("Email", email);
                    intent.putExtra("userType", user_type);
                    intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                    intent.putExtra("aviation_type", aviation_type);
                    intent.putExtra("job_sector", JobSector);
                    intent.putExtra("username", username);
                    intent.putExtra("FirstName",firstname);
                    intent.putExtra("SurName", surname);
                    intent.putExtra("city", city);
                    intent.putExtra("country", country);
                    intent.putExtra("FilePath", filepath);
                    startActivity(intent);
                }

            }
        });
    }

    private void getSectorFromApi(String aviation_type) {
        userViewModel.getSector(aviation_type).observe(this, new Observer<BaseModel<List<String>>>() {
            @Override
            public void onChanged(BaseModel<List<String>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    jobsector.addAll(listBaseModel.getData());
                    SettingJobSectorSpinner();
                    Log.d("TAAAG", "" + jobsector);
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void SettingJobSectorSpinner() {
        arraySectorAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, jobsector);
        arraySectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sector.setAdapter(arraySectorAdapter);

        spinner_sector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JobSector = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        spinner_sector.setSelection(0);
//        if ((getArguments() != null)) {
//
//            if (companyJobAdModel.getJobSector() != null) {
//                int spinnerPosition = arraySectorAdapter.getPosition(companyJobAdModel.getJobSector());
//                spinner_jobsector.setSelection(0);
//            }
//        }
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
        }
    }
}
