package com.hypernym.evaconnect.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernym.evaconnect.EnterCodeActivity;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmailVerification extends BaseActivity implements View.OnClickListener  {

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.img_cross)
    ImageView img_cross;


    @BindView(R.id.tv_already_account)
    TextView tv_already_account;

    @BindView(R.id.btn_next)
    TextView btn_next;

    String email, user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        if (getIntent()!=null) {
            email = getIntent().getStringExtra("Email");
            user_type = getIntent().getStringExtra("user_type");
        }
        btn_next.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                        Intent intent = new Intent(EmailVerification.this, EnterCodeActivity.class);
                        intent.putExtra("Email", email);
                        intent.putExtra("user_type", user_type);
                        startActivity(intent);
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
                Intent intent = new Intent(EmailVerification.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}