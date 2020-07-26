package com.hypernym.evaconnect.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.onesignal.OneSignal;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PasswordActivity extends BaseActivity implements Validator.ValidationListener, View.OnClickListener {

    String email, password, photourl, activity_type, user_type,
            aviation_type, JobSector, username, firstname, surname,
            city, country, filepath, jobtitle, company_name;

    private Validator validator;

    private User user = new User();
    private UserViewModel userViewModel;
    private String type = "";

    @NotEmpty
    @Password(min = 8)
    @BindView(R.id.edt_password)
    EditText edt_password;

    @BindView(R.id.btn_finish)
    Button btn_finish;
    MultipartBody.Part partImage;
    SimpleDialog simpleDialog;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.img_cross)
    ImageView img_cross;


    @BindView(R.id.tv_already_account)
    TextView tv_already_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        init();
        img_backarrow.setOnClickListener(this);
        img_cross.setOnClickListener(this);
        tv_already_account.setOnClickListener(this);
        btn_finish.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                validator.validate();
            }
        });
    }

    private void init() {
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(this.getApplication())).get(UserViewModel.class);

        type = getIntent().getStringExtra(Constants.ACTIVITY_NAME);

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
            user.setIs_facebook(0);
            user.setLinkedin_image_url(photourl);
            user.setFacebook_image_url("");
            user.setType(user_type);
            user.setWork_aviation(aviation_type);
            user.setFirst_name(firstname);
            user.setSector(JobSector);
            user.setLast_name(surname);
            user.setCity(city);
            user.setCountry(country);
            user.setCompany_name(company_name);
            user.setDesignation(jobtitle);


        }
        else if (!TextUtils.isEmpty(type) && type.equals(AppConstants.FACEBOOK_LOGIN_TYPE))
        {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            user_type = getIntent().getStringExtra("userType");
            username = getIntent().getStringExtra("username");
            activity_type = AppConstants.FACEBOOK_LOGIN_TYPE;
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
            user.setIs_facebook(1);
            user.setIsLinkedin(0);
            user.setFacebook_image_url(photourl);
            user.setLinkedin_image_url("");
            user.setType(user_type);
            user.setWork_aviation(aviation_type);
            user.setFirst_name(firstname);
            user.setSector(JobSector);
            user.setLast_name(surname);
            user.setCity(city);
            user.setCountry(country);
            user.setCompany_name(company_name);
            user.setDesignation(jobtitle);
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
            aviation_type = getIntent().getStringExtra("aviation_type");
            JobSector = getIntent().getStringExtra("job_sector");
            jobtitle = getIntent().getStringExtra("jobtitle");
            company_name = getIntent().getStringExtra("companyname");
            activity_type = "normal_type";
            user.setLinkedin_image_url("");
            user.setFacebook_image_url("");
            if(filepath!=null){
                File file = new File(filepath);

                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                partImage = MultipartBody.Part.createFormData("user_image", file.getName(), reqFile);
            }


            user.setWork_aviation(aviation_type);
            user.setUsername(email);
            user.setEmail(email);
            user.setIsLinkedin(0);
            user.setIs_facebook(0);
            user.setType(user_type);
            user.setFirst_name(firstname);
            user.setSector(JobSector);
            user.setLast_name(surname);
            user.setCity(city);
            user.setCountry(country);
            user.setCompany_name(company_name);
            user.setDesignation(jobtitle);
            user.setLinkedin_image_url("");
            user.setFacebook_image_url("");
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
                          JustLoginApiCall();
                    }
                    else if (type.equals(AppConstants.FACEBOOK_LOGIN_TYPE)){
                        loginWithFacebook();
                    }
                    else {
                        callLoginApi();
                    }

                } else {
                    hideDialog();
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
                  //  simpleDialog.dismiss();
                    Intent intent = new Intent(PasswordActivity.this, NewsActivity.class);
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

    private void JustLoginApiCall() {

        if (NetworkUtils.isNetworkConnected(this)) {
//            showDialog();
            userViewModel.linkedin_login(email, Constants.LINKEDIN_TYPE).observe(this, new Observer<BaseModel<List<User>>>() {
                @Override
                public void onChanged(BaseModel<List<User>> listBaseModel) {
                    LoginUtils.userLoggedIn();
                    User userData = listBaseModel.getData().get(0);
                    userData.setUser_id(userData.getId());
                    LoginUtils.saveUser(listBaseModel.getData().get(0));
                    OneSignal.sendTag("email", userData.getEmail());
                    UserDetails.username = userData.getFirst_name();

                    if (listBaseModel.getData().get(0) != null) {
                        LoginUtils.saveUserToken(listBaseModel.getData().get(0).getToken());
                    }
                    Intent intent = new Intent(PasswordActivity.this, NewsActivity.class);
                    // set the new task and clear flags
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }
    }

    private void loginWithFacebook() {
        if (NetworkUtils.isNetworkConnected(this))
        {
            userViewModel.facebookLogin(email, Constants.FACEBOOK_TYPE).observe(this, listBaseModel -> {
                LoginUtils.userLoggedIn();
                User userData = listBaseModel.getData().get(0);
                userData.setUser_id(userData.getId());
                LoginUtils.saveUser(listBaseModel.getData().get(0));
                OneSignal.sendTag("email", userData.getEmail());
                UserDetails.username = userData.getFirst_name();
                if (listBaseModel.getData().get(0) != null) {
                    LoginUtils.saveUserToken(listBaseModel.getData().get(0).getToken());
                }

                Intent intent = new Intent(PasswordActivity.this, NewsActivity.class);
                // set the new task and clear flags
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view.getId() == R.id.edt_password) {
                message = getString(R.string.err_password);
            }
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
        switch (v.getId()) {
            case R.id.img_backarrow:
                this.finish();
                break;

            case R.id.img_cross:
                this.finish();
                break;

            case R.id.tv_already_account:
                Intent intent = new Intent(PasswordActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}
