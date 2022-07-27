package com.hypernym.evaconnect.view.bottomsheets;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hypernym.evaconnect.R;

public class BottomsheetShareSelection extends BottomSheetDialogFragment implements View.OnClickListener {
    BottomsheetShareSelection.ViewHolder mHolder;
    Handler handleDismissal = new Handler();

    public BottomsheetShareSelection() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public BottomsheetShareSelection(Handler handlerEvents) {
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
        return inflater.inflate(R.layout.layout_bottomsheet_share, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new BottomsheetShareSelection.ViewHolder(view);
        mHolder.share_with_connection.setOnClickListener(this);
        mHolder.share_with_other.setOnClickListener(this);
        mHolder.add_url_link.setOnClickListener(this);
        mHolder.share_with_whatsapp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_with_connection:
                handleDismissal.sendEmptyMessage(100);
                dismiss();
                break;
            case R.id.share_with_other:
                handleDismissal.sendEmptyMessage(101);

                break;
            case R.id.share_with_whatsapp:
                handleDismissal.sendEmptyMessage(102);
                dismiss();
                break;
            case R.id.add_url_link:
                handleDismissal.sendEmptyMessage(103);
                dismiss();
                break;
        }
    }


    public static class ViewHolder {
        LinearLayout share_with_connection, share_with_whatsapp,add_url_link,share_with_other;

        public ViewHolder(View view) {
            share_with_whatsapp= view.findViewById(R.id.share_with_whatsapp);
            share_with_connection = view.findViewById(R.id.share_with_connection);
            add_url_link = view.findViewById(R.id.add_url_link);
            share_with_other = view.findViewById(R.id.share_with_other);

        }


    }
}