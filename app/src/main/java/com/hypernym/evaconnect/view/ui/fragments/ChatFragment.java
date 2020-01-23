package com.hypernym.evaconnect.view.ui.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.communication.api.AppApi;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.ChatMessage;
import com.hypernym.evaconnect.models.Contents;
import com.hypernym.evaconnect.models.Data;
import com.hypernym.evaconnect.models.Filter;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.Notification_onesignal;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.toolbar.OnItemClickListener;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.adapters.ChatAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.rc_chat)
    RecyclerView rc_chat;

    @BindView(R.id.messageArea)
    EditText messageArea;

    @BindView(R.id.sendButton)
    TextView sendButton;

    @BindView(R.id.browsefiles)
    TextView browsefiles;
    @BindView(R.id.scrollView)
    ScrollView scrollView;


    ChatMessage mMessage;
    List<ChatMessage> chatMessageList = new ArrayList<>();
    private NetworkConnection networkConnection = new NetworkConnection();
    Firebase reference1, reference2;
    LinearLayoutManager layoutManager;
    ChatAdapter chatAdapter;
    String messageText, ChatTime;
    public static final String DATE_INPUT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    private static final int REQUEST_PHOTO_GALLERY = 4;


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
        assert getArguments() != null;
        networkConnection = (NetworkConnection) getArguments().getSerializable(Constants.DATA);

        messageText = getArguments().getString("MESSAGE");
        UserDetails.username = LoginUtils.getUser().getFirst_name();
//        Log.d("TAAAG", "" + GsonUtils.toJson(networkConnection));
        if (networkConnection.getSenderId().equals(LoginUtils.getUser().getId())) {
            UserDetails.chatWith = networkConnection.getReceiver().getFirstName();
            UserDetails.email = networkConnection.getReceiver().getEmail();
            setPageTitle(networkConnection.getReceiver().getFirstName());
            //  Toast.makeText(getContext(), "receivername" + networkConnection.getReceiver().getFirstName(), Toast.LENGTH_SHORT).show();
        } else {
            UserDetails.chatWith = networkConnection.getSender().getFirstName();
            UserDetails.email = networkConnection.getSender().getEmail();
            setPageTitle(networkConnection.getSender().getFirstName());
            //  Toast.makeText(getContext(), "sendername" + networkConnection.getSender().getFirstName(), Toast.LENGTH_SHORT).show();
        }
        init();
        return view;

    }

    private void init() {
        Firebase.setAndroidContext(AppUtils.getApplicationContext());
        reference1 = new Firebase("https://evaconnect-df08d.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://evaconnect-df08d.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);
        SettingFireBaseChat();
        if (getArguments() != null) {
            CheckMessageText();
        }

    }

    private void CheckMessageText() {
        if (messageText != null && !messageText.equals("")) {

            Map<String, String> map = new HashMap<String, String>();
            map.put("Message", messageText);
            map.put("user", UserDetails.username);
            map.put("time", DateUtils.Getdatetime());
            reference1.push().setValue(map);
            reference2.push().setValue(map);
            messageArea.setText("");
        }
    }


    private void SettingFireBaseChat() {
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("Message").toString();
                String userName = map.get("user").toString();
                String chatTime = map.get("time").toString();

                if (userName.equals(UserDetails.username)) {
                    mMessage = new ChatMessage();
                    mMessage.setMessage(message);
                    mMessage.setType(1);
                    mMessage.setChattime(chatTime);
                    chatMessageList.add(mMessage);
                    Log.d("Taag", "" + chatMessageList.size());
                    setupRecycler(chatMessageList);
                } else {
                    mMessage = new ChatMessage();
                    mMessage.setMessage(message);
                    mMessage.setType(2);
                    chatMessageList.add(mMessage);
                    mMessage.setChattime(chatTime);
                    Log.d("Taag", "" + chatMessageList.size());
                    setupRecycler(chatMessageList);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void setupRecycler(List<ChatMessage> chatMessageList) {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        rc_chat.setLayoutManager(layoutManager);

        chatAdapter = new ChatAdapter(getActivity(), chatMessageList, networkConnection);
        rc_chat.setAdapter(chatAdapter);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sendButton:
                //  Toast.makeText(getActivity(), "sss", Toast.LENGTH_SHORT).show();
                messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Message", messageText);
                    map.put("user", UserDetails.username);
                    map.put("time", DateUtils.Getdatetime());
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                    sendNotification();
                } else {
                    // UploadImageToFirebase();
//                    Map<String, String> map = new HashMap<String, String>();
//                    map.put("Message", String.valueOf(filePath));
//                    map.put("user", UserDetails.username);
//                    reference1.push().setValue(map);
//                    reference2.push().setValue(map);
                }
                break;

            case R.id.browsefiles:
                //  Toast.makeText(getActivity(), "sss", Toast.LENGTH_SHORT).show();
                //  openPictureDialog();
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("*/*");
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PHOTO_GALLERY);
                break;

        }

    }

    public void sendNotification() {
        Notification_onesignal request = new Notification_onesignal();
        request.app_id = "44bb428a-54f5-4155-bea3-c0ac2d0b3c1a";
        request.contents = new Contents();
        request.contents.en = "Testing";
        request.data = new Data();
        request.data.data = "data";
        Filter filter = new Filter();
        filter.field = "tag";
        filter.key = "email";
        filter.relation = "=";
        filter.value = UserDetails.email;
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


    private void UploadImageToFirebase() {
        if (filePath != null) {
            // pd.show();

            StorageReference childRef = storageRef.child("image.jpg");

            //uploading the image
            UploadTask uploadTask = childRef.putFile(filePath);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // pd.dismiss();
                    Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // pd.dismiss();
                    Toast.makeText(getContext(), "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Select an image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == REQUEST_PHOTO_GALLERY && resultCode == RESULT_OK) {
            try {
                if (data != null && data.getData() != null) {
                    filePath = data.getData();
                    Toast.makeText(getContext(), "" + filePath, Toast.LENGTH_SHORT).show();
//                    Uri SelectedImageUri = data.getData();
//                    GalleryImage = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);
//                    mProfileImageDecodableString = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);
//                    Log.e(getClass().getName(), "image file path: " + GalleryImage);
//
//                    tempFile = new File(GalleryImage);
//
//                    Log.e(getClass().getName(), "file path details: " + tempFile.getName() + " " + tempFile.getAbsolutePath() + "length" + tempFile.length());
//
//
//                    if (tempFile.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.FILE_SIZE_LIMIT_IN_KB) {
//                        networkResponseDialog(getString(R.string.error),getString(R.string.err_image_size_large));
//                        return;
//                    } else {
//                        if (photoVar == null) {
//                            currentPhotoPath = GalleryImage;
//                            // photoVar = GalleryImage;
//                            file_name = new File(ImageFilePathUtil.getPath(getActivity(), SelectedImageUri));
//                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);
//
//                            // partImage = MultipartBody.Part.createFormData("user_image", file_name.getName(), reqFile);
//                            part_images.add(MultipartBody.Part.createFormData("post_image", file_name.getName(), reqFile));
//                            if (!TextUtils.isEmpty(currentPhotoPath) || currentPhotoPath != null) {
//                                attachments.add(currentPhotoPath);
//                                attachmentsAdapter.notifyDataSetChanged();
//                                rc_attachments.setVisibility(View.VISIBLE);
//
//                            } else {
//                                networkResponseDialog(getString(R.string.error),getString(R.string.err_internal_supported));
//                            }
//                        } else {
//                            networkResponseDialog(getString(R.string.error),getString(R.string.err_one_file_at_a_time));
//                            return;
//                        }
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "Something went wrong while retrieving image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                Log.e(getClass().getName(), "exc: " + exc.getMessage());
            }
        }
    }


}
