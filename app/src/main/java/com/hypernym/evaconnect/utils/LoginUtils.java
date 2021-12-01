package com.hypernym.evaconnect.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.User;

import java.io.IOException;

public class LoginUtils {

    public static boolean isUserLogin() {
        return PrefUtils.getBoolean(AppUtils.getApplicationContext(), AppConstants.USER_AVAILABLE, false);
    }

    public static void saveUser( User user) {
        if (user == null)
            return;
        PrefUtils.persistString(AppUtils.getApplicationContext(), AppConstants.USER, GsonUtils.toJson(user));
    }
    public static void saveUserToken(String token) {
        if (token == null)
            return;

        PrefUtils.persistString(AppUtils.getApplicationContext(), AppConstants.USER_TOKEN, token);
    }

    public static User getLoggedinUser() {
        return GsonUtils.fromJson(PrefUtils.getString(AppUtils.getApplicationContext(), AppConstants.USER), User.class);
    }
    public static User getUser() {
        return GsonUtils.fromJson(PrefUtils.getString(AppUtils.getApplicationContext(), AppConstants.USER), User.class);
    }
    public static void userLoggedIn() {
        PrefUtils.persistBoolean(AppUtils.getApplicationContext(), AppConstants.USER_AVAILABLE, true);
    }

    public static void clearUser(Context context) {
        clearFirebaseFCMToken(LoginUtils.getLoggedinUser().getId().toString());
        PrefUtils.remove(context, AppConstants.USER_AVAILABLE);
        PrefUtils.remove(context, AppConstants.USER);
        PrefUtils.remove(context, AppConstants.USER_TOKEN);
   }

    private static void clearFirebaseFCMToken(String id) {
        try {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference user = databaseReference.child(AppConstants.FIREASE_USER_ENDPOINT);
            user.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        user.child(id).child("fcm-token").removeValue();
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception ex)
        {

        }
    }

    public static String getAuthToken(Context context) {
        String token = PrefUtils.getString(context, AppConstants.USER_TOKEN);
        return (token == null) ? null : token;
    }
    public static void removeAuthToken(Context context) {
        PrefUtils.remove(context, AppConstants.USER_TOKEN);
    }
}
