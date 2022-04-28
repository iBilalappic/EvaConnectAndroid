package com.hypernym.evaconnect.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.onesignal.OneSignal;

import butterknife.ButterKnife;

public class BlankActivity extends BaseActivity {


    private UserViewModel userViewModel;
    String email, photourl, path, fName, lName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_blank);
        ButterKnife.bind(this);
        showDialog();
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getApplication(), this)).get(UserViewModel.class);
        email = getIntent().getStringExtra("Email");
        photourl = getIntent().getStringExtra("Photo");
        path = getIntent().getStringExtra("Path");
        fName = getIntent().getStringExtra("firstName");
        lName = getIntent().getStringExtra("lastName");
        CheckUserExist(email);
    }

    private void CheckUserExist(String linkedInUserEmailAddress) {
        if (NetworkUtils.isNetworkConnected(this)) {
//            showDialog();
            userViewModel.isEmailExist_linkedin(linkedInUserEmailAddress).observe(this, listBaseModel -> {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    //  if (listBaseModel.getData().get(0).getIsLinkedin() != null && listBaseModel.getData().get(0).getIsLinkedin() == 1) {
                    JustLoginApiCall();
                    //  }
                } else {
                    Intent intent = new Intent(BlankActivity.this, CreateAccount_1_Activity.class);
                    intent.putExtra("Email", linkedInUserEmailAddress);
                    intent.putExtra("Photo", photourl);
                    intent.putExtra("Path", path);
                    Log.d("sign_up", "Blank Activity photo url " + photourl);
                    Log.d("sign_up", "Blank Activity path " + path);
                    intent.putExtra("firstName", fName);
                    intent.putExtra("lastName", lName);
                    intent.putExtra(Constants.ACTIVITY_NAME, "LinkedinActivity");
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void JustLoginApiCall() {

        if (NetworkUtils.isNetworkConnected(this)) {
//            showDialog();
            userViewModel.linkedin_login(email, Constants.LINKEDIN_TYPE).observe(this, listBaseModel -> {

                if (!listBaseModel.isError()) {
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
                } else {
                    UserExistFbLinkedin(getString(R.string.error), listBaseModel.getMessage());


                }
            });
        }

    }


}
