package com.hypernym.evaconnect.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.view.ui.fragments.JobListingFragment;
import com.hypernym.evaconnect.view.ui.fragments.NotificationsFragment;
import com.hypernym.evaconnect.view.ui.fragments.PersonDetailFragment;

public class NavigationDialog extends Dialog implements View.OnClickListener {

    private ImageView img_close;
    private LinearLayout editProfile,logout,notifications,mJoblisting;
    private Context context;

    public NavigationDialog(Context context) {
        super(context);
        this.context=context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_menu);
        img_close=findViewById(R.id.img_close);
        editProfile=findViewById(R.id.editProfile);
        logout=findViewById(R.id.logout);
        notifications=findViewById(R.id.notifications);
        mJoblisting=findViewById(R.id.joblisting);
        mJoblisting.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP |Gravity.RIGHT;
        wlp.flags &= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.y = 50;
        this.getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                FragmentTransaction transaction =((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.framelayout, new PersonDetailFragment());
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
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                FragmentTransaction transaction =((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.framelayout, new NotificationsFragment());
                transaction.commit();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.joblisting:
                dismiss();
                FragmentTransaction transaction =((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.framelayout, new JobListingFragment());
                transaction.commit();
            break;
        }
    }
}
