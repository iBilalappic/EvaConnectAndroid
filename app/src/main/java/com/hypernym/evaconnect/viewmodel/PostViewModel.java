package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IPostRepository;
import com.hypernym.evaconnect.repositories.impl.PostRepository;

import java.util.List;

public class PostViewModel extends AndroidViewModel {
    private IPostRepository iPostRepository=new PostRepository();

    public PostViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseModel<List<Post>>> createPost(Post post)
    {
        return iPostRepository.createPost(post);
    }

    public LiveData<BaseModel<List<Post>>> editPost(Post post)
    {
        return iPostRepository.editPost(post);
    }

    public LiveData<BaseModel<List<Post>>> deletePost(Post post)
    {
        return iPostRepository.deletePost(post);
    }
    public LiveData<BaseModel<List<Post>>> deleteJob(Post post)
    {
        return iPostRepository.deleteJob(post);
    }


    public LiveData<BaseModel<List<Comment>>> addComment(Comment comment)
    {
        return iPostRepository.addComment(comment);
    }

    public LiveData<BaseModel<List<Comment>>> editComment(Comment comment,Integer id)
    {
        return iPostRepository.editComment(comment,id);
    }

    public LiveData<BaseModel<List<Comment>>> deleteComment(Integer id)
    {
        return iPostRepository.deleteComment(id);
    }


    public LiveData<BaseModel<List<Post>>> likePost(Post post)
    {
        return iPostRepository.likePost(post);
    }
    public LiveData<BaseModel<List<Comment>>> getComments(Post post)
    {
        return iPostRepository.getComments(post);
    }
    public LiveData<BaseModel<List<Post>>> getPostByID(int id)
    {
        return iPostRepository.getPostById(id);
    }
    public LiveData<BaseModel<List<Post>>> getPost(User user, int totalpages, int currentPage)
    {
        return iPostRepository.getPost(user,totalpages,currentPage);
    }

    public LiveData<BaseModel<List<Post>>> getPostFilter(User user, int totalpages, int currentPage)
    {
        return iPostRepository.getPostFilter(user,totalpages,currentPage);
    }
}
