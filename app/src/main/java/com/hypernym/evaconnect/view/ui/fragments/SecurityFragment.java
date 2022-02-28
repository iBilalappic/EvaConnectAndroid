package com.hypernym.evaconnect.view.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.view.ui.activities.LoginActivity;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.onesignal.OneSignal;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SecurityFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.ly_change_password)
    LinearLayout ly_change_password;

    @BindView(R.id.ly_notification)
    LinearLayout ly_notification;

    private UserViewModel userViewModel;
    private SimpleDialog simpleDialog;

    public SecurityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        img_backarrow.setOnClickListener(this);
        ly_change_password.setOnClickListener(this);

        // initialize viewmodel
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        /**
         * layout to delete a user
         * **/
        ly_notification.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                simpleDialog = new SimpleDialog(
                        getContext(),
                        AppUtils.getApplicationContext().getResources().getString(R.string.dialog_title),
                        AppUtils.getApplicationContext().getResources().getString(R.string.dialog_message),
                        AppUtils.getApplicationContext().getResources().getString(R.string.button_no),
                        AppUtils.getApplicationContext().getResources().getString(R.string.button_yes),
                        new OnOneOffClickListener() {
                            @Override
                            public void onSingleClick(View v) {
                                switch (v.getId()){
                                    case R.id.button_negative:
                                        simpleDialog.dismiss();
                                        break;
                                    case R.id.button_positive:
                                        simpleDialog.dismiss();
                                        callDeleteUserApi();
                                        break;
                                }
                            }
                        }
                );
                simpleDialog.show();
            }
        });
    }

    private void callDeleteUserApi() {
        try {
            showDialog();
            userViewModel.deleteuser(LoginUtils.getLoggedinUser().getId()).observe(getViewLifecycleOwner(), listBaseModel -> {
                hideDialog();
                if (listBaseModel!=null && !listBaseModel.isError()){
                    logout();
                }
            });
        }
        catch (Exception exception){
            hideDialog();
            exception.printStackTrace();
        }
    }

    private void logout(){
        Toast.makeText(getContext(), getContext().getResources().getString(R.string.success_msg_user_deleted), Toast.LENGTH_LONG).show();
        LoginUtils.clearUser(getContext());
        LoginUtils.removeAuthToken(getContext());
        AppUtils.facebookLogout();
        OneSignal.sendTag("email","null");
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_security, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;
            case R.id.ly_change_password:
                EditProfileChangePasswordFragment editProfileChangePasswordFragment = new EditProfileChangePasswordFragment();
                loadFragment(R.id.framelayout, editProfileChangePasswordFragment, getContext(), true);
                break;
        }
    }
}