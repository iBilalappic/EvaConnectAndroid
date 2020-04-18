package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.PostViewModel;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends BaseFragment implements TextInputEditText.OnTouchListener, Validator.ValidationListener {
    @NotEmpty
    @BindView(R.id.tv_location)
    TextInputEditText tv_location;

    @NotEmpty
    @BindView(R.id.tv_designation)
    TextInputEditText tv_designation;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_connect)
    TextView tv_connect;

    @NotEmpty
    @BindView(R.id.tv_biodata)
    TextInputEditText tv_biodata;


    @NotEmpty
    @BindView(R.id.tv_field)
    TextInputEditText tv_field;

    @NotEmpty
    @BindView(R.id.tv_company)
    TextInputEditText tv_company;

    @BindView(R.id.update)
    TextView update;


    private UserViewModel userViewModel;


    @BindView(R.id.profile_image)
    CircleImageView cv_profile_image;

    User user = new User();
    private Validator validator;
    SimpleDialog simpleDialog;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        validator = new Validator(this);
        validator.setValidationListener(this);
        tv_designation.setOnTouchListener(this);
        user = LoginUtils.getUser();
        SettingUserProfile(user);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        return view;
    }

    private void SettingUserProfile(User user) {
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);
        GetUserDetails();

//        if (user != null) {
//            Log.d("USERDATA", "" + GsonUtils.toJson(user));
//            AppUtils.setGlideImage(getContext(), cv_profile_image, user.getUser_image());
//            tv_name.setText(user.getFirst_name());
//            tv_biodata.setText(user.getBio_data());
//            //  tv_location.setText(user.getBio_data());
//            // tv_designation.setText();
//        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;

        switch (v.getId()) {
            case R.id.tv_designation:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (tv_designation.getRight() - tv_designation.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        tv_designation.setEnabled(true);
                        return true;
                    }
                }
                break;
            case R.id.tv_location:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (tv_location.getRight() - tv_location.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        return true;
                    }
                }
                break;


        }


        return false;
    }

    @Override
    public void onValidationSucceeded() {
        UpdateProfileUser();
    }

    private void UpdateProfileUser() {
        showDialog();
        userViewModel.profile_update(
                user.getId(),
                tv_designation.getText().toString(),
                tv_location.getText().toString(),
                tv_biodata.getText().toString(),
                tv_company.getText().toString(),
                tv_field.getText().toString()
        ).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> listBaseModel) {
                if (!listBaseModel.isError()) {
                    simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), "Profile updated successfully", null, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            simpleDialog.dismiss();
                        }
                    });
                    GetUserDetails();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
                simpleDialog.show();
                simpleDialog.setCancelable(false);
            }
        });
    }

    private void GetUserDetails() {
        showDialog();
        userViewModel.getuser_details(user.getId()
        ).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    tv_biodata.setText(listBaseModel.getData().get(0).getBio_data());
                    tv_designation.setText(listBaseModel.getData().get(0).getDesignation());
                    tv_field.setText(listBaseModel.getData().get(0).getAddress());
                    tv_location.setText(listBaseModel.getData().get(0).getField());
                    tv_company.setText(listBaseModel.getData().get(0).getCompany_name());
                    tv_name.setText(listBaseModel.getData().get(0).getFirst_name());
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
