package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;

import java.util.List;

public interface INewsRepository {
    LiveData<BaseModel<List<Post>>> getNews(User user, int total, int current);

    LiveData<BaseModel<List<Comment>>> addComment(Comment comment);

    LiveData<BaseModel<List<Post>>> likePost(Post post);

    LiveData<BaseModel<List<Comment>>> getComments(Post post);

    LiveData<BaseModel<List<Post>>> getNewsByID(int id);

    LiveData<BaseModel<List<Comment>>> editComment(Comment comment);

    LiveData<BaseModel<List<Comment>>> deleteComment(Integer id);
}
