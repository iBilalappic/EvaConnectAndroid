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

public class BottomsheetAttachmentSelection extends BottomSheetDialogFragment implements View.OnClickListener {
    BottomsheetAttachmentSelection.ViewHolder mHolder;
    Handler handleDismissal = new Handler();

    public BottomsheetAttachmentSelection() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public BottomsheetAttachmentSelection(Handler handlerEvents) {
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
        return inflater.inflate(R.layout.layout_bottomsheet_attachment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new BottomsheetAttachmentSelection.ViewHolder(view);
        mHolder.add_photo_video.setOnClickListener(this);
        mHolder.add_document.setOnClickListener(this);
        mHolder.add_url_link.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_photo_video:
                handleDismissal.sendEmptyMessage(50);
                dismiss();
                break;
            case R.id.add_document:
                handleDismissal.sendEmptyMessage(2);
                dismiss();
                break;
            case R.id.add_url_link:
                handleDismissal.sendEmptyMessage(3);
                dismiss();
                break;
        }
    }


    public static class ViewHolder {
        LinearLayout add_photo_video, add_document,add_url_link;

        public ViewHolder(View view) {
            add_photo_video= view.findViewById(R.id.add_photo_video);
            add_document = view.findViewById(R.id.add_document);
            add_url_link = view.findViewById(R.id.add_url_link);

        }


    }
}