package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Dashboard;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IHomeRepository;
import com.hypernym.evaconnect.repositories.impl.HomeRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private IHomeRepository iHomeRepository =new HomeRepository();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseModel<List<Post>>> getDashboard(User user)
    {
        return iHomeRepository.getDashboard(user);
    }

}