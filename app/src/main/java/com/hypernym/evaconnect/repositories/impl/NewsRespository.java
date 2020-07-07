package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.INewsRepository;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRespository implements INewsRepository {
    private MutableLiveData<BaseModel<List<Post>>>  newsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Comment>>> commentMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Post>>> dashboardMutableLiveData = new MutableLiveData<>();
    @Override
    public LiveData<BaseModel<List<Post>>> getNews(User user, int total, int current) {
        newsMutableLiveData=new MutableLiveData<>();
        user.setUser_id(user.getId());
        RestClient.get().appApi().getNews(user,total,current).enqueue(new Callback<BaseModel<List<Post>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Post>>> call, Response<BaseModel<List<Post>>> response) {
               newsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseModel<List<Post>>> call, Throwable t) {
                newsMutableLiveData.setValue(null);
            }
        });
        return newsMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Comment>>> addComment(Comment comment) {
        commentMutableLiveData=new MutableLiveData<>();
        comment.setRss_news_id(comment.getPost_id());
        RestClient.get().appApi().addNewsComment(comment).enqueue(new Callback<BaseModel<List<Comment>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Comment>>> call, Response<BaseModel<List<Comment>>> response) {
                commentMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseModel<List<Comment>>> call, Throwable t) {
                commentMutableLiveData.setValue(null);
            }
        });
        return commentMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Post>>> likePost(Post post) {
        newsMutableLiveData=new MutableLiveData<>();
        post.setRss_news_id(post.getId());
        RestClient.get().appApi().likeNews(post).enqueue(new Callback<BaseModel<List<Post>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Post>>> call, Response<BaseModel<List<Post>>> response) {
                newsMutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<Post>>> call, Throwable t) {
                newsMutableLiveData.setValue(null);
            }
        });
        return newsMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Comment>>> getComments(Post post) {
        commentMutableLiveData=new MutableLiveData<>();
        Post postModel=new Post();
        postModel.setRss_news_id(post.getId());

        RestClient.get().appApi().getNewsComments(postModel).enqueue(new Callback<BaseModel<List<Comment>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Comment>>> call, Response<BaseModel<List<Comment>>> response) {
                commentMutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<Comment>>> call, Throwable t) {
                commentMutableLiveData.setValue(null);
            }
        });

        return commentMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Post>>> getNewsByID(int id) {
        newsMutableLiveData=new MutableLiveData<>();
        HashMap<String ,Object> postObject=new HashMap<>();
        postObject.put("user_id", LoginUtils.getLoggedinUser().getId());
        postObject.put("rss_news_id",id);

        RestClient.get().appApi().getNewsByID(postObject).enqueue(new Callback<BaseModel<List<Post>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Post>>> call, Response<BaseModel<List<Post>>> response) {
                newsMutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<Post>>> call, Throwable t) {
                newsMutableLiveData.setValue(null);
            }
        });
        return newsMutableLiveData;
    }
}
