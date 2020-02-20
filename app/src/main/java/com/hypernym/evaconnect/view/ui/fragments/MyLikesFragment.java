package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.adapters.MessageAdapter;
import com.hypernym.evaconnect.view.adapters.MyLikeAdapter;
import com.hypernym.evaconnect.view.adapters.NotificationsAdapter;
import com.hypernym.evaconnect.viewmodel.MessageViewModel;
import com.hypernym.evaconnect.viewmodel.MylikesViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyLikesFragment extends BaseFragment implements MyLikeAdapter.OnItemClickListener{

    @BindView(R.id.rc_mylikes)
    RecyclerView rc_mylikes;
    private MylikesViewModel mylikeViewModel;
    private MyLikeAdapter myLikeAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<MyLikesModel> myLikesModelList = new ArrayList<>();

    public MyLikesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_likes, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mylikeViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(MylikesViewModel.class);
        GetMyLikes();
        setupRecyclerview();
        setPageTitle("My Likes");
    }

    private void setupRecyclerview() {
//        networkConnectionList = removeDuplicates(networkConnectionList);
        myLikeAdapter = new MyLikeAdapter(getContext(), myLikesModelList,this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rc_mylikes.setLayoutManager(linearLayoutManager);
        rc_mylikes.setAdapter(myLikeAdapter);
    }

    private void GetMyLikes() {
        showDialog();
        User user = LoginUtils.getLoggedinUser();
        mylikeViewModel.SetLikes(user.getUser_id()).observe(this, new Observer<BaseModel<List<MyLikesModel>>>() {
            @Override
            public void onChanged(BaseModel<List<MyLikesModel>> getnetworkconnection) {
                if (getnetworkconnection != null && !getnetworkconnection.isError()) {
                    myLikesModelList.clear();
                    myLikesModelList.addAll(getnetworkconnection.getData());
                   setupRecyclerview();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        PostDetailsFragment postDetailsFragment=new PostDetailsFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("post",myLikesModelList.get(position).getObjectId());
        postDetailsFragment.setArguments(bundle);
        loadFragment(R.id.framelayout,postDetailsFragment,getContext(),true);
    }
}
