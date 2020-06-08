package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.ShareConnection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IConnectionRespository;
import com.hypernym.evaconnect.repositories.IHomeRepository;
import com.hypernym.evaconnect.repositories.impl.ConnectionRepository;
import com.hypernym.evaconnect.repositories.impl.HomeRepository;

import java.util.List;

public class ConnectionViewModel extends AndroidViewModel {

    private IConnectionRespository iConnectionRepository =new ConnectionRepository();

    public ConnectionViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseModel<List<Connection>>> connect(Connection connection)
    {
        return iConnectionRepository.connect(connection);
    }

    public LiveData<BaseModel<List<User>>> getAllConnections(int total,int current)
    {
        return iConnectionRepository.getAllConnections(total,current);
    }

    public LiveData<BaseModel<List<User>>> getConnectionByFilter(User user,int total,int current)
    {
        return iConnectionRepository.getConnectionByFilter(user,total,current);
    }

    public LiveData<BaseModel<User>> getConnectionCount(User user)
    {
        return iConnectionRepository.getConnectionCount(user);
    }
    public LiveData<BaseModel<List<Object>>> remove_user(Integer connection_id)
    {
        return iConnectionRepository.remove_user(connection_id);
    }
    public LiveData<BaseModel<List<Object>>> block_user(Integer connection_id, User user)
    {
        return iConnectionRepository.block_user(connection_id,user);
    }

    public LiveData<BaseModel<List<Object>>> share_connection(ShareConnection shareConnection)
    {
        return iConnectionRepository.share_connection(shareConnection);
    }
    public LiveData<BaseModel<List<Object>>> share_connection_event(ShareConnection shareConnection)
    {
        return iConnectionRepository.share_connection_event(shareConnection);
    }
    public LiveData<BaseModel<List<Object>>> share_connection_post(ShareConnection shareConnection)
    {
        return iConnectionRepository.share_connection_post(shareConnection);
    }
}
