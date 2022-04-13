package com.hypernym.evaconnect.view.ui.fragments;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.SavedJobData;
import com.hypernym.evaconnect.models.SpecficJobAd;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.MyLikeAdapter;
import com.hypernym.evaconnect.view.bottomsheets.BottomsheetShareSelection;
import com.hypernym.evaconnect.viewmodel.JobListViewModel;

import java.text.DecimalFormat;
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

//    @BindView(R.id.tv_minago)
//    TextView tv_minago;

//    @BindView(R.id.tv_createddateTime)
//    TextView tv_createddateTime;

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

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.img_like)
    ImageView img_like;

    @BindView(R.id.img_favourite)
    ImageView img_favourite;



    @BindView(R.id.img_comment)
    ImageView img_comment;

    @BindView(R.id.img_share)
    ImageView img_share;

    @BindView(R.id.like_click)
    LinearLayout like_click;

    @BindView(R.id.comment_click)
    LinearLayout comment_click;

    @BindView(R.id.share_click)
    LinearLayout share_click;

    int job_id;
    int Savejob_id;
    JobAd jobAd = new JobAd();
    SpecficJobAd specficJobAd=new SpecficJobAd();
    private SpecficJobAd checkLikeCount = new SpecficJobAd();
    User user;
    private String user_image = "";

    public Boolean is_favourite_job;

    public SpecficJobFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_specfic_job_ad, container, false);
        ButterKnife.bind(this, view);
        tv_apply.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);
        img_like.setOnClickListener(this);
        img_share.setOnClickListener(this);
        like_click.setOnClickListener(this);
        share_click.setOnClickListener(this);
        comment_click.setOnClickListener(this);
        img_favourite.setOnClickListener(this);

        return view;
    }

    private void init() {

        jobListViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(JobListViewModel.class);
        user = LoginUtils.getUser();
       // showBackButton();
       // setPageTitle("Job Listing");
        if ((getArguments() != null)) {
        //    setPageTitle("");
         //   showBackButton();
          //  jobAd = (JobAd) getArguments().getSerializable("JOB_AD");
            user_image  = getArguments().getString("user_image");
            Log.v("user_image",user_image+"");
            job_id = getArguments().getInt("job_id");
            if (job_id != 0) {

                GetSavedJob(job_id);
                GetJob_id(job_id);

            } else {
                GetJob_id(jobAd.getId());
            }
            if (LoginUtils.getUser().getType().equals("company")) {
                tv_apply.setVisibility(View.GONE);
                img_favourite.setVisibility(View.GONE);
            } else {
                tv_apply.setVisibility(View.VISIBLE);
            }
        }
    }

    private void GetSavedJob(int job_id) {
        jobListViewModel.getSavedJob(job_id).observe(this, new Observer<BaseModel<List<SavedJobData>>>() {
            @Override
            public void onChanged(BaseModel<List<SavedJobData>> getjobAd) {
                if (getjobAd.getData().size()>0 && !getjobAd.isError()) {
                    if(getjobAd.getData().get(0).getIsFavourite()){
                        img_favourite.setBackgroundResource(R.drawable.ic_star_selected);
                        is_favourite_job=true;
                        Savejob_id=getjobAd.getData().get(0).getId();
                    }

                }else{
                    img_favourite.setBackgroundResource(R.drawable.ic_star_unselected);
                    is_favourite_job=false;
                }
//                else {
//                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
//                }
                hideDialog();
            }
        });

    }

    private void GetJob_id(Integer id) {
        jobListViewModel.getJobId(id).observe(this, getjobAd -> {
            if (getjobAd != null && !getjobAd.isError()) {
                settingData(getjobAd.getData().get(0));
                specficJobAd=getjobAd.getData().get(0);
                checkLikeCount = getjobAd.getData().get(0);
                AppUtils.setGlideImage(getContext(), profile_image,getjobAd.getData().get(0).getUser().getUser_image() );
                tv_name.setText(getjobAd.getData().get(0).getJobTitle());
                tv_positionName.setText(getjobAd.getData().get(0).getPosition());
                DecimalFormat myFormatter = new DecimalFormat("############");
                tv_salaryAmount.setText("£ " + myFormatter.format(getjobAd.getData().get(0).getSalary()) + " pa");
                tv_description.setText(getjobAd.getData().get(0).getContent());
                tv_locationName.setText(getjobAd.getData().get(0).getLocation());
              //  tv_weeklyHoursNumber.setText(getjobAd.getData().get(0).getWeeklyHours());
//                    tv_createddateTime.setText(DateUtils.getFormattedDateTime(getjobAd.getData().get(0).getCreatedDatetime()));
//                    tv_minago.setText(DateUtils.getTimeAgo(getjobAd.getData().get(0).getCreatedDatetime()));
                if (getjobAd.getData().get(0).getIsJobLike() != null && getjobAd.getData().get(0).getIsJobLike() > 0) {
                    img_like.setBackground(getActivity().getDrawable(R.drawable.like_selected));
                } else {
                    img_like.setBackground(getActivity().getDrawable(R.drawable.ic_like));
                }
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
        });
    }

    private void settingData(SpecficJobAd specficJobAd) {
        jobAd = new JobAd();
        jobAd.setCommentCount(specficJobAd.getCommentCount());
        jobAd.setContent(specficJobAd.getContent());
        jobAd.setCreatedById(specficJobAd.getCreatedById());
        jobAd.setCreatedDatetime(specficJobAd.getCreatedDatetime());
        jobAd.setId(specficJobAd.getId());
        jobAd.setIs_applied(specficJobAd.getIsApplied());
        jobAd.setJobImage(specficJobAd.getJobImage());
        jobAd.setJobNature(specficJobAd.getJobNature());
        jobAd.setJobTitle(specficJobAd.getJobTitle());
        jobAd.setLocation(specficJobAd.getLocation());
        jobAd.setLikeCount(specficJobAd.getLikeCount());
        jobAd.setPosition(specficJobAd.getPosition());
        jobAd.setSalary(specficJobAd.getSalary());
        jobAd.setWeeklyHours(specficJobAd.getWeeklyHours());
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_apply:
                if (checkLikeCount.getIsApplied() == 0) {
                    ApplicationFormFragment applicationFormFragment = new ApplicationFormFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("JOB_AD", jobAd);
                    applicationFormFragment.setArguments(bundle);
                    loadFragment(R.id.framelayout, applicationFormFragment, getContext(), true);
                } else {
                    Toast.makeText(getContext(), "You have already applied to this job", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;
            case R.id.like_click:
                if (checkLikeCount != null && checkLikeCount.getIsJobLike() > 0) {
                    SetJobUnLike(checkLikeCount.getId());
                } else {
                    SetJobLike(checkLikeCount.getId());
                }
                break;
            case R.id.share_click:
//                ShareDialog shareDialog;
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("specficJob",specficJobAd);
//                bundle.putString(Constants.FRAGMENT_NAME,"JOB_FRAGMENT");
//                shareDialog = new ShareDialog(getContext(),bundle);
//                shareDialog.show();
                BottomsheetShareSelection bottomSheetPictureSelection = new BottomsheetShareSelection(new YourDialogFragmentDismissHandler());
                bottomSheetPictureSelection.show(getActivity().getSupportFragmentManager(), bottomSheetPictureSelection.getTag());

                break;
            case R.id.img_favourite:
                showDialog();
                if(is_favourite_job){
                    setUnFavJob(false,"deleted");
                }else{
                    setFavJob(true, "active");
                }
                break;


        }
    }

    private void setFavJob(boolean is_favourite, String status) {
        jobListViewModel.setFavJob(job_id,is_favourite,status).observe(this, new Observer<BaseModel<SavedJobData>>() {
            @Override
            public void onChanged(BaseModel<SavedJobData> setlike) {
                if(setlike.getData()!=null){
                    hideDialog();
                    img_favourite.setBackgroundResource(R.drawable.ic_star_selected);
                    Savejob_id=setlike.getData().getId();
                    is_favourite_job=true;
                }
            }
        });
    }
    private void setUnFavJob(boolean is_favourite, String status) {
        jobListViewModel.setUnfavJob(Savejob_id,is_favourite,status).observe(this, new Observer<BaseModel<Object>>() {
            @Override
            public void onChanged(BaseModel<Object> setlike) {
                if(setlike.getData()!=null){
                    hideDialog();
                    img_favourite.setBackgroundResource(R.drawable.ic_star_unselected);
                    is_favourite_job=false;
                }
            }
        });
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
            if (job_id != 0) {
                GetJob_id(job_id);
            } else {
                GetJob_id(jobAd.getId());
            }

            //  }
        } else {
            networkErrorDialog();
        }

    }

    @Override
    public void onResume() {
        init();
        super.onResume();
    }



    protected class YourDialogFragmentDismissHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 102) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "https://www.evaintmedia.com/" + specficJobAd.getType() + "/" + specficJobAd.getId());
                try {
                    getContext().startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(requireContext(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            }
            else if(msg.what==100){
                ShareConnectionFragment shareConnectionFragment = new ShareConnectionFragment();
                FragmentTransaction transaction = ((AppCompatActivity) requireActivity()).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.framelayout, shareConnectionFragment);
                Bundle bundle = new Bundle();
                {
                    bundle.putInt(Constants.DATA, specficJobAd.getId());
                    bundle.putString(Constants.TYPE,  specficJobAd.getType());
                }
                shareConnectionFragment.setArguments(bundle);
                if (true) {
                    transaction.addToBackStack(null);
                }
                transaction.commit();
            }else if(msg.what==103){
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip;
                clip = ClipData.newPlainText("label", "https://www.evaintmedia.com/" + specficJobAd.getType() + "/" + specficJobAd.getId());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(requireContext(), "link copied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
