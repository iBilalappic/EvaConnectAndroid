package com.hypernym.evaconnect.view.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Notification;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.NotificationsAdapter;
import com.hypernym.evaconnect.view.dialogs.CustomProgressBar;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.view.ui.fragments.BaseFragment;
import com.hypernym.evaconnect.view.ui.fragments.ConnectionsFragment;
import com.hypernym.evaconnect.view.ui.fragments.HomeFragment;
import com.hypernym.evaconnect.view.ui.fragments.PostDetailsFragment;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity  {
    private CustomProgressBar customProgressBar=new CustomProgressBar();
    private SimpleDialog simpleDialog;
    private RecyclerView rc_notifications;
    private TextView tv_pagetitle;
    private ImageView img_uparrow;
    private static int count=0;

    @Override
    public void onBackPressed() {

          Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);
            if (fragment instanceof HomeFragment || fragment instanceof ConnectionsFragment) {
                simpleDialog = new SimpleDialog(this, null, getString(R.string.msg_exit),
                        getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.button_positive:
                                simpleDialog.dismiss();
                                finish();
                                break;
                            case R.id.button_negative:
                                simpleDialog.dismiss();
                                break;
                        }
                    }
                });
                simpleDialog.show();

            } else {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                if( findViewById(R.id.tv_back)!=null)
                    findViewById(R.id.tv_back).setVisibility(View.GONE);
                super.onBackPressed();
            }
    }
    public void showDialog() {

        if(customProgressBar != null && !customProgressBar.isShowing())
            customProgressBar.showProgress(this,false);
    }

    public void hideDialog() {

        if(customProgressBar != null && customProgressBar.isShowing())
            customProgressBar.hideProgress();
    }

    public SimpleDialog networkErrorDialog() {
        simpleDialog=new SimpleDialog(BaseActivity.this,getString(R.string.error),getString(R.string.network_error),null,getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDialog.dismiss();
            }
        });
        simpleDialog.show();
        return simpleDialog;
    }
    public SimpleDialog networkResponseDialog(String title,String message) {
        simpleDialog=new SimpleDialog(BaseActivity.this,title,message,null,getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDialog.dismiss();
            }
        });
        simpleDialog.show();
        return simpleDialog;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideDialog();
    }



    public void loadFragment(int id, Fragment fragment, Context context,boolean isBack)
    {
        FragmentTransaction transaction =((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
   //     transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(id, fragment);
        if(isBack)
        {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public static int getNotificationCount()
    {
    return count;
    }

    public void setNotificationCount(int mcount)
    {
      count=mcount;
    }

}