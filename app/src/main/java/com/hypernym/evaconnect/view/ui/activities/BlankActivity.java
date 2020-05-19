package com.hypernym.evaconnect.view.ui.activities;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.AccountCheck;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.ui.activities.BaseActivity;
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

public class BlankActivity extends BaseActivity {


    private UserViewModel userViewModel;
    String email,photourl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_blank);
        ButterKnife.bind(this);
        showDialog();
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getApplication(), this)).get(UserViewModel.class);
        email = getIntent().getStringExtra("Email");
        photourl = getIntent().getStringExtra("Photo");
        CheckUserExist(email);
    }

    private void CheckUserExist(String linkedInUserEmailAddress) {
        if (NetworkUtils.isNetworkConnected(this)) {
//            showDialog();
            userViewModel.isEmailExist_linkedin(linkedInUserEmailAddress).observe(this, new Observer<BaseModel<List<AccountCheck>>>() {
                @Override
                public void onChanged(BaseModel<List<AccountCheck>> listBaseModel) {
                    if (listBaseModel != null && !listBaseModel.isError()) {
                        //  if (listBaseModel.getData().get(0).getIsLinkedin() != null && listBaseModel.getData().get(0).getIsLinkedin() == 1) {
                        JustLoginApiCall();
                        //  }
                    } else {
                        Intent intent = new Intent(BlankActivity.this, CreateAccount_1_Activity.class);
                        intent.putExtra("Email", linkedInUserEmailAddress);
                        intent.putExtra("Photo", photourl);
                        intent.putExtra(Constants.ACTIVITY_NAME,"LinkedinActivity");
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void JustLoginApiCall() {

        if (NetworkUtils.isNetworkConnected(this)) {
//            showDialog();
            userViewModel.linkedin_login(email).observe(this, new Observer<BaseModel<List<User>>>() {
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

                    Intent intent = new Intent(BlankActivity.this, HomeActivity.class);
                    // set the new task and clear flags
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }

    }


}
