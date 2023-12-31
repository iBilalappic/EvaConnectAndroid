package com.hypernym.evaconnect.utils;

import android.content.Context;
import androidx.preference.PreferenceManager;


import com.google.gson.reflect.TypeToken;
import com.hypernym.evaconnect.models.ChatMessage;
import com.hypernym.evaconnect.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrefUtils {
    public static boolean persistString(Context context, String key, String toPersist) {
        return PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, toPersist).commit();
    }

    public static String getString(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
    }

    public static void persistBoolean(Context context, String key, boolean toPersist) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, toPersist).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defaultValue);
    }

    public static void persistInt(Context context, String key, int nextTheme) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, nextTheme).apply();
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, defaultValue);
    }

    public static void persistLong(Context context, String key, long nextTime) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(key, nextTime).apply();
    }

    public static void remove(Context context, String key) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(key).apply();
    }

    public static void reset(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }

    public static void persistStringSet(Context context, String key, Set<String> toPersist) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(key, toPersist).apply();
    }

    public static Set<String> getStringSet(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getStringSet(key, new HashSet<String>());
    }

    public static void persistConnections(Context context, List<User> users){
        PrefUtils.persistString(context,Constants.PERSIST_CONNECTIONS,GsonUtils.toJson(users));
    }

    public static List<User> getConnections(Context context){
        return GsonUtils.fromJson(PrefUtils.getString(context, Constants.PERSIST_CONNECTIONS), new TypeToken<List<User>>(){}.getType());
    }

    public static void persistConnectionsMeeting(Context context, List<User> users){
        PrefUtils.persistString(context,Constants.PERSIST_CONNECTIONS_MEETING,GsonUtils.toJson(users));
    }

    public static List<User> getConnectionsMeeting(Context context){
        return GsonUtils.fromJson(PrefUtils.getString(context, Constants.PERSIST_CONNECTIONS_MEETING), new TypeToken<List<User>>(){}.getType());
    }

    public static void saveChat(Context context, List<ChatMessage> chatMessageList){
        PrefUtils.persistString(context,Constants.CHAT_MESSAGE,GsonUtils.toJson(chatMessageList));
    }

    public static List<ChatMessage> getChat(Context context){
        return GsonUtils.fromJson(PrefUtils.getString(context,Constants.CHAT_MESSAGE),new TypeToken<List<ChatMessage>>(){}.getType());
    }

    public static void saveMessageCount(Context context, int count){
        PrefUtils.persistInt(context,Constants.MESSAGE_NOTIFICATION,count);
    }

    public static Integer getMessageCount(Context context){
        return PrefUtils.getInt(context,Constants.MESSAGE_NOTIFICATION,0);
    }


}
