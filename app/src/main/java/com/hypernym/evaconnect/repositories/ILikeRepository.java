package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.User;

import java.util.List;

public interface ILikeRepository {
    LiveData<BaseModel<List<MyLikesModel>>> getLikes(int user_id);
}
