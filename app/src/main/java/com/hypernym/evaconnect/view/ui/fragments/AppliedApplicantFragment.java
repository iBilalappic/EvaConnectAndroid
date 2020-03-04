package com.hypernym.evaconnect.view.ui.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.AppliedApplicants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AppliedApplicantFragment extends BaseFragment implements View.OnClickListener {
    private AppliedApplicants appliedApplicants = new AppliedApplicants();

    @BindView(R.id.datePicker)
    DatePicker datePicker;

    @BindView(R.id.tv_download_cv)
    TextView tv_download_cv;

    @BindView(R.id.tv_view_cv)
    TextView tv_view_cv;

    @BindView(R.id.tv_description)
    TextView tv_description;

    @BindView(R.id.tv_offerinterview)
    TextView tv_offerinterview;

    @BindView(R.id.tv_declineApplicant)
    TextView tv_declineApplicant;

    @BindView(R.id.tv_activefor)
    TextView tv_activefor;

    @BindView(R.id.tv_applicant_cv_name)
    TextView tv_applicant_cv_name;


    @BindView(R.id.profile_image)
    CircleImageView cv_profile_image;

    @BindView(R.id.timePicker)
    TimePicker timePicker;

    @BindView(R.id.tv_name)
    TextView tv_name;


    Uri uri;
    User user = new User();

    int hour, minute;
    String Job_name;
    private ConnectionViewModel connectionViewModel;

    public AppliedApplicantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applicant_detail, container, false);
        ButterKnife.bind(this, view);
        tv_view_cv.setOnClickListener(this);
        tv_download_cv.setOnClickListener(this);
        tv_offerinterview.setOnClickListener(this);
        timePicker.setIs24HourView(true);
        user = LoginUtils.getUser();
        init();
        return view;
    }

    private void init() {
        if ((getArguments() != null)) {
            setPageTitle("");
            showBackButton();
            connectionViewModel= ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(ConnectionViewModel.class);

            appliedApplicants = (AppliedApplicants) getArguments().getSerializable(Constants.DATA);
            Job_name = getArguments().getString("JOB_NAME");
            Log.d("TAAAG", "" + GsonUtils.toJson(appliedApplicants));
            AppUtils.setGlideImage(getContext(), cv_profile_image, appliedApplicants.getUser().getUserImage());
            tv_activefor.setText(Constants.SUBMITTED + DateUtils.getTimeAgo(appliedApplicants.getCreatedDatetime()));
            tv_description.setText(appliedApplicants.getContent());
            tv_name.setText(appliedApplicants.getUser().getFirstName());
            File file = new File(appliedApplicants.getApplicationAttachment());
            tv_applicant_cv_name.setText(file.getName());

//            tv_positionName.setText(companyJobAdModel.getPosition());
//            tv_name.setText(companyJobAdModel.getJobTitle());
//            tv_salaryAmount.setText("$" + String.valueOf(companyJobAdModel.getSalary()));
//            tv_locationName.setText(companyJobAdModel.getLocation());
//            tv_weeklyHoursNumber.setText(companyJobAdModel.getWeeklyHours());
//            tv_minago.setText(DateUtils.getTimeAgo(jobAd.getCreatedDatetime()));
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_view_cv:
                if (appliedApplicants.getApplicationAttachment() != null) {
                    WebviewCvFragment webviewCvFragment = new WebviewCvFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("applicant_cv", appliedApplicants.getApplicationAttachment());
                    webviewCvFragment.setArguments(bundle);
                    loadFragment(R.id.framelayout, webviewCvFragment, getContext(), true);
                } else {
                    Toast.makeText(getActivity(), "No File to View", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_download_cv:
                if (appliedApplicants.getApplicationAttachment() != null) {
                    try {
                        uri = Uri.parse("googlechrome://navigate?url=" + appliedApplicants.getApplicationAttachment());
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } catch (ActivityNotFoundException e) {
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(getActivity(), "No File to Download", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_offerinterview:
                OfferInterviewCall();
                SetTimePicker();
                ChatFragment chatFragment = new ChatFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FRAGMENT_NAME, AppConstants.APPLICANT_FRAGMENT);
                bundle.putSerializable(Constants.DATA, appliedApplicants);
                bundle.putString("JOB_NAME", Job_name);
                bundle.putInt("Day", datePicker.getDayOfMonth());
                bundle.putInt("Month", datePicker.getMonth() + 1);
                bundle.putInt("Year", datePicker.getYear());
                bundle.putInt("Hour", hour);
                bundle.putInt("Mintues", minute);
                chatFragment.setArguments(bundle);
                loadFragment(R.id.framelayout, chatFragment, getContext(), true);
                break;


        }
    }

    private void OfferInterviewCall() {
        if(NetworkUtils.isNetworkConnected(getContext())) {
            ConnectionApiCall();
        }
        else
        {
            networkErrorDialog();
        }
    }

    private void ConnectionApiCall() {
        Connection connection=new Connection();
        User user= LoginUtils.getLoggedinUser();
        connection.setReceiver_id(appliedApplicants.getUserId());
        connection.setStatus(AppConstants.ACTIVE);
        connection.setSender_id(user.getId());
        connectionViewModel.connect(connection).observe(this, new Observer<BaseModel<List<Connection>>>() {
            @Override
            public void onChanged(BaseModel<List<Connection>> listBaseModel) {
                if(listBaseModel!=null && !listBaseModel.isError())
                {

                }
                hideDialog();
            }
        });
    }

    private void SetTimePicker() {
        if (Build.VERSION.SDK_INT >= 23) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }
    }
}
