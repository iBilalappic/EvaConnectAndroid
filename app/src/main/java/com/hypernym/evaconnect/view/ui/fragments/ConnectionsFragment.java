package com.hypernym.evaconnect.view.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Options;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.utils.TextWatcher;
import com.hypernym.evaconnect.view.adapters.ConnectionsAdapter;
import com.hypernym.evaconnect.view.adapters.OptionsAdapter;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

public class ConnectionsFragment extends BaseFragment implements OptionsAdapter.ItemClickListener, ConnectionsAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.rc_connections)
    RecyclerView rc_connections;

    @BindView(R.id.rc_maincategories)
    RecyclerView rc_maincategories;

    @BindView(R.id.rc_subcategories)
    RecyclerView rc_subcategories;

    @BindView(R.id.edt_search)
    EditText edt_search;

    @BindView(R.id.empty)
    TextView empty;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;


    List<Options> mainCategories = new ArrayList<>();
    List<Options> subCategories = new ArrayList<>();

    private ConnectionsAdapter connectionsAdapter;
    private List<User> connectionList = new ArrayList<>();

    private OptionsAdapter optionsAdapter, subOptionsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ConnectionViewModel connectionViewModel;
    private UserViewModel userViewModel;
    String type = "user";
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private boolean isSearchFlag = false;

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
        View view = inflater.inflate(R.layout.fragment_connections, container, false);
        ButterKnife.bind(this, view);
        connectionViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ConnectionViewModel.class);
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);

        currentPage = PAGE_START;
        swipeRefresh.setOnRefreshListener(this);
        initMainOptionsRecView();
        initSubOptionsRecView();
        initRecyclerView();
        setPageTitle(getString(R.string.connections));
        if (NetworkUtils.isNetworkConnected(getContext())) {
            getConnectionByFilter(type, currentPage, false);
        } else {
            networkErrorDialog();
        }
        edt_search.addTextChangedListener(new TextWatcher());

        return view;
    }

    private void getUserConnections() {
        showDialog();
        connectionViewModel.getAllConnections(AppConstants.TOTAL_PAGES, currentPage).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    connectionList.clear();
                    connectionList.addAll(listBaseModel.getData());
                    connectionsAdapter.notifyDataSetChanged();
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
//        Options option1=new Options();
//        option1.setText(getString(R.string.everyone));
//        option1.setColor(getContext().getResources().getColor(R.color.light_black));
//        option1.setElevation(2);
//        option1.setMainCategory(true);
//        mainCategories.add(option1);

        Options option2 = new Options();
        option2.setText(getString(R.string.people));
        option2.setColor(getContext().getResources().getColor(R.color.light_black));
        option2.setElevation(2);
        option2.setMainCategory(true);
        mainCategories.add(option2);

        Options option3 = new Options();
        option3.setText(getString(R.string.companies));
        option3.setColor(getContext().getResources().getColor(R.color.gray));
        option3.setElevation(0);
        option3.setMainCategory(true);
        mainCategories.add(option3);

        optionsAdapter = new OptionsAdapter(getContext(), mainCategories, this);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rc_maincategories.setLayoutManager(linearLayoutManager);
        rc_maincategories.setAdapter(optionsAdapter);
    }

    private void initSubOptionsRecView() {
        Options option1 = new Options();
        option1.setText("Piolots");
        option1.setColor(getContext().getResources().getColor(R.color.light_black));
        option1.setElevation(2);
        option1.setMainCategory(false);
        subCategories.add(option1);

        Options option2 = new Options();
        option2.setText("IT Systems");
        option2.setColor(getContext().getResources().getColor(R.color.gray));
        option2.setElevation(0);
        option2.setMainCategory(false);
        subCategories.add(option2);

        Options option3 = new Options();
        option3.setText("Security");
        option3.setColor(getContext().getResources().getColor(R.color.gray));
        option3.setElevation(0);
        option3.setMainCategory(false);
        subCategories.add(option3);

        subOptionsAdapter = new OptionsAdapter(getContext(), subCategories, this);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rc_subcategories.setLayoutManager(linearLayoutManager);
        rc_subcategories.setAdapter(subOptionsAdapter);
    }

    private void initRecyclerView() {
        connectionsAdapter = new ConnectionsAdapter(getContext(), connectionList, this);
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
//                if(!isSearchFlag)
//                {
                currentPage = AppConstants.TOTAL_PAGES + currentPage;
                if (NetworkUtils.isNetworkConnected(getContext())) {
                    getConnectionByFilter(type, currentPage, false);
                } else {
                    networkErrorDialog();
                }
                //}


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
        List<Options> categories = new ArrayList<>();
        if (isMainCatrgory) {
            categories.addAll(mainCategories);
            rc_subcategories.setVisibility(View.GONE);
            Log.e("type", "pos " + position);
            if (position == 0) {
                rc_subcategories.setVisibility(View.VISIBLE);
                type = "user";
            } else if (position == 1) {
                rc_subcategories.setVisibility(View.GONE);
                type = "company";

            } else {
                type = getString(R.string.everyone);
            }

        } else {
            categories.addAll(subCategories);
        }
        for (Options options : categories) {
            options.setColor(getContext().getResources().getColor(R.color.gray));
            options.setElevation(0);
        }
        categories.get(position).setColor(getContext().getResources().getColor(R.color.light_black));
        categories.get(position).setElevation(2);
        if (isMainCatrgory) {
            optionsAdapter.notifyDataSetChanged();
        } else {
            subOptionsAdapter.notifyDataSetChanged();
        }
        if (NetworkUtils.isNetworkConnected(getContext())) {
            connectionList.clear();
            connectionsAdapter.notifyDataSetChanged();
            currentPage = PAGE_START;
            getConnectionByFilter(type, currentPage, false);
        } else {
            networkErrorDialog();
        }
    }

    public void getConnectionByFilter(String mtype, int currentPage, boolean isSearch) {

        // showDialog();
        User userData = new User();
        User user = LoginUtils.getLoggedinUser();
        if (mtype.equals("company")) {
            rc_subcategories.setVisibility(View.GONE);
        } else {
            rc_subcategories.setVisibility(View.VISIBLE);
        }

        // if(!mtype.equalsIgnoreCase(getString(R.string.everyone)))
//        if(mtype.equals("user"))
        userData.setType(mtype);
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
                        connectionsAdapter.notifyDataSetChanged();
                    }
                    //
                    connectionList.addAll(listBaseModel.getData());
                    connectionsAdapter.notifyDataSetChanged();
                    if (connectionList.size() > 0) {
                        rc_connections.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.GONE);
                    }
                    isLoading = false;
                } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {
                    rc_connections.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
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
            TextView textView = (TextView) view;
            callConnectApi(textView, connectionList.get(position));
            GetUserDetails();
        } else {
            networkErrorDialog();
        }
    }

    @Override
    public void onRefresh() {
        if (NetworkUtils.isNetworkConnected(getContext())) {
            GetUserDetails();
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
            } else {
                isSearchFlag = false;
            }

            connectionList.clear();
            connectionsAdapter.notifyDataSetChanged();
            getConnectionByFilter(type, PAGE_START, true);

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
