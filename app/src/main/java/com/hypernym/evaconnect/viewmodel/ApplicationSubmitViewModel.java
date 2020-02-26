package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IApplicaitonSubmitRepository;
import com.hypernym.evaconnect.repositories.ICreateJobAdRepository;
import com.hypernym.evaconnect.repositories.impl.ApplicationSubmitRepository;
import com.hypernym.evaconnect.repositories.impl.CreateJobRepository;

import java.util.List;

import okhttp3.MultipartBody;

public class ApplicationSubmitViewModel extends AndroidViewModel {

    private IApplicaitonSubmitRepository iApplicaitonSubmitRepository = new ApplicationSubmitRepository();

    public ApplicationSubmitViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseModel<List<Object>>> submitApplication(User user, int job_id, String content, MultipartBody.Part image) {
        return iApplicaitonSubmitRepository.submitApplication(user, job_id, content, image);
    }
}
