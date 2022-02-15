package com.hypernym.evaconnect.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
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

public class CreateAccount_4_Activity extends BaseActivity implements View.OnClickListener {

    String email, password, photourl, activity_type, user_type,path, about,
            aviation_type = "Commercial Aviation", JobSector,username,firstname,surname,city,country,filepath, language, companyUrl;

    @BindView(R.id.tv_general_business)
    TextView tv_general_business;

    @BindView(R.id.tv_commercial_aviation)
    TextView tv_commercial_aviation;

    @BindView(R.id.spinner_sector)
    Spinner spinner_sector;

    @BindView(R.id.btn_next)
    TextView btn_next;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.img_cross)
    ImageView img_cross;


    @BindView(R.id.tv_already_account)
    TextView tv_already_account;

    @BindView(R.id.edt_sector)
    EditText edt_sector;

    @BindView(R.id.edt_company)
    EditText edt_company;

    @BindView(R.id.edt_jobtitle)
    EditText edt_jobtitle;

    @BindView(R.id.othersector)
    LinearLayout othersector;

    @BindView(R.id.layout_individual)
    ConstraintLayout layout_individual;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.tv_company)
    TextView tv_company;




    private List<String> jobsector = new ArrayList<>();
    ArrayAdapter<String> arraySectorAdapter;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_4);
        ButterKnife.bind(this);
        img_backarrow.setOnClickListener(this);
        img_cross.setOnClickListener(this);
        tv_already_account.setOnClickListener(this);
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
            activity_type = "normal_type";
        }

        if (user_type.equals("user")) {
            layout_individual.setVisibility(View.VISIBLE);

            title.setText("Business Sector / Job Title");
            tv_company.setText("Which sector is your business in:");
        } else {
            layout_individual.setVisibility(View.GONE);
            title.setText("Company & Job Title");
            tv_company.setText("Which sector:");
        }

        tv_general_business.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                tv_commercial_aviation.setBackground(getDrawable(R.drawable.rounded_button_border));
                tv_general_business.setBackground(getDrawable(R.drawable.rounded_button_selected));
                tv_commercial_aviation.setTextColor(getResources().getColor(R.color.gray));
                tv_general_business.setTextColor(getResources().getColor(R.color.white));
                aviation_type = "General Aviation";
                jobsector.clear();
                getSectorFromApi(aviation_type);
            }

        });

        tv_commercial_aviation.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
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

                if((!spinner_sector.getSelectedItem().equals("Choose"))) {

                    if (activity_type.equals("LinkedinActivity")) {
                        Intent intent = new Intent(CreateAccount_4_Activity.this, PasswordActivity.class);
                        intent.putExtra("Email", email);
                        intent.putExtra("Photo", photourl);
                        intent.putExtra("userType", user_type);
                        intent.putExtra("Path", path);
                        intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                        intent.putExtra("aviation_type", aviation_type);
                        intent.putExtra("companyUrl", companyUrl);
                        intent.putExtra("job_sector", JobSector);
                        intent.putExtra("other_job_sector", edt_sector.getText().toString());
                        intent.putExtra("username", username);
                        intent.putExtra("FirstName", firstname);
                        intent.putExtra("SurName", surname);
                        intent.putExtra("city", city);
                        intent.putExtra("country", country);
                        intent.putExtra("about", about);
                        intent.putExtra("language", language);
                        intent.putExtra("jobtitle", edt_jobtitle.getText().toString());
                        intent.putExtra("companyname", edt_company.getText().toString());
                        startActivity(intent);

                    } else if (activity_type.equals(AppConstants.FACEBOOK_LOGIN_TYPE)) {
                        Intent intent = new Intent(CreateAccount_4_Activity.this, PasswordActivity.class);
                        intent.putExtra("Email", email);
                        intent.putExtra("Photo", photourl);
                        intent.putExtra("Path", path);
                        intent.putExtra("userType", user_type);
                        intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                        intent.putExtra("aviation_type", aviation_type);
                        intent.putExtra("companyUrl", companyUrl);
                        intent.putExtra("job_sector", JobSector);
                        intent.putExtra("other_job_sector", edt_sector.getText().toString());
                        intent.putExtra("username", username);
                        intent.putExtra("FirstName", firstname);
                        intent.putExtra("SurName", surname);
                        intent.putExtra("city", city);
                        intent.putExtra("country", country);
                        intent.putExtra("about", about);
                        intent.putExtra("language", language);
                        intent.putExtra("jobtitle", edt_jobtitle.getText().toString());
                        intent.putExtra("companyname", edt_company.getText().toString());
                        startActivity(intent);
                    } else {

                        Intent intent = new Intent(CreateAccount_4_Activity.this, PasswordActivity.class);
                        if (filepath != null) {

                            intent.putExtra("Email", email);
                            intent.putExtra("userType", user_type);
                            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                            intent.putExtra("aviation_type", aviation_type);
                            intent.putExtra("job_sector", JobSector);
                            intent.putExtra("other_job_sector", edt_sector.getText().toString());
                            intent.putExtra("username", username);
                            intent.putExtra("FirstName", firstname);
                            intent.putExtra("SurName", surname);
                            intent.putExtra("city", city);
                            intent.putExtra("companyUrl", companyUrl);
                            intent.putExtra("country", country);
                            intent.putExtra("FilePath", filepath);
                            intent.putExtra("about", about);
                            intent.putExtra("language", language);
                            intent.putExtra("jobtitle", edt_jobtitle.getText().toString());
                            intent.putExtra("companyname", edt_company.getText().toString());
                            startActivity(intent);
                        } else {
                            intent.putExtra("Email", email);
                            intent.putExtra("userType", user_type);
                            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                            intent.putExtra("aviation_type", aviation_type);
                            intent.putExtra("job_sector", JobSector);
                            intent.putExtra("other_job_sector", edt_sector.getText().toString());
                            intent.putExtra("username", username);
                            intent.putExtra("companyUrl", companyUrl);
                            intent.putExtra("FirstName", firstname);
                            intent.putExtra("SurName", surname);
                            intent.putExtra("city", city);
                            intent.putExtra("country", country);
                            intent.putExtra("about", about);
                            intent.putExtra("language", language);
                            intent.putExtra("jobtitle", edt_jobtitle.getText().toString());
                            intent.putExtra("companyname", edt_company.getText().toString());
                            startActivity(intent);
                        }

                    }
                }
                else
                {
                    Toast.makeText(CreateAccount_4_Activity.this, "Kindly Choose the Sector first", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getSectorFromApi(String aviation_type) {
        userViewModel.getSector(aviation_type).observe(this, new Observer<BaseModel<List<String>>>() {
            @Override
            public void onChanged(BaseModel<List<String>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    jobsector.add("Choose");
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
              //  JobSector=jobsector.get(position).toString();

                JobSector = parent.getItemAtPosition(position).toString();
                if(JobSector.equalsIgnoreCase("Other"))
                {
                    othersector.setVisibility(View.VISIBLE);

                }
                else
                {
                    othersector.setVisibility(View.GONE);
                }

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
