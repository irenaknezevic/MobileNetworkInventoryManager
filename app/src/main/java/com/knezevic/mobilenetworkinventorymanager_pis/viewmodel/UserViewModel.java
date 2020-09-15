package com.knezevic.mobilenetworkinventorymanager_pis.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.knezevic.mobilenetworkinventorymanager_pis.model.User;
import com.knezevic.mobilenetworkinventorymanager_pis.repositories.UserRepository;

import java.util.ArrayList;

public class UserViewModel extends ViewModel {
    public MutableLiveData<String> userId = new MutableLiveData<>();
    public MutableLiveData<User> user = new MutableLiveData<>();
    public MutableLiveData<ArrayList<User>> usersList = new MutableLiveData<>();

    private UserRepository userRepository = new UserRepository();

    public void getProfileData() {
        userRepository.getUserDataById(userId.getValue(), user);
    }

    public void getAllUsers() {
        userRepository.getAllUsers(usersList);
    }
}
