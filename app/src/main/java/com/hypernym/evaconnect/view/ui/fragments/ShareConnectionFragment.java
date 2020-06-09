package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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

import com.facebook.share.Share;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.ShareConnection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.utils.PrefUtils;
import com.hypernym.evaconnect.view.adapters.InviteConnectionsAdapter;
import com.hypernym.evaconnect.view.adapters.ShareConnectionAdapter;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

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

    private ShareConnectionAdapter shareConnectionAdapter;
    private List<User> connectionList = new ArrayList<>();
    private List<User> shareConnections_user = new ArrayList<>();
    private List<Integer> share_users = new ArrayList<>();

    private String type = "user";
    private ConnectionViewModel connectionViewModel;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private boolean isSearchFlag = false;

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
        connectionViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ConnectionViewModel.class);
        currentPage = PAGE_START;

        initRecyclerView();

        if (NetworkUtils.isNetworkConnected(getContext())) {
            getConnectionByFilter(type, currentPage, false);
        } else {
            networkErrorDialog();
        }
        edt_search.addTextChangedListener(new TextWatcher());

        init();

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
                    } else if (fragment_type.equals("JOB_FRAGMENT")) {
                        SendShareConnectionsPost();
                    }
                } else {
                    Toast.makeText(getContext(), "please select users from connections", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void SendShareConnections() {
        share_users.clear();
        for (User inviteConnections : shareConnections_user) {
            share_users.add(inviteConnections.getId());
        }
        ShareConnection shareConnection = new ShareConnection(LoginUtils.getUser().getId(), share_users);
        shareConnection.setJob_id(id);
        connectionViewModel.share_connection(shareConnection).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    Toast.makeText(getContext(), "successfully shared with desired connection", Toast.LENGTH_SHORT).show();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void SendShareConnectionsEvent() {
        share_users.clear();
        for (User inviteConnections : shareConnections_user) {
            share_users.add(inviteConnections.getId());
        }
        ShareConnection shareConnection = new ShareConnection(LoginUtils.getUser().getId(), share_users);
        shareConnection.setEvent_id(id);
        connectionViewModel.share_connection_event(shareConnection).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    Toast.makeText(getContext(), "successfully shared with desired connection", Toast.LENGTH_SHORT).show();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void SendShareConnectionsPost() {
        share_users.clear();
        for (User inviteConnections : shareConnections_user) {
            share_users.add(inviteConnections.getId());
        }
        ShareConnection shareConnection = new ShareConnection(LoginUtils.getUser().getId(), share_users);
        shareConnection.setPost_id(id);
        connectionViewModel.share_connection_post(shareConnection).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    Toast.makeText(getContext(), "successfully shared with desired connection", Toast.LENGTH_SHORT).show();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void init() {
        if ((getArguments() != null)) {
            id = getArguments().getInt(Constants.DATA);
            Share_type = getArguments().getString(Constants.TYPE);
            fragment_type = getArguments().getString(Constants.FRAGMENT_NAME);
        }

    }

    private void getConnectionByFilter(String type, int currentPage, boolean b) {
        User userData = new User();
        User user = LoginUtils.getLoggedinUser();
        userData.setUser_id(user.getId());
        user.setConnection_status("active");
        if (edt_search.getText().toString().length() > 0)
            userData.setFirst_name(edt_search.getText().toString());


        connectionViewModel.getConnectionByFilter(userData, AppConstants.TOTAL_PAGES, currentPage)
                .observe(this, listBaseModel ->
                {
                    if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {
                        if (currentPage == PAGE_START) {
                            connectionList.clear();
                            shareConnectionAdapter.notifyDataSetChanged();
                        }

                        connectionList.addAll(listBaseModel.getData());
                        shareConnectionAdapter.notifyDataSetChanged();

                        if (connectionList.size() > 0) {
                            rc_connections.setVisibility(View.VISIBLE);
                            isLastPage = false;
                            isLoading = true;
                            empty.setVisibility(View.GONE);
                        }

                        isLoading = false;
                    } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {
                        if (listBaseModel.getData().size() == 0 && currentPage == 0) {
                            rc_connections.setVisibility(View.GONE);
                            empty.setVisibility(View.VISIBLE);
                        }
//                        rc_connections.setVisibility(View.GONE);
//                        empty.setVisibility(View.VISIBLE);
                        isLastPage = true;
                        isLoading = false;
                    } else {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
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
    public void onItemClick(View view, int position, User data) {
        switch (view.getId()) {
            case R.id.tv_share:
                addConnection(data);
                break;
            case R.id.imageView9:
                removeConnection(data);
                break;
        }
    }

    private void removeConnection(User data) {
        for (User connection : shareConnections_user) {
            if (connection.getId() == data.getId())
                shareConnections_user.remove(connection);
        }
    }

    private void addConnection(User data) {
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
            if (s.length() > 0) {
                isSearchFlag = true;
            } else {
                isSearchFlag = false;
                currentPage = 0;
                PAGE_START = 0;
            }

            connectionList.clear();
            shareConnectionAdapter.notifyDataSetChanged();
            getConnectionByFilter(type, PAGE_START, true);

        }

    }
}
