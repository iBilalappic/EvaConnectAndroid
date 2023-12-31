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
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.GetBlockedData;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.BlockedAdapter;
import com.hypernym.evaconnect.view.adapters.OptionsAdapter;
import com.hypernym.evaconnect.view.adapters.RecommendedUser_HorizontalAdapter;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BlockedFragment extends BaseFragment implements OptionsAdapter.ItemClickListener, BlockedAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

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


    private BlockedAdapter blockedAdapter;
    private RecommendedUser_HorizontalAdapter recommendedUser_horizontalAdapter;
    private List<GetBlockedData> connectionList = new ArrayList<>();
    private List<User> recommendeduserList = new ArrayList<>();
    private List<GetBlockedData> hSearchList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager, linearLayoutManagerHorizontal;
    private ConnectionViewModel connectionViewModel;
    private UserViewModel userViewModel;
    String type = "user";
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private boolean isSearchFlag = false;
    public BlockedFragment() {
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
        View view = inflater.inflate(R.layout.fragment_connections, container, false);
        ButterKnife.bind(this, view);

        connectionViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ConnectionViewModel.class);
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);

        currentPage = PAGE_START;
        swipeRefresh.setOnRefreshListener(this);
        edt_search.setHint("Search for a Blocked User");
        initMainOptionsRecView();
        initRecyclerView();
        if (NetworkUtils.isNetworkConnected(getContext())) {
            getBlockedConnections();
        } else {
            networkErrorDialog();
        }
        edt_search.addTextChangedListener(new BlockedFragment.TextWatcher());

        tv_seeAll.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                RecommendedAllUserFragment recommendedAllUserFragment = new RecommendedAllUserFragment();
                loadFragment(R.id.framelayout, recommendedAllUserFragment, getContext(), true);
            }
        });

        return view;
    }

    private void getBlockedConnections() {
        showDialog();
        User userData = new User();
        User user = LoginUtils.getLoggedinUser();
        userData.setType(type);
        userData.setUser_id(user.getId());
        connectionViewModel.getBlockedUsers().observe(getViewLifecycleOwner(), listBaseModel -> {

            if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {
                if (currentPage == PAGE_START) {
                    connectionList.clear();
                    blockedAdapter.notifyDataSetChanged();
                }
                connectionList.clear();
                connectionList.addAll(listBaseModel.getData());
                blockedAdapter.notifyDataSetChanged();
                if (connectionList.size() > 0) {
                    rc_connections.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                }else{
                    rc_connections.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }
                isLoading = false;
            } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {

                if (connectionList.size() == 0) {
                    rc_connections.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }
                isLastPage = true;
                // homePostsAdapter.removeLoading();
                isLoading = false;
            } else {
                networkResponseDialog(getString(R.string.error), listBaseModel.getMessage());
            }

            hideDialog();

            swipeRefresh.setRefreshing(false);

        });
    }

    /*private void getUserConnections() {
        showDialog();
        connectionViewModel.getAllConnections(AppConstants.TOTAL_PAGES, currentPage).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    connectionList.clear();
                    connectionList.addAll(listBaseModel.getData());
                    blockedAdapter.notifyDataSetChanged();
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
    }*/

    private void initMainOptionsRecView() {
        recommendedUser_horizontalAdapter = new RecommendedUser_HorizontalAdapter(getContext(), recommendeduserList);
        linearLayoutManagerHorizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rc_maincategories.setLayoutManager(linearLayoutManagerHorizontal);
        rc_maincategories.setAdapter(recommendedUser_horizontalAdapter);
    }

    private void initRecyclerView() {
        blockedAdapter = new BlockedAdapter(getContext(), connectionList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rc_connections.setLayoutManager(linearLayoutManager);
        rc_connections.setAdapter(blockedAdapter);
        /**
         * add scroll listener while user reach in bottom load more will call
         */
        rc_connections.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage = AppConstants.TOTAL_PAGES + currentPage;
                if (NetworkUtils.isNetworkConnected(getContext())) {
                  //  getConnectionByFilter(type, currentPage, false);
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

    public void getConnectionByFilter( boolean isSearch) {

        // showDialog();
        User userData = new User();
        User user = LoginUtils.getLoggedinUser();
        // userData.setType(mtype);
        userData.setUser_id(user.getId());
        userData.setFilter("deleted");

        if (edt_search.getText().toString().length() > 0)
            userData.setFirst_name(edt_search.getText().toString());
//        Log.e("type", mtype);

        connectionViewModel.getBlockedByFilter(userData).observe(getViewLifecycleOwner(), listBaseModel -> {
            swipeRefresh.setRefreshing(false);


            if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {
//
//                   if (currentPage == PAGE_START) {
//                        connectionList.clear();
//                        blockedAdapter.notifyDataSetChanged();
//                    }
                //
                connectionList.clear();
                connectionList.addAll(listBaseModel.getData());
                blockedAdapter.notifyDataSetChanged();
                //  if (connectionList.size() > 0) {
                rc_connections.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
                //    }
                isLoading = false;
            } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {

                if (connectionList.size() == 0) {
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

                case R.id.tv_unblock:
                    try {

                        UnBlockUser(connectionList.get(position), position);
                    } catch (Exception e) {
                        e.getLocalizedMessage();
                    }
                    break;
              /*  case R.id.tv_decline:
                    SettingDeclineData(connectionList.get(position));
                    break;*/

               /* case R.id.tv_connect:
                    TextView textView = (TextView) view;
                    callConnectApi(textView, connectionList.get(position));
                    //GetUserDetails();
                    if(textView.getText().toString().equalsIgnoreCase(AppConstants.REQUEST_ACCEPT)){
                        Connection connection = new Connection();
                        User user = LoginUtils.getLoggedinUser();
                        connection.setStatus(AppConstants.ACTIVE);
                        connection.setId(connectionList.get(position).getConnection_id());
                        connection.setModified_by_id(user.getId());
                        connection.setModified_datetime(DateUtils.GetCurrentdatetime());
                        callDeclineConnectApi(connection);
                    }
                    break;*/

               /* case R.id.ly_main:
                    User user = connectionList.get(position);
                    PersonProfileFragment personProfileFragment = new PersonProfileFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putParcelable("user", user);
                    loadFragment_bundle(R.id.framelayout, personProfileFragment, getContext(), true, bundle2);
                    break;*/

            }

        } else {
            networkErrorDialog();
        }
    }

    private void UnBlockUser(GetBlockedData blockedData, int position) {
        Connection connection = new Connection();
        User user = LoginUtils.getLoggedinUser();
        connection.setSender_id(blockedData.getSenderId());
        connection.setReceiver_id(blockedData.getReceiverId());
        connection.setStatus(AppConstants.ACTIVE);
        callUnBlockUser(connection, position);
    }


    private void callUnBlockUser(Connection connection, int position) {
        showDialog();

        connectionViewModel.block(connection).observe(this, listBaseModel -> {
            if (listBaseModel != null && !listBaseModel.isError()) {

                blockedAdapter.removeAt(position);
                // getConnectionByFilter(type, PAGE_START, true);
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
            Toast.makeText(requireContext(), "Connection is unblocked successfully", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onRefresh() {
        if (NetworkUtils.isNetworkConnected(getContext())) {
            getBlockedConnections();
            //GetUserDetails();
          //  getConnectionByFilter(type, currentPage, false);
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
            Log.d("blocked", "afterTextChanged: " + s);

            Log.d("blocked", "block connections :" + connectionList.size());


            if (s.equals("")) {
                blockedAdapter.hSetList(connectionList);
            } else {
                hFilterList(s);
            }

         /*   currentPage = PAGE_START;
            if (s.length() > 0) {
                isSearchFlag = true;
                connectionList.clear();
                blockedAdapter.notifyDataSetChanged();
                getConnectionByFilter(true);
            } else {
                isSearchFlag = false;
                getBlockedConnections();
            }*/


            // getConnectionByFilter(type, PAGE_START, true);

        }

    }

    private void hFilterList(Editable s) {

        Log.d("blocked", "hFilterList: " + connectionList.size());

        if (!s.equals("")) {
            hSearchList.clear();
            if (connectionList != null) {
                for (GetBlockedData user : connectionList) {
                    String name = user.getReceiver().getFirstName().toLowerCase(Locale.getDefault());
                    Log.d("blocked", "Name : " + name);

                    if (name.contains(s.toString().toLowerCase(Locale.getDefault()))) {
                        Log.d("blocked", "User Found: " + user.getReceiver().getFirstName());
                        hSearchList.add(user);
                    }
                }

                blockedAdapter.hSetList(hSearchList);

                Log.d("blocked", "hSearchList size: " + hSearchList.size());


            }
        } else {
            blockedAdapter.hSetList(connectionList);
        }
    }

    /*private void GetUserDetails() {
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
    }*/
}