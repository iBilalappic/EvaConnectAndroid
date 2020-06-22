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
import com.hypernym.evaconnect.models.AppliedApplicants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.view.adapters.AppliedApplicantAdapter;
import com.hypernym.evaconnect.viewmodel.AppliedApplicantViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CompanyApplicantFragment extends BaseFragment implements View.OnClickListener, AppliedApplicantAdapter.OnItemClickListener {

    private AppliedApplicantViewModel appliedApplicantViewModel;
    private AppliedApplicantAdapter appliedApplicantAdapter;
    private LinearLayoutManager linearLayoutManager;

    public CompanyApplicantFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_totalapplicant)
    TextView tv_totalapplicant;

    @BindView(R.id.tv_activehour)
    TextView tv_activehour;

    @BindView(R.id.tv_sector)
    TextView tv_sector;
//
//    @BindView(R.id.tv_weeklyHoursNumber)
//    TextView tv_weeklyHoursNumber;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.rc_applicants)
    RecyclerView rc_applicants;


    private CompanyJobAdModel companyJobAdModel = new CompanyJobAdModel();
    private List<AppliedApplicants> appliedApplicantModel = new ArrayList<>();


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

    private void GetApplicants() {
        showDialog();
        appliedApplicantViewModel.getApplicants(companyJobAdModel.getId()).observe(this, new Observer<BaseModel<List<AppliedApplicants>>>() {
            @Override
            public void onChanged(BaseModel<List<AppliedApplicants>> getApplicants) {
                if (getApplicants != null && !getApplicants.isError()) {
                    hideDialog();
                    appliedApplicantModel.clear();
                    appliedApplicantModel.addAll(getApplicants.getData());
                    setupRecyclerview();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void init() {
        appliedApplicantViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(AppliedApplicantViewModel.class);

        if ((getArguments() != null)) {
            setPageTitle("");
          //  showBackButton();
            companyJobAdModel = (CompanyJobAdModel) getArguments().getSerializable("COMPANY_AD");
            AppUtils.setGlideImage(getContext(), profile_image, companyJobAdModel.getJobImage());
            tv_name.setText(companyJobAdModel.getJobTitle());
            tv_content.setText(companyJobAdModel.getContent());
            tv_sector.setText(companyJobAdModel.getJobSector());
            tv_activehour.setText("Active for"+companyJobAdModel.getActive_hours()+" hrs");
         //   tv_weeklyHoursNumber.setText(companyJobAdModel.getWeeklyHours());
            tv_totalapplicant.setText(companyJobAdModel.getApplicant_count()+" Applicants");
//            tv_minago.setText(DateUtils.getTimeAgo(jobAd.getCreatedDatetime()));
            GetApplicants();

        }
    }

    private void setupRecyclerview() {

        appliedApplicantAdapter = new AppliedApplicantAdapter(getContext(), appliedApplicantModel, this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rc_applicants.setLayoutManager(linearLayoutManager);
        rc_applicants.setAdapter(appliedApplicantAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit:
                CreateJobFragment createJobFragment = new CreateJobFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("COMPANY_AD", companyJobAdModel);
                createJobFragment.setArguments(bundle);
                loadFragment(R.id.framelayout, createJobFragment, getContext(), true);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        AppliedApplicantFragment appliedApplicantFragment = new AppliedApplicantFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.DATA, appliedApplicantModel.get(position));
        bundle.putString("JOB_NAME", companyJobAdModel.getJobTitle());
        bundle.putSerializable("JOB_DETAIL", companyJobAdModel);
        appliedApplicantFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, appliedApplicantFragment, getContext(), true);


    }
}
