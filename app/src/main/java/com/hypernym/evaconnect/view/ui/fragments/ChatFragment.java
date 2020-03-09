package com.hypernym.evaconnect.view.ui.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.communication.api.AppApi;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.AppliedApplicants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.ChatMessage;
import com.hypernym.evaconnect.models.Contents;
import com.hypernym.evaconnect.models.Data;
import com.hypernym.evaconnect.models.Filter;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.Notification_onesignal;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.Receiver;
import com.hypernym.evaconnect.models.Sender;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.toolbar.OnItemClickListener;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.adapters.AttachmentsAdapter;
import com.hypernym.evaconnect.view.adapters.ChatAdapter;
import com.hypernym.evaconnect.view.adapters.MyLikeAdapter;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.JobListViewModel;

import org.jsoup.helper.StringUtil;

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
    private List<Uri> MultiplePhoto = new ArrayList<>();
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
        manipulateBundle();
        initViewModel();

        return view;

    }

    private void initViewModel() {
        jobListViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(JobListViewModel.class);
    }

    private void manipulateBundle() {
        if (getArguments() != null) {
            mFragmentname = getArguments().getString(Constants.FRAGMENT_NAME);

            if (mFragmentname != null && mFragmentname.equals(AppConstants.APPLICANT_FRAGMENT)) {
                Interviewcode();
                Toast.makeText(getContext(), "applicant", Toast.LENGTH_SHORT).show();
            } else {
                ChatCode();
            }

        }
    }

    private void Interviewcode() {
        UserDetails.username = LoginUtils.getUser().getFirst_name();
        UserDetails.userid = LoginUtils.getUser().getId();
        appliedApplicants = (AppliedApplicants) getArguments().getSerializable(Constants.DATA);
        Job_name = getArguments().getString("JOB_NAME");
        Day = getArguments().getInt("Day");
        Month = getArguments().getInt("Month");
        Year = getArguments().getInt("Year");
        Hour = getArguments().getInt("Hour");
        Minutes = getArguments().getInt("Mintues");

        messageText = "<p><b>You have been shortlisted for an interview</b> for the postion of </p>" + Job_name + "," +
                "<p>working internationally.</p><br>" + "Your interview slot is " + "<b>" + Day + "</b>" + " " + "<b>" + DateUtils.convertnumtocharmonths(Month) + "</b>" + " at " + "<b>" + Hour + "</b>" + ":" + "<b>" + Minutes + "</b>";
        // messageText = "You Have been shortlisted for an interview for the position of " + Job_name + " working internationally.";
        if (LoginUtils.getUser().getUser_id().equals(LoginUtils.getUser().getId())) {
            UserDetails.chatWith = appliedApplicants.getUser().getId().toString();
            UserDetails.email = appliedApplicants.getUser().getEmail();
            UserDetails.receiverName = appliedApplicants.getUser().getFirstName();
            UserDetails.receiverImage = appliedApplicants.getUser().getUserImage();
            UserDetails.senderName = LoginUtils.getUser().getFirst_name();
            UserDetails.senderEmail = appliedApplicants.getUser().getEmail();
            UserDetails.receiverEmail = LoginUtils.getUser().getEmail();
            UserDetails.senderImage = LoginUtils.getUser().getUser_image();
        } else {
            UserDetails.chatWith = LoginUtils.getUser().getId().toString();
            UserDetails.email = LoginUtils.getUser().getEmail();
            UserDetails.senderName = appliedApplicants.getUser().getFirstName();
            UserDetails.receiverName = LoginUtils.getUser().getFirst_name();
            UserDetails.receiverImage = LoginUtils.getUser().getUser_image();
            UserDetails.senderEmail = appliedApplicants.getUser().getEmail();
            UserDetails.receiverEmail = LoginUtils.getUser().getEmail();
            UserDetails.senderImage = appliedApplicants.getUser().getUserImage();
        }
//        if (networkConnection.getSender().getFirstName().equalsIgnoreCase(LoginUtils.getUser().getFirst_name())) {
//            setPageTitle(networkConnection.getReceiver().getFirstName());
//        } else {
//            setPageTitle(networkConnection.getSender().getFirstName());
//        }
        Receiver receiver = new Receiver();
        receiver.setFirstName(UserDetails.receiverName);
        receiver.setEmail(UserDetails.email);
        receiver.setUserImage(UserDetails.receiverImage);
        Sender sender = new Sender();
        sender.setFirstName(UserDetails.senderName);
        sender.setEmail(UserDetails.email);
        sender.setUserImage(UserDetails.senderImage);
        networkConnection.setReceiver(receiver);
        networkConnection.setSender(sender);
        networkConnection.setSenderId(UserDetails.userid);

        initInterview();
    }

    private void initInterview() {
        Firebase.setAndroidContext(AppUtils.getApplicationContext());
        reference1 = new Firebase("https://evaconnect-df08d.firebaseio.com/" + AppConstants.FIREASE_CHAT_ENDPOINT + "/" + UserDetails.userid + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://evaconnect-df08d.firebaseio.com/" + AppConstants.FIREASE_CHAT_ENDPOINT + "/" + UserDetails.chatWith + "_" + UserDetails.userid);
        SendChatToFirbase(messageText);
        SettingFireBaseChat();
        setupRecycler(chatMessageList);

    }

    private void ChatCode() {
        assert getArguments() != null;
        networkConnection = (NetworkConnection) getArguments().getSerializable(Constants.DATA);
        messageText = getArguments().getString("MESSAGE");
        currentPhotoPath = getArguments().getString("IMAGEURI");
        FileName = getArguments().getString("FILENAME");
        MultiplePhotoString = getArguments().getStringArrayList("IMAGEURILIST");
        MultipleFileString = getArguments().getStringArrayList("FILENAMELIST");
        if (MultipleFileString != null &&
                MultipleFileString.size() > 1 &&
                MultiplePhotoString != null &&
                MultiplePhotoString.size() > 1) {
            SettingMuilplePhotoString(MultiplePhotoString, MultipleFileString);
        }

        if (currentPhotoPath != null && FileName != null) {
            SelectedImageUri = Uri.parse(currentPhotoPath);
            tempFile = new File(FileName);
        }
        UserDetails.username = LoginUtils.getUser().getFirst_name();
        UserDetails.userid = LoginUtils.getUser().getId();
        UserDetails.status = AppConstants.UN_READ;
//        Log.d("TAAAG", "" + GsonUtils.toJson(networkConnection));
        if (networkConnection.getSenderId().equals(LoginUtils.getUser().getId())) {
            UserDetails.chatWith = networkConnection.getReceiver().getId().toString();
            UserDetails.email = networkConnection.getReceiver().getEmail();
            UserDetails.receiverName = networkConnection.getReceiver().getFirstName();
            UserDetails.receiverImage = networkConnection.getReceiver().getUserImage();
            UserDetails.senderName = networkConnection.getSender().getFirstName();
            UserDetails.senderEmail = networkConnection.getReceiver().getEmail();
            UserDetails.receiverEmail = networkConnection.getSender().getEmail();
            UserDetails.senderImage = networkConnection.getReceiver().getUserImage();
            //  setPageTitle(networkConnection.getReceiver().getFirstName());
            //  Toast.makeText(getContext(), "receivername" + networkConnection.getReceiver().getFirstName(), Toast.LENGTH_SHORT).show();
        } else {
            UserDetails.chatWith = networkConnection.getSender().getId().toString();
            UserDetails.email = networkConnection.getSender().getEmail();
            UserDetails.senderName = networkConnection.getReceiver().getFirstName();
            UserDetails.receiverName = networkConnection.getSender().getFirstName();
            UserDetails.receiverImage = networkConnection.getSender().getUserImage();
            UserDetails.senderEmail = networkConnection.getReceiver().getEmail();
            UserDetails.receiverEmail = networkConnection.getSender().getEmail();
            UserDetails.senderImage = networkConnection.getReceiver().getUserImage();
            // setPageTitle(networkConnection.getSender().getFirstName());
            //  Toast.makeText(getContext(), "sendername" + networkConnection.getSender().getFirstName(), Toast.LENGTH_SHORT).show();
        }
        if (networkConnection.getSender().getFirstName().equalsIgnoreCase(LoginUtils.getUser().getFirst_name())) {
            setPageTitle(networkConnection.getReceiver().getFirstName());
        } else {
            setPageTitle(networkConnection.getSender().getFirstName());
        }
        init();
    }


    private void SettingMuilplePhotoString(List<String> multiplePhotoString, List<String> multipleFileString) {
        for (int i = 0; i < multiplePhotoString.size(); i++) {
            MultiplePhoto.add(Uri.parse(multiplePhotoString.get(i)));
        }
        for (int i = 0; i < multipleFileString.size(); i++) {
            MultipleFile.add(new File(multipleFileString.get(i)));
        }

    }

    private void init() {
        showBackButton();
        Firebase.setAndroidContext(AppUtils.getApplicationContext());
        reference1 = new Firebase("https://evaconnect-df08d.firebaseio.com/" + AppConstants.FIREASE_CHAT_ENDPOINT + "/" + UserDetails.userid + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://evaconnect-df08d.firebaseio.com/" + AppConstants.FIREASE_CHAT_ENDPOINT + "/" + UserDetails.chatWith + "_" + UserDetails.userid);
        setupRecycler(chatMessageList);
        SettingFireBaseChat();
        if (getArguments() != null) {
            CheckMessageText();
        }
        attachmentsAdapter = new AttachmentsAdapter(getContext(), attachments, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rc_attachments.setLayoutManager(linearLayoutManager);
        rc_attachments.setAdapter(attachmentsAdapter);
    }

    private void CheckMessageText() {

        if ((messageText != null && !messageText.equals("") || SelectedImageUri != null || (MultiplePhoto != null && MultiplePhoto.size() > 1))) {
            if (MultiplePhoto != null && MultiplePhoto.size() > 1) {
                UploadImageToFirebase();
            } else if (SelectedImageUri != null) {
                UploadImageToFirebase();
            } else {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("message", messageText);
                map.put("email", UserDetails.email);
                map.put("time", DateUtils.GetCurrentdatetime());
                map.put("user", UserDetails.username);
                map.put("image", null);
                map.put("sender_name", UserDetails.username);
                map.put("receiver_name", UserDetails.receiverName);
                map.put("receiver_image", UserDetails.receiverImage);
                map.put("timestamp", ServerValue.TIMESTAMP);
                map.put("sender_email", UserDetails.senderEmail);
                map.put("receiver_email", UserDetails.receiverEmail);
                map.put("sender_image", UserDetails.senderImage);
                map.put("status", UserDetails.status);
                map.put("type", null);
                reference1.push().setValue(map);
                reference2.push().setValue(map);
                sendNotification();
                messageArea.setText("");
            }
        }
    }

    private void SettingFireBaseChat() {
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String email = map.get("email").toString();
                String chatTime = map.get("time").toString();
                String Day = null;
                String Month = null;
                String Year = null;
                String Hour = null;
                String Mintues = null;
                String Job_id = null;
                String Application_id = null;
                String Sender_id = null;

                String userKey = dataSnapshot.getKey();
                if (email.equalsIgnoreCase(LoginUtils.getLoggedinUser().getEmail()) && map.get("status").equals(AppConstants.UN_READ)) {
                    reference1.child(userKey).child("status").setValue(AppConstants.READ);
                } else if (!email.equalsIgnoreCase(LoginUtils.getLoggedinUser().getEmail())) {
                    reference1.child(userKey).child("status").setValue(AppConstants.UN_READ);
                }

                // reference1.push().setValue("status",AppConstants.READ);

                List<String> image = new ArrayList<>();
                if (map.get("image") != null) {
                    image = (List<String>) map.get("image");
                }
                String type = null;
                if (map.get("type") != null) {
                    type = map.get("type").toString();
                }
                if (map.get("Day") != null) {
                    Day = map.get("Day").toString();
                }
                if (map.get("Month") != null) {
                    Month = map.get("Month").toString();
                }
                if (map.get("Year") != null) {
                    Year = map.get("Year").toString();
                }
                if (map.get("Hour") != null) {
                    Hour = map.get("Hour").toString();
                }
                if (map.get("Minutes") != null) {
                    Mintues = map.get("Minutes").toString();
                }
                if (map.get("Job_id") != null) {
                    Job_id = map.get("Job_id").toString();
                }
                if (map.get("application_id") != null) {
                    Application_id = map.get("application_id").toString();
                }
                if (map.get("Sender_id") != null) {
                    Sender_id = map.get("Sender_id").toString();
                }
                if (userName.equals(UserDetails.username)) {
                    mMessage = new ChatMessage();
                    mMessage.setMessage(message);
                    mMessage.setType(1);
                    mMessage.setChattime(chatTime);
                    mMessage.setImage(image);
                    mMessage.setEmail(email);
                    mMessage.setType_interview(type);
                    mMessage.setDay(Day);
                    mMessage.setMonth(Month);
                    mMessage.setYear(Year);
                    mMessage.setHour(Hour);
                    mMessage.setMinutes(Mintues);
                    mMessage.setMessage_key(userKey);
                    mMessage.setJob_id(Job_id);
                    mMessage.setApplication_id(Application_id);
                    mMessage.setSender_id(Sender_id);
                    chatMessageList.add(mMessage);
                    Log.d("Taag", "" + chatMessageList.size());
                    chatAdapter.notifyDataSetChanged();
                    // setupRecycler(chatMessageList);
                } else {
                    mMessage = new ChatMessage();
                    mMessage.setMessage(message);
                    mMessage.setType(2);
                    mMessage.setImage(image);
                    mMessage.setEmail(email);
                    mMessage.setType_interview(type);
                    mMessage.setDay(Day);
                    mMessage.setMonth(Month);
                    mMessage.setYear(Year);
                    mMessage.setHour(Hour);
                    mMessage.setMinutes(Mintues);
                    mMessage.setMessage_key(userKey);
                    mMessage.setJob_id(Job_id);
                    mMessage.setApplication_id(Application_id);
                    mMessage.setSender_id(Sender_id);
                    chatMessageList.add(mMessage);
                    mMessage.setChattime(chatTime);
                    Log.d("Taag", "" + chatMessageList.size());
                    chatAdapter.notifyDataSetChanged();
                    //setupRecycler(chatMessageList);
                }

                if (chatMessageList.size() > 0) {
                    rc_chat.smoothScrollToPosition(chatMessageList.size() - 1);
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
                if (mFragmentname != null && mFragmentname.equals(AppConstants.APPLICANT_FRAGMENT)) {
                    sendtofirebase_interview();
                } else {
                    sendtofirebase_chat();
                }
                break;

            case R.id.browsefiles:
                //   openPictureDialog();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PHOTO_GALLERY);
                break;

        }

    }

    private void sendtofirebase_chat() {
        messageText = messageArea.getText().toString();
        Map<String, Object> map = new HashMap<String, Object>();
        if (attachments.size() == 0 && messageText.length() > 0) {
            map.put("message", messageText);
            map.put("user", UserDetails.username);
            map.put("time", DateUtils.GetCurrentdatetime());
            map.put("status", AppConstants.UN_READ);
            map.put("image", null);
            map.put("type", null);
            if (networkConnection.getSender().getId().equals(LoginUtils.getLoggedinUser().getId())) {
                map.put("sender_name", networkConnection.getSender().getFirstName());
                map.put("receiver_name", networkConnection.getReceiver().getFirstName());
                map.put("sender_email", networkConnection.getSender().getEmail());
                map.put("receiver_email", networkConnection.getReceiver().getEmail());
                map.put("sender_image", networkConnection.getSender().getUserImage());
                map.put("receiver_image", networkConnection.getReceiver().getUserImage());
                map.put("email", networkConnection.getReceiver().getEmail());
            } else {
                map.put("sender_name", networkConnection.getReceiver().getFirstName());
                map.put("receiver_name", networkConnection.getSender().getFirstName());
                map.put("sender_email", networkConnection.getReceiver().getEmail());
                map.put("receiver_email", networkConnection.getSender().getEmail());
                map.put("sender_image", networkConnection.getReceiver().getUserImage());
                map.put("receiver_image", networkConnection.getSender().getUserImage());
                map.put("email", networkConnection.getSender().getEmail());
            }

            map.put("timestamp", ServerValue.TIMESTAMP);
            reference1.push().setValue(map);
            reference2.push().setValue(map);
            sendNotification();
            messageArea.setText("");
            //  messageArea.requestFocus();

        } else if (attachments.size() > 0) {
            UploadImageToFirebase();
        } else {
            Toast.makeText(getContext(), "Please type message...", Toast.LENGTH_SHORT).show();
        }
    }

    private void SendChatToFirbase(String messgae) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (SelectedImageUri == null) {
            if (messgae.length() > 0) {
                map.put("message", messgae);
            } else {
                map.put("message", messageText);
            }

            map.put("user", UserDetails.username);
            map.put("time", DateUtils.GetCurrentdatetime());
            map.put("status", AppConstants.UN_READ);
            map.put("image", null);
            map.put("type", "Interview");
            map.put("Day", Day);
            map.put("Month", Month);
            map.put("Year", Year);
            map.put("Hour", Hour);
            map.put("Minutes", Minutes);
            map.put("Job_id", appliedApplicants.getJobId());
            map.put("application_id", appliedApplicants.getId());
            map.put("Sender_id",LoginUtils.getUser().getId());
            if (LoginUtils.getUser().getUser_id().equals(LoginUtils.getUser().getId())) {
                map.put("sender_name", LoginUtils.getUser().getFirst_name());
                map.put("receiver_name", appliedApplicants.getUser().getFirstName());
                map.put("sender_email", LoginUtils.getUser().getEmail());
                map.put("receiver_email", appliedApplicants.getUser().getEmail());
                map.put("sender_image", LoginUtils.getUser().getUser_image());
                map.put("receiver_image", appliedApplicants.getUser().getUserImage());
                map.put("email", appliedApplicants.getUser().getEmail());
            } else {
                map.put("sender_name", appliedApplicants.getUser().getFirstName());
                map.put("receiver_name", LoginUtils.getUser().getFirst_name());
                map.put("sender_email", appliedApplicants.getUser().getEmail());
                map.put("receiver_email", LoginUtils.getUser().getEmail());
                map.put("sender_image", appliedApplicants.getUser().getUserImage());
                map.put("receiver_image", LoginUtils.getUser().getUser_image());
                map.put("email", LoginUtils.getUser().getEmail());
            }
            map.put("timestamp", ServerValue.TIMESTAMP);


            reference1.push().setValue(map);
            reference2.push().setValue(map);
            sendNotification();
            messageArea.setText("");
        } else if (SelectedImageUri != null) {
            UploadImageToFirebase();
        }

        //sendNotification();
    }

    private void sendtofirebase_interview() {
        if (messageArea.getText().toString().length() > 0) {
            SendChatToFirbase_interview(messageArea.getText().toString());
        } else {
            Toast.makeText(getContext(), "Please type message...", Toast.LENGTH_SHORT).show();
        }

    }

    private void SendChatToFirbase_interview(String messgae) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (SelectedImageUri == null) {
            if (messgae.length() > 0) {
                map.put("message", messgae);
            }
            map.put("user", UserDetails.username);
            map.put("time", DateUtils.GetCurrentdatetime());
            map.put("status", AppConstants.UN_READ);
            map.put("image", null);
            if (LoginUtils.getUser().getUser_id().equals(LoginUtils.getUser().getId())) {
                map.put("sender_name", LoginUtils.getUser().getFirst_name());
                map.put("receiver_name", appliedApplicants.getUser().getFirstName());
                map.put("sender_email", LoginUtils.getUser().getEmail());
                map.put("receiver_email", appliedApplicants.getUser().getEmail());
                map.put("sender_image", LoginUtils.getUser().getUser_image());
                map.put("receiver_image", appliedApplicants.getUser().getUserImage());
                map.put("email", appliedApplicants.getUser().getEmail());
            } else {
                map.put("sender_name", appliedApplicants.getUser().getFirstName());
                map.put("receiver_name", LoginUtils.getUser().getFirst_name());
                map.put("sender_email", appliedApplicants.getUser().getEmail());
                map.put("receiver_email", LoginUtils.getUser().getEmail());
                map.put("sender_image", appliedApplicants.getUser().getUserImage());
                map.put("receiver_image", LoginUtils.getUser().getUser_image());
                map.put("email", LoginUtils.getUser().getEmail());
            }
            map.put("timestamp", ServerValue.TIMESTAMP);
            messageArea.setText("");

            reference1.push().setValue(map);
            reference2.push().setValue(map);
        } else if (SelectedImageUri != null) {
            UploadImageToFirebase();
        }
    }

    public void sendNotification() {
        Notification_onesignal request = new Notification_onesignal();
        request.app_id = "44bb428a-54f5-4155-bea3-c0ac2d0b3c1a";
        request.contents = new Contents();
        if (!messageText.equals("")) {
            request.contents.en = "New Message From " + UserDetails.username;
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
        if (networkConnection.getSenderId().equals(LoginUtils.getLoggedinUser().getId())) {
            filter.value = networkConnection.getReceiver().getEmail();
        } else {
            filter.value = networkConnection.getSender().getEmail();
        }
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
            for (int i = 0; i < MultiplePhoto.size(); i++) {
                // MultiplePhoto.add(Uri.parse(String.valueOf(attachments)));
                rc_attachments.setVisibility(View.GONE);
                StorageReference childRef = storageRef.child(MultipleFile.get(i).getName().toString());

                UploadTask uploadTask = childRef.putFile(MultiplePhoto.get(i));
                showDialog();
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // pd.dismiss();
                        childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                ChatUrl.add(uri.toString());
                                if (count == MultiplePhoto.size() - 1) {
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("message", messageText);
                                    map.put("user", UserDetails.username);
                                    map.put("email", UserDetails.email);
                                    map.put("time", DateUtils.GetCurrentdatetime());
                                    map.put("image", ChatUrl);
                                    map.put("status", AppConstants.UN_READ);
                                    map.put("type", null);
                                    if (networkConnection.getSenderId().equals(LoginUtils.getUser().getId())) {
                                        map.put("sender_name", networkConnection.getSender().getFirstName());
                                        map.put("receiver_name", networkConnection.getReceiver().getFirstName());
                                        map.put("sender_image", networkConnection.getSender().getUserImage());
                                        map.put("receiver_image", networkConnection.getReceiver().getUserImage());
                                        map.put("sender_email", networkConnection.getSender().getEmail());
                                        map.put("receiver_email", networkConnection.getReceiver().getEmail());
                                    } else {
                                        map.put("sender_name", networkConnection.getReceiver().getFirstName());
                                        map.put("receiver_name", networkConnection.getSender().getFirstName());
                                        map.put("sender_image", networkConnection.getReceiver().getUserImage());
                                        map.put("receiver_image", networkConnection.getSender().getUserImage());
                                        map.put("sender_email", networkConnection.getReceiver().getEmail());
                                        map.put("receiver_email", networkConnection.getSender().getEmail());
                                    }


                                    map.put("timestamp", ServerValue.TIMESTAMP);
                                    reference1.push().setValue(map);
                                    reference2.push().setValue(map);
                                    sendNotification();
                                    attachments.clear();
                                    MultipleFile.clear();
                                    ChatUrl.clear();
                                    MultiplePhoto.clear();
                                    SelectedImageUri = null;
                                    messageArea.setText("");
                                    count = 0;
                                }
                                count++;
                                //  Toast.makeText(getContext(), "onSuccess: uri= " + uri.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        hideDialog();
                        Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                    }

                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // pd.dismiss();
                        hideDialog();
                        Toast.makeText(getContext(), "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
//        else {
//            rc_attachments.setVisibility(View.GONE);
//            StorageReference childRef = storageRef.child(tempFile.getName().toString());
//
//            //uploading the image
//            UploadTask uploadTask = childRef.putFile(SelectedImageUri);
//            showDialog();
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // pd.dismiss();
//                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            ChatUrl.add(uri.toString());
//                            Map<String, Object> map = new HashMap<String, Object>();
//                            map.put("message", messageText);
//                            map.put("user", UserDetails.username);
//                            map.put("email", UserDetails.email);
//                            map.put("time", DateUtils.GetCurrentdatetime());
//                            map.put("image", ChatUrl);
//                            if (networkConnection.getSenderId().equals(LoginUtils.getUser().getId())) {
//                                map.put("sender_name",networkConnection.getSender().getFirstName());
//                                map.put("receiver_name",networkConnection.getReceiver().getFirstName());
//                            }
//                            else
//                            {
//                                map.put("sender_name",networkConnection.getReceiver().getFirstName());
//                                map.put("receiver_name",networkConnection.getSender().getFirstName());
//                            }
//
//                            map.put("receiver_image",UserDetails.receiverImage);
//                            map.put("timestamp", ServerValue.TIMESTAMP);
//                            reference1.push().setValue(map);
//                            reference2.push().setValue(map);
//                            sendNotification();
//                            attachments.clear();
//                            MultipleFile.clear();
//                            MultiplePhoto.clear();
//                            ChatUrl.clear();
//                            SelectedImageUri = null;
//                            messageArea.setText("");
//                            //  Toast.makeText(getContext(), "onSuccess: uri= " + uri.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    hideDialog();
//                    Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
//                }

//            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    // pd.dismiss();
//                    hideDialog();
//                    Toast.makeText(getContext(), "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
//                }
//            });
        // }


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
                            MultiplePhoto.add(SelectedImageUri);
                            // photoVar = GalleryImage;
                            file_name = new File(ImageFilePathUtil.getPath(getActivity(), SelectedImageUri));
                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);

                            // partImage = MultipartBody.Part.createFormData("user_image", file_name.getName(), reqFile);

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
            case R.id.tv_accept:
                Log.d("TAAAG",GsonUtils.toJson(chatMessageList.get(position)));
                AppliedForInterview(position);
//                Toast.makeText(getContext(), "accept", Toast.LENGTH_SHORT).show();
                String message_accept="The applicant has accepted a interview slot";
                chat_acceptFirebase(message_accept);
                break;
            case R.id.tv_decline:
                reference1.child(chatMessageList.get(position).getMessage_key()).child("type").setValue(AppConstants.INTERVIEW_DECLINE);
//                Toast.makeText(getContext(), "accept", Toast.LENGTH_SHORT).show();
                String message_decline="The applicant has decline a interview slot";
                chat_acceptFirebase(message_decline);
                break;
            case R.id.tv_reschedule:
                reference1.child(chatMessageList.get(position).getMessage_key()).child("type").setValue(AppConstants.RESCHEDULE);
//                Toast.makeText(getContext(), "accept", Toast.LENGTH_SHORT).show();
                String message_schedule="The applicant has requested a rescheduled interview slot";
                chat_acceptFirebase(message_schedule);
                break;
        }

    }

    private void AppliedForInterview(int position) {

        jobListViewModel.apply_interview(
                Integer.parseInt(chatMessageList.get(position).getJob_id()),
                Integer.parseInt(chatMessageList.get(position).getSender_id()),
                Integer.parseInt(chatMessageList.get(position).getApplication_id()),
                chatMessageList.get(position).getDay(),
                chatMessageList.get(position).getMonth(),
                chatMessageList.get(position).getYear(),
                chatMessageList.get(position).getHour(),
                chatMessageList.get(position).getMinutes()).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> apply_interview) {
                if (apply_interview != null && !apply_interview.isError()) {
                    reference1.child(chatMessageList.get(position).getMessage_key()).child("type").setValue(AppConstants.INTERVIEW_ACCEPTED);
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();

            }
        });
    }

    private void chat_acceptFirebase(String messageText) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", messageText);
        map.put("type", AppConstants.REQUEST_RESCHEDULE);
        map.put("user", UserDetails.username);
        map.put("time", DateUtils.GetCurrentdatetime());
        map.put("status", AppConstants.UN_READ);
        map.put("image", null);
        if (networkConnection.getSender().getId().equals(LoginUtils.getLoggedinUser().getId())) {
            map.put("sender_name", networkConnection.getSender().getFirstName());
            map.put("receiver_name", networkConnection.getReceiver().getFirstName());
            map.put("sender_email", networkConnection.getSender().getEmail());
            map.put("receiver_email", networkConnection.getReceiver().getEmail());
            map.put("sender_image", networkConnection.getSender().getUserImage());
            map.put("receiver_image", networkConnection.getReceiver().getUserImage());
            map.put("email", networkConnection.getReceiver().getEmail());
        } else {
            map.put("sender_name", networkConnection.getReceiver().getFirstName());
            map.put("receiver_name", networkConnection.getSender().getFirstName());
            map.put("sender_email", networkConnection.getReceiver().getEmail());
            map.put("receiver_email", networkConnection.getSender().getEmail());
            map.put("sender_image", networkConnection.getReceiver().getUserImage());
            map.put("receiver_image", networkConnection.getSender().getUserImage());
            map.put("email", networkConnection.getSender().getEmail());
        }
        map.put("timestamp", ServerValue.TIMESTAMP);
        reference2.push().setValue(map);
    }
}
