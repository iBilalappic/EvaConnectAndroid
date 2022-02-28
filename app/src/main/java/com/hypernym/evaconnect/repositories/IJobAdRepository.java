package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.SpecficJobAd;
import com.hypernym.evaconnect.models.User;

import java.util.List;

public interface IJobAdRepository {
    LiveData<BaseModel<List<JobAd>>> getjobAd(User user);
    LiveData<BaseModel<List<CompanyJobAdModel>>> getCompanyAd(User user);
    LiveData<BaseModel<List<Object>>> setLike(User user ,int application_id,String action);
    LiveData<BaseModel<List<SpecficJobAd>>> getJobId(int job_id);
    LiveData<BaseModel<List<Object>>> apply_interview(int job_id,int sender_id,int application_id,String day,String month,String year,String hour,String minutes);
    LiveData<BaseModel<List<Post>>> getJob(User user/*, int total, int current*/);
    LiveData<BaseModel<List<Object>>>setComment(User user ,int application_id,String comment);
    LiveData<BaseModel<List<Comment>>> getJobComments(int id);

    LiveData<BaseModel<List<Comment>>> deleteComment(Integer id);
    LiveData<BaseModel<List<Comment>>> editComment(Comment comment,Integer id);
    LiveData<BaseModel<Object>> setFavJob(int job_id, Boolean is_favourite_job);


}