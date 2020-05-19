package com.hypernym.evaconnect.view.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.utils.PrefUtils;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
                validator.validate();
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
                    String first_name = object.getString(AppConstants.FIRST_NAME);
                    String last_name = object.getString(AppConstants.LAST_NAME);
                    String email = object.getString(AppConstants.EMAIL);
                    String id = object.getString(AppConstants.ID);
                    String facebook_photo = AppConstants.FACEBOOK_PIC_BASE_URL + id + AppConstants.FACEBOOK_PIC_URL;

                    Log.e(TAG, "onCompleted: " + first_name + " " + last_name + " " + email + " " + id + " " + facebook_photo);

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
                Intent intent = new Intent(getBaseContext(), CreateAccount_1_Activity.class);
                intent.putExtra("Email", edt_email.getText().toString());
                startActivity(intent);
            }
        } else {
            networkErrorDialog();
        }

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
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    // set the new task and clear flags
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (user != null && user.isError()) {
                    hideDialog();
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_login));
                } else if (user == null) {
                    hideDialog();
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
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
