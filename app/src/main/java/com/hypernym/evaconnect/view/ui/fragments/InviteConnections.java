package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.InvitedConnectionListener;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.utils.PrefUtils;
import com.hypernym.evaconnect.view.adapters.InviteConnectionsAdapter;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

/**
 * A simple {@link Fragment} subclass.
 */
public class InviteConnections extends BaseFragment implements InviteConnectionsAdapter.OnItemClickListener {

    @BindView(R.id.edt_search)
    EditText edt_search;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.rc_connections)
    RecyclerView rc_connections;

    @BindView(R.id.empty)
    TextView empty;

    @BindView(R.id.inviteButton)
    TextView inviteButton;

    @BindView(R.id.btn_search)
    ImageView btn_search;

    private InviteConnectionsAdapter inviteConnectionsAdapter;
    private List<User> connectionList = new ArrayList<>();
    private List<User> invitedConnections = new ArrayList<>();
    private String type = "user";
    private ConnectionViewModel connectionViewModel;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private boolean isSearchFlag = false;
    private final String TAG = InviteConnections.class.getSimpleName();
    private String fragment_type = "";
    List<User> users=new ArrayList<>();
    InvitedConnectionListener mListener;

    public InviteConnections(InvitedConnectionListener eventListener) {
        this.mListener=eventListener;
        // Required empty public constructor
    }

    public InviteConnections() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invite_connections, container, false);
        ButterKnife.bind(this, view);
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

        // avoid double click
        inviteButton.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                Log.e(TAG, "onSingleClick: " + GsonUtils.toJson(invitedConnections));
                mListener.invitedConnections(invitedConnections);
                if (fragment_type.equals(Constants.FRAGMENT_NAME_2)) {
                    PrefUtils.persistConnections(getContext(), invitedConnections);
                } else if (fragment_type.equals(Constants.FRAGMENT_NAME_1)) {
                    PrefUtils.persistConnectionsMeeting(getContext(), invitedConnections);
                }

                if (invitedConnections != null && invitedConnections.size() > 0) {
                    // go back
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }
                }
            }
        });

        return view;
    }
    public void setCustomEventListener(InvitedConnectionListener eventListener) {
        mListener = eventListener;
    }
    private void getConnectionByFilter(String type, int currentPage, boolean b) {
        User userData = new User();
        User user = LoginUtils.getLoggedinUser();
        userData.setType(type);
        userData.setUser_id(user.getId());

        if (edt_search.getText().toString().length() > 0)
            userData.setFirst_name(edt_search.getText().toString());

        Log.e("type", type);

        connectionViewModel.getConnectionByFilter(userData, AppConstants.TOTAL_PAGES, currentPage)
                .observe(this, listBaseModel ->
                {
                    if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {
                        if (currentPage == PAGE_START) {
                            connectionList.clear();
                            inviteConnectionsAdapter.notifyDataSetChanged();
                        }
                        connectionList.addAll(listBaseModel.getData());
                        inviteConnectionsAdapter.notifyDataSetChanged();
                       // PrefUtils.persistConnectionsMeeting(getContext(),new ArrayList<>());
                        //PrefUtils.persistConnections(getContext(),new ArrayList<>());
                        if (connectionList.size() > 0) {
                            rc_connections.setVisibility(View.VISIBLE);
                            empty.setVisibility(View.GONE);
                        }

                        isLoading = false;
                    } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {
                        rc_connections.setVisibility(View.GONE);
                        empty.setVisibility(View.VISIBLE);
                        isLastPage = true;
                        isLoading = false;
                    } else {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                    }
                });
    }

    private void initRecyclerView() {
        inviteConnectionsAdapter = new InviteConnectionsAdapter(getContext(), connectionList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rc_connections.setLayoutManager(linearLayoutManager);
        rc_connections.setAdapter(inviteConnectionsAdapter);

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

    private void init() {
        setPageTitle("Invite Connections");
        //
        Bundle bundle = getArguments();
        if (bundle != null) {
            fragment_type = bundle.getString(Constants.FRAGMENT_TYPE);

            if (fragment_type.equals(Constants.FRAGMENT_NAME_1)) {
                inviteButton.setText("Invite Connections to Meeting");
                users=bundle.getParcelableArrayList("connections");
                //users=PrefUtils.getConnectionsMeeting(getContext());
            } else {

                inviteButton.setText("Invite Connections to Event");
                users=PrefUtils.getConnections(getContext());
            }
        }
    }

    @Override
    public void onItemClick(View view, int position, User data) {
        switch (view.getId()) {
            case R.id.tv_invite:
                addConnection(data);
                break;
            case R.id.imageView9:
                removeConnection(data);
                break;
        }
    }

    private void addConnection(User data) {
        invitedConnections.add(data);
        Log.e(TAG, "onItemClick: " + GsonUtils.toJson(invitedConnections));

        if (fragment_type.equals(Constants.FRAGMENT_NAME_2)) {
            if (invitedConnections.size() > 0)
                inviteButton.setText("Invite (" + invitedConnections.size() + ")" + " Connections to Event");
            else
                inviteButton.setText("Invite Connections to Event");
        } else {
            if (invitedConnections.size() > 0)
                inviteButton.setText("Invite (" + invitedConnections.size() + ")" + " Connections to Meeting");
            else
                inviteButton.setText("Invite Connections to Meeting");
        }
    }

    private void removeConnection(User data) {
        for (User connection : invitedConnections) {
            if (connection.getId() == data.getId())
                invitedConnections.remove(connection);
        }

        Log.e(TAG, "onItemClick: " + GsonUtils.toJson(invitedConnections));

        if (fragment_type.equals(Constants.FRAGMENT_NAME_2)) {
            if (invitedConnections.size() > 0)
                inviteButton.setText("Invite (" + invitedConnections.size() + ")" + " Connections to Event");
            else
                inviteButton.setText("Invite Connections to Event");
        } else {
            if (invitedConnections.size() > 0)
                inviteButton.setText("Invite (" + invitedConnections.size() + ")" + " Connections to Meeting");
            else
                inviteButton.setText("Invite Connections to Meeting");
        }
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
            currentPage = PAGE_START;
            if (s.length() > 0) {
                isSearchFlag = true;
            } else {
                isSearchFlag = false;
            }

            connectionList.clear();
            inviteConnectionsAdapter.notifyDataSetChanged();
            getConnectionByFilter(type, PAGE_START, true);

        }

    }
}
