package com.hypernym.evaconnect.utils;

import android.content.Context;

import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.User;

import butterknife.internal.Constants;

public class LoginUtils {

    public static boolean isUserLogin(Context context) {
        return PrefUtils.getBoolean(context, AppConstants.USER_AVAILABLE, false);
    }

    public static void saveUser(Context context, User user) {
        if (user == null)
            return;
        PrefUtils.persistString(context, AppConstants.USER, GsonUtils.toJson(user));
    }
    public static void saveUserToken(Context context, String token) {
        if (token == null)
            return;

        PrefUtils.persistString(context, AppConstants.USER_TOKEN, token);
    }

    public static User getLoggedinUser(Context context) {
        return GsonUtils.fromJson(PrefUtils.getString(context, AppConstants.USER), User.class);
    }
    public static User getUser(Context context) {
        return GsonUtils.fromJson(PrefUtils.getString(context, AppConstants.USER), User.class);
    }
    public static void userLoggedIn(Context context) {
        PrefUtils.persistBoolean(context, AppConstants.USER_AVAILABLE, true);
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
