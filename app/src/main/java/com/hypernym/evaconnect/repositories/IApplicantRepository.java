package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.AppliedApplicants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.viewmodel.AppliedApplicantViewModel;

import java.util.List;

public interface IApplicantRepository {
    LiveData<BaseModel<List<AppliedApplicants>>> getApplicants(int job_id);

    LiveData<BaseModel<List<AppliedApplicants>>> declineApplication(int applicant_job_id,AppliedApplicants appliedApplicants);
}