package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.ChatMessage;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.toolbar.OnItemClickListener;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.adapters.ChatAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.rc_chat)
    RecyclerView rc_chat;

    @BindView(R.id.messageArea)
    EditText messageArea;

    @BindView(R.id.sendButton)
    TextView sendButton;


    ChatMessage mMessage;
    List<ChatMessage> chatMessageList = new ArrayList<>();
    public NetworkConnection networkConnection = new NetworkConnection();
    Firebase reference1, reference2;
    LinearLayoutManager layoutManager;
    ChatAdapter chatAdapter;

    public ChatFragment() {
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
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        sendButton.setOnClickListener(this);
        networkConnection = (NetworkConnection) getArguments().getSerializable(Constants.DATA);
        UserDetails.username = LoginUtils.getUser().getFirst_name();
        Log.d("TAAAG", "" + GsonUtils.toJson(networkConnection));
        if (networkConnection.getSenderId().equals(LoginUtils.getUser().getId())) {
            UserDetails.chatWith = networkConnection.getReceiver().getFirstName();
            Toast.makeText(getContext(), "receivername" + networkConnection.getReceiver().getFirstName(), Toast.LENGTH_SHORT).show();
        } else {
            UserDetails.chatWith = networkConnection.getSender().getFirstName();
            Toast.makeText(getContext(), "sendername" + networkConnection.getSender().getFirstName(), Toast.LENGTH_SHORT).show();
        }
        init();
        return view;

    }




    private void init() {
        Firebase.setAndroidContext(AppUtils.getApplicationContext());
        reference1 = new Firebase("https://evaconnect-df08d.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://evaconnect-df08d.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);
        SettingFireBaseChat();
    }

    private void SettingFireBaseChat() {
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("Message").toString();
                String userName = map.get("user").toString();

                if (userName.equals(UserDetails.username)) {
                    mMessage = new ChatMessage();
                    mMessage.setMessage(message);
                    mMessage.setType(1);
                    chatMessageList.add(mMessage);
                    Log.d("Taag", "" + chatMessageList.size());
                    setupRecycler(chatMessageList);
                } else {
                    mMessage = new ChatMessage();
                    mMessage.setMessage(message);
                    mMessage.setType(2);
                    chatMessageList.add(mMessage);
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
        rc_chat.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(getActivity(), chatMessageList);
        rc_chat.setAdapter(chatAdapter);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.sendButton:
              //  Toast.makeText(getActivity(), "sss", Toast.LENGTH_SHORT).show();
                String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }
                break;

        }

    }
}
