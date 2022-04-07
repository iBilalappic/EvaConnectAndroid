package com.hypernym.evaconnect.view.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.ly_profile)
    LinearLayout ly_profile;


    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.ly_notification)
    LinearLayout ly_notification;

    @BindView(R.id.ly_term_condition)
    LinearLayout ly_term_condition;

    @BindView(R.id.ly_cookie)
    LinearLayout ly_cookie;

    @BindView(R.id.ly_help)
    LinearLayout ly_help;

    @BindView(R.id.ly_security)
    LinearLayout ly_security;

    @BindView(R.id.ly_news)
    LinearLayout ly_news;

    @BindView(R.id.view17)
    View view;

    @BindView(R.id.ly_language_region)
    LinearLayout ly_language_region;


    @BindView(R.id.layout_location)
    LinearLayout layout_location;


    User user = new User();
    User userData = new User();
    private UserViewModel userViewModel;

    private Validator validator;
    Integer notification_check = 0;

    Dialog mdialog;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        img_backarrow.setOnClickListener(this);
        ly_profile.setOnClickListener(this);
        ly_notification.setOnClickListener(this);
        ly_help.setOnClickListener(this);
        ly_term_condition.setOnClickListener(this);
        ly_cookie.setOnClickListener(this);
        ly_security.setOnClickListener(this);
        ly_news.setOnClickListener(this);
        ly_language_region.setOnClickListener(this);
        layout_location.setOnClickListener(this);

        user = LoginUtils.getUser();
/*
        if(user.getType().equalsIgnoreCase("company")){
            ly_news.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }*/
        SettingUserProfile(user);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SettingUserProfile(user);

        Log.d("setting_user", user.getLanguage());

    }

    private void SettingUserProfile(User user) {
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(requireActivity().getApplication())).get(UserViewModel.class);
        GetUserDetails();
    }

    private void GetUserDetails() {
        showDialog();
        userViewModel.getuser_details(user.getId()).observe(getViewLifecycleOwner(), new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    userData = listBaseModel.getData().get(0);
                    LoginUtils.saveUser(listBaseModel.getData().get(0));
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }


    private void ShowConfirmationDialog() {
        mdialog = new Dialog(getContext());
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setCancelable(true);
        mdialog.setContentView(R.layout.dialog_confirm_password);
        EditText editText_confirmpassword = mdialog.findViewById(R.id.edt_confirmpassword);
        EditText editText_newpassword = mdialog.findViewById(R.id.edt_newpassword);
        EditText editText_old_password = mdialog.findViewById(R.id.edt_oldpassword);
        TextView tv_confirm = mdialog.findViewById(R.id.btn_confirmPassword);


        tv_confirm.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {

                if (!(TextUtils.isEmpty(editText_old_password.getText())) && !(TextUtils.isEmpty(editText_newpassword.getText()) &&
                        !(TextUtils.isEmpty(editText_confirmpassword.getText())))) {
                    if (editText_old_password.length() >= 8 && editText_newpassword.length() >= 8 && editText_confirmpassword.length() >= 8) {

                        if (editText_newpassword.getText().toString().matches(editText_confirmpassword.getText().toString())) {
                            mdialog.dismiss();
                            UpdatePassword(editText_old_password.getText().toString(), editText_newpassword.getText().toString());
                        } else {
                            Toast.makeText(getContext(), "password didn't match", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if (editText_old_password.length() < 8) {
                            editText_old_password.setError(getString(R.string.err_password));
                        }
                        if (editText_newpassword.length() < 8) {
                            editText_newpassword.setError(getString(R.string.err_password));
                        }
                        if (editText_confirmpassword.length() < 8) {
                            editText_confirmpassword.setError(getString(R.string.err_password));
                        }

                    }
                } else {
                    if (editText_old_password.length() < 8) {
                        editText_old_password.setError(getString(R.string.err_password));
                    }
                    if (editText_newpassword.length() < 8) {
                        editText_newpassword.setError(getString(R.string.err_password));
                    }
                    if (editText_confirmpassword.length() < 8) {
                        editText_confirmpassword.setError(getString(R.string.err_password));
                    }

                }
            }
        });
        mdialog.show();
    }

    private void UpdatePassword(String oldpassword, String Newpassword) {
        userViewModel.update_password(oldpassword, Newpassword).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    mdialog.dismiss();
                    Toast.makeText(getContext(), "" + listBaseModel.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (listBaseModel.isError()) {
                    networkResponseDialog(getString(R.string.messages), listBaseModel.getMessage());
                } else {
                    mdialog.dismiss();
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        TermConditionHelpPolicyFragment termConditionHelpPolicyFragment = new TermConditionHelpPolicyFragment();
        switch (v.getId()) {
            case R.id.ly_profile:
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                loadFragment(R.id.framelayout, editProfileFragment, getContext(), true);
                break;

            case R.id.ly_notification:
                PushNotificationsFragment pushNotificationsFragment = new PushNotificationsFragment();
                loadFragment(R.id.framelayout, pushNotificationsFragment, getContext(), true);
                break;

            case R.id.ly_help:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FRAGMENT_NAME, Constants.HELP);
                loadFragment_bundle(R.id.framelayout, termConditionHelpPolicyFragment, getContext(), true, bundle);
                break;

            case R.id.ly_cookie:
                Bundle bundle2 = new Bundle();
                bundle2.putString(Constants.FRAGMENT_NAME, Constants.COOKIES_POLICY);
                loadFragment_bundle(R.id.framelayout, termConditionHelpPolicyFragment, getContext(), true, bundle2);
                break;

            case R.id.ly_term_condition:
                Bundle bundle3 = new Bundle();
                bundle3.putString(Constants.FRAGMENT_NAME, Constants.TERMS_AND_CONDITION);
                loadFragment_bundle(R.id.framelayout, termConditionHelpPolicyFragment, getContext(), true, bundle3);
                break;

            case R.id.ly_security:
                SecurityFragment securityFragment = new SecurityFragment();
                loadFragment(R.id.framelayout, securityFragment, getContext(), true);
                break;

            case R.id.ly_news:
                RssNewsFragment rssNewsFragment = new RssNewsFragment();
                loadFragment(R.id.framelayout, rssNewsFragment, getContext(), true);
                break;
            case R.id.layout_location:

            case R.id.ly_language_region:
                LanguageFragment languageFragment = new LanguageFragment(userData);
                loadFragment(R.id.framelayout, languageFragment, getContext(), true);
                break;


            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;
        }
    }
}

