package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.Comments;
import com.hypernym.evaconnect.view.adapters.CommentsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailsFragment extends Fragment {

    @BindView(R.id.rc_comments)
    RecyclerView rc_comments;

    private CommentsAdapter commentsAdapter;
    private List<Comments> comments=new ArrayList<>();

    public PostDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_post_details, container, false);
        ButterKnife.bind(this,view);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        comments.add(new Comments());
        comments.add(new Comments());
        comments.add(new Comments());
        commentsAdapter=new CommentsAdapter(getContext(),comments);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rc_comments.setLayoutManager(linearLayoutManager);
        rc_comments.setAdapter(commentsAdapter);
    }

}
