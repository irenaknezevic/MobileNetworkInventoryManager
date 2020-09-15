package com.knezevic.mobilenetworkinventorymanager_pis.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.knezevic.mobilenetworkinventorymanager_pis.R;
import com.knezevic.mobilenetworkinventorymanager_pis.repositories.UserRepository;

public class LoginViewModel extends ViewModel {
    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    public MutableLiveData<Integer> usernameError = new MutableLiveData<>();
    public MutableLiveData<Integer> passwordError = new MutableLiveData<>();

    public MutableLiveData<String> userId = new MutableLiveData<>();
    public MutableLiveData<Boolean> result = new MutableLiveData<>();

    private UserRepository userRepository = new UserRepository();

    public void loginUser() {
        if (username.getValue() != null && !username.getValue().isEmpty()) {
            if (password.getValue() != null && !password.getValue().isEmpty()) {
                userRepository.checkUserCredentials(username.getValue(), password.getValue(), userId, result);
            } else {
                passwordError.setValue(R.string.required_field);
            }
        } else {
            usernameError.setValue(R.string.required_field);
        }
    }
}
