package com.hypernym.evaconnect.view.ui.fragments;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.ConnectionModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.ConnectionsAdapter;
import com.hypernym.evaconnect.view.adapters.OptionsAdapter;
import com.hypernym.evaconnect.view.adapters.RecommendedUser_HorizontalAdapter;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GlobalConnectionsFragment extends BaseFragment implements OptionsAdapter.ItemClickListener, ConnectionsAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.rc_connections)
    RecyclerView rc_connections;

    @BindView(R.id.rc_maincategories)
    RecyclerView rc_maincategories;


    @BindView(R.id.edt_search)
    EditText edt_search;

    @BindView(R.id.empty)
    TextView empty;


    @BindView(R.id.tv_seeAll)
    TextView tv_seeAll;


    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;


    private Boolean search;
    private ConnectionsAdapter connectionsAdapter;
    private RecommendedUser_HorizontalAdapter recommendedUser_horizontalAdapter;
    private List<User> connectionList = new ArrayList<>();
    private List<ConnectionModel> connectedList = new ArrayList<>();
    private List<ConnectionModel> hSearchList = new ArrayList<>();
    private List<User> recommendeduserList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager, linearLayoutManagerHorizontal;
    private ConnectionViewModel connectionViewModel;
    private UserViewModel userViewModel;
    String type = "user";
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private boolean isSearchFlag = false;
    private User user;
    private String search_key;


    public GlobalConnectionsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_global_connected, container, false);
        ButterKnife.bind(this, view);

        connectionViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ConnectionViewModel.class);
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);
        user = LoginUtils.getLoggedinUser();
        currentPage = PAGE_START;
        swipeRefresh.setOnRefreshListener(this);
        initMainOptionsRecView();
        initRecyclerView();
        if (NetworkUtils.isNetworkConnected(getContext())) {
            getConnectionByFilter(type, currentPage, false);
        } else {
            networkErrorDialog();
        }

        if (user.getType().equalsIgnoreCase("user")) {
            edt_search.setHint("Search for a Connection");
        } else {
            edt_search.setHint("Search for a Followers");
        }
        edt_search.addTextChangedListener(new TextWatcher());

        tv_seeAll.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                RecommendedAllUserFragment recommendedAllUserFragment = new RecommendedAllUserFragment();
                loadFragment(R.id.framelayout, recommendedAllUserFragment, getContext(), true);
            }
        });

        return view;
    }

    private void initMainOptionsRecView() {

        recommendedUser_horizontalAdapter = new RecommendedUser_HorizontalAdapter(getContext(), recommendeduserList);
        linearLayoutManagerHorizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rc_maincategories.setLayoutManager(linearLayoutManagerHorizontal);
        rc_maincategories.setAdapter(recommendedUser_horizontalAdapter);
    }

    private void initRecyclerView() {
        connectionsAdapter = new ConnectionsAdapter(getContext(), connectedList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rc_connections.setLayoutManager(linearLayoutManager);
        rc_connections.setAdapter(connectionsAdapter);
        /**
         * add scroll listener while user reach in bottom load more will call
         */
        rc_connections.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage = AppConstants.TOTAL_PAGES + currentPage;
                if (NetworkUtils.isNetworkConnected(getContext())) {
                    //   getConnectionByFilter(type, currentPage, false);
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(View view, int position, boolean isMainCatrgory) {
    }

    public void getConnectionByFilter(String mtype, int currentPage, boolean isSearch) {

        // showDialog();
        User userData = new User();
        swipeRefresh.setRefreshing(false);

        // userData.setType(mtype);
        userData.setUser_id(user.getId());
        userData.setFilter("active");
        if (edt_search.getText().toString().length() > 0)
            userData.setFirst_name(edt_search.getText().toString());
        Log.e("type", mtype);
        //   connectedList.clear();


        connectionViewModel.getConnected(userData, AppConstants.TOTAL_PAGES, currentPage).observe(getViewLifecycleOwner(), listBaseModel -> {
            if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {

                Log.d("Connection_in", String.valueOf(listBaseModel.getData().size()));

//                    if (currentPage == PAGE_START) {
//                        connectionList.clear();
//                        connectionsAdapter.notifyDataSetChanged();
//                    }
                connectedList.clear();
                connectedList.addAll(listBaseModel.getData());
                connectionsAdapter.hSetList(connectedList);
//                    if (connectionList.size() > 0) {
                rc_connections.setVisibility(View.GONE);
                empty.setVisibility(View.GONE);
                //  }
//                    isLoading = false;
            } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {

                if (connectionList.size() == 0) {
                    connectedList.clear();
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
        });

    }

    private void hSearchUser(String hSearch) {

        if (connectionList.size() > 0) {
            for (ConnectionModel user : connectedList) {
                if (user.firstName.contains(hSearch)) ;

            }
        }
    }


    public void getConnectedFilter(boolean isSearch) {

        // showDialog();
        User userData = new User();

        // userData.setType(mtype);
        userData.setUser_id(user.getId());
        userData.setFilter("active");
        if (edt_search.getText().toString().length() > 0)
            userData.setFirst_name(edt_search.getText().toString());
        userData.setLast_name("");
        //   connectedList.clear();
        connectionViewModel.getConnectedFilter(userData).observe(getViewLifecycleOwner(), listBaseModel -> {
            if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {
//                    if (currentPage == PAGE_START) {
//                        connectionList.clear();
//                        connectionsAdapter.notifyDataSetChanged();
//                    }
                connectedList.clear();


                connectedList.addAll(listBaseModel.getData());
                Log.d("TAG", "getConnectedFilter: " + connectedList.size());

                connectionsAdapter.notifyDataSetChanged();
//                    if (connectionList.size() > 0) {
                rc_connections.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
                //  }
//                    isLoading = false;
            } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {

                if (connectionList.size() == 0) {
                    connectedList.clear();
                    rc_connections.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }
//                    isLastPage = true;
//                    // homePostsAdapter.removeLoading();
//                    isLoading = false;
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
        });
    }


    @Override
    public void onItemClick(View view, int position) {
        if (NetworkUtils.isNetworkConnected(getContext())) {
            switch (view.getId()) {
                case R.id.tv_decline:
                    SettingDeclineData(connectionList.get(position));
                    break;

                case R.id.tv_connect:
                    TextView textView = (TextView) view;
                    callConnectApi(textView, connectionList.get(position));
                    //GetUserDetails();
                    if (textView.getText().toString().equalsIgnoreCase(AppConstants.REQUEST_ACCEPT)) {
                        Connection connection = new Connection();
                        User user = LoginUtils.getLoggedinUser();
                        connection.setStatus(AppConstants.ACTIVE);
                        connection.setId(connectionList.get(position).getConnection_id());
                        connection.setModified_by_id(user.getId());
                        connection.setModified_datetime(DateUtils.GetCurrentdatetime());
                        callDeclineConnectApi(connection);
                    }
                    break;

                case R.id.ly_main:


                    try {
                        ConnectionModel user = connectedList.get(position);
                        PersonProfileFragment personProfileFragment = new PersonProfileFragment();
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("user_id", user.id);
                        Log.d("connection", "onItemClick: user " + user.id);
                        bundle2.putParcelable("connected_user", user);
                        Log.d("connection", "onItemClick: " + GsonUtils.toJson(user));
                        loadFragment_bundle(R.id.framelayout, personProfileFragment, getContext(), true, bundle2);

                    } catch (Exception e) {

                        Log.d("connection", "onItemClick: " + position);

                    }

                    break;
            }

        } else {
            networkErrorDialog();
        }
    }

    private void SettingDeclineData(User connectionItem) {
        Connection connection = new Connection();
        User user = LoginUtils.getLoggedinUser();
        connection.setStatus(AppConstants.REQUEST_DECLINE);
        connection.setId(connectionItem.getConnection_id());
        connection.setModified_by_id(user.getId());
        connection.setModified_datetime(DateUtils.GetCurrentdatetime());
        callDeclineConnectApi(connection);
    }

    private void callDeclineConnectApi(Connection connection) {

        connectionViewModel.connect(connection).observe(this, listBaseModel -> {
            if (listBaseModel != null && !listBaseModel.isError()) {

                connectionList.clear();
                connectionsAdapter.notifyDataSetChanged();
                getConnectionByFilter(type, PAGE_START, true);
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
        });
    }

    @Override
    public void onRefresh() {
        if (NetworkUtils.isNetworkConnected(getContext())) {
            // GetUserDetails();
            getConnectionByFilter(type, currentPage, false);
        } else {
            networkErrorDialog();
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


            Log.d("search", "afterTextChanged: " + s);

            hFilterList(s);
         /*   currentPage = PAGE_START;
            if (s.length() > 0) {
                isSearchFlag = true;
                connectionList.clear();
                connectionsAdapter.notifyDataSetChanged();
                getConnectedFilter(true);
            } else {
                isSearchFlag = false;
                getConnectionByFilter(type,currentPage,false);
            }*/
        }

    }

    private void hFilterList(Editable s) {
        if (!s.equals("")) {
            hSearchList.clear();
            for (ConnectionModel user : connectedList) {
                if (user.firstName.toLowerCase(Locale.getDefault()).contains(s)) {
                    hSearchList.add(user);
                }
            }
            connectionsAdapter.hSetList(hSearchList);

        } else {
            connectionsAdapter.hSetList(connectedList);
        }


    }

    private void GetUserDetails() {
        try {
            User user = new User();
            user = LoginUtils.getUser();
            userViewModel.getuser_details(user.getId()
            ).observe(this, listBaseModel -> {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    swipeRefresh.setRefreshing(false);
                    LoginUtils.saveUser(listBaseModel.getData().get(0));
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Subscribe
    public void onEvent(String mtitle) {
        Log.d("news", "onEvent: " + mtitle);
        search_key = mtitle;
        hFilterList(search_key);

    }

    private void hFilterList(String s) {
        if (!s.equals("")) {
            hSearchList.clear();
            for (ConnectionModel user : connectedList) {
                if (user.firstName.toLowerCase(Locale.getDefault()).contains(s)) {
                    hSearchList.add(user);
                }
            }
            rc_connections.setVisibility(View.VISIBLE);

            connectionsAdapter.hSetList(hSearchList);

        } else {
            rc_connections.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

}
