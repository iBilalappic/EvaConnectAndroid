package com.hypernym.evaconnect.utils;

import android.app.Activity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.view.adapters.ConnectionsAdapter;
import com.nguyencse.URLEmbeddedView;

import java.util.ArrayList;
import java.util.List;

public class URLTextWatcher implements android.text.TextWatcher {

    private Activity activity;
    private EditText editText;
    private URLEmbeddedView urlEmbeddedView;


    public URLTextWatcher(Activity activity, EditText editText, URLEmbeddedView urlEmbeddedView) {
        this.activity=activity;
        this.editText=editText;
        this.urlEmbeddedView=urlEmbeddedView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        ArrayList<String> urlList=AppUtils.containsURL(s.toString());
           if(urlList.size()>0)
           {
               AppUtils.showUrlEmbeddedView(urlList.get(0).toString(),urlEmbeddedView);
               urlEmbeddedView.setVisibility(View.VISIBLE);
           }
           else
           {
               urlEmbeddedView.setVisibility(View.GONE);
           }

    }

}