package com.knezevic.mobilenetworkinventorymanager_pis.services;

import com.knezevic.mobilenetworkinventorymanager_pis.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @GET("json.php?action=check_if_user_exists")
    Call<String> checkUserCredentials(
            @Query("username") String username,
            @Query("password") String password);

    @GET("json.php?action=get_all_users")
    Call<ArrayList<User>> getAllUsers();

    @GET("json.php?action=get_user_data")
    Call<ArrayList<User>> getUserById(@Query("user_id") String userId);

    @FormUrlEncoded
    @POST("json.php?action=update_user_location")
    Call<Void> updateUserLocation(@Field("user_id") String userId,
                                  @Field("new_lat") Double lat,
                                  @Field("new_lng") Double lng);

}
