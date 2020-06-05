package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.ICreateJobAdRepository;
import com.hypernym.evaconnect.repositories.ILikeRepository;
import com.hypernym.evaconnect.repositories.impl.CreateJobRepository;
import com.hypernym.evaconnect.repositories.impl.LikeRepository;

import java.util.List;

import okhttp3.MultipartBody;

public class CreateJobAdViewModel extends AndroidViewModel {

    private ICreateJobAdRepository iCreateJobAdRepository = new CreateJobRepository();

    public CreateJobAdViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseModel<List<Object>>> createJobAd(User user, MultipartBody.Part image,
                                                         String jobsector, int amount,
                                                         String companyName, String jobDescription, String Location, String jobtitle, String postion,int duration) {
        return iCreateJobAdRepository.createJobAd(user, image, jobsector, amount, companyName, jobDescription, Location, jobtitle, postion,duration);
    }

    public LiveData<BaseModel<List<Object>>> UpdateJobAd(int job_id, User user, MultipartBody.Part image,
                                                         String jobsector, int amount,
                                                         String companyName, String jobDescription, String Location, String jobtitle, String postion,int duration) {
        return iCreateJobAdRepository.UpdateJobAd(job_id, user, image, jobsector,  amount, companyName, jobDescription, Location, jobtitle, postion,duration);
    }
}