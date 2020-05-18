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
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.onesignal.OneSignal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PasswordActivity extends BaseActivity implements Validator.ValidationListener {

    String email, password, photourl, activity_type, user_type,
                    aviation_type, JobSector, username, firstname, surname,
                    city, country, filepath, jobtitle, company_name;

    private Validator validator;

    private User user = new User();
    private UserViewModel userViewModel;

    @NotEmpty
    @Password(min = 8)
    @BindView(R.id.edt_password)
    EditText edt_password;

    @BindView(R.id.btn_finish)
    Button btn_finish;
    MultipartBody.Part partImage;
    SimpleDialog simpleDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        init();
        btn_finish.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                validator.validate();
            }
        });
    }

    private void init() {
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(this.getApplication())).get(UserViewModel.class);


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
            jobtitle = getIntent().getStringExtra("jobtitle");
            company_name = getIntent().getStringExtra("companyname");



            user.setUsername(email);
            user.setEmail(email);
            user.setPassword("hypernym");
            user.setIsLinkedin(1);
            user.setLinkedin_image_url(photourl);
            user.setType(user_type);
            user.setWork_aviation(aviation_type);
            user.setFirst_name(firstname);
            user.setSector(JobSector);
            user.setLast_name(surname);
            user.setCity(city);
            user.setCountry(country);
            user.setCompany_name(company_name);
            user.setDesignation(jobtitle);



        } else {
            email = getIntent().getStringExtra("Email");
            user_type = getIntent().getStringExtra("userType");
            username = getIntent().getStringExtra("username");
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
            city = getIntent().getStringExtra("city");
            country = getIntent().getStringExtra("country");
            filepath = getIntent().getStringExtra("FilePath");
            aviation_type = getIntent().getStringExtra("aviation_type");
            JobSector = getIntent().getStringExtra("job_sector");
            jobtitle = getIntent().getStringExtra("jobtitle");
            company_name = getIntent().getStringExtra("companyname");
            activity_type = "normal_type";
            user.setLinkedin_image_url("");

//            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), filepath);
//            partImage = MultipartBody.Part.createFormData("user_image", filepath.getName(), reqFile);

            user.setWork_aviation(aviation_type);
            user.setUsername(email);
            user.setEmail(email);
            user.setIsLinkedin(0);
            user.setType(user_type);
            user.setFirst_name(firstname);
            user.setSector(JobSector);
            user.setLast_name(surname);
            user.setCity(city);
            user.setCountry(country);
            user.setCompany_name(company_name);
            user.setDesignation(jobtitle);
            user.setLinkedin_image_url("");
        }
    }

    @Override
    public void onValidationSucceeded() {
        user.setStatus(AppConstants.USER_STATUS);
        user.setPassword(edt_password.getText().toString());
        if (NetworkUtils.isNetworkConnected(this)) {
            showDialog();
            callSignupApi();
        } else {
            networkErrorDialog();
        }

    }

    public void callSignupApi() {
        userViewModel.signUp(user, partImage).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> logins) {
                if (logins != null && !logins.isError()) {
                    LoginUtils.saveUser(user);
                    if ("LinkedinActivity".equals(getIntent().getStringExtra(Constants.ACTIVITY_NAME))) {
                      //  JustLoginApiCall();
                    } else {
                        simpleDialog = new SimpleDialog(PasswordActivity.this, getString(R.string.success), getString(R.string.msg_signup), null, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callLoginApi();

                            }
                        });
                        simpleDialog.show();
                    }

                } else {
                    simpleDialog = networkResponseDialog(getString(R.string.error), logins.getMessage());
                }
            }
        });
    }

    private void callLoginApi() {
        showDialog();
        userViewModel.login(user).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> user) {
                if (user != null && !user.isError() && user.getData().get(0) != null) {
                    LoginUtils.userLoggedIn();

                    User userData = user.getData().get(0);
                    userData.setUser_id(userData.getId());
                    LoginUtils.saveUser(user.getData().get(0));
                    OneSignal.sendTag("email", userData.getEmail());
                    UserDetails.username = userData.getFirst_name();
                    if (user.getData().get(0) != null) {
                        LoginUtils.saveUserToken(user.getData().get(0).getToken());
                    }
                    simpleDialog.dismiss();
                    Intent intent = new Intent(PasswordActivity.this, HomeActivity.class);
                    // set the new task and clear flags
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else if (user != null && user.isError()) {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_login));
                } else if (user == null) {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
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
