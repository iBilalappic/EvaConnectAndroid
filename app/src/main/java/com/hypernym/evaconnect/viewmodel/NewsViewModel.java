package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.INewsRepository;
import com.hypernym.evaconnect.repositories.impl.NewsRespository;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {
    INewsRepository iNewsRepository=new NewsRespository();
    public NewsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseModel<List<Post>>> getNews(User user, int totalpages, int currentPage)
    {
        return iNewsRepository.getNews(user,totalpages,currentPage);
    }

    public LiveData<BaseModel<List<Comment>>> addComment(Comment comment)
    {
        return iNewsRepository.addComment(comment);
    }


    public LiveData<BaseModel<List<Comment>>> editComment(Comment comment)
    {
        return iNewsRepository.editComment(comment);
    }

    public LiveData<BaseModel<List<Post>>> likePost(Post post)
    {
        return iNewsRepository.likePost(post);
    }
    public LiveData<BaseModel<List<Comment>>> getComments(Post post)
    {
        return iNewsRepository.getComments(post);
    }
    public LiveData<BaseModel<List<Post>>> getNewsByID(int id)
    {
        return iNewsRepository.getNewsByID(id);
    }

    public LiveData<BaseModel<List<Comment>>> deleteComment(Integer id)
    {
        return iNewsRepository.deleteComment(id);
    }

}
