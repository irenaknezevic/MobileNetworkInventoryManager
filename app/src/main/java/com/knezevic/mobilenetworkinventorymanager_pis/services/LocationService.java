package com.knezevic.mobilenetworkinventorymanager_pis.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.knezevic.mobilenetworkinventorymanager_pis.repositories.UserRepository;
import com.knezevic.mobilenetworkinventorymanager_pis.util.SharedPreferencesHelper;

import static com.knezevic.mobilenetworkinventorymanager_pis.util.Constants.USER_ID;

public class LocationService extends Service {

    private static final String LOCATION_SERVICE = "LOCATION_SERVICE";
    private static final int UPDATE_INTERVAL = 1000 * 60;
    private static final int FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;

    private UserRepository userRepository;
    private SharedPreferencesHelper sharedPreferences;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onCreate() {
        super.onCreate();

        userRepository = new UserRepository();
        sharedPreferences = new SharedPreferencesHelper(getApplicationContext());

        createLocationRequest();
        createLocationCallback();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        Log.d(LOCATION_SERVICE, "onStartCommand: ");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                updateUserLocation(locationResult.getLastLocation());
            }
        };
    }

    private void updateUserLocation(Location location) {
        Log.d(LOCATION_SERVICE, "onLocationResult LOCATION: " + location.getLatitude() + " " + location.getLongitude());
        userRepository.updateUserLocation(
                sharedPreferences.getValueString(USER_ID),
                location.getLatitude(),
                location.getLongitude()
        );
    }
}
