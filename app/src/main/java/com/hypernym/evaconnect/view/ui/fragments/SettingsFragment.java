package com.hypernym.evaconnect.view.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends BaseFragment implements View.OnClickListener {


    @NotEmpty
    @BindView(R.id.edt_email)
    EditText edt_email;

    @BindView(R.id.tv_password)
    TextView tv_password;

    @BindView(R.id.tv_pushNotification)
    TextView tv_pushNotification;


    @BindView(R.id.tv_text_manage)
    TextView tv_text_manage;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;
    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_location)
    TextView tv_location;

    @BindView(R.id.btn_save)
    TextView btn_save;


    @BindView(R.id.profile_image)
    CircleImageView cv_profile_image;

    User user = new User();
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
        showBackButton();
        setPageTitle("Edit Settings");
//        switch_push.setOnClickListener(this);
//
//        switch_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    notification_check = 1;
//                }else{
//                    notification_check = 0;
//                }
//
//            }
//        });
        btn_save.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {

            }
        });

        tv_password.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                ShowConfirmationDialog();
            }
        });
        tv_pushNotification.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                PushNotificationsFragment pushNotificationsFragment = new PushNotificationsFragment();
                loadFragment(R.id.framelayout, pushNotificationsFragment, getContext(), true);
            }
        });


        user = LoginUtils.getUser();
        SettingUserProfile(user);
        return view;
    }

    private void SettingUserProfile(User user) {
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);
        GetUserDetails();
    }

    private void GetUserDetails() {
        showDialog();
        userViewModel.getuser_details(user.getId()).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    if (!TextUtils.isEmpty(listBaseModel.getData().get(0).getUser_image())) {
                        AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getUser_image());
                        tv_password.setEnabled(false);
                    }
//                    else if (listBaseModel.getData().get(0).getIs_facebook() == 1 && !TextUtils.isEmpty(listBaseModel.getData().get(0).getFacebook_image_url())) {
//                        AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getFacebook_image_url());
//                        tv_password.setEnabled(false);
//                    } else {
//                        AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getUser_image());
//                    }

                    edt_email.setText(listBaseModel.getData().get(0).getEmail());
                    tv_location.setText(listBaseModel.getData().get(0).getCountry() + "," + listBaseModel.getData().get(0).getCity());
                    tv_name.setText(listBaseModel.getData().get(0).getFirst_name());
                    notification_check = listBaseModel.getData().get(0).getIs_notifications();

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
                        if(editText_old_password.length()< 8)
                        {
                            editText_old_password.setError(getString(R.string.err_password));
                        }
                        if(editText_newpassword.length()<8)
                        {
                            editText_newpassword.setError(getString(R.string.err_password));
                        }
                      if(editText_confirmpassword.length()<8)
                        {
                            editText_confirmpassword.setError(getString(R.string.err_password));
                        }

                    }
                } else {
                    if(editText_old_password.length()< 8)
                    {
                        editText_old_password.setError(getString(R.string.err_password));
                    }
                     if(editText_newpassword.length()<8)
                    {
                        editText_newpassword.setError(getString(R.string.err_password));
                    }
                    if(editText_confirmpassword.length()<8)
                    {
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
        switch (v.getId()) {
            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;
        }
    }
}

