package com.hypernym.evaconnect.view.ui.activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hypernym.evaconnect.EnterCodeActivity;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
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
import com.ssw.linkedinmanager.dto.LinkedInAccessToken;
import com.ssw.linkedinmanager.dto.LinkedInEmailAddress;
import com.ssw.linkedinmanager.dto.LinkedInUserProfile;
import com.ssw.linkedinmanager.events.LinkedInManagerResponse;
import com.ssw.linkedinmanager.events.LinkedInUserLoginDetailsResponse;
import com.ssw.linkedinmanager.events.LinkedInUserLoginValidationResponse;
import com.ssw.linkedinmanager.ui.LinkedInRequestManager;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements Validator.ValidationListener, LinkedInManagerResponse {
    @BindView(R.id.tv_signup)
    TextView tv_signup;

    @BindView(R.id.tv_forgotpass)
    TextView tv_forgotpass;

    @BindView(R.id.btn_linkedin)
    Button btn_linkedin;

    @BindView(R.id.btn_facebook)
    LoginButton btn_facebook;

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.cbRememberMe)
    CheckBox cbRememberMe;

    @NotEmpty
    @Email
    @BindView(R.id.edt_email)
    EditText edt_email;

    @NotEmpty
    @Password(min = 8)
    @BindView(R.id.edt_password)
    EditText edt_password;

    @BindView(R.id.rootview)
    FrameLayout rootview;


    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private String username, password;

    private Validator validator;
    private UserViewModel userViewModel;
    private User user = new User();
    private SimpleDialog simpleDialog;
    private final String TAG = LoginActivity.class.getSimpleName();
    private CallbackManager facebookCallbackManager;
    String value;

    private LinkedInRequestManager linkedInRequestManager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initFacebookLogin();

        linkedInRequestManager = new LinkedInRequestManager(this, this, "77bx4v2e0qv4yv", "7Wez2dGZP5leqzcb", "https%3A%2F%2Fwww.google.com", true);


        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        initRememberMe();

        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getApplication(), this)).get(UserViewModel.class);
        validator = new Validator(this);
        validator.setValidationListener(this);
        btn_login.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                value = "login";
                validator.validate();
            }
        });

        tv_signup.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                value = "signup";
               // validator.validate();
                Intent intent = new Intent(getBaseContext(), CreateAccount_1_Activity.class);
              //  intent.putExtra("Email", edt_email.getText().toString());
                startActivity(intent);
            }
        });

        tv_forgotpass.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });

        btn_linkedin.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), LinkedinActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

//            startLinkedInSignIn();


        });
        findViewById(R.id.rootview).setOnTouchListener((View v , @SuppressLint("ClickableViewAccessibility") MotionEvent event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(LoginActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return true;
        });

    }

    private void initRememberMe() {

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            edt_email.setText(loginPreferences.getString("username", ""));
            edt_password.setText(loginPreferences.getString("password", ""));
            cbRememberMe.setChecked(true);
        }
    }

    private void initFacebookLogin() {
        facebookCallbackManager = CallbackManager.Factory.create();
        btn_facebook.setPermissions(Arrays.asList(AppConstants.EMAIL, AppConstants.PUBLIC_PROFILE));
       /* btn_facebook.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.com_facebook_button_icon), null, null, null);
        */btn_facebook.setLoginBehavior(LoginBehavior.WEB_ONLY);

        btn_facebook.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("facebook", "onSuccess: " + loginResult.getAccessToken());

                // using the access token call the facebook graph api to get the user's profile information
                requestUserProfile(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.e("facebook", "facebook login cancelled by the user");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                Log.e("facebook", "onError: " + error.getMessage());
            }
        });
    }

    private void requestUserProfile(AccessToken accessToken) {
        if (accessToken!=null)
        {
            GraphRequest request = GraphRequest.newMeRequest(accessToken, (object, response) -> {
                try{
                    /*String first_name = object.getString(AppConstants.FIRST_NAME);
                    String last_name = object.getString(AppConstants.LAST_NAME);*/
                    String email = object.getString(AppConstants.EMAIL);
                    String id = object.getString(AppConstants.ID);
                    String facebook_photo = AppConstants.FACEBOOK_PIC_BASE_URL + id + AppConstants.FACEBOOK_PIC_URL;
//                    String image_path = null;

                    Glide.with(this)
                            .asBitmap()
                            .load(facebook_photo)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

//                                   image_path=ImageFilePathUtil.saveImage(resource);

                                    checkUserExist(email, facebook_photo,ImageFilePathUtil.SaveImage(resource,"Profile.png"));
                                }
                            });


//                    Glide.with(this)
//                            .load("YOUR_URL")
//                            .asBitmap()
//                            .into(new SimpleTarget<Bitmap>(150,150) {
//                                @Override
//                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {
////                                    saveImage(resource);
//
//                                    ImageFilePathUtil.saveImage(resource);
//                                }
//                            });



                    // check whether user already exists or not

                }
                catch(JSONException exc){
                    exc.printStackTrace();
                    Log.e(TAG, "exc: "+ exc.getMessage());
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "first_name, last_name, email, id");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    private void checkUserExist(String email, String facebook_photo,String path)
    {
        if (NetworkUtils.isNetworkConnected(LoginActivity.this))
        {
            userViewModel.isEmailExist_linkedin(email).observe(this, listBaseModel -> {
                if (listBaseModel != null && !listBaseModel.isError())
                {
                    JustLoginApiCall(email);
                }
                else {
                    Intent intent = new Intent(LoginActivity.this, CreateAccount_1_Activity.class);
                    intent.putExtra("Email", email);
                    intent.putExtra("Photo", facebook_photo);
                    intent.putExtra("Path", path);
                    intent.putExtra(Constants.ACTIVITY_NAME, AppConstants.FACEBOOK_LOGIN_TYPE);
                    startActivity(intent);
                }
            });
        }
    }

    private void JustLoginApiCall(String email) {
        userViewModel.facebookLogin(email,Constants.FACEBOOK_TYPE).observe(this, listBaseModel -> {
            LoginUtils.userLoggedIn();
            User userData = listBaseModel.getData().get(0);
            userData.setUser_id(userData.getId());
            LoginUtils.saveUser(listBaseModel.getData().get(0));
            OneSignal.sendTag("email", userData.getEmail());
            UserDetails.username = userData.getFirst_name();
            if (listBaseModel.getData().get(0) != null) {
                LoginUtils.saveUserToken(listBaseModel.getData().get(0).getToken());
            }

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            // set the new task and clear flags
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void saveCredential() {
        username = edt_email.getText().toString();
        password = edt_password.getText().toString();

        if (cbRememberMe.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onValidationSucceeded() {
        user.setUsername(edt_email.getText().toString());
        user.setPassword(edt_password.getText().toString());
        if (NetworkUtils.isNetworkConnected(this)) {
            if (value.equals("login")) {
                showDialog();
                callLoginApi();

            } else {
                isEmailExist();
            }
        } else {
            networkErrorDialog();
        }

    }

    private void isEmailExist() {

        userViewModel.isEmailExist(user.getUsername()).observe(this, listBaseModel -> {
            if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().get(0) != null) {
                networkResponseDialog(getString(R.string.error), listBaseModel.getMessage());
                //  callLoginApi();
            } else {
                hideDialog();

                Intent intent = new Intent(getBaseContext(), CreateAccount_1_Activity.class);
                intent.putExtra("Email", edt_email.getText().toString());
                startActivity(intent);
            }
        });

    }

    private void callLoginApi() {

        userViewModel.login(user).observe(this, user -> {
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
                createUserOnFirebase();
                saveCredential();
                hideDialog();

                if(userData.getStatus()!=null&&userData.getStatus().equalsIgnoreCase("active")){
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    // set the new task and clear flags
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    simpleDialog = new SimpleDialog(LoginActivity.this, "Warning", "Please verify yourself", "Cancel", "Verify", new OnOneOffClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            switch (v.getId()) {
                                case R.id.button_positive:

                                   callVerifyEmail(userData);
                                    break;
                                case R.id.button_negative:
                                    break;
                            }
                            simpleDialog.dismiss();
                        }
                    });
                    simpleDialog.show();

                }

            } else if (user != null && user.isError()) {
                hideDialog();
                networkResponseDialog(getString(R.string.error), user.getMessage());
            } else if (user == null) {
                hideDialog();
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
        });
    }

    private void callVerifyEmail(User userdata) {
        userViewModel.verify_email(userdata.getEmail()).observe(this, user -> {
            Toast.makeText(LoginActivity.this, "EMAIL SENT", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, EnterCodeActivity.class);
            intent.putExtra("Email", userdata.getEmail());
            intent.putExtra("user_type", userdata.getType());
            intent.putExtra(Constants.ACTIVITY_NAME,"");

            startActivity(intent);
        });

    }

    public void createUserOnFirebase()
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        //To do//
                        return;
                    }
                    // Get the Instance ID token//
                    String token = task.getResult().getToken();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference userRef = databaseReference.child(AppConstants.FIREASE_USER_ENDPOINT);
                    DatabaseReference userRefByID = userRef.child(LoginUtils.getLoggedinUser().getId().toString());
                    userRefByID.child("imageName").setValue(LoginUtils.getLoggedinUser().getUser_image());
                    userRefByID.child("name").setValue(LoginUtils.getLoggedinUser().getEmail());
                    userRefByID.child("fcm-token").setValue(token);
                });

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


    private void startLinkedInSignIn() {
        linkedInRequestManager.showAuthenticateView(LinkedInRequestManager.MODE_BOTH_OPTIONS);

        //LinkedInRequestManager.MODE_BOTH_OPTIONS - can get email and user profile data with user profile image
        //LinkedInRequestManager.MODE_EMAIL_ADDRESS_ONLY - can get only the user profile email address
        //LinkedInRequestManager.MODE_LITE_PROFILE_ONLY - can get the user profile details with profile image
    }

    private void closeAuthenticationView() {
        linkedInRequestManager.dismissAuthenticateView();
    }


    @Override
    public void onGetAccessTokenFailed() {

        Log.d("linkedin", "onGetAccessTokenFailed: ");
    }

    @Override
    public void onGetAccessTokenSuccess(LinkedInAccessToken linkedInAccessToken) {
        Log.d("linkedin", "onGetAccessTokenSuccess: ");
    }

    @Override
    public void onGetCodeFailed() {
        Log.d("linkedin", "onGetCodeFailed: ");
    }

    @Override
    public void onGetCodeSuccess(String code) {
        Log.d("linkedin", "onGetCodeSuccess: ");
    }

    @Override
    public void onGetProfileDataFailed() {
        Log.d("linkedin", "onGetProfileDataFailed: ");
    }

    @Override
    public void onGetProfileDataSuccess(LinkedInUserProfile linkedInUserProfile) {

        Log.d("linkedin", "onGetProfileDataSuccess: " + linkedInUserProfile.getUserName().getFirstName().getLocalized().getEn_US());
        Log.d("linkedin", "onGetProfileDataSuccess: " + linkedInUserProfile.getUserName().getLastName().getLocalized().getEn_US());
        Log.d("linkedin", "onGetProfileDataSuccess: " + linkedInUserProfile.getImageURL().toString());


        linkedInRequestManager.dismissAuthenticateView();
    }

    @Override
    public void onGetEmailAddressFailed() {

    }

    @Override
    public void onGetEmailAddressSuccess(LinkedInEmailAddress linkedInEmailAddress) {
        Log.d("linkedin", "onGetEmailAddressSuccess: ");

    }

    private void isUserLoggedIn() {
        linkedInRequestManager.isLoggedIn(new LinkedInUserLoginValidationResponse() {
            @Override
            public void activeLogin() {
                //Session token is active. can use to get data from linkedin
            }

            @Override
            public void tokenExpired() {
                //token has been expired. need to obtain a new code
            }

            @Override
            public void notLogged() {
                //user is not logged into the application
            }
        });
    }

    private void logout() {
        //logout the user
        linkedInRequestManager.logout();
    }


    private void checkUserLoggedPermissions() {
        linkedInRequestManager.getLoggedRequestedMode(new LinkedInUserLoginDetailsResponse() {
            @Override
            public void loggedMode(int mode) {
                //user is already logged in. active token. mode is available
                switch (mode) {
                    case LinkedInRequestManager.MODE_LITE_PROFILE_ONLY:
                        break;

                    case LinkedInRequestManager.MODE_EMAIL_ADDRESS_ONLY:
                        break;

                    case LinkedInRequestManager.MODE_BOTH_OPTIONS:
                        break;
                }
            }

            @Override
            public void tokenExpired() {
                //token has been expired. need to obtain a new code
            }

            @Override
            public void notLogged() {
                //user is not logged into the application
            }
        });
    }

}
