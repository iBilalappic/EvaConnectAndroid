package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.ILikeRepository;
import com.hypernym.evaconnect.repositories.IMessageRepository;
import com.hypernym.evaconnect.repositories.impl.LikeRepository;
import com.hypernym.evaconnect.repositories.impl.MessageRepository;

import java.util.List;

public class MylikesViewModel extends AndroidViewModel {

    private ILikeRepository iLikeRepository = new LikeRepository();

    public MylikesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseModel<List<MyLikesModel>>> SetLikes(int user_id) {
        return iLikeRepository.getLikes(user_id);
    }
}