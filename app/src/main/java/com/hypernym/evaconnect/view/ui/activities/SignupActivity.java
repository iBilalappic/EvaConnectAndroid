package com.hypernym.evaconnect.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hypernym.evaconnect.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends BaseActivity implements Validator.ValidationListener  {

    @BindView(R.id.btn_signup)
    Button btn_next;

    @NotEmpty
    @Email
    @BindView(R.id.edt_email)
    EditText edt_email;

    @NotEmpty
    @Password(min=4)
    @BindView(R.id.edt_password)
    EditText edt_password;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @OnClick(R.id.btn_signup)
    public void next()
    {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        Intent intent=new Intent(SignupActivity.this,SignupDetailsActivity.class);
        intent.putExtra("Email",edt_email.getText().toString());
        intent.putExtra("Password",edt_password.getText().toString());
        startActivity(intent);
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
