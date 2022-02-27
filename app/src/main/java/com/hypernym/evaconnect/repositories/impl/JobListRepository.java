package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.SpecficJobAd;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IJobAdRepository;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobListRepository implements IJobAdRepository {

    private MutableLiveData<BaseModel<List<JobAd>>> MessageMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<CompanyJobAdModel>>> CompanyMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> LikeMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<SpecficJobAd>>> JobMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> InterviewMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Post>>> dashboardMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Comment>>> commentMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<Object>> FavMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<JobAd>>> getjobAd(User user) {
        MessageMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_id", user.getId());
        RestClient.get().appApi().getjobAd(body).enqueue(new Callback<BaseModel<List<JobAd>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<JobAd>>> call, Response<BaseModel<List<JobAd>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    MessageMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    MessageMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<JobAd>>> call, Throwable t) {
                MessageMutableLiveData.setValue(null);
            }
        });
        return MessageMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<CompanyJobAdModel>>> getCompanyAd(User user) {
        CompanyMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_id", user.getId());
        RestClient.get().appApi().getCompanyAd(body).enqueue(new Callback<BaseModel<List<CompanyJobAdModel>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<CompanyJobAdModel>>> call, Response<BaseModel<List<CompanyJobAdModel>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    CompanyMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    CompanyMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<CompanyJobAdModel>>> call, Throwable t) {
                CompanyMutableLiveData.setValue(null);
            }
        });
        return CompanyMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<SpecficJobAd>>> getJobId(int job_id) {
        JobMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_id", LoginUtils.getUser().getId());
        RestClient.get().appApi().GetJobAd_ID(job_id,body).enqueue(new Callback<BaseModel<List<SpecficJobAd>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<SpecficJobAd>>> call, Response<BaseModel<List<SpecficJobAd>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                   JobMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    JobMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<SpecficJobAd>>> call, Throwable t) {
                JobMutableLiveData.setValue(null);
            }
        });
        return JobMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Object>>> apply_interview(int job_id,int sender_id,int application_id,String day,String month,String year,String hour,String mintues) {
        InterviewMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_id",LoginUtils.getUser().getId());
        body.put("job_id",job_id);
        body.put("job_application_id",application_id);
        body.put("created_by_id",sender_id);
        body.put("interview_date",year+"-"+month+"-"+day);
        body.put("interview_time",hour+":"+mintues+":"+"00");
        body.put("status",LoginUtils.getUser().getStatus());
        RestClient.get().appApi().apply_interview(body).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    InterviewMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    InterviewMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                InterviewMutableLiveData.setValue(null);
            }
        });
        return InterviewMutableLiveData;
    }


    @Override
    public LiveData<BaseModel<List<Object>>> setLike(User user, int application_id, String action) {
        LikeMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("job_id", application_id);
        body.put("created_by_id", user.getId());
        body.put("status", user.getStatus());
        body.put("action", action);
        RestClient.get().appApi().setLikeJob(body).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    LikeMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    LikeMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                LikeMutableLiveData.setValue(null);
            }
        });
        return LikeMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Post>>> getJob(User user, int total, int current) {
        dashboardMutableLiveData = new MutableLiveData<>();
        user.setUser_id(user.getId());
        RestClient.get().appApi().getJob(user, total, current).enqueue(new Callback<BaseModel<List<Post>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Post>>> call, Response<BaseModel<List<Post>>> response) {
                dashboardMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseModel<List<Post>>> call, Throwable t) {
                dashboardMutableLiveData.setValue(null);
            }
        });
        return dashboardMutableLiveData;
    }







    @Override
    public LiveData<BaseModel<List<Object>>> setComment(User user, int application_id, String comment) {
        LikeMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("job_id", application_id);
        body.put("created_by_id", user.getId());
        body.put("status", user.getStatus());
        body.put("content", comment);
        RestClient.get().appApi().setJobComment(body).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    LikeMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    LikeMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                LikeMutableLiveData.setValue(null);
            }
        });
        return LikeMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Comment>>> getJobComments(int id) {
        commentMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("job_id", id);

        RestClient.get().appApi().getJobComments(body).enqueue(new Callback<BaseModel<List<Comment>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Comment>>> call, Response<BaseModel<List<Comment>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    commentMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    commentMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Comment>>> call, Throwable t) {
                commentMutableLiveData.setValue(null);
            }
        });
        return commentMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Comment>>> deleteComment(Integer id) {
        commentMutableLiveData = new MutableLiveData<>();
        RestClient.get().appApi().deleteJobComment(id).enqueue(new Callback<BaseModel<List<Comment>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Comment>>> call, Response<BaseModel<List<Comment>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    commentMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    commentMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Comment>>> call, Throwable t) {
                commentMutableLiveData.setValue(null);
            }
        });
        return commentMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Comment>>> editComment(Comment comment, Integer id) {
        commentMutableLiveData = new MutableLiveData<>();
         RestClient.get().appApi().editJobComment(comment,id).enqueue(new Callback<BaseModel<List<Comment>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Comment>>> call, Response<BaseModel<List<Comment>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    commentMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    commentMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Comment>>> call, Throwable t) {
                commentMutableLiveData.setValue(null);
            }
        });
        return commentMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<Object>> setFavJob(int job_id, Boolean is_favourite_job) {
        FavMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("job_id", job_id);
        body.put("user_id", LoginUtils.getUser().getId());
        body.put("is_favourite", true);
        body.put("status", "active");

        RestClient.get().appApi().save_job(body).enqueue(new Callback<BaseModel<Object>>() {
            @Override
            public void onResponse(Call<BaseModel<Object>> call, Response<BaseModel<Object>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    FavMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    FavMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Object>> call, Throwable t) {
                FavMutableLiveData.setValue(null);
            }
        });
        return FavMutableLiveData;
    }


}