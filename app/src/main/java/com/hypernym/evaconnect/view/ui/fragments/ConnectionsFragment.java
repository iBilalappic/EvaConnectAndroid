package com.hypernym.evaconnect.view.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.view.adapters.ConnectionsAdapter;
import com.hypernym.evaconnect.view.adapters.OptionsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectionsFragment extends Fragment {
    @BindView(R.id.rc_connections)
    RecyclerView rc_connections;

    @BindView(R.id.tv_everyone)
    TextView tv_everyone;

    @BindView(R.id.tv_people)
    TextView tv_people;

    @BindView(R.id.tv_companies)
    TextView tv_companies;

    @BindView(R.id.rc_maincategories)
    RecyclerView rc_maincategories;
    List<String> mainCategories=new ArrayList<>();

    private ConnectionsAdapter connectionsAdapter;
    private List<Connection> connectionList =new ArrayList<>();
    private OptionsAdapter optionsAdapter;
    private LinearLayoutManager linearLayoutManager;

    public ConnectionsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_connections, container, false);
        ButterKnife.bind(this,view);
        initOptionsRecView();
        initRecyclerView();
        selectEveryOneOption();
        return view;
    }

    private void initOptionsRecView() {
        mainCategories.add(getString(R.string.everyone));
        mainCategories.add(getString(R.string.people));
        mainCategories.add(getString(R.string.companies));
        optionsAdapter=new OptionsAdapter(getContext(),mainCategories);
        linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, true);
        rc_maincategories.setLayoutManager(linearLayoutManager);
        rc_maincategories.setAdapter(optionsAdapter);
    }

    @OnClick(R.id.tv_everyone)
    public void selectEveryOneOption() {
        tv_companies.setTextColor(getResources().getColor(R.color.gray));
        tv_people.setTextColor(getResources().getColor(R.color.gray));
        tv_everyone.setTextColor(getResources().getColor(R.color.light_black));
        tv_everyone.setElevation(2);
    }

    @OnClick(R.id.tv_companies)
    public void selectCompanyOption() {
        tv_companies.setTextColor(getResources().getColor(R.color.light_black));
        tv_people.setTextColor(getResources().getColor(R.color.gray));
        tv_everyone.setTextColor(getResources().getColor(R.color.gray));
        tv_companies.setElevation(2);
    }
    @OnClick(R.id.tv_people)
    public void selectPeopleOption() {
        tv_companies.setTextColor(getResources().getColor(R.color.gray));
        tv_people.setTextColor(getResources().getColor(R.color.light_black));
        tv_everyone.setTextColor(getResources().getColor(R.color.gray));
        tv_people.setElevation(2);
    }

    private void initRecyclerView() {
        connectionList.add(new Connection());
        connectionList.add(new Connection());
        connectionList.add(new Connection());
        connectionsAdapter=new ConnectionsAdapter(getContext(), connectionList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rc_connections.setLayoutManager(linearLayoutManager);
        rc_connections.setAdapter(connectionsAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
