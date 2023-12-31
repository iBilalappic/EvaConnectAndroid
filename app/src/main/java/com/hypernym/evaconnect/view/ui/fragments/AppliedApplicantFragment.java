package com.hypernym.evaconnect.view.ui.fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.AppliedApplicantViewModel;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AppliedApplicantFragment extends BaseFragment implements View.OnClickListener {
    private AppliedApplicants appliedApplicants = new AppliedApplicants();


    @BindView(R.id.tv_view_cv)
    TextView tv_view_cv;


    @BindView(R.id.tv_cvname)
    TextView tv_cvname;

    @BindView(R.id.tv_description)
    TextView tv_description;


//    @BindView(R.id.tv_declineApplicant)
//    TextView tv_declineApplicant;

    @BindView(R.id.tv_activehour)
    TextView tv_activehour;

    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    @BindView(R.id.profile_image_applicant)
    CircleImageView profile_image_applicant;


//    @BindView(R.id.timePicker)
//    TimePicker timePicker;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_download_cv)
    TextView tv_download_cv;

    @BindView(R.id.tv_name_applicant)
    TextView tv_name_applicant;

    @BindView(R.id.tv_sector)
    TextView tv_sector;

    @BindView(R.id.tv_message)
    TextView tv_message;

    @BindView(R.id.tv_hide)
    TextView tv_hide;


    @BindView(R.id.tv_totalapplicant)
    TextView tv_totalapplicant;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.hidden_layout)
    LinearLayout hidden_layout;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;


    Uri uri;
    User user = new User();
    int current_hour, current_mintues;

    int hour, minute;
    String Job_name;
    int hidden_value = 0;
    private ConnectionViewModel connectionViewModel;
    private AppliedApplicantViewModel appliedApplicantViewModel;
    private SimpleDialog simpleDialog;
    private CompanyJobAdModel companyJobAdModel;

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
        tv_edit.setOnClickListener(this);
        tv_hide.setOnClickListener(this);
        tv_message.setOnClickListener(this);
        tv_download_cv.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);

        //   tv_declineApplicant.setOnClickListener(this);
        //  timePicker.setIs24HourView(true);
        user = LoginUtils.getUser();
        init();
        return view;
    }

    private void init() {
        if ((getArguments() != null)) {
            setPageTitle("Applicant Details");
         //   showBackButton();
            connectionViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ConnectionViewModel.class);
            appliedApplicantViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(AppliedApplicantViewModel.class);
            appliedApplicants = (AppliedApplicants) getArguments().getSerializable(Constants.DATA);
            Job_name = getArguments().getString("JOB_NAME");
            companyJobAdModel = (CompanyJobAdModel) getArguments().getSerializable("JOB_DETAIL");
            Log.d("TAAAG", "" + GsonUtils.toJson(appliedApplicants));
            AppUtils.setGlideImage(getContext(), profile_image_applicant, appliedApplicants.getUser().getUserImage());
            tv_date.setText(DateUtils.getTimeAgo(appliedApplicants.getCreatedDatetime()));
            tv_description.setText(appliedApplicants.getContent());
            tv_name_applicant.setText(appliedApplicants.getUser().getFirstName());
            AppUtils.setGlideImage(getContext(), profile_image, companyJobAdModel.getJobImage());
            tv_activehour.setText("Active for " + String.valueOf(companyJobAdModel.getActive_hours()) + " hrs");
            tv_totalapplicant.setText(String.valueOf(companyJobAdModel.getApplicant_count() + " Applicants"));
            tv_sector.setText(companyJobAdModel.getJobSector());
            File file = new File(appliedApplicants.getApplicationAttachment());
            //  tv_applicant_cv_name.setText(file.getName());
            //  datePicker.setMinDate(new Date().getTime());
            getAppicantDetail();
        }
    }

    private void getAppicantDetail() {
        appliedApplicantViewModel.getApplicant(appliedApplicants.getId()).observe(this, new Observer<BaseModel<List<AppliedApplicants>>>() {
            @Override
            public void onChanged(BaseModel<List<AppliedApplicants>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    appliedApplicants = listBaseModel.getData().get(0);
                    settingApplicant();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
            }
        });
    }

    private void settingApplicant() {
        if (appliedApplicants.getIs_hidden() == 0) {
            tv_hide.setText("Hide");
            hidden_value = 1;
            hidden_layout.setVisibility(View.GONE);
            tv_message.setVisibility(View.GONE);
            tv_view_cv.setEnabled(true);
            tv_view_cv.setTextColor(Color.BLACK);
            tv_description.setTextColor(Color.BLACK);
        } else {
            tv_hide.setText("Hidden");
            hidden_value = 0;
            hidden_layout.setVisibility(View.GONE);
            tv_message.setVisibility(View.GONE);
            tv_view_cv.setEnabled(false);
            tv_view_cv.setTextColor(Color.GRAY);
            tv_description.setTextColor(Color.GRAY);

        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_download_cv:
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
            case R.id.tv_message:
                ChatFragment chatFragment=new ChatFragment();
                Bundle bundlemessage=new Bundle();
                bundlemessage.putSerializable("applicant",appliedApplicants.getUser());
                chatFragment.setArguments(bundlemessage);
                loadFragment(R.id.framelayout, chatFragment, getContext(), true);
                break;
//            case R.id.tv_download_cv:
//                if (appliedApplicants.getApplicationAttachment() != null) {
//                    try {
//                        uri = Uri.parse("googlechrome://navigate?url=" + appliedApplicants.getApplicationAttachment());
//                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(i);
//                    } catch (ActivityNotFoundException e) {
//                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(i);
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "No File to Download", Toast.LENGTH_SHORT).show();
//                }
//                break;
            case R.id.tv_edit:
                CreateJobFragment createJobFragment = new CreateJobFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("COMPANY_AD", companyJobAdModel);
                createJobFragment.setArguments(bundle);
                loadFragment(R.id.framelayout, createJobFragment, getContext(), true);
                break;
            case R.id.tv_hide:
                HideApplication();
                break;
            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;

//            case R.id.tv_offerinterview:
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hours = mcurrentTime.get(Calendar.HOUR_OF_DAY);  // current hour
//                int minutess = mcurrentTime.get(Calendar.MINUTE);      // current min
//                current_hour = hours;
//                current_mintues = minutess;
//                SetTimePicker();
//                String date = DateUtils.GetCurrentdate();
//
//                String[] separated = date.split("-");
//                String year = separated[0];
//                String month = separated[1];
//                String day = separated[2];
//
//                if (datePicker.getDayOfMonth() > Integer.parseInt(day)) {
//                    OfferInterviewCall();
//                } else if (datePicker.getMonth() + 1 > Integer.parseInt(month)) {
//                    OfferInterviewCall();
//                } else if (datePicker.getYear() > Integer.parseInt(year)) {
//                    OfferInterviewCall();
//                } else if (hour > current_hour) {
//                    OfferInterviewCall();
//                } else if (hour == current_hour && minute >= current_mintues) {
//                    OfferInterviewCall();
//                } else {
//                    Toast.makeText(getContext(), "you cannot set past time for interview", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.tv_declineApplicant:
//                simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.decline_application_confirmation), getString(R.string.button_cancel), getString(R.string.ok), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // getActivity().onBackPressed();
//                        switch (v.getId()) {
//                            case R.id.button_positive:
//                                simpleDialog.dismiss();
//                                declineApplication();
//                                break;
//                            case R.id.button_negative:
//                                simpleDialog.dismiss();
//                                break;
//                        }
//                    }
//                });
//                simpleDialog.show();
//                break;
        }
    }

    private void HideApplication() {
        AppliedApplicants declineData = new AppliedApplicants();
//        declineData.setStatus(AppConstants.DELETED);
        declineData.setModified_by_id(user.getId());
        declineData.setModified_datetime(DateUtils.GetCurrentdatetime());
        declineData.setIs_hidden(hidden_value);
        showDialog();
        appliedApplicantViewModel.declineApplication(appliedApplicants.getId(), declineData).observe(this, new Observer<BaseModel<List<AppliedApplicants>>>() {
            @Override
            public void onChanged(BaseModel<List<AppliedApplicants>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
//                    if (getFragmentManager().getBackStackEntryCount() != 0) {
//                        getFragmentManager().popBackStack();
//                    }
                  //  hidden_layout.setForeground(R.drawable.layout_overlay);
                    hideDialog();

                    getAppicantDetail();
                }else{
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
            }
        });
    }

//    private void OfferInterviewCall() {
//        if(NetworkUtils.isNetworkConnected(getContext())) {
//            ConnectionApiCall();
//            ChatFragment chatFragment = new ChatFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString(Constants.FRAGMENT_NAME, AppConstants.APPLICANT_FRAGMENT);
//            bundle.putSerializable(Constants.DATA, appliedApplicants);
//            bundle.putString("JOB_NAME", Job_name);
//            bundle.putInt("Day", datePicker.getDayOfMonth());
//            bundle.putInt("Month", datePicker.getMonth() + 1);
//            bundle.putInt("Year", datePicker.getYear());
//            bundle.putInt("Hour", hour);
//            bundle.putInt("Mintues", minute);
//            chatFragment.setArguments(bundle);
//            loadFragment(R.id.framelayout, chatFragment, getContext(), true);
//        }
//        else
//        {
//            networkErrorDialog();
//        }
//    }

    private void ConnectionApiCall() {
        Connection connection = new Connection();
        User user = LoginUtils.getLoggedinUser();
        connection.setReceiver_id(appliedApplicants.getUserId());
        connection.setStatus(AppConstants.ACTIVE);
        connection.setSender_id(user.getId());
        connectionViewModel.connect(connection).observe(this, new Observer<BaseModel<List<Connection>>>() {
            @Override
            public void onChanged(BaseModel<List<Connection>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {

                }
                hideDialog();
            }
        });
    }

//    private void SetTimePicker() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            hour = timePicker.getHour();
//            minute = timePicker.getMinute();
//        } else {
//            hour = timePicker.getCurrentHour();
//            minute = timePicker.getCurrentMinute();
//        }
//    }
}
