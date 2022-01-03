package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.hypernym.evaconnect.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CreateNoteFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    public CreateNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        img_backarrow.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_backarrow) {
            Objects.requireNonNull(getActivity()).onBackPressed();
        }
    }
}