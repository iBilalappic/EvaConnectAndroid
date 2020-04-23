package com.hypernym.evaconnect.view.ui.fragments;


import android.app.Application;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.HomePostsAdapter;
import com.hypernym.evaconnect.view.dialogs.Remove_block_dialog;
import com.hypernym.evaconnect.view.dialogs.ShareDialog;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonDetailFragment extends BaseFragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    @BindView(R.id.profile_image)
    ImageView profile_image;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_connect)
    TextView tv_connect;

    @BindView(R.id.tv_biodata)
    TextView tv_biodata;

    @BindView(R.id.tv_openchat)
    TextView tv_openchat;

    @BindView(R.id.img_options)
    ImageView img_options;

    LinearLayout unfollow, block;
    Application context;
    CircleImageView profile_image_dialog;
    TextView tv_profilename, connection_stauts;
    ImageView img_close;

    private ConnectionViewModel connectionViewModel;
    Post post = new Post();
    User user = new User();
    SimpleDialog simpleDialog;

    public PersonDetailFragment() {
        // Required empty public constructor
    }

    Dialog Remove_block_Dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_detail, container, false);
        ButterKnife.bind(this, view);
        tv_openchat.setOnClickListener(this);
        img_options.setOnClickListener(this);


        init();
        return view;
    }

    private void init() {
        connectionViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ConnectionViewModel.class);

        setPageTitle("Profile");
        user = LoginUtils.getLoggedinUser();
        if ((getArguments() != null)) {
            showBackButton();
            post = (Post) getArguments().getSerializable("PostData");
            if (post.getUser().getIs_linkedin() == 1) {
                AppUtils.setGlideImage(getContext(), profile_image, post.getUser().getLinkedin_image_url());
            } else {
                AppUtils.setGlideImage(getContext(), profile_image, post.getUser().getUser_image());
            }
            tv_name.setText(post.getUser().getFirst_name());

            if (post.getUser().getId().equals(user.getId())) {
                tv_connect.setVisibility(View.GONE);
                img_options.setVisibility(View.GONE);
                tv_openchat.setVisibility(View.GONE);
            } else {
                tv_connect.setVisibility(View.VISIBLE);
                img_options.setVisibility(View.VISIBLE);
                tv_openchat.setVisibility(View.VISIBLE);
                String status = AppUtils.getConnectionStatus(getContext(), post.getIs_connected(), post.isIs_receiver());
                if (status.equals(AppConstants.DELETED)) {
                    tv_connect.setVisibility(View.GONE);
                } else {
                    tv_connect.setVisibility(View.VISIBLE);
                    tv_connect.setText(AppUtils.getConnectionStatus(getContext(), post.getIs_connected(), post.isIs_receiver()));

                }
            }

            tv_biodata.setText(post.getUser().getBio_data());
            tv_connect.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (NetworkUtils.isNetworkConnected(getContext())) {
                        callConnectApi(tv_connect, post.getUser());
                    } else {
                        networkErrorDialog();
                    }
                }
            });
        } else {
            user = LoginUtils.getLoggedinUser();
            AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());
            tv_name.setText(user.getFirst_name());
            if (post.getUser().getId() == user.getId()) {
                tv_connect.setVisibility(View.GONE);
                img_options.setVisibility(View.GONE);

            } else {
                tv_connect.setVisibility(View.VISIBLE);
                img_options.setVisibility(View.VISIBLE);
                String status = AppUtils.getConnectionStatus(getContext(), post.getIs_connected(), post.isIs_receiver());
                if (status.equals(AppConstants.DELETED)) {
                    tv_connect.setVisibility(View.GONE);
                } else {
                    tv_connect.setText(AppUtils.getConnectionStatus(getContext(), post.getIs_connected(), post.isIs_receiver()));

                }
            }

            tv_biodata.setText(user.getBio_data());
            tv_connect.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (NetworkUtils.isNetworkConnected(getContext())) {
                        callConnectApi(tv_connect, user);
                    } else {
                        networkErrorDialog();
                    }
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_openchat:
                MessageFragment fragment = new MessageFragment();
                loadFragment(R.id.framelayout, fragment, getContext(), true);
                break;
            case R.id.img_options:
                //    Remove_block_dialog remove_block_dialog;
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("Data",post);
//                remove_block_dialog = new Remove_block_dialog(this,getActivity().getApplication(),bundle,getActivity());
//                remove_block_dialog.show();
//                PopupMenu popup = new PopupMenu(getContext(), v);
//                popup.setOnMenuItemClickListener(this);
//                popup.inflate(R.menu.menu_options);
//                popup.show();
                ShowOptionDialog();

                break;
            case R.id.img_close:
                Remove_block_Dialog.dismiss();
                break;
            case R.id.unfollow:
                RemoveUserApiCall();
                break;
            case R.id.block:
                BlockUserApiCall();
                break;
        }

    }

    private void ShowOptionDialog() {
        Remove_block_Dialog = new Dialog(getContext());
        Remove_block_Dialog.setCancelable(true);
        Remove_block_Dialog.setContentView(R.layout.dialog_remove_block);


        profile_image_dialog = Remove_block_Dialog.findViewById(R.id.profile_image);
        img_close = Remove_block_Dialog.findViewById(R.id.img_close);
        tv_profilename = Remove_block_Dialog.findViewById(R.id.tv_profilename);
        connection_stauts = Remove_block_Dialog.findViewById(R.id.tv_connect);
        unfollow = Remove_block_Dialog.findViewById(R.id.unfollow);
        block = Remove_block_Dialog.findViewById(R.id.block);
        tv_profilename.setText(post.getUser().getFirst_name());
        if (post.getUser().getIs_linkedin() == 1) {
            AppUtils.setGlideImage(getContext(), profile_image_dialog, post.getUser().getLinkedin_image_url());
        } else {
            AppUtils.setGlideImage(getContext(), profile_image_dialog, post.getUser().getUser_image());
        }
        connection_stauts.setText(AppUtils.getConnectionStatus(getContext(), post.getIs_connected(), post.isIs_receiver()));
        img_close.setOnClickListener(this);
        unfollow.setOnClickListener(this);
        block.setOnClickListener(this);

        Remove_block_Dialog.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Toast.makeText(getContext(), "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.item_blockuser:
                // do your code
                BlockUserApiCall();
                return true;
            case R.id.item_removeuser:
                RemoveUserApiCall();
                // do your code
                return true;
            default:
                return false;
        }
    }

    private void BlockUserApiCall() {
        if (post.getConnection_id() != null) {
            connectionViewModel.block_user(post.getConnection_id(), user).observe(this, new Observer<BaseModel<List<Object>>>() {
                @Override
                public void onChanged(BaseModel<List<Object>> listBaseModel) {
                    if (!listBaseModel.isError()) {
                        Remove_block_Dialog.dismiss();
                        simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.msg_block_user), null, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().onBackPressed();
                                simpleDialog.dismiss();
                            }
                        });
                    } else {
                        Remove_block_Dialog.dismiss();
                        simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), "Your connection is already block", null, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().onBackPressed();
                                simpleDialog.dismiss();
                            }
                        });
                    }
                    hideDialog();
                    Remove_block_Dialog.dismiss();
                    simpleDialog.show();
                    simpleDialog.setCancelable(false);
                }
            });
        } else {
            Toast.makeText(getActivity(), "connection not exsist", Toast.LENGTH_SHORT).show();
        }

    }

    private void RemoveUserApiCall() {
        if (post.getConnection_id() != null) {
            connectionViewModel.remove_user(post.getConnection_id()).observe(this, new Observer<BaseModel<List<Object>>>() {
                @Override
                public void onChanged(BaseModel<List<Object>> listBaseModel) {
                    if (!listBaseModel.isError()) {
                        Remove_block_Dialog.dismiss();
                        simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.msg_remove_user), null, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().onBackPressed();
                                simpleDialog.dismiss();
                            }
                        });
                    } else {
                        Remove_block_Dialog.dismiss();
                        simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), "Your connection is already remove", null, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().onBackPressed();
                                simpleDialog.dismiss();
                            }
                        });
                    }
                    Remove_block_Dialog.dismiss();
                    hideDialog();
                    simpleDialog.show();
                    simpleDialog.setCancelable(false);
                }
            });
        } else {
            Toast.makeText(getActivity(), "connection not exsist", Toast.LENGTH_SHORT).show();
        }

    }
}
