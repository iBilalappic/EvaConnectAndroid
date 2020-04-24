package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.CompanyJobAdAdapter;
import com.hypernym.evaconnect.view.adapters.JobAdAdapter;
import com.hypernym.evaconnect.view.adapters.MyLikeAdapter;
import com.hypernym.evaconnect.view.adapters.NotificationsAdapter;
import com.hypernym.evaconnect.viewmodel.CreateJobAdViewModel;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;
import com.hypernym.evaconnect.viewmodel.JobListViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JobListingFragment extends BaseFragment implements View.OnClickListener, JobAdAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.newjobAd)
    TextView newjobAd;


    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.rc_joblisting)
    RecyclerView rc_joblisting;


    User user = new User();
    private JobListViewModel jobListViewModel;

    private JobAdAdapter jobAdAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<JobAd> jobAdList = new ArrayList<>();

    private CompanyJobAdAdapter companyJobAdAdapter;
    private LinearLayoutManager CompanylinearLayoutManager;
    private List<CompanyJobAdModel> companyJobAdModelList = new ArrayList<>();

    public JobListingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_joblisting, container, false);
        setPageTitle(getString(R.string.joblist));
        ButterKnife.bind(this, view);
        newjobAd.setOnClickListener(this);
        swipeRefresh.setOnRefreshListener(this);
        user = LoginUtils.getUser();

        init();
        return view;
    }

    private void CheckJobAd(User user) {
        if (user != null && user.getType().equals("company")) {
            newjobAd.setVisibility(View.VISIBLE);
            GetCompanyJobAd();
        } else {
            newjobAd.setVisibility(View.GONE);
            GetJobAd();
            setupRecyclerview();
        }
    }


    private void init() {
        jobListViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(JobListViewModel.class);
        CheckJobAd(user);

    }

    private void setupRecyclerview() {
        jobAdAdapter = new JobAdAdapter(getContext(), jobAdList, this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rc_joblisting.setLayoutManager(linearLayoutManager);
        rc_joblisting.setAdapter(jobAdAdapter);
    }

    private void GetJobAd() {
        jobListViewModel.getjobAd(user).observe(this, new Observer<BaseModel<List<JobAd>>>() {
            @Override
            public void onChanged(BaseModel<List<JobAd>> getnetworkconnection) {
                if (getnetworkconnection != null && !getnetworkconnection.isError()) {
                    jobAdList.clear();
                    jobAdList.addAll(getnetworkconnection.getData());
                    Collections.reverse(jobAdList);
                    swipeRefresh.setRefreshing(false);
                    setupRecyclerview();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();

            }
        });
    }

    private void GetCompanyJobAd() {
        jobListViewModel.getcompanyAd(user).observe(this, new Observer<BaseModel<List<CompanyJobAdModel>>>() {
            @Override
            public void onChanged(BaseModel<List<CompanyJobAdModel>> getnetworkconnection) {
                if (getnetworkconnection != null && !getnetworkconnection.isError()) {
                    companyJobAdModelList.clear();
                    companyJobAdModelList.addAll(getnetworkconnection.getData());
                    Collections.reverse(companyJobAdModelList);
                    swipeRefresh.setRefreshing(false);
                    setupRecyclerviewCompany();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();

            }
        });
    }

    private void setupRecyclerviewCompany() {
        companyJobAdAdapter = new CompanyJobAdAdapter(getContext(), companyJobAdModelList, this);
        CompanylinearLayoutManager = new LinearLayoutManager(getContext());
        rc_joblisting.setLayoutManager(CompanylinearLayoutManager);
        rc_joblisting.setAdapter(companyJobAdAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newjobAd:
                loadFragment(R.id.framelayout, new JobCreateFragment(), getContext(), true);
                break;

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.tv_gotoadd:
                SpecficJobFragment specficJobFragment = new SpecficJobFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("job_id", jobAdList.get(position).getId());;
                specficJobFragment.setArguments(bundle);
                loadFragment(R.id.framelayout, specficJobFragment, getContext(), true);
//                Toast.makeText(getContext(), "goto" + position, Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_like:
                if (jobAdList.get(position).getIs_job_like() > 0) {
                    SetJobUnLike(jobAdList.get(position).getId(), position);
                } else {
                    SetJobLike(jobAdList.get(position).getId(), position);
                }

                break;
            case R.id.contraintlayout:
                CompanyApplicantFragment companyApplicantFragment = new CompanyApplicantFragment();
                Bundle bundle_0 = new Bundle();
                bundle_0.putSerializable("COMPANY_AD", companyJobAdModelList.get(position));
                companyApplicantFragment.setArguments(bundle_0);
                loadFragment(R.id.framelayout, companyApplicantFragment, getContext(), true);
                break;
        }
    }

    private void SetJobLike(Integer id, int position) {
        jobListViewModel.setJobLike(user, id, "like").observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> setlike) {
//                if (setlike != null && !setlike.isError()) {
                onRefresh();
//                } else {
//                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
//                }
                hideDialog();

            }
        });
    }

    private void SetJobUnLike(Integer id, int position) {
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
            if (user != null && user.getType().equals("company")) {
                GetCompanyJobAd();
            } else {
                GetJobAd();
            }
        } else {
            networkErrorDialog();
        }
    }
}
