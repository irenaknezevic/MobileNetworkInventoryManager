package com.knezevic.mobilenetworkinventorymanager_pis.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.knezevic.mobilenetworkinventorymanager_pis.R;
import com.knezevic.mobilenetworkinventorymanager_pis.activities.LoginActivity;
import com.knezevic.mobilenetworkinventorymanager_pis.databinding.FragmentProfileBinding;
import com.knezevic.mobilenetworkinventorymanager_pis.model.User;
import com.knezevic.mobilenetworkinventorymanager_pis.util.SharedPreferencesHelper;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.UserViewModel;

import static com.knezevic.mobilenetworkinventorymanager_pis.util.Constants.USER_ID;


public class ProfileFragment extends Fragment implements OnMapReadyCallback {

    private FragmentProfileBinding dataBinding;
    private UserViewModel userViewModel;

    private SharedPreferencesHelper sharedPrefs;
    private String userId;

    private GoogleMap mMap;
    private MapView mapView;

    private Observer<User> userObserver = user -> {
        if (user != null) {

            Double lat = user.getLat();
            Double lng = user.getLng();
            LatLng location = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10F));

            dataBinding.setUser(user);
        } else {
            if (getView() != null) {
                Snackbar.make(getView(), R.string.user_data_error, Snackbar.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        sharedPrefs = new SharedPreferencesHelper(requireActivity().getApplicationContext());

        userViewModel = new UserViewModel();
        sharedPrefs = new SharedPreferencesHelper(requireActivity().getApplicationContext());
        userId = sharedPrefs.getValueString(USER_ID);

        userViewModel.userId.setValue(userId);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.action_logout).setOnMenuItemClickListener(item -> {
            sharedPrefs.clearSharedPreferences();
            Intent logoutIntent = new Intent(requireContext(), LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logoutIntent);
            super.requireActivity().finish();
            return false;
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        View view = dataBinding.getRoot();
        mapView = view.findViewById(R.id.profile_map_view);
        dataBinding.setLifecycleOwner(getViewLifecycleOwner());
        dataBinding.setUserViewModel(userViewModel);
        userViewModel.user.observe(getViewLifecycleOwner(), userObserver);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("PROFILE_FRAGMENT", "onMapReady: ENTER");
        mMap = googleMap;
        userViewModel.getProfileData();
    }
}
