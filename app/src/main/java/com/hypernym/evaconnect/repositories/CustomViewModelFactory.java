package com.hypernym.evaconnect.repositories;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

public class CustomViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private Activity activity;
    public CustomViewModelFactory(@NonNull Application application) {
        super(application);
    }
    public CustomViewModelFactory(@NonNull Application application, Activity activity) {
        super(application);
        this.activity=activity;
    }
}
