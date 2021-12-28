package com.hypernym.evaconnect.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.hypernym.evaconnect.MyActivityFragment;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Stats;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.ui.fragments.ActivityFragment;
import com.hypernym.evaconnect.view.ui.fragments.CalendarFragment;
import com.hypernym.evaconnect.view.ui.fragments.JobListingFragment;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationDialog extends Dialog implements View.OnClickListener {

    private ImageView img_close;
    private LinearLayout editProfile, notifications, mJoblisting, myactivity, calendar;
    private TextView logout, tv_name, tv_designation,
            tv_company, tv_location, tv_connections_count, tv_notication_count, tv_calender_event, tv_joblisting;
    private Context context;
    private CircleImageView profile_image;
    User user = new User();
    UserViewModel userViewModel;

    public NavigationDialog(Context context) {
        super(context);
        this.context = context;
        user = LoginUtils.getUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_menu);
    //    userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getApplication(), this)).get(UserViewModel.class);

        img_close = findViewById(R.id.img_close);
//        editProfile=findViewById(R.id.editProfile);
        logout = findViewById(R.id.tv_logout);
        tv_name = findViewById(R.id.tv_name);
        tv_designation = findViewById(R.id.tv_designation);
        tv_company = findViewById(R.id.tv_company);
        tv_location = findViewById(R.id.tv_location);
        tv_connections_count = findViewById(R.id.tv_connections_count);
        tv_notication_count = findViewById(R.id.tv_notication_count);
        tv_calender_event = findViewById(R.id.tv_calender_event);
        tv_joblisting = findViewById(R.id.tv_joblisting);
        profile_image = findViewById(R.id.profile_image);
//        notifications=findViewById(R.id.notifications);
        mJoblisting=findViewById(R.id.joblisting);
        calendar = findViewById(R.id.calendar);
        myactivity = findViewById(R.id.layout_myactivity);
        mJoblisting.setOnClickListener(this);
        // mLike.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = getWindow();


        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.flags &= WindowManager.LayoutParams.FLAG_DIM_BEHIND ;
        window.setAttributes(wlp);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();



        params.y = 50;
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        this.getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        gettingUserStats();
        SettingUserData();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        myactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                //ActivityFragment activityFragment=new ActivityFragment();
                MyActivityFragment activityFragment=new MyActivityFragment();
                Bundle bundle=new Bundle();
                bundle.putBoolean("isNotification",true);
                activityFragment.setArguments(bundle);
                transaction.replace(R.id.framelayout, activityFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                AppUtils.logout(context);
            }
        });
//        notifications.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//                FragmentTransaction transaction =((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
//                transaction.replace(R.id.framelayout, new NotificationsFragment());
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.framelayout, new CalendarFragment());
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
    }

    private void gettingUserStats() {

        RestClient.get().appApi().getUserStats(LoginUtils.getLoggedinUser().getId()).enqueue(new Callback<BaseModel<List<Stats>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Stats>>> call, Response<BaseModel<List<Stats>>> response) {
                if(response.body()!=null)
                {
                    tv_connections_count.setText(String.valueOf(response.body().getData().get(0).getConnection_count()));
                    tv_notication_count.setText(String.valueOf(response.body().getData().get(0).getNotification_count()));
                }

            }

            @Override
            public void onFailure(Call<BaseModel<List<Stats>>> call, Throwable t) {

            }
        });
    }

    private void SettingUserData() {
        if (user != null && user.getType().equals("company")) {
            tv_calender_event.setText("Calendar and Events");
            tv_joblisting.setText("My Jobs");

        } else {
            tv_calender_event.setText("Calendar");
            tv_joblisting.setText("Job Listings");
        }
        tv_name.setText(user.getFirst_name() + " " + user.getLast_name());
        if(user.getDesignation()!=null)
        {
            tv_designation.setText(user.getDesignation());
        }
        tv_company.setText(user.getCompany_name());
        tv_location.setText(user.getCountry() + "," + user.getCity());

        if (!TextUtils.isEmpty(user.getUser_image())) {
            AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());

        }
//        else if (user.getIs_facebook() == 1 && !TextUtils.isEmpty(user.getFacebook_image_url())) {
//            AppUtils.setGlideImage(getContext(), profile_image, user.getFacebook_image_url());
//        } else {
//            AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.joblisting:
                dismiss();
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.framelayout, new JobListingFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
//            case R.id.layoutLike:
//                dismiss();
//                FragmentTransaction transaction_1 =((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
//                transaction_1.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
//                transaction_1.replace(R.id.framelayout, new MyLikesFragment());
//                transaction_1.addToBackStack(null);
//                transaction_1.commit();
//
//                break;
        }
    }

}
