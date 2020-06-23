package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.ChatAttachment;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IMessageRepository;
import com.hypernym.evaconnect.repositories.impl.MessageRepository;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {

    private IMessageRepository iMessageRepository = new MessageRepository();

    public MessageViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseModel<List<NetworkConnection>>> SetUser(User user) {
        return iMessageRepository.getFriendList(user);
    }

    public LiveData<BaseModel<ChatAttachment>> uploadAttachment(ChatAttachment attachment) {
        return iMessageRepository.uploadAttachment(attachment);
    }
}