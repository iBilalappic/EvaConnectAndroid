package com.hypernym.evaconnect.view.ui.fragments;

import androidx.fragment.app.Fragment;

import com.hypernym.evaconnect.view.dialogs.CustomProgressBar;

public class BaseFragment extends Fragment {
    private CustomProgressBar customProgressBar=new CustomProgressBar();
    /**
     * Could handle back press.
     * @return true if back press was handled
     */
    public boolean onBackPressed() {
        return false;
    }

    public void showDialog() {

        if(customProgressBar != null && !customProgressBar.isShowing())
            customProgressBar.showProgress(getContext(),false);
    }

    public void hideDialog() {

        if(customProgressBar != null && customProgressBar.isShowing())
            customProgressBar.hideProgress();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideDialog();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hideDialog();
    }


}
