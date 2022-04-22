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
import android.widget.ImageView;
import android.widget.TextView;

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
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.GetPendingData;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.OptionsAdapter;
import com.hypernym.evaconnect.view.adapters.PendingAdapter;
import com.hypernym.evaconnect.view.adapters.RecommendedUser_HorizontalAdapter;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PendingFragment extends BaseFragment implements OptionsAdapter.ItemClickListener, PendingAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

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





    private PendingAdapter pendingAdapter;
    private RecommendedUser_HorizontalAdapter recommendedUser_horizontalAdapter;
    private List<User> connectionList = new ArrayList<>();
    private List<GetPendingData> pendingList = new ArrayList<>();
    private List<User> recommendeduserList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager, linearLayoutManagerHorizontal;
    private ConnectionViewModel connectionViewModel;
    private UserViewModel userViewModel;
    String type = "user";
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private boolean isSearchFlag = false;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connections, container, false);
        ButterKnife.bind(this, view);

        connectionViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ConnectionViewModel.class);
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);

        currentPage = PAGE_START;
        swipeRefresh.setOnRefreshListener(this);
        initMainOptionsRecView();
        initRecyclerView();
        user = LoginUtils.getLoggedinUser();

        setPageTitle(getString(R.string.connections));
        if (NetworkUtils.isNetworkConnected(getContext())) {
            getAllPending();
        } else {
            networkErrorDialog();
        }
        if(user.getType().equalsIgnoreCase("user")){
            edt_search.setHint("Search for a Connection");
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

    private void getConnectionByRecommendedUser() {
        User userData = new User();
        User user = LoginUtils.getLoggedinUser();
        userData.setType(type);
        userData.setUser_id(user.getId());
        connectionViewModel.getConnectionByRecommendedUser(userData, 6, 0).observe(getViewLifecycleOwner(), new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    recommendeduserList.addAll(listBaseModel.getData());
                    recommendedUser_horizontalAdapter.notifyDataSetChanged();
                    getConnectionByFilter(type, currentPage, false);
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void getAllPending() {
        showDialog();
        connectionViewModel.getAllPending(AppConstants.TOTAL_PAGES, currentPage).observe(getViewLifecycleOwner(), new Observer<BaseModel<List<GetPendingData>>>() {
            @Override
            public void onChanged(BaseModel<List<GetPendingData>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    pendingList.clear();
                    pendingList.addAll(listBaseModel.getData());
                    pendingAdapter.notifyDataSetChanged();
                    if (connectionList.size() > 0) {
                        rc_connections.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.GONE);
                    }else{
                        rc_connections.setVisibility(View.GONE);
                        empty.setVisibility(View.VISIBLE);
                    }
                    isLoading = false;
                } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {
                    isLastPage = true;
                    // homePostsAdapter.removeLoading();
                    isLoading = false;
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void initMainOptionsRecView() {

        recommendedUser_horizontalAdapter = new RecommendedUser_HorizontalAdapter(getContext(), recommendeduserList);
        linearLayoutManagerHorizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rc_maincategories.setLayoutManager(linearLayoutManagerHorizontal);
        rc_maincategories.setAdapter(recommendedUser_horizontalAdapter);
    }

    private void initRecyclerView() {
        pendingAdapter = new PendingAdapter(getContext(), pendingList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rc_connections.setLayoutManager(linearLayoutManager);
        rc_connections.setAdapter(pendingAdapter);
        /**
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
        User user = LoginUtils.getLoggedinUser();
        // userData.setType(mtype);
        userData.setUser_id(user.getId());
        if (edt_search.getText().toString().length() > 0)
            userData.setFirst_name(edt_search.getText().toString());
        Log.e("type", mtype);

        connectionViewModel.getConnectionByFilter(userData, AppConstants.TOTAL_PAGES, currentPage).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {
                    if (currentPage == PAGE_START) {
                        connectionList.clear();
                        pendingAdapter.notifyDataSetChanged();
                    }
                    //
                    connectionList.addAll(listBaseModel.getData());
                    pendingAdapter.notifyDataSetChanged();
                    if (connectionList.size() > 0) {
                        rc_connections.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.GONE);
                    }else{
                        rc_connections.setVisibility(View.GONE);
                        empty.setVisibility(View.VISIBLE);
                    }
                    isLoading = false;
                } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {

                    if(connectionList.size()==0)
                    {
                        rc_connections.setVisibility(View.GONE);
                        empty.setVisibility(View.VISIBLE);
                    }
                    isLastPage = true;
                    // homePostsAdapter.removeLoading();
                    isLoading = false;
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        if (NetworkUtils.isNetworkConnected(getContext())) {
            switch (view.getId()) {
                case R.id.iv_reject:
                    SettingDeclineData(pendingList.get(position), position);
                    break;

                case R.id.iv_accept:
                    ImageView textView = (ImageView) view;
                   // callConnectApi(pendingList.get(position));
                    //GetUserDetails();

                        Connection connection = new Connection();
                        User user2 = LoginUtils.getLoggedinUser();
                        connection.setStatus(AppConstants.ACTIVE);
                        connection.setId(pendingList.get(position).getId());
                    connection.setSender_id(pendingList.get(position).getSenderId());
                    connection.setReceiver_id(pendingList.get(position).getReceiverId());
                        connection.setModified_by_id(user2.getId());
                        connection.setModified_datetime(DateUtils.GetCurrentdatetime());
                        callDeclineConnectApi(connection, position);
                    break;

                case R.id.ly_main:
                    User user = connectionList.get(position);
                    PersonProfileFragment personProfileFragment = new PersonProfileFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putParcelable("user", user);
                    loadFragment_bundle(R.id.framelayout, personProfileFragment, getContext(), true, bundle2);
                    break;

            }

        } else {
            networkErrorDialog();
        }
    }

    private void SettingDeclineData(GetPendingData connectionItem, int position) {
        Connection connection = new Connection();
        User user = LoginUtils.getLoggedinUser();
        connection.setStatus(AppConstants.REQUEST_DECLINE);
        connection.setId(connectionItem.getId());
        connection.setSender_id(connectionItem.getSenderId());
        connection.setReceiver_id(connectionItem.getReceiverId());
        connection.setModified_by_id(user.getId());
        connection.setModified_datetime(DateUtils.GetCurrentdatetime());
        callDeclineConnectApi(connection,position);
    }

    private void SettingAcceptData(GetPendingData connectionItem, int position) {
        Connection connection = new Connection();
        User user = LoginUtils.getLoggedinUser();
        connection.setStatus(AppConstants.REQUEST_ACCEPT);
        connection.setId(connectionItem.getId());
        connection.setSender_id(connectionItem.getSenderId());
        connection.setReceiver_id(connectionItem.getReceiverId());
        connection.setModified_by_id(user.getId());
        connection.setModified_datetime(DateUtils.GetCurrentdatetime());
        callDeclineConnectApi(connection,position);
    }

    private void callDeclineConnectApi(Connection connection, int position) {

        connectionViewModel.connect(connection).observe(this, new Observer<BaseModel<List<Connection>>>() {
            @Override
            public void onChanged(BaseModel<List<Connection>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {

                    //pendingList.clear();
                    pendingAdapter.removeAt(position);
                   // getConnectionByFilter(type, PAGE_START, true);
                } else {
                    networkResponseDialog(getString(R.string.error), listBaseModel.getMessage());
                }
                hideDialog();
            }
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
            currentPage = PAGE_START;
            if (s.length() > 0) {
                isSearchFlag = true;
                pendingList.clear();
                pendingAdapter.notifyDataSetChanged();
                getConnectedFilter(true);
            } else {
                isSearchFlag = false;
                getAllPending();
            }

//            connectionList.clear();
//            pendingAdapter.notifyDataSetChanged();
//            getConnectionByFilter(type, PAGE_START, true);

        }

    }


        public void getConnectedFilter(boolean isSearch) {

            // showDialog();
            User userData = new User();

            // userData.setType(mtype);
            userData.setUser_id(user.getId());
            userData.setFilter("pending");
            if (edt_search.getText().toString().length() > 0)
                userData.setFirst_name(edt_search.getText().toString());
            //   connectedList.clear();
            connectionViewModel.getPendingFilter(userData).observe(getViewLifecycleOwner(), new Observer<BaseModel<List<GetPendingData>>>() {
                @Override
                public void onChanged(BaseModel<List<GetPendingData>> listBaseModel) {
                    if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {
    //                    if (currentPage == PAGE_START) {
    //                        connectionList.clear();
    //                        connectionsAdapter.notifyDataSetChanged();
    //                    }
                        pendingList.clear();
                        pendingList.addAll(listBaseModel.getData());
                        pendingAdapter.notifyDataSetChanged();
    //                    if (connectionList.size() > 0) {
                        rc_connections.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.GONE);

                        //  }
    //                    isLoading = false;
                    } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {

                        if(connectionList.size()==0)
                        {
                            pendingList.clear();
                            rc_connections.setVisibility(View.GONE);
                            empty.setVisibility(View.VISIBLE);
                            empty.setText("No Pending Request Found");
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

}