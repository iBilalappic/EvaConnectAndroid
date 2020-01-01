package com.hypernym.evaconnect.view.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.PrefUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements Validator.ValidationListener {
    @BindView(R.id.tv_signup)
    TextView tv_signup;

    @BindView(R.id.tv_forgotpass)
    TextView tv_forgotpass;

    @BindView(R.id.btn_login)
    Button btn_login;

    @NotEmpty
    @Email
    @BindView(R.id.edt_email)
    EditText edt_email;

    @NotEmpty
    @Password(min=4)
    @BindView(R.id.edt_password)
    EditText edt_password;

    private Validator validator;
    private UserViewModel userViewModel;
    private User user=new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        userViewModel = ViewModelProviders.of(this,new CustomViewModelFactory(getApplication(),this)).get(UserViewModel.class);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @OnClick(R.id.tv_signup)
    public void signUp()
    {
        startActivity(new Intent(LoginActivity.this,SignupActivity.class));
    }

    @OnClick(R.id.tv_forgotpass)
    public void forgotpassword()
    {

        startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
    }

    @OnClick(R.id.btn_login)
    public void btn_login()
    {
        validator.validate();

    }

    @Override
    public void onValidationSucceeded() {
        user.setUsername(edt_email.getText().toString());
        user.setPassword(edt_password.getText().toString());
        showDialog();
        userViewModel.login(user).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> user) {
                if(!user.isError())
                {
                   LoginUtils.userLoggedIn(AppUtils.getApplicationContext());
                   startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                }
                else if(user.isError())
                {
                    Toast.makeText(LoginActivity.this,user.getMessage(),Toast.LENGTH_LONG).show();
                }
                else if(user==null)
                {
                    Toast.makeText(LoginActivity.this,getString(R.string.err_unknown),Toast.LENGTH_LONG).show();
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
            if(view.getId()==R.id.edt_password)
            {
                message=getString(R.string.err_password);
            }
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
