package com.knezevic.mobilenetworkinventorymanager_pis.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.knezevic.mobilenetworkinventorymanager_pis.R;
import com.knezevic.mobilenetworkinventorymanager_pis.model.User;
import com.knezevic.mobilenetworkinventorymanager_pis.services.LocationService;
import com.knezevic.mobilenetworkinventorymanager_pis.util.SharedPreferencesHelper;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.UserViewModel;

import static com.knezevic.mobilenetworkinventorymanager_pis.util.Constants.USER_ID;
import static com.knezevic.mobilenetworkinventorymanager_pis.util.Constants.USER_ROLE;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private LocationManager locationManager;
    private UserViewModel userViewModel;
    private SharedPreferencesHelper sharedPrefs;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private Intent locationServiceIntent;
    private NavHostFragment navHostFragment;
    private AppBarConfiguration appBarConfiguration;

    private Observer<User> userObserver = user -> {
        if (user != null) {
            sharedPrefs.setValueString(USER_ROLE, user.getRole());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationServiceIntent = new Intent(getApplicationContext(), LocationService.class);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        userViewModel = new UserViewModel();
        sharedPrefs = new SharedPreferencesHelper(getApplicationContext());
        userViewModel.userId.setValue(sharedPrefs.getValueString(USER_ID));

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            if (locationManager != null
                    && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            ) {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.location_needed)
                        .setPositiveButton(R.string.location_settings, (dialog, which) ->
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                        .setCancelable(false)
                        .setNegativeButton(R.string.deny, (dialog, which) ->
                                Toast.makeText(getApplicationContext(), getString(R.string.enable_location), Toast.LENGTH_SHORT).show()
                        )
                        .setTitle(R.string.app_name)
                        .show();
            }
            startService(locationServiceIntent);
        }
        setUpNavigation();

        userViewModel.user.observe(this, userObserver);
        userViewModel.getProfileData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startService(locationServiceIntent);
            } else {
                Toast.makeText(this, getString(R.string.location_needed), Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void setUpNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_sites_fragment,
                R.id.nav_tasks_fragment,
                R.id.nav_maps_fragment,
                R.id.nav_profile_fragment)
                .build();
        if (navHostFragment != null) {
            NavigationUI.setupWithNavController(bottomNavigationView,
                    getNavController());
            NavigationUI.setupActionBarWithNavController(this, getNavController(), appBarConfiguration);
        }

        if (getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public NavController getNavController() {
        return navHostFragment.getNavController();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
