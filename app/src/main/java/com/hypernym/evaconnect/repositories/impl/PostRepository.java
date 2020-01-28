package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IPostRepository;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepository implements IPostRepository {
    private MutableLiveData<BaseModel<List<Post>>> postMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Comment>>> commentMutableLiveData = new MutableLiveData<>();
    @Override
    public LiveData<BaseModel<List<Post>>> createPost(Post post) {
        postMutableLiveData=new MutableLiveData<>();
        User user=LoginUtils.getLoggedinUser();
        RestClient.get().appApi().createPost(user.getUser_id(),RequestBody.create(MediaType.parse("text/plain"),
                post.getContent()),user.getUser_id(),RequestBody.create(MediaType.parse("text/plain"), AppConstants.STATUS_PENDING),post.getAttachments(),post.getVideo()).enqueue(new Callback<BaseModel<List<Post>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Post>>> call, Response<BaseModel<List<Post>>> response) {
                postMutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<Post>>> call, Throwable t) {
                postMutableLiveData.setValue(null);
            }
        });
        return  postMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Comment>>> addComment(Comment comment) {
        commentMutableLiveData=new MutableLiveData<>();
        RestClient.get().appApi().addComment(comment).enqueue(new Callback<BaseModel<List<Comment>>>() {
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
        postMutableLiveData=new MutableLiveData<>();
        RestClient.get().appApi().likePost(post).enqueue(new Callback<BaseModel<List<Post>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Post>>> call, Response<BaseModel<List<Post>>> response) {
                postMutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<Post>>> call, Throwable t) {
                postMutableLiveData.setValue(null);
            }
        });
        return postMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Comment>>> getComments(Post post) {
        commentMutableLiveData=new MutableLiveData<>();
        Post postModel=new Post();
        postModel.setPost_id(post.getId());

        RestClient.get().appApi().getPostComments(postModel).enqueue(new Callback<BaseModel<List<Comment>>>() {
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
}
