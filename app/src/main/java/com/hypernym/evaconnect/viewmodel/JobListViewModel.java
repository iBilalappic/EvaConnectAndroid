package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IJobAdRepository;
import com.hypernym.evaconnect.repositories.ILikeRepository;
import com.hypernym.evaconnect.repositories.impl.JobListRepository;
import com.hypernym.evaconnect.repositories.impl.LikeRepository;

import java.util.List;

public class JobListViewModel extends AndroidViewModel {

    private IJobAdRepository iJobAdRepository = new JobListRepository() {
    };

    public JobListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseModel<List<JobAd>>> getjobAd() {
        return iJobAdRepository.getjobAd();
    }

    public LiveData<BaseModel<List<CompanyJobAdModel>>> getcompanyAd(User user) {
        return iJobAdRepository.getCompanyAd(user);
    }

}