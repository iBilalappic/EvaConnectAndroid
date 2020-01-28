package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;

import java.util.List;

public interface IConnectionRespository {
    LiveData<BaseModel<List<Connection>>> connect(Connection connection);
    LiveData<BaseModel<List<User>>> getAllConnections();
    LiveData<BaseModel<List<User>>> getConnectionByFilter(User user);
}
