package com.hypernym.evaconnect.view.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;


public class EditProfileJobTitleFragment extends BaseFragment implements  View.OnClickListener {


    @BindView(R.id.edt_jobtitle)
    EditText edt_jobtitle;

    @BindView(R.id.btn_save)
    TextView btn_save;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    private MultipartBody.Part partImage;

    String city,country, first_name, sector, designation, company = "";

    private UserViewModel userViewModel;
    User user = new User();
    User userData = new User();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile_job_title, container, false);
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);
        ButterKnife.bind(this, view);
        btn_save.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);
        init();

        return view;
    }

    private void init() {

        if ((getArguments() != null)) {
            userData = getArguments().getParcelable(Constants.USER);
            edt_jobtitle.setText(userData.getDesignation().toString());
        }
    }

    private void editJobTitle() {
        showDialog();
        userData.setDesignation(edt_jobtitle.getText().toString());

        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("designation", userData.getDesignation());
        body.put("modified_by_id", userData.getId());
        body.put("modified_datetime", DateUtils.GetCurrentdatetime());
        userViewModel.editProfile(body, userData.getId()).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> listBaseModel) {
                if (!listBaseModel.isError()) {
                    getActivity().onBackPressed();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.img_backarrow){
            getActivity().onBackPressed();
        }else if (v.getId()==R.id.btn_save){

            if (!edt_jobtitle.getText().toString().isEmpty()) {
                editJobTitle();
            }
        }

    }

}