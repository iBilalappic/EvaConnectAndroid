package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.City;
import com.hypernym.evaconnect.repositories.ICityRepository;
import com.hypernym.evaconnect.repositories.impl.ICitiesRepositoryImpl;

import java.util.List;

public class CreateAccountViewModel extends AndroidViewModel {

    private ICityRepository iCitiesRepository = new ICitiesRepositoryImpl();

    public CreateAccountViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<City>> hGetAllCities(String countyCode) {
        return iCitiesRepository.hGetAllCities(countyCode);
    }
}
