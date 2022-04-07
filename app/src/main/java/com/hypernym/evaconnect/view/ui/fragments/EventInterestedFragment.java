package com.hypernym.evaconnect.view.ui.fragments;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.InvitedConnectionListener;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.GetEventInterestedUsers;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.EventInterestedUserAdapter;
import com.hypernym.evaconnect.view.adapters.InviteConnectionsAdapter;
import com.hypernym.evaconnect.viewmodel.EventViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventInterestedFragment extends BaseFragment implements InviteConnectionsAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.edt_search)
    EditText edt_search;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.rc_interested)
    RecyclerView rc_interested;

    @BindView(R.id.empty)
    TextView empty;


    private EventInterestedUserAdapter inviteConnectionsAdapter;
    private List<GetEventInterestedUsers> connectionList = new ArrayList<>();
    private EventViewModel eventViewModel;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private boolean isSearchFlag = false;

    List<User> users=new ArrayList<>();
    InvitedConnectionListener mListener;
    private int event_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_interested, container, false);

        ButterKnife.bind(this, view);
        eventViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(EventViewModel.class);
        currentPage = PAGE_START;

      //  init();
        initRecyclerView();

        if (NetworkUtils.isNetworkConnected(getContext())) {
          //  getConnectionByFilter(type, currentPage, false);
            init();
        } else {
            networkErrorDialog();
        }

        edt_search.addTextChangedListener(new EventInterestedFragment.TextWatcher());
        return view;
    }

    private void initRecyclerView() {
        inviteConnectionsAdapter = new EventInterestedUserAdapter(getContext(), connectionList, this::onItemClick);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),4);
        rc_interested.setLayoutManager(linearLayoutManager);
        rc_interested.setAdapter(inviteConnectionsAdapter);

        /*
         * add scroll listener while user reach in bottom load more will call
         */
        rc_interested.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage = AppConstants.TOTAL_PAGES + currentPage;
                if (NetworkUtils.isNetworkConnected(getContext())) {
                   // getConnectionByFilter(type, currentPage, false);
                  //  init();
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            event_id = bundle.getInt("event_id");
            getInterestedEvent(event_id);
        }
    }

    private void getInterestedEvent(int event_id) {

        eventViewModel.getEventInterested(event_id).observe(getViewLifecycleOwner(), new Observer<BaseModel<List<GetEventInterestedUsers>>>() {
            @Override
            public void onChanged(BaseModel<List<GetEventInterestedUsers>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {
/*                    if (currentPage == PAGE_START) {
                        connectionList.clear();
                        inviteConnectionsAdapter.notifyDataSetChanged();
                    }*/
                    //
                    connectionList.addAll(listBaseModel.getData());
                    inviteConnectionsAdapter.notifyDataSetChanged();
                    if (connectionList.size() > 0) {
                        rc_interested.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.GONE);
                    }
                    isLoading = false;
                } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {

                    if(connectionList.size()==0)
                    {
                        rc_interested.setVisibility(View.GONE);
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
    public void onRefresh() {

    }

    @Override
    public void onItemClick(View view, int position, User data) {
        PersonProfileFragment personDetailFragment = new PersonProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", data);
        personDetailFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, personDetailFragment, getContext(), true);

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
           // getConnectionByFilter(type, PAGE_START, true);

        }

    }
}