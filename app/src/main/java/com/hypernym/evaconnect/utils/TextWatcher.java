package com.hypernym.evaconnect.utils;

import android.app.Activity;
import android.text.Editable;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.view.adapters.ConnectionsAdapter;
import com.hypernym.evaconnect.view.ui.fragments.ConnectionsFragment;

import java.util.ArrayList;
import java.util.List;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

public class TextWatcher implements android.text.TextWatcher {

    private ConnectionsFragment fragment;
    private EditText editText;
    private List<User> users=new ArrayList<>();
    private ConnectionsAdapter connectionsAdapter;
    private String type;
    private int currentPage = PAGE_START;
    public TextWatcher(ConnectionsFragment fragment, EditText editText, List<User> arrayList, ConnectionsAdapter connectionsAdapter, String type) {
        this.fragment=fragment;
        this.editText=editText;
        this.users=arrayList;
        this.connectionsAdapter=connectionsAdapter;
        this.type=type;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        users.clear();
        connectionsAdapter.notifyDataSetChanged();
        fragment.getConnectionByFilter(type,PAGE_START);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}