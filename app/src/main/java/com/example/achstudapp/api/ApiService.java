package com.example.achstudapp.api;

import com.example.achstudapp.models.AchievementItem;
import com.example.achstudapp.models.AchievementsItemRequest;
import com.example.achstudapp.models.ChangeRoleUserForAdminRequest;
import com.example.achstudapp.models.GrandToStudentRequest;
import com.example.achstudapp.models.LoginRequest;
import com.example.achstudapp.models.LoginResponse;
import com.example.achstudapp.models.RegisterRequest;
import com.example.achstudapp.models.RegisterResponse;
import com.example.achstudapp.models.StudentAchievementResponce;
import com.example.achstudapp.models.User;
import com.example.achstudapp.models.UserUpdateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("student-achievements")
    Call<List<StudentAchievementResponce>> getAllMyAchievement(
            @Query("studentId") Integer studentId,
            @Query("achievementId") Integer achievementId,
            @Query("status") String status
    );

    @POST("users/{studentId}/achievements")
    Call<StudentAchievementResponce> grandToStudent(@Path("studentId") int studentId, @Body GrandToStudentRequest body);

    @GET("student-achievements/{id}")
    Call<StudentAchievementResponce> getStudAchById(@Path("id") int id);

    @DELETE("student-achievements/{id}")
    Call<Void> deleteStudAch(@Path("id") int id);

    @PATCH("users/{id}")
    Call<User> updateUser(@Path("id") int id, @Body UserUpdateRequest body);

    @PATCH("users/{id}")
    Call<User> updateRoleUserForAdmin(@Path("id") int id, @Body ChangeRoleUserForAdminRequest body);
}
