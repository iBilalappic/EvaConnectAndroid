package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.NewSources;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IHomeRepository;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepository implements IHomeRepository {

    private MutableLiveData<BaseModel<List<Post>>> dashboardMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Post>>> notificationMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Post>>> myActivityMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<NewSources>>> newsoucesMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<NewSources>>> selectedNewsoucesMutableLiveData = new MutableLiveData<>();


    @Override
    public LiveData<BaseModel<List<Post>>> getDashboard(User user, int total, int current) {
        dashboardMutableLiveData = new MutableLiveData<>();
        user.setUser_id(user.getId());
        RestClient.get().appApi().getDashboard(user, total, current).enqueue(new Callback<BaseModel<List<Post>>>() {
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
    public LiveData<BaseModel<List<Post>>> getDashboardSearch(User user, int total, int current, String filter) {
        dashboardMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("user_id", user.getId());
        data.put("filter", filter);
        data.put("search_key", user.getSearch_key());

        RestClient.get().appApi().getDashboardSearch(data, total, current).enqueue(new Callback<BaseModel<List<Post>>>() {
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
    public LiveData<BaseModel<List<Post>>> getAllNotifications(int totalpages, int currentPage) {
        notificationMutableLiveData = new MutableLiveData<>();
        User user = LoginUtils.getLoggedinUser();
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("receiver_id", user.getId());
        // data.put("is_read",0);
        RestClient.get().appApi().getAllNotifications(data, totalpages, currentPage).enqueue(new Callback<BaseModel<List<Post>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Post>>> call, Response<BaseModel<List<Post>>> response) {
                notificationMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseModel<List<Post>>> call, Throwable t) {
                notificationMutableLiveData.setValue(null);
            }
        });
        return notificationMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Post>>> getAllMyActivity() {
        notificationMutableLiveData = new MutableLiveData<>();
        User user = LoginUtils.getLoggedinUser();
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("id", user.getId());
        // data.put("is_read",0);
        RestClient.get().appApi().getAllMyActivity(data/*data, totalpages, currentPage*/).enqueue(new Callback<BaseModel<List<Post>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Post>>> call, Response<BaseModel<List<Post>>> response) {
                notificationMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseModel<List<Post>>> call, Throwable t) {
                notificationMutableLiveData.setValue(null);
            }
        });
        return notificationMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Post>>> notificationMarkAsRead(int id) {
        dashboardMutableLiveData = new MutableLiveData<>();
        User user = new User();
        user.setUser_id(id);
        RestClient.get().appApi().notificationMarkAsRead(user).enqueue(new Callback<BaseModel<List<Post>>>() {
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
    public LiveData<BaseModel<List<Post>>> getAllUnReadNotifications() {
        notificationMutableLiveData = new MutableLiveData<>();
        User user = LoginUtils.getLoggedinUser();
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("receiver_id", user.getId());
        data.put("is_read", 0);
        RestClient.get().appApi().getAllUnreadNotifications(data).enqueue(new Callback<BaseModel<List<Post>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Post>>> call, Response<BaseModel<List<Post>>> response) {
                notificationMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseModel<List<Post>>> call, Throwable t) {
                notificationMutableLiveData.setValue(null);
            }
        });
        return notificationMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<NewSources>>> getNewSources() {
        newsoucesMutableLiveData = new MutableLiveData<>();
        RestClient.get().appApi().getNewSources().enqueue(new Callback<BaseModel<List<NewSources>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<NewSources>>> call, Response<BaseModel<List<NewSources>>> response) {
                newsoucesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseModel<List<NewSources>>> call, Throwable t) {
                newsoucesMutableLiveData.setValue(null);
            }
        });
        return newsoucesMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<NewSources>>> getSelectedNewSources(Integer user_id) {
        selectedNewsoucesMutableLiveData = new MutableLiveData<>();
        RestClient.get().appApi().getSelectedNewSources(user_id).enqueue(new Callback<BaseModel<List<NewSources>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<NewSources>>> call, Response<BaseModel<List<NewSources>>> response) {
                selectedNewsoucesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseModel<List<NewSources>>> call, Throwable t) {
                selectedNewsoucesMutableLiveData.setValue(null);
            }
        });
        return selectedNewsoucesMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<NewSources>>> setNewSources(List<Integer> newsSelectedids) {
        newsoucesMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("user_id", LoginUtils.getUser().getId());
        data.put("news_ids", newsSelectedids);
        data.put("created_by_id", LoginUtils.getUser().getId());
        data.put("status", LoginUtils.getUser().getStatus());
        RestClient.get().appApi().setNewSources(data).enqueue(new Callback<BaseModel<List<NewSources>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<NewSources>>> call, Response<BaseModel<List<NewSources>>> response) {
                newsoucesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseModel<List<NewSources>>> call, Throwable t) {
                newsoucesMutableLiveData.setValue(null);
            }
        });
        return newsoucesMutableLiveData;
    }
}
