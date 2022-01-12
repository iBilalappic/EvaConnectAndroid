package com.hypernym.evaconnect.view.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.dialogs.CustomProgressBar;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.view.ui.fragments.ActivityFragment;
import com.hypernym.evaconnect.view.ui.fragments.MainViewPagerFragment;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import java.util.List;

public class BaseActivity extends AppCompatActivity {
    private CustomProgressBar customProgressBar = new CustomProgressBar();
    private SimpleDialog simpleDialog;
    private RecyclerView rc_notifications;
    private TextView tv_pagetitle;
    private ImageView img_uparrow;
    private static int count = 0;
    private UserViewModel userViewModel;


    public void showDialog() {

        if (customProgressBar != null && !customProgressBar.isShowing())
            customProgressBar.showProgress(this, true);
    }

    public void hideDialog() {

        if (customProgressBar != null && customProgressBar.isShowing())
            customProgressBar.hideProgress();
    }

    public SimpleDialog networkErrorDialog() {
        simpleDialog = new SimpleDialog(BaseActivity.this, getString(R.string.error), getString(R.string.network_error), null, getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDialog.dismiss();
            }
        });
        simpleDialog.show();
        return simpleDialog;
    }

    public SimpleDialog networkResponseDialog(String title, String message) {
        simpleDialog = new SimpleDialog(BaseActivity.this, title, message, null, getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDialog.dismiss();
            }
        });
        simpleDialog.show();
        return simpleDialog;
    }


    public SimpleDialog UserExistFbLinkedin(String title, String message) {
        simpleDialog = new SimpleDialog(BaseActivity.this, title, message, null, getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDialog.dismiss();
                startActivity(new Intent(BaseActivity.this,LoginActivity.class));
                finishAffinity();
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

    @Override
    protected void onResume() {
        super.onResume();
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    public void loadFragment(int id, Fragment fragment, Context context, boolean isBack) {
        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        //     transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(id, fragment);
        findViewById(R.id.seprator_line).setVisibility(View.VISIBLE);
        if (fragment instanceof MainViewPagerFragment || fragment instanceof ActivityFragment) {
            findViewById(R.id.seprator_line).setVisibility(View.GONE);
        }
        if (isBack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public static int getNotificationCount() {
        return count;
    }

    public static void setNotificationCount(int mcount) {
        count = mcount;
    }

    @Override
    protected void onStop() {
        super.onStop();
        setUserOffline();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    private void setUserOffline() {
        Log.d("APPLICATION", "APPLICATION OFFLINE");
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getApplication(), this)).get(UserViewModel.class);
        if (LoginUtils.getLoggedinUser() != null) {
            userViewModel.userOnline(false).observe(this, new Observer<BaseModel<List<User>>>() {
                @Override
                public void onChanged(BaseModel<List<User>> listBaseModel) {
                    //Toast.makeText(BaseActivity.this, "offline", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}