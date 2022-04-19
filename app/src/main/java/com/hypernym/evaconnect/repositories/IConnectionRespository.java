package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.ConnectionModel;
import com.hypernym.evaconnect.models.GetBlockedData;
import com.hypernym.evaconnect.models.GetPendingData;
import com.hypernym.evaconnect.models.ShareConnection;
import com.hypernym.evaconnect.models.User;

import java.util.List;

public interface IConnectionRespository {
    LiveData<BaseModel<List<Connection>>> connect(Connection connection);

    LiveData<BaseModel<List<Object>>> block(Connection connection);

    LiveData<BaseModel<List<User>>> getAllConnections(int total, int current);

    LiveData<BaseModel<List<GetPendingData>>> getAllPending(int total, int current);

    LiveData<BaseModel<List<User>>> getConnectionByFilter(User user, int total, int current);

    LiveData<BaseModel<List<ConnectionModel>>> getConnected(User user, int total, int current);

    LiveData<BaseModel<List<ConnectionModel>>> getCompanies(User user, int total, int current);

    LiveData<BaseModel<List<ConnectionModel>>> getConnectedFilter(User user);

    LiveData<BaseModel<List<GetPendingData>>> getPendingFilter(User user);

    LiveData<BaseModel<List<GetBlockedData>>> getBlockedByFilter(User user);

    LiveData<BaseModel<List<User>>> getConnectionByRecommendedUser(User user, int total, int current);

    LiveData<BaseModel<User>> getConnectionCount(User user);

    LiveData<BaseModel<List<Object>>> remove_user(Integer connection_id);

    LiveData<BaseModel<List<Object>>> block_user(Integer connection_id, User user);

    LiveData<BaseModel<List<Object>>> share_connection(ShareConnection connection);
    LiveData<BaseModel<List<Object>>> share_connection_event(ShareConnection connection);
    LiveData<BaseModel<List<Object>>> share_connection_post(ShareConnection connection);
    LiveData<BaseModel<List<Object>>> share_connection_news(ShareConnection connection);
    LiveData<BaseModel<List<GetBlockedData>>> getBlockedUsers();

}
