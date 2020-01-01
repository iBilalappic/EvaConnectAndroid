package com.hypernym.evaconnect.communication;


import com.hypernym.evaconnect.models.BaseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class APICallBack<T> implements Callback<T> {

    private BaseModel<T> baseModel;

    public APICallBack(T model) {
       // this.baseModel = model;
    }

}
