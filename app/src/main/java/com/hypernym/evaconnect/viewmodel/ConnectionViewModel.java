package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Post;
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

    public LiveData<BaseModel<List<User>>> getAllConnections()
    {
        return iConnectionRepository.getAllConnections();
    }

    public LiveData<BaseModel<List<User>>> getConnectionByFilter(User user)
    {
        return iConnectionRepository.getConnectionByFilter(user);
    }
}
