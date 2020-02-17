package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.Post;
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

public class SpecficJobFragment extends BaseFragment implements MyLikeAdapter.OnItemClickListener, View.OnClickListener {


    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_minago)
    TextView tv_minago;

    @BindView(R.id.tv_createddateTime)
    TextView tv_createddateTime;

    @BindView(R.id.tv_positionName)
    TextView tv_positionName;

    @BindView(R.id.tv_salaryAmount)
    TextView tv_salaryAmount;

    @BindView(R.id.tv_locationName)
    TextView tv_locationName;

    @BindView(R.id.tv_weeklyHoursNumber)
    TextView tv_weeklyHoursNumber;

    @BindView(R.id.tv_apply)
    TextView tv_apply;

    @BindView(R.id.tv_description)
    TextView tv_description;

    @BindView(R.id.tv_goback)
    TextView tv_goback;

    @BindView(R.id.img_like)
    ImageView img_like;

    @BindView(R.id.img_comment)
    ImageView img_comment;

    @BindView(R.id.img_share)
    ImageView img_share;


    private JobAd jobAd = new JobAd();

    public SpecficJobFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_specfic_job_ad, container, false);
        ButterKnife.bind(this, view);
        tv_apply.setOnClickListener(this);
        tv_goback.setOnClickListener(this);
        init();
        return view;
    }

    private void init() {
        if ((getArguments() != null)) {
            setPageTitle("");
            showBackButton();
            jobAd = (JobAd) getArguments().getSerializable("JOB_AD");
            AppUtils.setGlideImage(getContext(), profile_image, jobAd.getJobImage());
            tv_name.setText(jobAd.getJobTitle());
            tv_positionName.setText(jobAd.getPosition());
            tv_salaryAmount.setText("$" + String.valueOf(jobAd.getSalary()));
            tv_description.setText(jobAd.getContent());
            tv_locationName.setText(jobAd.getLocation());
            tv_weeklyHoursNumber.setText(jobAd.getWeeklyHours());
            tv_createddateTime.setText(DateUtils.getFormattedDateTime(jobAd.getCreatedDatetime()));
            tv_minago.setText(DateUtils.getTimeAgo(jobAd.getCreatedDatetime()));
        }
    }


    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_apply:
                ApplicationFormFragment applicationFormFragment = new ApplicationFormFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("JOB_AD", jobAd);
                applicationFormFragment.setArguments(bundle);
                loadFragment(R.id.framelayout, applicationFormFragment, getContext(), true);
                break;
            case R.id.tv_goback:
                getActivity().onBackPressed();
                break;
        }
    }
}
