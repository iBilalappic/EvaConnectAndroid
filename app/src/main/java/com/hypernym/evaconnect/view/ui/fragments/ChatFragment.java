package com.hypernym.evaconnect.view.ui.fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateUtils;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends BaseFragment implements View.OnClickListener, AttachmentsAdapter.ItemClickListener, ChatAdapter.OnItemClickListener {


    @BindView(R.id.rc_chat)
    RecyclerView rc_chat;

    @BindView(R.id.messageArea)
    EditText messageArea;

    @BindView(R.id.sendButton)
    TextView sendButton;

    @BindView(R.id.browsefiles)
    TextView browsefiles;


    @BindView(R.id.rc_attachments)
    RecyclerView rc_attachments;

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
    User user=new User();

    public ChatFragment() {
        // Required empty public constructor
    }

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://evaconnect-df08d.appspot.com");    //change the url according to your firebase app


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
        init();
        initViewModel();

        return view;

    }

    private void initViewModel() {
        jobListViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(JobListViewModel.class);
        messageViewModel=ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(MessageViewModel.class);

    }

    private void init() {
        showBackButton();
        setupRecycler(chatMessageList);

        attachmentsAdapter = new AttachmentsAdapter(getContext(), attachments, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rc_attachments.setLayoutManager(linearLayoutManager);
        rc_attachments.setAdapter(attachmentsAdapter);
        if(getArguments()!=null && getArguments().getSerializable("user")!=null)
        {
            user=(User) getArguments().getSerializable("user");
            setPageTitle(user.getFirst_name());
        }
        else
        {
            NetworkConnection networkConnection=(NetworkConnection) getArguments().getSerializable(Constants.DATA);
            setPageTitle(networkConnection.getName());
            settingFireBaseChat(networkConnection);
            user.setEmail(networkConnection.getName());
            user.setId(networkConnection.getId());
            user.setChatID(networkConnection.getChatID());

        }
    }

    private void settingFireBaseChat(NetworkConnection message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = databaseReference.child(AppConstants.FIREASE_MESSAGES_ENDPOINT);
        DatabaseReference roomChats=userRef.child(message.getChatID());
        roomChats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    //  networkConnectionList.clear();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        ChatMessage chatMessage =new ChatMessage();
                        chatMessage.setMessage(child.child("message").getValue().toString());
                        chatMessage.setName(child.child("name").getValue().toString());
                        chatMessage.setSenderID(child.child("senderID").getValue().toString());
                        chatMessage.setCreated_datetime(child.child("timestamp").toString());
                        if(child.child("images").getValue()!=null)
                        {
                            DataSnapshot images=child.child("images");
                            List<String> imageList=new ArrayList<>();
                            for(DataSnapshot image: images.getChildren())
                            {
                                imageList.add(image.getValue().toString());
                            }
                            chatMessage.setChatImages(imageList);
                        }
                        chatMessageList.add(chatMessage);
                    }
                    hideDialog();
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
                hideDialog();
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
                sendtofirebase_chat();
                break;

            case R.id.browsefiles:
                //   openPictureDialog();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PHOTO_GALLERY);
                break;

        }

    }

    private void sendtofirebase_chat() {
        messageText = messageArea.getText().toString();

        if (attachments.size() == 0 && messageText.length() > 0) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            //Insertion in user node
            if(chatMessageList.size()==0)
            {
                DatabaseReference userRef = databaseReference.child(AppConstants.FIREASE_USER_ENDPOINT);
                DatabaseReference userRefByID=userRef.child(LoginUtils.getLoggedinUser().getId().toString());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("imageName", LoginUtils.getLoggedinUser().getUser_image());
                map.put("name", LoginUtils.getLoggedinUser().getEmail());
                userRefByID.setValue(map);

                //Insertion in chats node
                DatabaseReference chatRef = databaseReference.child(AppConstants.FIREASE_CHAT_ENDPOINT);
                Map<String, Object> chatMap = new HashMap<String, Object>();
                Map<String, Object> lastMessageMap = new HashMap<String, Object>();
                lastMessageMap.put("message", messageText);
                lastMessageMap.put("name", LoginUtils.getLoggedinUser().getEmail());
                lastMessageMap.put("senderID", LoginUtils.getLoggedinUser().getId());
                lastMessageMap.put("timestamp",new Date().getTime());
                chatMap.put("lastMessage",lastMessageMap);
                Map<String, Object> membersMap = new HashMap<String, Object>();
                membersMap.put(LoginUtils.getLoggedinUser().getId().toString(),LoginUtils.getLoggedinUser().getEmail());
                membersMap.put(user.getId().toString(),user.getEmail());
                chatMap.put("members",membersMap);
                String key =  chatRef.push().getKey();
                user.setChatID(key);
                chatRef.child(key).setValue(chatMap);

                //Insertion in Messages node
                DatabaseReference messagesRef = databaseReference.child(AppConstants.FIREASE_MESSAGES_ENDPOINT);

                Log.d("KEY IS....",key);
                messagesRef.child(key).push().setValue(lastMessageMap);
            }
            else
            {
                //Insertion in chats node
                DatabaseReference chatRef = databaseReference.child(AppConstants.FIREASE_CHAT_ENDPOINT);
                Map<String, Object> chatMap = new HashMap<String, Object>();
                Map<String, Object> lastMessageMap = new HashMap<String, Object>();
                lastMessageMap.put("message", messageText);
                lastMessageMap.put("name", LoginUtils.getLoggedinUser().getEmail());
                lastMessageMap.put("senderID", LoginUtils.getLoggedinUser().getId());
                lastMessageMap.put("timestamp",new Date().getTime());
              //  chatMap.put("lastMessage",lastMessageMap);

//                Map<String, Object> membersMap = new HashMap<String, Object>();
//                membersMap.put(LoginUtils.getLoggedinUser().getId().toString(),LoginUtils.getLoggedinUser().getEmail());
//                membersMap.put(user.getId().toString(),user.getEmail());
//                chatMap.put("members",membersMap);

                chatRef.child(user.getChatID()).child("lastMessage").setValue(lastMessageMap);
                //Insertion in Messages node
                DatabaseReference messagesRef = databaseReference.child(AppConstants.FIREASE_MESSAGES_ENDPOINT);
                messagesRef.child(user.getChatID()).push().setValue(lastMessageMap);
            }
            ChatMessage chatMessage=new ChatMessage();
            chatMessage.setSenderID(LoginUtils.getLoggedinUser().getId().toString());
            chatMessage.setCreated_datetime(DateUtils.GetCurrentdatetime());
            chatMessage.setMessage(messageText);
            chatMessage.setName(LoginUtils.getLoggedinUser().getEmail());
            chatMessageList.add(chatMessage);
            chatAdapter.notifyDataSetChanged();

            sendNotification();
            messageArea.setText("");
            messageArea.requestFocus();

        } else if (attachments.size() > 0) {
            UploadImageToFirebase();
        } else {
            Toast.makeText(getContext(), "Please type message...", Toast.LENGTH_SHORT).show();
        }
    }


    public void sendNotification() {
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
        filter.value=LoginUtils.getLoggedinUser().getEmail();
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
            List<String> uploadedImages = new ArrayList<>();
            for (int i = 0; i < MultiplePhoto.size(); i++) {
                // MultiplePhoto.add(Uri.parse(String.valueOf(attachments)));
                rc_attachments.setVisibility(View.GONE);
                chatAttachment.setCreated_by_id(LoginUtils.getLoggedinUser().getId());
                chatAttachment.setChat_image(MultiplePhoto.get(i));
                chatAttachment.setStatus(AppConstants.ACTIVE);

                messageViewModel.uploadAttachment(chatAttachment).observe(this, new Observer<BaseModel<List<String>>>() {
                    @Override
                    public void onChanged(BaseModel<List<String>> listBaseModel) {
                        if (listBaseModel != null && !listBaseModel.isError()) {
                            if (count == MultiplePhoto.size() - 1) {
                                uploadedImages.add(listBaseModel.getData().get(0));

                                updateFirebase(uploadedImages);


                            } else {
                                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                            }
                        }
                    }


                });
            }
        }

    }
    private void updateFirebase(List<String> uploadedImages) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            if(chatMessageList.size()==0 ) {
                if (MultiplePhoto.size() == uploadedImages.size()) {
                    DatabaseReference userRef = databaseReference.child(AppConstants.FIREASE_USER_ENDPOINT);
                    DatabaseReference userRefByID = userRef.child(LoginUtils.getLoggedinUser().getId().toString());
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("imageName", LoginUtils.getLoggedinUser().getUser_image());
                    map.put("name", LoginUtils.getLoggedinUser().getEmail());
                    userRefByID.setValue(map);

                    //Insertion in chats node
                    DatabaseReference chatRef = databaseReference.child(AppConstants.FIREASE_CHAT_ENDPOINT);
                    Map<String, Object> chatMap = new HashMap<String, Object>();
                    Map<String, Object> lastMessageMap = new HashMap<String, Object>();
                    lastMessageMap.put("message", messageText);
                    lastMessageMap.put("name", LoginUtils.getLoggedinUser().getEmail());
                    lastMessageMap.put("senderID", LoginUtils.getLoggedinUser().getId());
                    lastMessageMap.put("timestamp", new Date().getTime());
                    lastMessageMap.put("images", uploadedImages);
                    chatMap.put("lastMessage", lastMessageMap);
                    Map<String, Object> membersMap = new HashMap<String, Object>();
                    membersMap.put(LoginUtils.getLoggedinUser().getId().toString(), LoginUtils.getLoggedinUser().getEmail());
                    membersMap.put(user.getId().toString(), user.getEmail());
                    chatMap.put("members", membersMap);
                    String key = chatRef.push().getKey();
                    user.setChatID(key);
                    chatRef.child(key).setValue(chatMap);

                    //Insertion in Messages node
                    DatabaseReference messagesRef = databaseReference.child(AppConstants.FIREASE_MESSAGES_ENDPOINT);

                    Log.d("KEY IS....", key);
                    messagesRef.child(key).push().setValue(lastMessageMap);
                }
            }else {
                if (MultiplePhoto.size() == uploadedImages.size()) {
                    //Insertion in chats node
                    DatabaseReference chatRef = databaseReference.child(AppConstants.FIREASE_CHAT_ENDPOINT);
                    Map<String, Object> chatMap = new HashMap<String, Object>();
                    Map<String, Object> lastMessageMap = new HashMap<String, Object>();
                    lastMessageMap.put("message", messageText);
                    lastMessageMap.put("name", LoginUtils.getLoggedinUser().getEmail());
                    lastMessageMap.put("senderID", LoginUtils.getLoggedinUser().getId());
                    lastMessageMap.put("timestamp", new Date().getTime());
                    lastMessageMap.put("images", uploadedImages);
                  //  chatMap.put("lastMessage", lastMessageMap);

//                    Map<String, Object> membersMap = new HashMap<String, Object>();
//                    membersMap.put(LoginUtils.getLoggedinUser().getId().toString(), LoginUtils.getLoggedinUser().getEmail());
//                    membersMap.put(user.getId().toString(), user.getEmail());
//                    chatMap.put("members", membersMap);

                    chatRef.child(user.getChatID()).child("lastMessage").setValue(lastMessageMap);
                    //Insertion in Messages node
                    DatabaseReference messagesRef = databaseReference.child(AppConstants.FIREASE_MESSAGES_ENDPOINT);
                    messagesRef.child(user.getChatID()).push().setValue(lastMessageMap);
                }
                }
                ChatMessage chatMessage=new ChatMessage();
                chatMessage.setSenderID(LoginUtils.getLoggedinUser().getId().toString());
                chatMessage.setCreated_datetime(DateUtils.GetCurrentdatetime());
                chatMessage.setMessage("");
                chatMessage.setChatImages(uploadedImages);
                chatMessage.setName(LoginUtils.getLoggedinUser().getEmail());
                chatMessageList.add(chatMessage);
                chatAdapter.notifyDataSetChanged();

                sendNotification();
                messageArea.setText("");
                messageArea.requestFocus();


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

                    currentPhotoPath = GalleryImage;


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
