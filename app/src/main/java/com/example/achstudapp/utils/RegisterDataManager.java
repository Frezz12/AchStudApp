package com.example.achstudapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class RegisterDataManager {
    private static final String PREF_NAME = "register_data";
    private final SharedPreferences prefs;

    public RegisterDataManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveStep1(String firstname, String lastname, String surname ) {
        prefs.edit()
                .putString("firstname", firstname)
                .putString("lastname", lastname)
                .putString("surname", surname)
                .apply();
    }

    public void saveStep2(String emaild, String password) {
        prefs.edit()
                .putString("email", emaild)
                .putString("password", password)
                .putString("role", "student")
                .apply();
    }

    public void saveStep3(String college) {
        prefs.edit()
                .putString("college", college)
                .apply();
    }

    public String getFirstname() { return prefs.getString("firstname", ""); }
    public String getLastname() { return prefs.getString("lastname", ""); }
    public String getSurname() { return prefs.getString("surname", ""); }

    public String getEmail() { return prefs.getString("email", ""); }
    public String getPassword() { return prefs.getString("password", ""); }
    public String getRole() { return prefs.getString("role", "student"); }
    public String getCollege() { return prefs.getString("college", ""); }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
