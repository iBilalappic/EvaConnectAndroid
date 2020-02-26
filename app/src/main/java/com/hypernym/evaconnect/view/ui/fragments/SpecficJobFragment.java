package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.SpecficJobAd;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.MyLikeAdapter;
import com.hypernym.evaconnect.viewmodel.JobListViewModel;
import com.hypernym.evaconnect.viewmodel.MylikesViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SpecficJobFragment extends BaseFragment implements MyLikeAdapter.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private JobListViewModel jobListViewModel;
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

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;


    private JobAd jobAd = new JobAd();
    private SpecficJobAd checkLikeCount = new SpecficJobAd();
    User user;

    public SpecficJobFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_specfic_job_ad, container, false);
        ButterKnife.bind(this, view);
        tv_apply.setOnClickListener(this);
        tv_goback.setOnClickListener(this);
        img_like.setOnClickListener(this);
        swipeRefresh.setOnRefreshListener(this);
        init();
        return view;
    }

    private void init() {
        jobListViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(JobListViewModel.class);
        user = LoginUtils.getUser();
        if ((getArguments() != null)) {
            setPageTitle("");
            showBackButton();
            jobAd = (JobAd) getArguments().getSerializable("JOB_AD");
            GetJob_id(jobAd.getId());
            Log.d("TAAAG", "" + GsonUtils.toJson(jobAd));
            AppUtils.setGlideImage(getContext(), profile_image, jobAd.getJobImage());
            tv_name.setText(jobAd.getJobTitle());
            tv_positionName.setText(jobAd.getPosition());
            tv_salaryAmount.setText("$" + String.valueOf(jobAd.getSalary()));
            tv_description.setText(jobAd.getContent());
            tv_locationName.setText(jobAd.getLocation());
            tv_weeklyHoursNumber.setText(jobAd.getWeeklyHours());
            tv_createddateTime.setText(DateUtils.getFormattedDateTime(jobAd.getCreatedDatetime()));
            tv_minago.setText(DateUtils.getTimeAgo(jobAd.getCreatedDatetime()));
            if (jobAd.getLikeCount() != null && jobAd.getLikeCount() > 0) {
                img_like.setBackground(getActivity().getDrawable(R.mipmap.ic_like_selected));
            } else {
                img_like.setBackground(getActivity().getDrawable(R.mipmap.ic_like));
            }
        }
    }

    private void GetJob_id(Integer id) {
        jobListViewModel.getJobId(id).observe(this, new Observer<BaseModel<List<SpecficJobAd>>>() {
            @Override
            public void onChanged(BaseModel<List<SpecficJobAd>> getjobAd) {
                if (getjobAd != null && !getjobAd.isError()) {
                    checkLikeCount = getjobAd.getData().get(0);
                    if (getjobAd.getData().get(0).getLikeCount() != null && getjobAd.getData().get(0).getLikeCount() > 0) {
                        img_like.setBackground(getActivity().getDrawable(R.mipmap.ic_like_selected));
                    } else {
                        img_like.setBackground(getActivity().getDrawable(R.mipmap.ic_like));
                    }
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_apply:
                if (jobAd.getIs_applied() == 0) {
                    ApplicationFormFragment applicationFormFragment = new ApplicationFormFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("JOB_AD", jobAd);
                    applicationFormFragment.setArguments(bundle);
                    loadFragment(R.id.framelayout, applicationFormFragment, getContext(), true);
                } else {
                    Toast.makeText(getContext(), "You have already applied to this job", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_goback:
                getActivity().onBackPressed();
                break;
            case R.id.img_like:
                if (checkLikeCount.getLikeCount() > 0) {
                    SetJobUnLike(checkLikeCount.getId());
                } else {
                    SetJobLike(checkLikeCount.getId());
                }
                break;
        }
    }

    private void SetJobLike(Integer id) {
        jobListViewModel.setJobLike(user, id, "like").observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> setlike) {
                onRefresh();
                hideDialog();
            }
        });
    }

    private void SetJobUnLike(Integer id) {
        jobListViewModel.setJobLike(user, id, "unlike").observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> setlike) {
                onRefresh();
                hideDialog();
            }
        });
    }

    @Override
    public void onRefresh() {
        if (NetworkUtils.isNetworkConnected(getContext())) {
//            if (user != null && user.getType().equals("company")) {
//            } else {
            GetJob_id(jobAd.getId());
            //  }
        } else {
            networkErrorDialog();
        }

    }
}
