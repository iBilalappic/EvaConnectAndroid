package com.hypernym.evaconnect.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
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
    @Password(min=8)
    @BindView(R.id.edt_password)
    EditText edt_password;

    private Validator validator;
    private UserViewModel userViewModel;

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
        userViewModel = ViewModelProviders.of(this,new CustomViewModelFactory(getApplication(),this)).get(UserViewModel.class);
        edt_email.setText(getIntent().getStringExtra("Email"));
        edt_password.setText(getIntent().getStringExtra("Password"));
        btn_next.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                validator.validate();
            }
        });
    }


    @Override
    public void onValidationSucceeded() {

        if(NetworkUtils.isNetworkConnected(this)) {
            showDialog();
            userViewModel.isEmailExist(edt_email.getText().toString()).observe(this, new Observer<BaseModel<List<User>>>() {
                @Override
                public void onChanged(BaseModel<List<User>> listBaseModel) {
                    if (listBaseModel != null && listBaseModel.isError()) {
                        Intent intent = new Intent(SignupActivity.this, SignupDetailsActivity.class);
                        intent.putExtra("Email", edt_email.getText().toString());
                        intent.putExtra("Password", edt_password.getText().toString());
                        startActivity(intent);
                    } else if (!listBaseModel.isError()) {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_email_already_exist));
                    } else {
                       networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
                    }
                    hideDialog();
                }
            });
        }
        else
        {
            networkErrorDialog();
        }


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
