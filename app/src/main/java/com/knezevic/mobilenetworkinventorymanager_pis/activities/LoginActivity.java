package com.knezevic.mobilenetworkinventorymanager_pis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.google.android.material.textfield.TextInputLayout;
import com.knezevic.mobilenetworkinventorymanager_pis.R;
import com.knezevic.mobilenetworkinventorymanager_pis.databinding.ActivityLoginBinding;
import com.knezevic.mobilenetworkinventorymanager_pis.util.SharedPreferencesHelper;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.LoginViewModel;

import static com.knezevic.mobilenetworkinventorymanager_pis.util.Constants.EMPTY_STRING;
import static com.knezevic.mobilenetworkinventorymanager_pis.util.Constants.USER_ID;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding dataBinding;
    private LoginViewModel loginViewModel;
    private Intent mainActivityIntent;
    private TextInputLayout usernameTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private SharedPreferencesHelper sharedPrefs;

    private Observer<Integer> usernameErrorObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer errorResource) {
            if (errorResource != null) {
                usernameTextInputLayout.setError(getString(errorResource));
            }

        }
    };

    private Observer<Integer> passwordErrorObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer errorResource) {
            if (errorResource != null) {
                passwordTextInputLayout.setError(getString(errorResource));
            }
        }
    };

    private Observer<Boolean> resultObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSuccessful) {
            if (isSuccessful) {
                sharedPrefs.setValueString(USER_ID, loginViewModel.userId.getValue());
                logIn();
            } else {
                Toast.makeText(getApplicationContext(), R.string.login_data_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        sharedPrefs = new SharedPreferencesHelper(getApplicationContext());
        loginViewModel = new LoginViewModel();

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        dataBinding.setLifecycleOwner(this);
        dataBinding.setLoginViewModel(loginViewModel);

        usernameTextInputLayout = findViewById(R.id.text_input_layout_username);
        usernameTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    usernameTextInputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordTextInputLayout = findViewById(R.id.text_input_layout_pass);
        passwordTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    passwordTextInputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.circle_image_view_logo).setEnabled(false);

        loginViewModel.usernameError.observe(this, usernameErrorObserver);
        loginViewModel.passwordError.observe(this, passwordErrorObserver);
        loginViewModel.result.observe(this, resultObserver);

        if (!sharedPrefs.getValueString(USER_ID).equals(EMPTY_STRING)) {
            logIn();
        }
    }

    private void logIn() {
        startActivity(mainActivityIntent);
        finish();
    }


}
