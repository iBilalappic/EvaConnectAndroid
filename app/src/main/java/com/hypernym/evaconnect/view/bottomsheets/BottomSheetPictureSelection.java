package com.hypernym.evaconnect.view.bottomsheets;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hypernym.evaconnect.R;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetPictureSelection extends BottomSheetDialogFragment implements View.OnClickListener {
    ViewHolder mHolder;
    Handler handleDismissal = new Handler();

    public BottomSheetPictureSelection() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public BottomSheetPictureSelection(Handler handlerEvents) {
        this.handleDismissal = handlerEvents;
    }

//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//        handleDismissal.sendEmptyMessage(0);
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottomsheet_pictureselection, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
        mHolder.linearLayoutCamera.setOnClickListener(this);
        mHolder.linearLayoutGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layoutCamera:
                handleDismissal.sendEmptyMessage(45);
                dismiss();
                break;
            case R.id.layoutGallery:
                handleDismissal.sendEmptyMessage(50);
                dismiss();
                break;
        }
    }


    public static class ViewHolder {
        LinearLayout linearLayoutGallery,linearLayoutCamera;

        public ViewHolder(View view) {
            linearLayoutGallery = view.findViewById(R.id.layoutGallery);
            linearLayoutCamera = view.findViewById(R.id.layoutCamera);

        }


    }

}