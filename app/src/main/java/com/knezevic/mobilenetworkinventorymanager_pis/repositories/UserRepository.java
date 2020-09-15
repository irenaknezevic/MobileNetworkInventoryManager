package com.knezevic.mobilenetworkinventorymanager_pis.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.knezevic.mobilenetworkinventorymanager_pis.api.APIManager;
import com.knezevic.mobilenetworkinventorymanager_pis.model.User;
import com.knezevic.mobilenetworkinventorymanager_pis.services.UserService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private UserService userService = APIManager.getInstance().getUserService();

    public void checkUserCredentials(String username, String password, MutableLiveData<String> userId, MutableLiveData<Boolean> result) {
        userService.checkUserCredentials(username, password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    userId.setValue(response.body());
                    result.setValue(true);
                    Log.d("LOGIN_USER", "onResponse: " + response.body());
                } else {
                    result.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.d("LOGIN_USER", "onFailure: " + t.getMessage());
                result.setValue(false);
            }
        });
    }

    public void getUserDataById(String userId, MutableLiveData<User> user) {
        userService.getUserById(userId).enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<User>> call, @NonNull Response<ArrayList<User>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    user.setValue(response.body().get(0));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<User>> call, @NonNull Throwable t) {
                user.setValue(null);
            }
        });
    }

    public void getAllUsers(MutableLiveData<ArrayList<User>> users) {
        userService.getAllUsers().enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<User>> call, @NonNull Response<ArrayList<User>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    users.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<User>> call, @NonNull Throwable t) {
                users.setValue(null);
            }
        });
    }

    public void updateUserLocation(String userId, Double lat, Double lng) {
        userService.updateUserLocation(userId, lat, lng).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Log.d("USER_REPO", "onResponse CALL: " + call.request());
                Log.d("USER_REPO", "onResponse RESPONSE: " + response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("USER_REPO", "onFailure: " + call.request());
            }
        });
    }
}
