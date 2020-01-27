package com.hypernym.evaconnect.view.ui.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.toolbar.OnItemClickListener;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.adapters.AttachmentsAdapter;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.HorizontalMessageAdapter;
import com.hypernym.evaconnect.view.adapters.MessageAdapter;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.MessageViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class MessageFragment extends BaseFragment implements OnItemClickListener, View.OnClickListener, TextWatcher, AttachmentsAdapter.ItemClickListener {

    @BindView(R.id.rc_message)
    RecyclerView re_message;

    @BindView(R.id.newmessage)
    TextView newmessage;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;


    private MessageAdapter messageAdapter;
    private HorizontalMessageAdapter messageAdapter_horizontal;
    private LinearLayoutManager linearLayoutManager;
    private MessageViewModel messageViewModel;
    private List<NetworkConnection> networkConnectionList = new ArrayList<>();
    Dialog mDialogMessage;
    EditText editTextSearch, editTextMessage;
    TextView mTextviewSend;
    RecyclerView mrecyclerviewFriends;
    TextView browsefiles;
    private int Itempostion;
    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int CAMERAA = 1;
    private String GalleryImage, mCurrentPhotoPath, globalImagePath;
    private String mProfileImageDecodableString;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    private AttachmentsAdapter attachmentsAdapter;
    private List<String> attachments = new ArrayList<>();
    private RecyclerView rc_attachments;
    SimpleDialog simpleDialog;
    private Uri SelectedImageUri;


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
        GetFriendDetails();
        return view;
    }


    private void init() {
        messageViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(MessageViewModel.class);

    }

    private void setupRecyclerview() {
        networkConnectionList = removeDuplicates(networkConnectionList);
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
                        networkConnectionList.addAll(getnetworkconnection.getData());
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
    public void onItemClick(View view, Object data, int position, String adaptertype) {
        if (adaptertype.equals("SimpleAdapter")) {
            ChatFragment chatFragment = new ChatFragment();
            Itempostion = position;
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.DATA, networkConnectionList.get(position));
            chatFragment.setArguments(bundle);
            //  Log.d("TAAAG", "" + GsonUtils.toJson(networkConnection));
            loadFragment(R.id.framelayout, chatFragment, getContext(), true);
        } else {
            Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
            Itempostion = position;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newmessage:
                if (networkConnectionList.size() > 0 && NetworkUtils.isNetworkConnected(getContext())) {
                    showMesssageDialog();
                } else {
                    Toast.makeText(getContext(), "You haven't any friends to chat", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showMesssageDialog() {

        mDialogMessage = new Dialog(getContext());
        mDialogMessage.setContentView(R.layout.dialog_message);
        editTextSearch = mDialogMessage.findViewById(R.id.edittextSearchUser);
        editTextMessage = mDialogMessage.findViewById(R.id.edittextMessageArea);
        mTextviewSend = mDialogMessage.findViewById(R.id.sendButton);
        browsefiles = mDialogMessage.findViewById(R.id.browsefiles);
        rc_attachments = mDialogMessage.findViewById(R.id.rc_attachments);

        attachmentsAdapter = new AttachmentsAdapter(getContext(), attachments, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rc_attachments.setLayoutManager(linearLayoutManager);
        rc_attachments.setAdapter(attachmentsAdapter);
        editTextSearch.addTextChangedListener(this);
        mrecyclerviewFriends = mDialogMessage.findViewById(R.id.recyclerViewNetworkConnection);
        setupNetworkConnectionRecycler();

        mTextviewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageArea = editTextMessage.getText().toString();
                if (!messageArea.equals("")) {
                    ChatFragment chatFragment = new ChatFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.DATA, networkConnectionList.get(Itempostion));
                    bundle.putString("MESSAGE", messageArea);
                    if (SelectedImageUri != null) {
                        bundle.putString("IMAGEURI", SelectedImageUri.toString());
                        bundle.putString("FILENAME", tempFile.toString());
                    }
                    chatFragment.setArguments(bundle);
                    SelectedImageUri = null;
                    tempFile = null;
                    //  Log.d("TAAAG", "" + GsonUtils.toJson(networkConnection));
                    loadFragment(R.id.framelayout, chatFragment, getContext(), true);
                    mDialogMessage.dismiss();
                }
            }
        });
        browsefiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPictureDialog();
            }
        });
        mDialogMessage.setCanceledOnTouchOutside(true);
        mDialogMessage.show();
        mDialogMessage.setCancelable(true);
    }

    private void setupNetworkConnectionRecycler() {
        networkConnectionList = removeDuplicates(networkConnectionList);
        messageAdapter_horizontal = new HorizontalMessageAdapter(getContext(), networkConnectionList, this);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mrecyclerviewFriends.setLayoutManager(linearLayoutManager);
        mrecyclerviewFriends.setAdapter(messageAdapter_horizontal);
    }

    private List<NetworkConnection> removeDuplicates(List<NetworkConnection> mCollectedBin) {
        List<NetworkConnection> newList = new ArrayList<NetworkConnection>();
        // Traverse through the first list
        for (NetworkConnection element : mCollectedBin) {
            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
                //  if (!newList.contains(element.location)) {
                newList.add(element);
                //   }
            }
        }

        return newList;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        filter(s.toString());
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        List<NetworkConnection> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (NetworkConnection s : networkConnectionList) {


            if (s.getReceiver().getFirstName().toLowerCase().contains(text.toLowerCase()) ||
                    s.getReceiver().getFirstName().toUpperCase().contains(text.toUpperCase())) {
                filterdNames.add(s);
            }

            if (s.getSender().getFirstName().toLowerCase().contains(text.toLowerCase()) ||
                    s.getSender().getFirstName().toUpperCase().contains(text.toUpperCase())) {
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        messageAdapter_horizontal.filterList(removeDuplicates(filterdNames));
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
            if (requestCode == CAMERAA) {

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
}
