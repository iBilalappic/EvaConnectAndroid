package com.hypernym.evaconnect.repositories.impl;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClientForCities;
import com.hypernym.evaconnect.communication.api.AppApi;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.City;
import com.hypernym.evaconnect.repositories.ICityRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ICitiesRepositoryImpl implements ICityRepository {
    private MutableLiveData<BaseModel<List<City>>> citiesMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<City>>> hGetAllCities(String countyCode) {
        citiesMutableLiveData = new MutableLiveData<>();

        AppApi service = RestClientForCities.getRetrofitInstance().create(AppApi.class);
        if (service != null) {
            service.get_all_cities(countyCode).enqueue(new Callback<BaseModel<List<City>>>() {
                @Override
                public void onResponse(Call<BaseModel<List<City>>> call, Response<BaseModel<List<City>>> response) {
                    if (response != null) {
                        citiesMutableLiveData.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<BaseModel<List<City>>> call, Throwable t) {
                    citiesMutableLiveData.setValue(null);
                }
            });
        } else {
            Log.d("hGetAllCities", "hGetAllCities: null");
        }


        return citiesMutableLiveData;
    }
}
