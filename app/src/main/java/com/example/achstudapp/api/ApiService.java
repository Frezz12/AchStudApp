package com.example.achstudapp.api;

import com.example.achstudapp.models.AchievementItem;
import com.example.achstudapp.models.AchievementsItemRequest;
import com.example.achstudapp.models.LoginRequest;
import com.example.achstudapp.models.LoginResponse;
import com.example.achstudapp.models.RegisterRequest;
import com.example.achstudapp.models.RegisterResponse;
import com.example.achstudapp.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest body);

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest body);

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") int id);

    @GET("users")
    Call<List<User>> getAllUsers();

    @POST("achievements")
    Call<AchievementItem> createAchievement(@Body AchievementsItemRequest body);

    @GET("achievements")
    Call<List<AchievementItem>> getAllMyAchievement();

}
