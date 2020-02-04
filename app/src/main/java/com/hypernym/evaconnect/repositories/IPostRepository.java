package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Post;

import java.util.List;

public interface IPostRepository {
    LiveData<BaseModel<List<Post>>> createPost(Post post);

    LiveData<BaseModel<List<Comment>>> addComment(Comment comment);

    LiveData<BaseModel<List<Post>>> likePost(Post post);

    LiveData<BaseModel<List<Comment>>> getComments(Post post);

    LiveData<BaseModel<List<Post>>> getPostById(int id);

}
