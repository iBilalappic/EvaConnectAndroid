package com.hypernym.evaconnect.view.ui.fragments;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.ConnectionModel;
import com.hypernym.evaconnect.models.ShareConnection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.ShareConnectionAdapter;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareConnectionFragment extends BaseFragment implements ShareConnectionAdapter.OnItemClickListener {

    @BindView(R.id.edt_search)
    EditText edt_search;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.rc_connections)
    RecyclerView rc_connections;

    @BindView(R.id.empty)
    TextView empty;

    @BindView(R.id.btn_shareButton)
    TextView btn_shareButton;

    @BindView(R.id.btn_search)
    ImageView btn_search;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    private ShareConnectionAdapter shareConnectionAdapter;
    private List<ConnectionModel> connectionList = new ArrayList<>();
    private List<ConnectionModel> shareConnections_user = new ArrayList<>();
    private List<Integer> share_users = new ArrayList<>();
    private List<ConnectionModel> hSearchList = new ArrayList<>();


    private String type = "user";
    private ConnectionViewModel connectionViewModel;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private boolean isSearchFlag = false;
    private User user;


    private int id;
    private String Share_type, fragment_type;

    public ShareConnectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_connection, container, false);
        ButterKnife.bind(this, view);
        getActivity().findViewById(R.id.seprator_line).setVisibility(View.VISIBLE);
        connectionViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ConnectionViewModel.class);
        currentPage = PAGE_START;
        init();

        initRecyclerView();

        if (NetworkUtils.isNetworkConnected(getContext())) {
            getConnectionByFilter(type, currentPage, false);
        } else {
            networkErrorDialog();
        }
        edt_search.addTextChangedListener(new TextWatcher());


        btn_shareButton.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (shareConnections_user.size() > 0) {
                    if (Share_type != null && Share_type.equals("job")) {
                        SendShareConnections();
                    } else if (Share_type != null && Share_type.equals("event")) {
                        SendShareConnectionsEvent();
                    } else if (Share_type != null && Share_type.equals("post")) {
                        SendShareConnectionsPost();
                    }else if (Share_type != null && Share_type.equals("news")) {
                        SendShareConnectionsNews();
                    } else if (fragment_type != null && fragment_type.equals("JOB_FRAGMENT")) {
                        SendShareConnectionsPost();
                    }
                } else {
                    Toast.makeText(getContext(), "please select users from connections", Toast.LENGTH_SHORT).show();
                }
            }
        });


        img_backarrow.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                getActivity().onBackPressed();
            }
        });


        return view;
    }

    private void SendShareConnections() {
        share_users.clear();
        for (ConnectionModel inviteConnections : shareConnections_user) {
            share_users.add(inviteConnections.connectionId);
        }
        ShareConnection shareConnection = new ShareConnection(LoginUtils.getUser().getId(), share_users);
        shareConnection.setObjectId(id);
        shareConnection.setObjectType("Job");
        connectionViewModel.share_connection(shareConnection).observe(this, listBaseModel -> {
            try {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    Toast.makeText(getContext(), "Successfully shared with desired connection", Toast.LENGTH_SHORT).show();
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
            } catch (Exception e) {
                e.getLocalizedMessage();
            }

            hideDialog();
        });
    }

    private void SendShareConnectionsEvent() {
        share_users.clear();
        for (ConnectionModel inviteConnections : shareConnections_user) {
            share_users.add(inviteConnections.connectionId);
        }
        ShareConnection shareConnection = new ShareConnection(LoginUtils.getUser().getId(), share_users);
        shareConnection.setObjectId(id);
        shareConnection.setObjectType("Event");
        connectionViewModel.share_connection_event(shareConnection).observe(this, listBaseModel -> {
            if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                Toast.makeText(getContext(), "Successfully shared with desired connection", Toast.LENGTH_SHORT).show();
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack();
                }
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
        });
    }

    private void SendShareConnectionsPost() {
        showDialog();

        share_users.clear();
        for (ConnectionModel inviteConnections : shareConnections_user) {
            share_users.add(inviteConnections.connectionId);
        }
        ShareConnection shareConnection = new ShareConnection(LoginUtils.getUser().getId(), share_users);
        shareConnection.setObjectId(id);
        shareConnection.setPostId(id);
        shareConnection.setObjectType("post");
        connectionViewModel.share_connection_post(shareConnection).observe(this, listBaseModel -> {

            try {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    Toast.makeText(getContext(), "Successfully shared with desired connection", Toast.LENGTH_SHORT).show();
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }

            } catch (Exception e) {
                e.getLocalizedMessage();
            }


            hideDialog();
        });
    }

    private void SendShareConnectionsNews() {
        share_users.clear();
        for (ConnectionModel inviteConnections : shareConnections_user) {
            share_users.add(inviteConnections.connectionId);
        }

        ShareConnection shareConnection = new ShareConnection(LoginUtils.getUser().getId(), share_users);
        shareConnection.setObjectId(id);
        shareConnection.setObjectType("news");

        connectionViewModel.share_connection_news(shareConnection).observe(this, listBaseModel -> {
            if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                Toast.makeText(getContext(), "Successfully shared with desired connection", Toast.LENGTH_SHORT).show();
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack();
                }
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
        });
    }

    private void init() {
        user = LoginUtils.getLoggedinUser();

        Log.d("TAG", "init: " + GsonUtils.toJson(user));


        if ((getArguments() != null)) {
            id = getArguments().getInt(Constants.DATA);
            Share_type = getArguments().getString(Constants.TYPE);
            fragment_type = getArguments().getString(Constants.FRAGMENT_NAME);
        }

    }

    public void getConnectionByFilter(String mtype, int currentPage, boolean isSearch) {

        // showDialog();
        User userData = new User();
        swipeRefresh.setRefreshing(false);


        userData.setUser_id(user.getId());


        userData.setFilter("active");
        if (edt_search.getText().toString().length() > 0)
            userData.setFirst_name(edt_search.getText().toString());
        Log.e("type", mtype);
        //   connectedList.clear();
        connectionViewModel.getConnected(userData, AppConstants.TOTAL_PAGES, currentPage).observe(getViewLifecycleOwner(), new Observer<BaseModel<List<ConnectionModel>>>() {
            @Override
            public void onChanged(BaseModel<List<ConnectionModel>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {

                    Log.d("Connection_in", String.valueOf(listBaseModel.getData().size()));

//                    if (currentPage == PAGE_START) {
//                        connectionList.clear();
//                        connectionsAdapter.notifyDataSetChanged();
//                    }
                    connectionList.clear();
                    connectionList.addAll(listBaseModel.getData());
                    shareConnectionAdapter.notifyDataSetChanged();
//                    if (connectionList.size() > 0) {
                    rc_connections.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                    //  }
//                    isLoading = false;
                } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {

                    if (connectionList.size() == 0) {
                        connectionList.clear();
                        rc_connections.setVisibility(View.GONE);
                        empty.setVisibility(View.VISIBLE);
                        empty.setText("No Connection Found");
                    }
//                    isLastPage = true;
//                    // homePostsAdapter.removeLoading();
//                    isLoading = false;
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
            }
        });
    }

    private void initRecyclerView() {
        shareConnectionAdapter = new ShareConnectionAdapter(getContext(), connectionList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rc_connections.setLayoutManager(linearLayoutManager);
        rc_connections.setAdapter(shareConnectionAdapter);

        /*
         * add scroll listener while user reach in bottom load more will call
         */
        rc_connections.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage = AppConstants.TOTAL_PAGES + currentPage;
                if (NetworkUtils.isNetworkConnected(getContext())) {
                    getConnectionByFilter(type, currentPage, false);
                } else {
                    networkErrorDialog();
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    @Override
    public void onItemClick(View view, int position, ConnectionModel data) {
        switch (view.getId()) {
            case R.id.tv_share:
                addConnection(data);
                break;
            case R.id.imageView9:
                removeConnection(data);
                break;
        }
    }

    private void removeConnection(ConnectionModel data) {
        List<ConnectionModel> newshareConnections_user = new ArrayList<>();
        newshareConnections_user.addAll(shareConnections_user);
        for (ConnectionModel connection : newshareConnections_user) {
            if (connection.connectionId == data.id)
                shareConnections_user.remove(connection);
        }
    }

    private void addConnection(ConnectionModel data) {
        shareConnections_user.add(data);
        shareConnectionAdapter.notifyDataSetChanged();
        Log.d("TAAAG", "" + GsonUtils.toJson(shareConnections_user));
    }


    public class TextWatcher implements android.text.TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            //  currentPage = PAGE_START;
       /*     if (s.length() > 0) {
                isSearchFlag = true;
            } else {
                isSearchFlag = false;
                currentPage = 0;
                PAGE_START = 0;
            }

            connectionList.clear();
            shareConnectionAdapter.notifyDataSetChanged();
            getConnectionByFilter(type, PAGE_START, true);*/


            if (!s.equals("")) {
                hSearchList.clear();
                for (ConnectionModel user : connectionList) {
                    if (user.firstName.toLowerCase(Locale.getDefault()).contains(s)) {
                        hSearchList.add(user);
                    }
                }
                shareConnectionAdapter.hSetList(hSearchList);

            } else {
                shareConnectionAdapter.hSetList(connectionList);
            }

        }

    }
}
