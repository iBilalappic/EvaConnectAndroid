package com.hypernym.evaconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.ui.activities.BaseActivity;
import com.hypernym.evaconnect.view.ui.activities.EmailVerification;
import com.hypernym.evaconnect.view.ui.activities.HomeActivity;
import com.hypernym.evaconnect.view.ui.activities.LoginActivity;
import com.hypernym.evaconnect.view.ui.activities.NewsActivity;
import com.hypernym.evaconnect.view.ui.activities.PasswordActivity;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.onesignal.OneSignal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EnterCodeActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.img_cross)
    ImageView img_cross;


    @BindView(R.id.tv_already_account)
    TextView tv_already_account;

    @BindView(R.id.btn_next)
    TextView btn_next;

    @BindView(R.id.ed_code)
    EditText ed_code;

    String email, user_type, activityName;
    private UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);
        ButterKnife.bind(this);
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getApplication(), this)).get(UserViewModel.class);
        init();
    }

    private void init() {
        if (getIntent()!=null && getIntent().getStringExtra(Constants.ACTIVITY_NAME).isEmpty()) {
            email = getIntent().getStringExtra("Email");
            user_type = getIntent().getStringExtra("user_type");
        }else if(getIntent()!=null && !getIntent().getStringExtra(Constants.ACTIVITY_NAME).isEmpty()){
            activityName = getIntent().getStringExtra(Constants.ACTIVITY_NAME);
            email = getIntent().getStringExtra("Email");

        }
        ed_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==4){
                   btn_next.setEnabled(true);
                   btn_next.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.skyblue)));
                }else{
                    btn_next.setEnabled(false);
                    btn_next.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        btn_next.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
              /*  if (activityName!=null && activityName.equalsIgnoreCase(Constants.FORGOT_PASSWORD) ) {
                    navigateNewPasswordActivity();
                } else {*/
                    callEmailVerificationCodeApi();
               // }
            }
        });
    }

    private void navigateNewPasswordActivity() {
        Intent intent = new Intent(this, PasswordActivity.class);
        intent.putExtra(Constants.ACTIVITY_NAME,Constants.FORGOT_PASSWORD);
        intent.putExtra("Email",email);
        intent.putExtra("Code",ed_code.getText().toString());
        startActivity(intent);
    }

    private void goToNextScreen() {
        Intent intent;
            if (user_type.equalsIgnoreCase("user")) {
                intent = new Intent(EnterCodeActivity.this, NewsActivity.class);
                LoginUtils.userLoggedIn();

            } else {
                intent = new Intent(EnterCodeActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                LoginUtils.userLoggedIn();

            }
            startActivity(intent);
    }

    private void callEmailVerificationCodeApi() {

        userViewModel.getEmailVerificationCode(email, Integer.parseInt(ed_code.getText().toString())).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> response) {
                if (response != null && !response.isError()) {
                    if (activityName!=null && activityName.equalsIgnoreCase(Constants.FORGOT_PASSWORD)) {
                       navigateNewPasswordActivity();
                    } else {
                        goToNextScreen();
                    }
                } else if (response != null && response.isError()) {
                    hideDialog();
                    networkResponseDialog(getString(R.string.error), response.getMessage());
                } else if (response == null) {
                    hideDialog();
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
            }
        });
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
                Intent intent = new Intent(EnterCodeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}