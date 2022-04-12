package com.hypernym.evaconnect.view.ui.fragments;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.AppliedApplicants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.ChatAttachment;
import com.hypernym.evaconnect.models.ChatMessage;
import com.hypernym.evaconnect.models.Contents;
import com.hypernym.evaconnect.models.Data;
import com.hypernym.evaconnect.models.Filter;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.Notification_onesignal;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.models.User_applicants;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.adapters.AttachmentsAdapter;
import com.hypernym.evaconnect.view.adapters.ChatAdapter;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.JobListViewModel;
import com.hypernym.evaconnect.viewmodel.MessageViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends BaseFragment implements View.OnClickListener, AttachmentsAdapter.ItemClickListener, ChatAdapter.OnItemClickListener {


    @BindView(R.id.rc_chat)
    RecyclerView rc_chat;

    @BindView(R.id.messageArea)
    EditText messageArea;

    @BindView(R.id.sendButton)
    TextView sendButton;

    @BindView(R.id.browsefiles)
    ImageView browsefiles;


    @BindView(R.id.rc_attachments)
    RecyclerView rc_attachments;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    List<String> attachments = new ArrayList<>();
    private AttachmentsAdapter attachmentsAdapter;
    ChatMessage mMessage;
    List<ChatMessage> chatMessageList = new ArrayList<>();
    private NetworkConnection networkConnection = new NetworkConnection();
    Firebase reference1, reference2;
    LinearLayoutManager layoutManager;
    ChatAdapter chatAdapter;
    String messageText, ChatTime;
    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int REQUEST_DOCUMENTS = 5;
    private static final int CAMERAA = 1;
    private String GalleryImage, globalImagePath, FileName;
    public String mProfileImageDecodableString;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private List<MultipartBody.Part> MultiplePhoto = new ArrayList<>();
    private List<String> MultiplePhotoString = new ArrayList<>();
    private List<String> MultipleFileString = new ArrayList<>();
    private List<String> ChatUrl = new ArrayList<>();
    private List<File> MultipleFile = new ArrayList<>();
    private String photoVar = null;
    private Uri SelectedImageUri;
    private SimpleDialog simpleDialog;
    private String mFragmentname, Job_name;
    private AppliedApplicants appliedApplicants = new AppliedApplicants();
    private int Day, Month, Year;
    private int Hour, Minutes;
    private JobListViewModel jobListViewModel;
    private MessageViewModel messageViewModel;
    private ChatAttachment chatAttachment=new ChatAttachment();
    List<String> uploadedImages = new ArrayList<>();
    List<String> uploadedDocuments = new ArrayList<>();
    User user =new User();

    public ChatFragment() {
        // Required empty public constructor
    }

    FirebaseStorage storage = FirebaseStorage.getInstance();
  //  StorageReference storageRef = storage.getReferenceFromUrl("gs://evaconnect-df08d.appspot.com");    //change the url according to your firebase app


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        sendButton.setOnClickListener(this);
        browsefiles.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);
        init();
        initViewModel();
        return view;
    }

    private void initViewModel() {
        jobListViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(JobListViewModel.class);
        messageViewModel=ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(MessageViewModel.class);
    }

    private void init() {
        //showBackButton();


        setupRecycler(chatMessageList);

        attachmentsAdapter = new AttachmentsAdapter(getContext(), attachments, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rc_attachments.setLayoutManager(linearLayoutManager);
        rc_attachments.setAdapter(attachmentsAdapter);
        if (getArguments() != null && getArguments().getSerializable("user") != null) {
            user = (User) getArguments().getSerializable("user");
            setPageTitle(user.getFirst_name());
            user.setReceiver_id(user.getId());
            if (user.getId() == null) {
                user.setId(LoginUtils.getLoggedinUser().getId());
            }
            user.setEmail(user.getFirst_name());
            setChatPerson(getContext(), user.getUser_image());
            findFirebaseChats(user.getId());
        } else if (getArguments().getSerializable("applicant") != null) {
            User_applicants appliedApplicants = (User_applicants) getArguments().getSerializable("applicant");
            setPageTitle(appliedApplicants.getFirstName());
            user.setReceiver_id(appliedApplicants.getId());
            user.setId(appliedApplicants.getId());
            user.setUser_image(appliedApplicants.getUserImage());
            if (appliedApplicants.getId() == null) {
                user.setId(LoginUtils.getLoggedinUser().getId());
            }
            user.setEmail(appliedApplicants.getFirstName());
            user.setFirst_name(appliedApplicants.getFirstName());
            setChatPerson(getContext(), appliedApplicants.getUserImage());
            findFirebaseChats(appliedApplicants.getId());
        }
        else if(getArguments().getSerializable("chat_room_id")!=null)
        {
            String chat_id=(String) getArguments().getSerializable("chat_room_id");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            ////////////////////////GET USER DATA////////////////////////////////
            DatabaseReference chatRef = databaseReference.child(AppConstants.FIREASE_CHAT_ENDPOINT);
            chatRef.child(chat_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null)
                    {
                        for(DataSnapshot dataSnapshot1: dataSnapshot.child("members").getChildren())
                        {
                            if(dataSnapshot1.getKey().equalsIgnoreCase(LoginUtils.getLoggedinUser().getId().toString()))
                            {
                               // findFirebaseChats(LoginUtils.getLoggedinUser().getId());
                            }
                            else
                            {
                                setPageTitle(dataSnapshot1.getValue().toString());
                                user.setEmail(dataSnapshot1.getValue().toString());
                                user.setId(Integer.parseInt(dataSnapshot1.getKey()));
                                user.setReceiver_id(Integer.parseInt(dataSnapshot1.getKey()));
                                user.setChatID(chat_id);
                                DatabaseReference otheruser = databaseReference.child(AppConstants.FIREASE_USER_ENDPOINT);
                                otheruser.child(dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {

                                          //  networkConnection.setUserImage(dataSnapshot.child("imageName").getValue().toString());

                                            user.setUser_image(dataSnapshot.child("imageName").getValue().toString());

                                            //showBackButton();

                                            setChatPerson(getContext(),user.getUser_image());
                                            findFirebaseChats(user.getId());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
        {
            NetworkConnection networkConnection=(NetworkConnection) getArguments().getSerializable(Constants.DATA);
            setPageTitle(networkConnection.getName());
            user.setEmail(networkConnection.getName());
            user.setId(networkConnection.getReceiverId());
            user.setReceiver_id(networkConnection.getReceiverId());
            user.setChatID(networkConnection.getChatID());
            user.setUser_image(networkConnection.getUserImage());
            setChatPerson(getContext(),networkConnection.getUserImage());
            settingFireBaseChat(networkConnection);
        }

    }

    private void findFirebaseChats(int id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        ////////////////////////////////////////////////////////
        DatabaseReference userRef = databaseReference.child(AppConstants.FIREASE_USER_ENDPOINT);

        userRef.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null && dataSnapshot.hasChild("chats")) {
                    for (DataSnapshot childSnapshot: dataSnapshot.child("chats").getChildren()) {
                        NetworkConnection networkConnection=new NetworkConnection();
                        String key=childSnapshot.getKey();
                        DatabaseReference chats = databaseReference.child(AppConstants.FIREASE_CHAT_ENDPOINT);
                        chats.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    String otherMember=null;
                                    DataSnapshot members=dataSnapshot.child("members");
                                    for (DataSnapshot member : members.getChildren()) {
                                        if(member.getKey().equalsIgnoreCase(LoginUtils.getLoggedinUser().getId().toString())) {
                                            networkConnection.setChatID(key);
                                            user.setChatID(key);
                                            settingFireBaseChat(networkConnection);
                                        }
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void settingFireBaseChat(NetworkConnection message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference user = databaseReference.child(AppConstants.FIREASE_USER_ENDPOINT);
        user.child(LoginUtils.getLoggedinUser().getId().toString()).child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    user.child(LoginUtils.getLoggedinUser().getId().toString()).child("chats").child(message.getChatID()).child("unread").setValue(false);
                    user.child(LoginUtils.getLoggedinUser().getId().toString()).child("chats").child(message.getChatID()).child("unread_count").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference userRef = databaseReference.child(AppConstants.FIREASE_MESSAGES_ENDPOINT);
        DatabaseReference roomChats=userRef.child(message.getChatID());

        roomChats.addChildEventListener(new ChildEventListener() {
         @Override
          public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
              if (dataSnapshot.getValue() != null) {
                ChatMessage chatMessage =new ChatMessage();

                chatMessage.setName(dataSnapshot.child("name").getValue().toString());
                chatMessage.setSenderID(dataSnapshot.child("senderID").getValue().toString());
                chatMessage.setCreated_datetime(dataSnapshot.child("timestamp").getValue().toString());
                if(dataSnapshot.child("images").getValue()!=null)
                {
                    DataSnapshot images=dataSnapshot.child("images");
                    List<String> imageList=new ArrayList<>();
                    for(DataSnapshot image: images.getChildren())
                    {
                        imageList.add(image.getValue().toString());
                    }
                    chatMessage.setChatImages(imageList);
                }
                else
                {
                    chatMessage.setMessage(dataSnapshot.child("message").getValue().toString());
                }
                if(dataSnapshot.child("documents").getValue()!=null)
                {
                    DataSnapshot documents=dataSnapshot.child("documents");
                    List<String> documentList=new ArrayList<>();
                    for(DataSnapshot image: documents.getChildren())
                    {
                        documentList.add(image.getValue().toString());
                    }
                    chatMessage.setChatDocuments(documentList);
                }
                chatMessageList.add(chatMessage);
            }
            hideDialog();
            chatAdapter.notifyDataSetChanged();
        if (chatMessageList.size() > 0) {
            rc_chat.smoothScrollToPosition(chatMessageList.size() - 1);
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});


    }

    private void setupRecycler(List<ChatMessage> chatMessageList) {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        rc_chat.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(getActivity(), chatMessageList, networkConnection, this);
        rc_chat.setAdapter(chatAdapter);
        if (chatMessageList.size() > 0) {
            rc_chat.smoothScrollToPosition(chatMessageList.size() - 1);
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sendButton:
                if (attachments.size() > 0 && messageArea.getText().length() == 0) {
                    UploadImageToFirebase();
                } else {
                    uploadedImages = new ArrayList<>();
                    uploadedDocuments = new ArrayList<>();
                    sendtofirebase_chat(uploadedImages, uploadedDocuments);
                }

                break;

            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;


            case R.id.browsefiles:
                //   openPictureDialog();
                attachments.clear();
                // uploadedImages.clear();
                //uploadedDocuments.clear();
                MultiplePhoto.clear();
                final OvershootInterpolator interpolator = new OvershootInterpolator();
                ViewCompat.animate(browsefiles).
                        rotation(135f).
                        withLayer().
                        setDuration(300).
                        setInterpolator(interpolator).
                        start();
                /** Instantiating PopupMenu class */
                PopupMenu popup = new PopupMenu(getContext(), v);

                /** Adding menu items to the popumenu */
                popup.getMenuInflater().inflate(R.menu.chat_options, popup.getMenu());

                popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        ViewCompat.animate(browsefiles).
                                rotation(0f).
                                withLayer().
                                setDuration(300).
                                setInterpolator(interpolator).
                                start();
                    }
                });
                /** Defining menu item click listener for the popup menu */
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        //    Toast.makeText(getContext(), item.getGroupId()+"You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.images))) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PHOTO_GALLERY);
                        }  else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.camera))) {
                            takePhotoFromCamera();
                        }
                        else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.document))) {
                            if (Checkpermission()) {
                                LaunchGallery();
                            } else {
                                requestpermission();
                            }
                        }

                        return true;
                    }
                });

                /** Showing the popup menu */
                popup.show();

                break;

        }
    }

    public void LaunchGallery() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_DOCUMENTS);
    }

    private void requestpermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]
                {
                        READ_EXTERNAL_STORAGE,
                        CAMERA
                }, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean ReadPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (ReadPermission && CameraPermission) {
                        // Toast.makeText(HomeActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(HomeActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }


    private void sendtofirebase_chat(List<String> uploadedImages,List<String> uploadedDocuments) {
        messageText = messageArea.getText().toString();

        if (uploadedImages.size() > 0 || uploadedDocuments.size()>0|| messageText.length() > 0) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            //Insertion in chats node
            DatabaseReference chatRef = databaseReference.child(AppConstants.FIREASE_CHAT_ENDPOINT);
            Map<String, Object> chatMap = new HashMap<String, Object>();
            Map<String, Object> lastMessageMap = new HashMap<String, Object>();
            lastMessageMap.put("message", messageText);
            lastMessageMap.put("name", LoginUtils.getLoggedinUser().getEmail());
            lastMessageMap.put("senderID", LoginUtils.getLoggedinUser().getId());
            lastMessageMap.put("timestamp", Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis());
            lastMessageMap.put("images", uploadedImages);
            lastMessageMap.put("documents", uploadedDocuments);
            if(chatMessageList.size()==0)
            {
                chatMap.put("lastMessage",lastMessageMap);
                //ADD members if it is first message
                Map<String, Object> membersMap = new HashMap<String, Object>();
                membersMap.put(LoginUtils.getLoggedinUser().getId().toString(),LoginUtils.getLoggedinUser().getFirst_name());
                membersMap.put(user.getReceiver_id().toString(), user.getFirst_name());
                chatMap.put("members",membersMap);

                String key =  chatRef.push().getKey();
                user.setChatID(key);
                chatRef.child(key).setValue(chatMap);

                //Insertion in Messages node
                DatabaseReference messagesRef = databaseReference.child(AppConstants.FIREASE_MESSAGES_ENDPOINT);

                Log.d("KEY IS....",key);
                messagesRef.child(key).push().setValue(lastMessageMap);

                DatabaseReference userRef = databaseReference.child(AppConstants.FIREASE_USER_ENDPOINT);
                DatabaseReference userRefByID=userRef.child(LoginUtils.getLoggedinUser().getId().toString());
                DatabaseReference otheruserRefByID=userRef.child(user.getReceiver_id().toString());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("last_update_time", new Date().getTime());
               // map.put("unread",false);

                Map<String, Object> receivermap = new HashMap<String, Object>();
                receivermap.put("last_update_time", new Date().getTime());
               // receivermap.put("unread",true);

                userRefByID.child("chats").child(key).setValue(map);

                otheruserRefByID.child("chats").child(key).setValue(receivermap);
                otheruserRefByID.child("name").setValue(user.getFirst_name());
                otheruserRefByID.child("imageName").setValue(user.getUser_image());
                networkConnection.setChatID(key);
                settingFireBaseChat(networkConnection);
            }
            else
            {

                chatRef.child(user.getChatID()).child("lastMessage").setValue(lastMessageMap);
                //Insertion in Messages node
                DatabaseReference messagesRef = databaseReference.child(AppConstants.FIREASE_MESSAGES_ENDPOINT);
                messagesRef.child(user.getChatID()).push().setValue(lastMessageMap);

                DatabaseReference userRef = databaseReference.child(AppConstants.FIREASE_USER_ENDPOINT);
                DatabaseReference userRefByID=userRef.child(LoginUtils.getLoggedinUser().getId().toString());
                DatabaseReference otheruserRefByID=userRef.child(user.getReceiver_id().toString());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("last_update_time", new Date().getTime());
              //  map.put("unread",false);

                Map<String, Object> receivermap = new HashMap<String, Object>();
                receivermap.put("last_update_time", new Date().getTime());
                receivermap.put("unread",true);


                otheruserRefByID.child("chats").child(user.getChatID()).setValue(receivermap);
               userRefByID.child("chats").child(user.getChatID()).setValue(map);
                networkConnection.setChatID(user.getChatID());

            }

//            ChatMessage chatMessage=new ChatMessage();
//            chatMessage.setSenderID(LoginUtils.getLoggedinUser().getId().toString());
//            chatMessage.setCreated_datetime(String.valueOf(new Date().getTime()));
//            chatMessage.setMessage(messageText);
//            chatMessage.setChatImages(uploadedImages);
//            chatMessage.setChatDocuments(uploadedDocuments);
//            chatMessage.setName(LoginUtils.getLoggedinUser().getFirst_name());
//            chatMessageList.add(chatMessage);
//            chatAdapter.notifyDataSetChanged();

          //  sendNotification(user.getEmail());

            messageArea.setText("");
            messageArea.requestFocus();

        } else {
            Toast.makeText(getContext(), "Please type message...", Toast.LENGTH_SHORT).show();
        }
    }


    public void sendNotification(String email) {
        Notification_onesignal request = new Notification_onesignal();
        request.app_id = "44bb428a-54f5-4155-bea3-c0ac2d0b3c1a";
        request.contents = new Contents();
        if (!messageText.equals("")) {
            request.contents.en = "New Message From " + LoginUtils.getLoggedinUser().getFirst_name();
        }
//        else {
//            request.contents.en = UserDetails.username + "\n" + "\n" + "\n" + "Photo";
//        }
        request.data = new Data();
        request.data.data = "data";
        Filter filter = new Filter();
        filter.field = "tag";
        filter.key = "email";
        filter.relation = "=";
//        if (networkConnection.getSenderId().equals(LoginUtils.getLoggedinUser().getId())) {
//            filter.value = networkConnection.getReceiver().getEmail();
//        } else {
//            filter.value = networkConnection.getSender().getEmail();
//        }
        filter.value=email;
        //filter.value = networkConnection.getReceiver().getEmail();
        request.filters = new ArrayList<>();
        request.filters.add(filter);
        RestClient.get().appApi_onesignal().postPackets(request).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.body() != null) {

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private int count = 0;

    private void UploadImageToFirebase() {
//        if (SelectedImageUri != null) {

        // pd.show();
        if (MultiplePhoto.size() > 0) {
            uploadedImages = new ArrayList<>();
            uploadedDocuments = new ArrayList<>();
            //for (int i = 0; i < MultiplePhoto.size(); i++) {
                // MultiplePhoto.add(Uri.parse(String.valueOf(attachments)));
                attachments.clear();
                rc_attachments.setVisibility(View.GONE);
                chatAttachment.setCreated_by_id(LoginUtils.getLoggedinUser().getId());
                chatAttachment.setChat_image(MultiplePhoto);
                chatAttachment.setStatus(AppConstants.ACTIVE);

                messageViewModel.uploadAttachment(chatAttachment).observe(this, new Observer<BaseModel<ChatAttachment>>() {
                    @Override
                    public void onChanged(BaseModel<ChatAttachment> listBaseModel) {
                        if (listBaseModel != null && !listBaseModel.isError()) {
                                uploadedImages.addAll(listBaseModel.getData().getImages());
                                uploadedDocuments.addAll(listBaseModel.getData().getDocuments());
                                sendtofirebase_chat(uploadedImages,uploadedDocuments);
                        }
                        else {
                            networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                        }
                    }


                });
          //  }

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == REQUEST_PHOTO_GALLERY && resultCode == RESULT_OK) {
            try {
                if (data != null && data.getData() != null) {
                    SelectedImageUri = data.getData();
                    ImageFilePathUtil.checkFileType(".jpg");
                    GalleryImage = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);

                    mProfileImageDecodableString = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);
                    Log.e(getClass().getName(), "image file path: " + GalleryImage);

                    tempFile = new File(GalleryImage);

                    currentPhotoPath = GalleryImage;

                    if(tempFile.toString().equalsIgnoreCase("File path not found"))
                    {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_storage));
                    }
                    else
                    {
                        if (tempFile.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.FILE_SIZE_LIMIT_IN_KB) {
                            networkResponseDialog(getString(R.string.error), getString(R.string.err_image_size_large));
                            return;
                        } else {
                            if (photoVar == null) {
                                MultipleFile.add(tempFile);
                                Log.e(getClass().getName(), "file path details: " + tempFile.getName() + " " + tempFile.getAbsolutePath() + "length" + tempFile.length());

                                // photoVar = GalleryImage;
                                file_name = new File(ImageFilePathUtil.getPath(getActivity(), SelectedImageUri));
                                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);

                                MultipartBody.Part partImage = MultipartBody.Part.createFormData("chat_file", file_name.getName(), reqFile);
                                MultiplePhoto.add(partImage);
                                if (!TextUtils.isEmpty(currentPhotoPath) || currentPhotoPath != null) {
                                    attachments.add(currentPhotoPath);
                                    Log.d("ATTACHMENT", GsonUtils.toJson(attachments));
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
                    }

                } else {
                    Toast.makeText(getActivity(), "Something went wrong while retrieving image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                Log.e(getClass().getName(), "exc: " + exc.getMessage());
            }
        }
        else if(requestCode == REQUEST_DOCUMENTS && resultCode == RESULT_OK)
        {
            try {
                if (data != null && data.getData() != null) {

                    SelectedImageUri = data.getData();

                    ImageFilePathUtil.checkFileType(".pdf");
                    GalleryImage = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);
                    mProfileImageDecodableString = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);
                    Log.e(getClass().getName(), "image file path: " + GalleryImage);

                    tempFile = new File(GalleryImage);

                    currentPhotoPath = GalleryImage;

                    if(tempFile.toString().equalsIgnoreCase("File path not found"))
                    {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_storage));
                    }
                    else
                    {
                        if (tempFile.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.FILE_SIZE_LIMIT_IN_KB) {
                            networkResponseDialog(getString(R.string.error), getString(R.string.err_image_size_large));
                            return;
                        } else {
                            if (photoVar == null) {
                                MultipleFile.add(tempFile);
                                Log.e(getClass().getName(), "file path details: " + tempFile.getName() + " " + tempFile.getAbsolutePath() + "length" + tempFile.length());

                                // photoVar = GalleryImage;
                                file_name = new File(ImageFilePathUtil.getPath(getActivity(), SelectedImageUri));
                                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);

                                MultipartBody.Part partImage = MultipartBody.Part.createFormData("chat_file", file_name.getName(), reqFile);
                                MultiplePhoto.add(partImage);
                                if (!TextUtils.isEmpty(currentPhotoPath) || currentPhotoPath != null) {
                                    attachments.add(currentPhotoPath);
                                    Log.d("ATTACHMENT", GsonUtils.toJson(attachments));
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
                    }

                } else {
                    Toast.makeText(getActivity(), "Something went wrong while retrieving image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                Log.e(getClass().getName(), "exc: " + exc.getMessage());
            }
        }
            else {
            if (requestCode == CAMERAA && resultCode == RESULT_OK) {

                //mIsProfileImageAdded = true;
                File file = galleryAddPic();
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);

                MultipartBody.Part partImage = MultipartBody.Part.createFormData("chat_file", file.getName(), reqFile);
                MultiplePhoto.add(partImage);
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

        switch (view.getId()) {
            case R.id.img_attach:
                simpleDialog = new SimpleDialog(getContext(), getString(R.string.confirmation), getString(R.string.msg_remove_attachment), getString(R.string.button_no), getString(R.string.button_yes), new OnOneOffClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        switch (v.getId()) {
                            case R.id.button_positive:
                                attachments.remove(position);
                                MultiplePhoto.remove(position);
//                        MultiplePhotoString.remove(position);
                                attachmentsAdapter.notifyDataSetChanged();
                                //  SelectedImageUri = null;
                                break;
                            case R.id.button_negative:
                                break;
                        }
                        simpleDialog.dismiss();
                    }
                });
                simpleDialog.show();
                break;

        }

    }

}
