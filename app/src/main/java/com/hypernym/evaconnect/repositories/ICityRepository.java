package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.City;

import java.util.List;

public interface ICityRepository {
    LiveData<List<City>> hGetAllCities(String countyCode);
}
