package com.hypernym.evaconnect.utils;

import android.app.Activity;
import android.text.Editable;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.view.adapters.ConnectionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class TextWatcher implements android.text.TextWatcher {

    private Activity activity;
    private EditText editText;
    private List<User> users=new ArrayList<>();
    private ConnectionsAdapter connectionsAdapter;

    public TextWatcher(Activity activity, EditText editText, List<User> arrayList, ConnectionsAdapter connectionsAdapter) {
        this.activity=activity;
        this.editText=editText;
        this.users=arrayList;
        this.connectionsAdapter=connectionsAdapter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        filter(s.toString());

    }
    private void filter(String text) {
        //new array list that will hold the filtered data
        List<User> filterdData = new ArrayList<>();

        //looping through existing elements
        for (User s : users) {
            //if the existing elements contains the search input
            if (s.getFirst_name().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdData.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
         connectionsAdapter.filterList(filterdData);
    }
}