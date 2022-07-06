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
    private MutableLiveData<List<City>> citiesMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<List<City>> hGetAllCities(String countyCode) {
        citiesMutableLiveData = new MutableLiveData<>();

        AppApi service = RestClientForCities.getRetrofitInstance().create(AppApi.class);
        Log.d("test123", "Calling Cities API :"+countyCode);


        service.get_all_cities(countyCode).enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {

                Log.d("test123", "onResponse");

                if (response != null) {
                    citiesMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                Log.d("test123", "fail" + t);

                citiesMutableLiveData.setValue(null);
            }
        });

        return citiesMutableLiveData;
    }
}
