package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        btn_save.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile_job_title, container, false);
    }

    private void UpdateProfileUser() {
        showDialog();
        userViewModel.profile_update(
                user.getId(),
                designation,
                company,
                first_name,partImage).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> listBaseModel) {
                if (!listBaseModel.isError()) {
                    //tv_updated.setVisibility(View.VISIBLE);
                    // initHandler();
                    GetUserDetails();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void GetUserDetails() {
        showDialog();
        userViewModel.getuser_details(user.getId()
        ).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel)
            {
                if (listBaseModel.getData() != null && !listBaseModel.isError())
                {
/*                    if (!TextUtils.isEmpty(listBaseModel.getData().get(0).getUser_image()))
                    {
                        AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getUser_image());
                    }*/
//                    else if (listBaseModel.getData().get(0).getIs_facebook() == 1 && !TextUtils.isEmpty(listBaseModel.getData().get(0).getFacebook_image_url())) {
//                        AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getFacebook_image_url());
//                    }
//                    else {
//                        AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getUser_image());
//                    }

                    city = listBaseModel.getData().get(0).getCountry();
                    country = listBaseModel.getData().get(0).getCity();
                    first_name = listBaseModel.getData().get(0).getFirst_name();

                    if (listBaseModel.getData().get(0).getDesignation()!=null
                            && listBaseModel.getData().get(0).getDesignation().isEmpty()) {
                        edt_jobtitle.setText(listBaseModel.getData().get(0).getDesignation());
                    }

                    if(listBaseModel.getData().get(0).getSector().equalsIgnoreCase("Other"))
                    {
                        sector = listBaseModel.getData().get(0).getOther_sector();
                    }
                    else
                    {
                        sector = listBaseModel.getData().get(0).getSector();
                    }

                    if(listBaseModel.getData().get(0).getType().equalsIgnoreCase("company"))
                    {
                        /*lbl_title.setVisibility(View.GONE);
                        edt_designation.setVisibility(View.GONE);
                        img_view2.setVisibility(View.GONE);*/
                    }
                    else
                    {
                        edt_jobtitle.setText(listBaseModel.getData().get(0).getDesignation());
/*                        lbl_title.setVisibility(View.VISIBLE);
                        edt_designation.setVisibility(View.VISIBLE);
                        img_view2.setVisibility(View.VISIBLE);*/
                    }


                    company = listBaseModel.getData().get(0).getCompany_name();
                    first_name = listBaseModel.getData().get(0).getFirst_name();
                    LoginUtils.saveUser(listBaseModel.getData().get(0));
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
        }

    }


}