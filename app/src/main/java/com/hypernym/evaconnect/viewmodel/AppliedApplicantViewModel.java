package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.AppliedApplicants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.repositories.IApplicantRepository;
import com.hypernym.evaconnect.repositories.ILikeRepository;
import com.hypernym.evaconnect.repositories.impl.ApplicantsRepository;
import com.hypernym.evaconnect.repositories.impl.LikeRepository;

import java.util.List;

public class AppliedApplicantViewModel extends AndroidViewModel {

    private IApplicantRepository iApplicantRepository = new ApplicantsRepository();

    public AppliedApplicantViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseModel<List<AppliedApplicants>>> getApplicants(int job_id) {
        return iApplicantRepository.getApplicants(job_id);
    }

    public LiveData<BaseModel<List<AppliedApplicants>>> declineApplication(int application_id,AppliedApplicants appliedApplicants) {
        return iApplicantRepository.declineApplication(application_id,appliedApplicants);
    }
}