package com.hypernym.evaconnect.view.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.hypernym.evaconnect.view.dialogs.CustomProgressBar;
import com.hypernym.evaconnect.view.ui.fragments.BaseFragment;

import java.util.List;

public class BaseActivity extends AppCompatActivity  {
private CustomProgressBar customProgressBar=new CustomProgressBar();
    @Override
    public void onBackPressed() {

        List fragmentList = getSupportFragmentManager().getFragments();

        boolean handled = false;
        for(Object f : fragmentList) {
            if(f instanceof BaseFragment) {
                handled = ((BaseFragment)f).onBackPressed();
                if(handled) {
                    break;
                }
            }
        }
        if(!handled) {
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

}