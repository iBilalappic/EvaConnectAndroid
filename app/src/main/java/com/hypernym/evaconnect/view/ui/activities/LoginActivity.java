package com.hypernym.evaconnect.view.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
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

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements Validator.ValidationListener {
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


    private Validator validator;
    private UserViewModel userViewModel;
    private User user = new User();
    private SimpleDialog simpleDialog;
    private final String TAG = LoginActivity.class.getSimpleName();
    private CallbackManager facebookCallbackManager;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initFacebookLogin();

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

        btn_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LinkedinActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        findViewById(R.id.rootview).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                View view = getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(LoginActivity.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return true;
            }
        });

    }

    private void initFacebookLogin() {
        facebookCallbackManager = CallbackManager.Factory.create();
        btn_facebook.setPermissions(Arrays.asList(AppConstants.EMAIL, AppConstants.PUBLIC_PROFILE));
        btn_facebook.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btn_facebook.setLoginBehavior(LoginBehavior.WEB_ONLY);

        btn_facebook.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "onSuccess: " + loginResult.getAccessToken());

                // using the access token call the facebook graph api to get the user's profile information
                requestUserProfile(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "facebook login cancelled by the user");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                Log.e(TAG, "onError: " + error.getMessage());
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

//                                    image_path=ImageFilePathUtil.saveImage(resource);

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

        userViewModel.isEmailExist(user.getUsername()).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().get(0) != null) {
                    networkResponseDialog(getString(R.string.error),listBaseModel.getMessage());
                      //  callLoginApi();
                } else {
                    hideDialog();

                    Intent intent = new Intent(getBaseContext(), CreateAccount_1_Activity.class);
                    intent.putExtra("Email", edt_email.getText().toString());
                    startActivity(intent);
                }
            }

        });

    }

    private void callLoginApi() {

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
                    createUserOnFirebase();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    // set the new task and clear flags
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (user != null && user.isError()) {
                    hideDialog();
                    networkResponseDialog(getString(R.string.error), user.getMessage());
                } else if (user == null) {
                    hideDialog();
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
            }
        });
    }
    public void createUserOnFirebase()
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //To do//
                            return;
                        }
                        // Get the Instance ID token//
                        String token = task.getResult().getToken();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference userRef = databaseReference.child(AppConstants.FIREASE_USER_ENDPOINT);
                        DatabaseReference userRefByID=userRef.child(LoginUtils.getLoggedinUser().getId().toString());
                        userRefByID.child("imageName").setValue(LoginUtils.getLoggedinUser().getUser_image());
                        userRefByID.child("name").setValue(LoginUtils.getLoggedinUser().getEmail());
                        userRefByID.child("fcm-token").setValue(token);
                    }
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
}
