package com.hypernym.evaconnect.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkChangeReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
           HashMap<String,Object> body=new HashMap<>();

            body.put("modified_by_id",LoginUtils.getLoggedinUser().getId());
            body.put("last_online_datetime", DateUtils.GetCurrentdatetime());
            if (isOnline(context)) {
               // dialog(true);
               // Toast.makeText(context, "Online wifi", Toast.LENGTH_SHORT).show();
                body.put("is_online",true);
            } else {
               // dialog(false);
                //Log.e("keshav", "Conectivity Failure !!! ");
                body.put("is_online",false);


            }
            RestClient.get().appApi().userOnline(LoginUtils.getLoggedinUser().getId(),body).enqueue(new Callback<BaseModel<List<User>>>() {
                @Override
                public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {

                }

                @Override
                public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {

                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}