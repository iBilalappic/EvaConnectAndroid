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
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepository implements IPostRepository {
    private MutableLiveData<BaseModel<List<Post>>> postMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Comment>>> commentMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Post>>> dashboardMutableLiveData = new MutableLiveData<>();
    @Override
    public LiveData<BaseModel<List<Post>>> createPost(Post post) {
        postMutableLiveData=new MutableLiveData<>();
        User user=LoginUtils.getLoggedinUser();
        RestClient.get().appApi().createPost(user.getId(),RequestBody.create(MediaType.parse("text/plain"),
                post.getContent()),user.getId(),RequestBody.create(MediaType.parse("text/plain"), AppConstants.STATUS_PENDING),post.isIs_url(),post.getAttachments(),post.getVideo(),post.getDocument()).enqueue(new Callback<BaseModel<List<Post>>>() {
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

    @Override
    public LiveData<BaseModel<List<Post>>> getPostById(int id) {
        postMutableLiveData=new MutableLiveData<>();
        HashMap<String ,Object> postObject=new HashMap<>();
        postObject.put("user_id",LoginUtils.getLoggedinUser().getId());
        postObject.put("post_id",id);

        RestClient.get().appApi().getPostById(postObject).enqueue(new Callback<BaseModel<List<Post>>>() {
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
    public LiveData<BaseModel<List<Post>>> getPost(User user, int total, int current) {
        dashboardMutableLiveData = new MutableLiveData<>();
        user.setUser_id(user.getId());
        user.setFilter("all_posts");
        RestClient.get().appApi().getPost(user, total, current).enqueue(new Callback<BaseModel<List<Post>>>() {
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
    public LiveData<BaseModel<List<Post>>> getPostFilter(User user, int total, int current) {
        dashboardMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("filter", "my_posts");
        body.put("user_key", user.getUser_id());
        body.put("user_id", LoginUtils.getUser().getId());
        RestClient.get().appApi().getPostFilter(body, total, current).enqueue(new Callback<BaseModel<List<Post>>>() {
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
    public LiveData<BaseModel<List<Comment>>> editComment(Comment comment,Integer id) {
        commentMutableLiveData=new MutableLiveData<>();

        RestClient.get().appApi().editComment(comment,id).enqueue(new Callback<BaseModel<List<Comment>>>() {
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
    public LiveData<BaseModel<List<Comment>>> deleteComment(Integer id) {
        commentMutableLiveData=new MutableLiveData<>();

        RestClient.get().appApi().deleteComment(id).enqueue(new Callback<BaseModel<List<Comment>>>() {
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
    public LiveData<BaseModel<List<Post>>> editPost(Post post) {
        postMutableLiveData=new MutableLiveData<>();
        post.setModified_by_id(LoginUtils.getLoggedinUser().getId());
        post.setModified_datetime(DateUtils.GetCurrentdatetime());

        RestClient.get().appApi().editPost(post.getModified_by_id(),RequestBody.create(MediaType.parse("text/plain"),
                post.getContent()),RequestBody.create(MediaType.parse("text/plain"),
                post.getModified_datetime()),false,post.getAttachments(),post.getVideo(),post.getDocument(),post.getId()).enqueue(new Callback<BaseModel<List<Post>>>() {
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
    public LiveData<BaseModel<List<Post>>> deletePost(Post post) {
        postMutableLiveData=new MutableLiveData<>();
        Post newPost=new Post();
        newPost.setModified_by_id(LoginUtils.getLoggedinUser().getId());
        newPost.setModified_datetime(DateUtils.GetCurrentdatetime());
        newPost.setStatus(AppConstants.DELETED);

        RestClient.get().appApi().deletePost(newPost,post.getId()).enqueue(new Callback<BaseModel<List<Post>>>() {
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
    public LiveData<BaseModel<List<Post>>> deleteJob(Post post) {
        postMutableLiveData=new MutableLiveData<>();
        Post newPost=new Post();
        newPost.setModified_by_id(LoginUtils.getLoggedinUser().getId());
        newPost.setModified_datetime(DateUtils.GetCurrentdatetime());
        newPost.setStatus(AppConstants.DELETED);

        RestClient.get().appApi().deleteJob(post.getId()).enqueue(new Callback<BaseModel<List<Post>>>() {
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
}
