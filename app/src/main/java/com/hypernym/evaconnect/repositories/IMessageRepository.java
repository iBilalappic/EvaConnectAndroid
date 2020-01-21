package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;

import java.util.List;

public interface IMessageRepository {
    LiveData<BaseModel<List<NetworkConnection>>> getFriendList(User user);
}
