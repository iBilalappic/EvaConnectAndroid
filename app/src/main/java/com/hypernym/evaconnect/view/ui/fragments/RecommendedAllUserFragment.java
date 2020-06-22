package com.hypernym.evaconnect.view.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.ConnectionsAdapter;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

public class RecommendedAllUserFragment extends BaseFragment implements ConnectionsAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.rc_connections)
    RecyclerView rc_connections;


    @BindView(R.id.edt_search)
    EditText edt_search;

    @BindView(R.id.empty)
    TextView empty;


    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;


    private ConnectionsAdapter connectionsAdapter;
    private List<User> connectionList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private ConnectionViewModel connectionViewModel;
    private UserViewModel userViewModel;

    private int freshpage = 0;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private boolean isSearchFlag = false;
    private boolean isFirstload=false;
    String type = "user";
    public RecommendedAllUserFragment() {
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
        View view = inflater.inflate(R.layout.fragment_recommended_user_all, container, false);
        ButterKnife.bind(this, view);
        connectionViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ConnectionViewModel.class);
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);

        freshpage = 0;
        swipeRefresh.setOnRefreshListener(this);
        initRecyclerView();
        setPageTitle(getString(R.string.connections));
        if (NetworkUtils.isNetworkConnected(getContext())) {
            getConnectionByFilter(freshpage, false);
        } else {
            networkErrorDialog();
        }
        edt_search.addTextChangedListener(new TextWatcher());
        return view;
    }


    private void initRecyclerView() {

        connectionsAdapter = new ConnectionsAdapter(getContext(), connectionList, this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rc_connections.setLayoutManager(linearLayoutManager);
        rc_connections.setAdapter(connectionsAdapter);
        /**
         * add scroll listener while user reach in bottom load more will call
         */
        rc_connections.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                freshpage = AppConstants.TOTAL_NEW_PAGES + freshpage;
                if (connectionList.size()>=9) {
                    if (NetworkUtils.isNetworkConnected(getContext())) {
                        getConnectionByFilter(freshpage, false);
                          isFirstload=isLoading;

                    } else {
                        networkErrorDialog();
                    }
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


    public void getConnectionByFilter(int currentPage, boolean isSearch) {

        // showDialog();
        User userData = new User();
        User user = LoginUtils.getLoggedinUser();
        userData.setUser_id(user.getId());
        if (edt_search.getText().toString().length() > 0)
            userData.setFirst_name(edt_search.getText().toString());

        connectionViewModel.getConnectionByRecommendedUser(userData, AppConstants.TOTAL_NEW_PAGES, currentPage).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {
                    if (currentPage == PAGE_START) {
                        connectionList.clear();
                        connectionsAdapter.notifyDataSetChanged();
                    }
                    connectionList.addAll(listBaseModel.getData());
                    connectionsAdapter.notifyDataSetChanged();
                    if (connectionList.size() > 0) {
                        rc_connections.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.GONE);
                    }
                    isLoading = false;
                } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {
                    rc_connections.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.VISIBLE);
                    isLastPage = true;
                    // homePostsAdapter.removeLoading();
                    isLoading = false;
                } else {
                    rc_connections.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
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
                    if(textView.getText().toString().equalsIgnoreCase(AppConstants.REQUEST_ACCEPT)){
                        Connection connection = new Connection();
                        User user = LoginUtils.getLoggedinUser();
                        connection.setStatus(AppConstants.ACTIVE);
                        connection.setId(connectionList.get(position).getConnection_id());
                        connection.setModified_by_id(user.getId());
                        connection.setModified_datetime(DateUtils.GetCurrentdatetime());
                        callDeclineConnectApi(connection);
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

        connectionViewModel.connect(connection).observe(this, new Observer<BaseModel<List<Connection>>>() {
            @Override
            public void onChanged(BaseModel<List<Connection>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {

                    connectionList.clear();
                    connectionsAdapter.notifyDataSetChanged();
                    getConnectionByFilter(PAGE_START, true);
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    @Override
    public void onRefresh() {
        if (NetworkUtils.isNetworkConnected(getContext())) {
            GetUserDetails();
//            if (freshpage != 0) {
//                getConnectionByFilter(freshpage, false);
//            }else{
//                getConnectionByFilter(0, false);
//            }

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
            freshpage = PAGE_START;
            if (s.length() > 0) {
                isSearchFlag = true;
            } else {
                isSearchFlag = false;
            }

            connectionList.clear();
            connectionsAdapter.notifyDataSetChanged();
            getConnectionByFilter(PAGE_START, true);

        }
    }

    private void GetUserDetails() {
        User user = new User();
        user = LoginUtils.getUser();
        userViewModel.getuser_details(user.getId()
        ).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    swipeRefresh.setRefreshing(false);
                    LoginUtils.saveUser(listBaseModel.getData().get(0));
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }
}
