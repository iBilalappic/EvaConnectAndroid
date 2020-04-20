package com.hypernym.evaconnect.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity_1 extends BaseActivity {

    String email, password, photourl, activity_type, user_type, aviation_type = "Commercial Aviation", JobSector,username;

    @BindView(R.id.tv_general_business)
    TextView tv_general_business;

    @BindView(R.id.tv_commercial_aviation)
    TextView tv_commercial_aviation;

    @BindView(R.id.spinner_sector)
    Spinner spinner_sector;

    @BindView(R.id.btn_next)
    Button btn_next;
    private List<String> jobsector = new ArrayList<>();
    ArrayAdapter<String> arraySectorAdapter;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_1);
        ButterKnife.bind(this);
        init();


    }

    private void init() {
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(this.getApplication())).get(UserViewModel.class);
        getSectorFromApi(aviation_type);
        tv_general_business.setBackground(getDrawable(R.drawable.rounded_button_border));
        tv_commercial_aviation.setBackground(getDrawable(R.drawable.rounded_button_selected));
        if ("LinkedinActivity".equals(getIntent().getStringExtra(Constants.ACTIVITY_NAME))) {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            user_type = getIntent().getStringExtra("userType");
            username = getIntent().getStringExtra("username");
            activity_type = "LinkedinActivity";

        } else {
            email = getIntent().getStringExtra("Email");
            password = getIntent().getStringExtra("Password");
            user_type = getIntent().getStringExtra("userType");
            username = getIntent().getStringExtra("username");
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
                    Intent intent = new Intent(SignupActivity_1.this, SignupActivity_2.class);
                    intent.putExtra("Email", email);
                    intent.putExtra("Photo", photourl);
                    intent.putExtra("userType", user_type);
                    intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                    intent.putExtra("aviation_type", aviation_type);
                    intent.putExtra("job_sector", JobSector);
                    intent.putExtra("username", username);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(SignupActivity_1.this, SignupActivity_2.class);
                    intent.putExtra("Email", email);
                    intent.putExtra("Password", password);
                    intent.putExtra("userType", user_type);
                    intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                    intent.putExtra("aviation_type", aviation_type);
                    intent.putExtra("job_sector", JobSector);
                    intent.putExtra("username", username);
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


}
