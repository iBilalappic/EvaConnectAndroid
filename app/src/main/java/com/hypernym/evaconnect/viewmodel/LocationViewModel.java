package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.City;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.ICityRepository;
import com.hypernym.evaconnect.repositories.IUserRespository;
import com.hypernym.evaconnect.repositories.impl.ICitiesRepositoryImpl;
import com.hypernym.evaconnect.repositories.impl.UserRepository;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {

    private ICityRepository iCitiesRepository = new ICitiesRepositoryImpl();
    private IUserRespository userRepository = new UserRepository();


    public LocationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<City>> hGetAllCities(String countyCode) {
        return iCitiesRepository.hGetAllCities(countyCode);
    }

    public LiveData<BaseModel<List<Object>>> hUpdateUserLocationData(Integer user_id, User userData) {
        return userRepository.updateUserLocation(user_id, userData);
    }
}
