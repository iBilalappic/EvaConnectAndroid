package com.hypernym.evaconnect.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPassword extends BaseActivity implements Validator.ValidationListener {

    @NotEmpty
    @Email
    @BindView(R.id.edt_email)
    EditText edt_email;

    @BindView(R.id.btn_reset)
    Button btn_reset;

    @BindView(R.id.layout_arrow)
    LinearLayout layout_arrow;


    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;



    private Validator validator;
    private UserViewModel userViewModel;
    private SimpleDialog simpleDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        userViewModel = ViewModelProviders.of(this,new CustomViewModelFactory(getApplication(),this)).get(UserViewModel.class);
        validator = new Validator(this);
        validator.setValidationListener(this);

        layout_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        if(NetworkUtils.isNetworkConnected(this))
        {
            showDialog();
            callForgotPassApi();
        }
        else
        {
            networkErrorDialog();
        }

    }

    private void callForgotPassApi() {
        showDialog();
        userViewModel.forgotPassword(edt_email.getText().toString()).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> user) {
                if(user !=null && !user.isError())
                {
                  networkResponseDialog(getString(R.string.success),getString(R.string.msg_forgotpass));
                }
                 else if(user.isError())
                {
                    networkResponseDialog(getString(R.string.error),user.getMessage());
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

    @OnClick(R.id.btn_reset)
    public void btn_reset()
    {
        validator.validate();
    }


}
