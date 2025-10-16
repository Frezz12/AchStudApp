package com.example.achstudapp.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {
    private static final String PREFS_NAME = "app_prefs";
    private static final String TOKEN_KEY = "token";
    private static final String USER_ID_KEY = "user_id";
    private SharedPreferences prefs;

    public TokenManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token, int userId) {
        if (token != null && !token.isEmpty()) {
            Log.d("TokenManager", "Сохранение токена: " + token + ", userId: " + userId);
            prefs.edit()
                    .putString(TOKEN_KEY, token)
                    .putInt(USER_ID_KEY, userId)
                    .apply();
        } else {
            Log.e("TokenManager", "Попытка сохранить пустой или null токен");
        }
    }

    public String getToken() {
        String token = prefs.getString(TOKEN_KEY, null);
        Log.d("TokenManager", "Извлечённый токен: " + (token != null ? token : "null"));
        return token;
    }

    public int getUserId() {
        int userId = prefs.getInt(USER_ID_KEY, -1);
        Log.d("TokenManager", "Извлечённый userId: " + userId);
        return userId;
    }

    public void clearToken() {
        Log.d("TokenManager", "Очистка токена и userId");
        prefs.edit()
                .remove(TOKEN_KEY)
                .remove(USER_ID_KEY)
                .apply();
    }

    public boolean hasToken() {
        boolean hasToken = prefs.contains(TOKEN_KEY);
        Log.d("TokenManager", "Проверка наличия токена: " + hasToken);
        return hasToken;
    }
}