package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.Receiver;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.toolbar.OnItemClickListener;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.adapters.HomePostsAdapter;
import com.hypernym.evaconnect.view.adapters.MessageAdapter;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;
import com.hypernym.evaconnect.viewmodel.MessageViewModel;
import com.hypernym.evaconnect.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageFragment extends BaseFragment implements OnItemClickListener {

    @BindView(R.id.rc_message)
    RecyclerView re_message;

    @BindView(R.id.newmessage)
    TextView newmessage;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private MessageAdapter messageAdapter;
    private LinearLayoutManager linearLayoutManager;
    private MessageViewModel messageViewModel;
    private List<NetworkConnection> networkConnectionList = new ArrayList<>();
    public Receiver receiver;
    NetworkConnection networkConnection;

    public MessageFragment() {
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
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        init();
        GetFriendDetails();
        return view;
    }


    private void init() {
        messageViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(MessageViewModel.class);

    }

    private void setupRecyclerview() {
        messageAdapter = new MessageAdapter(getContext(), networkConnectionList, this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        re_message.setLayoutManager(linearLayoutManager);
        re_message.setAdapter(messageAdapter);
    }

    private void GetFriendDetails() {
        showDialog();
        User user = LoginUtils.getLoggedinUser();
        messageViewModel.SetUser(user).observe(this, new Observer<BaseModel<List<NetworkConnection>>>() {
            @Override
            public void onChanged(BaseModel<List<NetworkConnection>> getnetworkconnection) {
                if (getnetworkconnection != null && !getnetworkconnection.isError()) {
                    networkConnectionList.clear();
                    for (int i = 0; i < getnetworkconnection.getData().size(); i++) {
                        networkConnectionList.addAll(i, getnetworkconnection.getData());
                    }
                    setupRecyclerview();

                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();

            }
        });
    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(Constants.DATA,networkConnectionList.get(position));
        chatFragment.setArguments(bundle);
      //  Log.d("TAAAG", "" + GsonUtils.toJson(networkConnection));
        loadFragment(R.id.framelayout, chatFragment, getContext(), true);
    }
}
