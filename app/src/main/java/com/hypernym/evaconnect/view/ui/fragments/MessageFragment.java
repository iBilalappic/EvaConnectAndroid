package com.hypernym.evaconnect.view.ui.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.toolbar.OnItemClickListener;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateTimeComparator;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.AttachmentsAdapter;
import com.hypernym.evaconnect.view.adapters.HorizontalMessageAdapter;
import com.hypernym.evaconnect.view.adapters.MessageAdapter;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.MessageViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class MessageFragment extends BaseFragment implements OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,  AttachmentsAdapter.ItemClickListener {

    @BindView(R.id.rc_message)
    RecyclerView re_message;

    @BindView(R.id.fab)
    FloatingActionButton newmessage;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.img_nomail)
    ImageView img_nomail;

    @BindView(R.id.tv_nomail)
    TextView tv_nomail;


    private MessageAdapter messageAdapter, newmessageAdapter;
    private HorizontalMessageAdapter messageAdapter_horizontal;
    private LinearLayoutManager linearLayoutManager;
    private MessageViewModel messageViewModel;
    private List<NetworkConnection> networkConnectionList = new ArrayList<>();
    private List<NetworkConnection> newNetworkConnectionList = new ArrayList<>();
    private List<NetworkConnection> originalNetworkConnectionList = new ArrayList<>();
    private List<NetworkConnection> neworiginalNetworkConnectionList = new ArrayList<>();
    Dialog mDialogMessage;
    EditText editTextSearch, editTextMessage;
    TextView mTextviewSend, empty;
    RecyclerView mrecyclerviewFriends;
    TextView browsefiles;
    private int Itempostion, ItemPostionHorizontal;
    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int CAMERAA = 1;
    private String GalleryImage, mCurrentPhotoPath, globalImagePath;
    private String mProfileImageDecodableString;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    private AttachmentsAdapter attachmentsAdapter;
    private List<String> attachments = new ArrayList<>();
    private List<String> MultiplePhotoString = new ArrayList<>();
    private List<String> MultipleFileString = new ArrayList<>();
    List<NetworkConnection> filterdNames = new ArrayList<>();

    private RecyclerView rc_attachments;
    SimpleDialog simpleDialog;
    private Uri SelectedImageUri;

    private List<Uri> MultiplePhoto = new ArrayList<>();
    private List<File> MultipleFile = new ArrayList<>();

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
        newmessage.setOnClickListener(this);
        init();
        swipeRefresh.setOnRefreshListener(this);
        //
        //  setupRecyclerview();
        if (NetworkUtils.isNetworkConnected(getContext())) {
          //  GetFriendDetails();

            // setupNetworkConnectionRecycler();
            GetFirebaseData();
        } else {
            networkErrorDialog();
        }

        return view;
    }

    private void GetFirebaseData() {
        showDialog();
        networkConnectionList.clear();
        //     messageAdapter.notifyDataSetChanged();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query lastQuery = databaseReference.child(AppConstants.FIREASE_CHAT_ENDPOINT);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    //  networkConnectionList.clear();
                    swipeRefresh.setRefreshing(true);
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        DataSnapshot members = child.child("members");
                        NetworkConnection networkConnection=new NetworkConnection();
                        for (DataSnapshot member : members.getChildren()) {
                            if(member.getKey().equalsIgnoreCase(LoginUtils.getLoggedinUser().getId().toString()))
                            {
                                DataSnapshot lastMessage = child.child("lastMessage");
                                networkConnection.setMessage(lastMessage.child("message").getValue().toString());
                                if (lastMessage.child("images").getValue() != null) {
                                   networkConnection.setMessage("image");
                                }
                                networkConnection.setId(Integer.parseInt(lastMessage.child("senderID").getValue().toString()));
                                networkConnection.setCreatedDatetime(lastMessage.child("timestamp").getValue().toString());
                                networkConnection.setChatID(child.getKey());
                                if(networkConnection.getName()!=null)
                                {
                                    networkConnectionList.add(networkConnection);
                                    setupRecyclerview();
                                    Collections.sort(networkConnectionList, new DateTimeComparator());
                                    Collections.reverse(networkConnectionList);
                                    messageAdapter.notifyDataSetChanged();
                                    swipeRefresh.setRefreshing(false);
                                }
                            }
                            else
                            {
                                Query userQuery = databaseReference.child(AppConstants.FIREASE_USER_ENDPOINT);
                                userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null)
                                        {
                                            for (DataSnapshot users : dataSnapshot.getChildren()) {
                                                if(users.getKey().equalsIgnoreCase(member.getKey()))
                                                {
                                                    networkConnection.setName(users.child("name").getValue().toString());
                                                    networkConnection.setUserImage(users.child("imageName").getValue().toString());
                                                    if(networkConnection.getChatID()!=null)
                                                    {
                                                        networkConnectionList.add(networkConnection);
                                                        setupRecyclerview();
                                                        Collections.sort(networkConnectionList, new DateTimeComparator());
                                                        Collections.reverse(networkConnectionList);
                                                        messageAdapter.notifyDataSetChanged();
                                                        swipeRefresh.setRefreshing(false);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            //messageAdapter.notifyDataSetChanged();
                        }

                    }

                     hideDialog();

                } else {
                    swipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
                hideDialog();
                swipeRefresh.setRefreshing(false);
            }
        });
        hideDialog();
    }


    private void init() {
        messageViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(MessageViewModel.class);
        setPageTitle(getString(R.string.messages));
    }

    private void setupRecyclerview() {
        re_message.setVisibility(View.VISIBLE);
        img_nomail.setVisibility(View.GONE);
        tv_nomail.setVisibility(View.GONE);
        messageAdapter = new MessageAdapter(getContext(), networkConnectionList, this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        re_message.setLayoutManager(linearLayoutManager);
        re_message.setAdapter(messageAdapter);

    }




    @Override
    public void onItemClick(View view, Object data, int position, String adaptertype) {
        if (adaptertype.equals("SimpleAdapter")) {
            ChatFragment chatFragment = new ChatFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.DATA, networkConnectionList.get(position));
            chatFragment.setArguments(bundle);
            //  Log.d("TAAAG", "" + GsonUtils.toJson(networkConnection));
            loadFragment(R.id.framelayout, chatFragment, getContext(), true);
        } else {
            // Toast.makeText(getContext(), "" + newNetworkConnectionList.get(position).getSenderId(), Toast.LENGTH_SHORT).show();
            ItemPostionHorizontal = position;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                loadFragment(R.id.framelayout, new MessageConnectionFragment(), getContext(), true);
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        setPageTitle("Message");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == REQUEST_PHOTO_GALLERY && resultCode == RESULT_OK) {
            try {
                if (data != null && data.getData() != null) {
                    SelectedImageUri = data.getData();

                    GalleryImage = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);
                    mProfileImageDecodableString = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);
                    Log.e(getClass().getName(), "image file path: " + GalleryImage);

                    tempFile = new File(GalleryImage);


                    Log.e(getClass().getName(), "file path details: " + tempFile.getName() + " " + tempFile.getAbsolutePath() + "length" + tempFile.length());


                    if (tempFile.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.FILE_SIZE_LIMIT_IN_KB) {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_image_size_large));
                        return;
                    } else {
                        if (photoVar == null) {
                            MultiplePhoto.add(SelectedImageUri);
                            MultiplePhotoString.add(SelectedImageUri.toString());
                            MultipleFile.add(tempFile);
                            MultipleFileString.add(tempFile.toString());
                            currentPhotoPath = GalleryImage;
                            // photoVar = GalleryImage;
                            file_name = new File(ImageFilePathUtil.getPath(getActivity(), SelectedImageUri));
                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);

                            // partImage = MultipartBody.Part.createFormData("user_image", file_name.getName(), reqFile);

                            if (!TextUtils.isEmpty(currentPhotoPath) || currentPhotoPath != null) {
                                attachments.add(currentPhotoPath);
                                attachmentsAdapter.notifyDataSetChanged();
                                rc_attachments.setVisibility(View.VISIBLE);
                            } else {
                                networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_supported));
                            }
                        } else {
                            networkResponseDialog(getString(R.string.error), getString(R.string.err_one_file_at_a_time));
                            return;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong while retrieving image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                Log.e(getClass().getName(), "exc: " + exc.getMessage());
            }
        } else {
            if (requestCode == CAMERAA && resultCode == RESULT_OK) {

                //mIsProfileImageAdded = true;
                File file = galleryAddPic();
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                // imgName = file_name.getName();
                globalImagePath = file.getAbsolutePath();
                if (file.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.IMAGE_SIZE_IN_KB) {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_image_size_large));
                    //   AppUtils.showSnackBar(getView(), getString(R.string.err_image_size_large, AppConstants.IMAGE_SIZE_IN_KB));
                    return;
                }
                Bitmap orignal = loadFromFile(globalImagePath);
                File filenew = new File(globalImagePath);
                try {
                    FileOutputStream out = new FileOutputStream(filenew);
                    orignal.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(globalImagePath) || globalImagePath != null) {

                    attachments.add(globalImagePath);
                    attachmentsAdapter.notifyDataSetChanged();
                    rc_attachments.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        simpleDialog = new SimpleDialog(getContext(), getString(R.string.confirmation), getString(R.string.msg_remove_attachment), getString(R.string.button_no), getString(R.string.button_yes), new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                switch (v.getId()) {
                    case R.id.button_positive:
                        attachments.remove(position);
                        MultiplePhoto.remove(position);
                        MultiplePhotoString.remove(position);
                        attachmentsAdapter.notifyDataSetChanged();
                        break;
                    case R.id.button_negative:
                        break;
                }
                simpleDialog.dismiss();
            }
        });
        simpleDialog.show();
    }

    @Override
    public void onRefresh() {

        if (NetworkUtils.isNetworkConnected(getContext())) {
            GetFirebaseData();
        } else {
            networkErrorDialog();
        }
    }
}
