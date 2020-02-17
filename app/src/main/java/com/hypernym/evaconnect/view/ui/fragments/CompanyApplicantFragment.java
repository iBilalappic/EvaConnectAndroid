package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.adapters.MyLikeAdapter;
import com.hypernym.evaconnect.viewmodel.MylikesViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CompanyApplicantFragment extends BaseFragment implements View.OnClickListener {


    public CompanyApplicantFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    @BindView(R.id.tv_positionName)
    TextView tv_positionName;

    @BindView(R.id.tv_salaryAmount)
    TextView tv_salaryAmount;

    @BindView(R.id.tv_name)
    TextView tv_name;


    @BindView(R.id.tv_locationName)
    TextView tv_locationName;

    @BindView(R.id.tv_weeklyHoursNumber)
    TextView tv_weeklyHoursNumber;

    @BindView(R.id.tv_edit)
    TextView tv_edit;


    private CompanyJobAdModel companyJobAdModel = new CompanyJobAdModel();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_companyjobad, container, false);
        ButterKnife.bind(this, view);
        tv_edit.setOnClickListener(this);
        init();
        return view;
    }

    private void init() {
        if ((getArguments() != null)) {
            setPageTitle("");
            showBackButton();
            companyJobAdModel = (CompanyJobAdModel) getArguments().getSerializable("COMPANY_AD");
            AppUtils.setGlideImage(getContext(), profile_image, companyJobAdModel.getJobImage());
            tv_positionName.setText(companyJobAdModel.getPosition());
            tv_name.setText(companyJobAdModel.getJobTitle());
            tv_salaryAmount.setText("$" + String.valueOf(companyJobAdModel.getSalary()));
            tv_locationName.setText(companyJobAdModel.getLocation());
            tv_weeklyHoursNumber.setText(companyJobAdModel.getWeeklyHours());
//            tv_minago.setText(DateUtils.getTimeAgo(jobAd.getCreatedDatetime()));
        }
    }

    private void setupRecyclerview() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit:
                JobCreateFragment jobCreateFragment = new JobCreateFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("COMPANY_AD", companyJobAdModel);
                jobCreateFragment.setArguments(bundle);
                loadFragment(R.id.framelayout, jobCreateFragment, getContext(), true);
                break;
        }
    }
}
