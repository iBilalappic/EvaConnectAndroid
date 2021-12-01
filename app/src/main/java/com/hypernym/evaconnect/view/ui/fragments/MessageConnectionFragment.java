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

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.MessageConnectionsAdapter;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageConnectionFragment extends BaseFragment implements MessageConnectionsAdapter.OnItemClickListener {

    @BindView(R.id.edt_search)
    EditText edt_search;


    @BindView(R.id.rc_connections)
    RecyclerView rc_connections;

    @BindView(R.id.empty)
    TextView empty;

    @BindView(R.id.btn_search)
    ImageView btn_search;

    private MessageConnectionsAdapter messageConnectionsAdapter;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_connections, container, false);
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

        edt_search.addTextChangedListener(new MessageConnectionFragment.TextWatcher());

        return view;
    }

    private void getConnectionByFilter(String type, int currentPage, boolean b) {
        User userData = new User();
        User user = LoginUtils.getLoggedinUser();

        userData.setUser_id(user.getId());
        userData.setConnection_status("active");

        if (edt_search.getText().toString().length() > 0)
            userData.setFirst_name(edt_search.getText().toString());

        Log.e("type", type);

        connectionViewModel.getConnectionByFilter(userData, AppConstants.TOTAL_PAGES, currentPage)
                .observe(this, listBaseModel ->
                {
                    if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {
                        if (currentPage == PAGE_START) {
                            connectionList.clear();
                            messageConnectionsAdapter.notifyDataSetChanged();
                        }

                        connectionList.addAll(listBaseModel.getData());
                        messageConnectionsAdapter.notifyDataSetChanged();

                        if (connectionList.size() > 0) {
                            rc_connections.setVisibility(View.VISIBLE);
                            empty.setVisibility(View.GONE);
                        }

                        isLoading = false;
                    } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {
                        if(connectionList.size()==0)
                        {
                            rc_connections.setVisibility(View.GONE);
                            messageConnectionsAdapter.notifyDataSetChanged();
                            empty.setVisibility(View.VISIBLE);
                        }
                        isLastPage = true;
                        isLoading = false;
                    } else {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                    }
                });

    }

    private void initRecyclerView() {
        messageConnectionsAdapter = new MessageConnectionsAdapter(getContext(), connectionList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rc_connections.setLayoutManager(linearLayoutManager);
        rc_connections.setAdapter(messageConnectionsAdapter);

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
        showBackButton();
        setPageTitle("Your Connections");

        Bundle bundle = getArguments();
        if (bundle != null) {
            fragment_type = bundle.getString(Constants.FRAGMENT_TYPE);
        }
    }

    @Override
    public void onItemClick(View view, int position, User data) {
        switch (view.getId()) {
            case R.id.tv_invite:
                ChatFragment chatFragment=new ChatFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("user",connectionList.get(position));
                chatFragment.setArguments(bundle);
                loadFragment(R.id.framelayout, chatFragment, getContext(), true);
               // addConnection(data);
                break;
            case R.id.imageView9:
                //removeConnection(data);
                break;
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
            messageConnectionsAdapter.notifyDataSetChanged();
            getConnectionByFilter(type, PAGE_START, true);

        }

    }
}
