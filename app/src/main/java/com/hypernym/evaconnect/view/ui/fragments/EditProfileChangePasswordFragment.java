package com.hypernym.evaconnect.view.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EditProfileChangePasswordFragment extends BaseFragment implements View.OnClickListener {

    @NotEmpty
    @BindView(R.id.editText_old_password)
    EditText editText_old_password;

    @NotEmpty
    @BindView(R.id.editText_newpassword)
    EditText editText_newpassword;

    @NotEmpty
    @BindView(R.id.editText_confirmpassword)
    EditText editText_confirmpassword;

    @BindView(R.id.btn_submit)
    TextView btn_submit;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    User user = new User();
    private UserViewModel userViewModel;

    public EditProfileChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);

        ButterKnife.bind(this, view);
        btn_submit.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile_change_password, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                ValidationPassword();
                break;
            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;
        }
    }


    public void ValidationPassword() {

        if (!(TextUtils.isEmpty(editText_old_password.getText())) && !(TextUtils.isEmpty(editText_newpassword.getText()) &&
                !(TextUtils.isEmpty(editText_confirmpassword.getText())))) {
            if (editText_old_password.length() >= 8 && editText_newpassword.length() >= 8 && editText_confirmpassword.length() >= 8) {

                if (editText_newpassword.getText().toString().matches(editText_confirmpassword.getText().toString())) {

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

    private void UpdatePassword(String oldpassword, String Newpassword) {
        showDialog();
        userViewModel.update_password(oldpassword, Newpassword).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    Toast.makeText(getContext(), "" + listBaseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else if (listBaseModel.isError()) {
                    networkResponseDialog(getString(R.string.messages), listBaseModel.getMessage());
                } else {

                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }
}