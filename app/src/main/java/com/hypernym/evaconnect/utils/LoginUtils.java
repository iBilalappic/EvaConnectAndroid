package com.hypernym.evaconnect.utils;

import android.content.Context;

import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.User;

import butterknife.internal.Constants;

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
        PrefUtils.remove(context, AppConstants.USER_AVAILABLE);
        PrefUtils.remove(context, AppConstants.USER);
        PrefUtils.remove(context, AppConstants.USER_TOKEN);
   }

    public static String getAuthToken(Context context) {
        String token = PrefUtils.getString(context, AppConstants.USER_TOKEN);
        return (token == null) ? null : token;
    }
    public static void removeAuthToken(Context context) {
        PrefUtils.remove(context, AppConstants.USER_TOKEN);
    }
}
