package com.knezevic.mobilenetworkinventorymanager_pis.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.knezevic.mobilenetworkinventorymanager_pis.databinding.FragmentMapsBinding;
import com.knezevic.mobilenetworkinventorymanager_pis.model.Site;
import com.knezevic.mobilenetworkinventorymanager_pis.model.User;
import com.knezevic.mobilenetworkinventorymanager_pis.util.Functions;
import com.knezevic.mobilenetworkinventorymanager_pis.util.SharedPreferencesHelper;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.MapViewModel;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.SiteViewModel;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.UserViewModel;

import java.util.ArrayList;

import static com.knezevic.mobilenetworkinventorymanager_pis.util.Constants.USER_ID;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapsBinding dataBinding;
    private GoogleMap mMap;
    private MapView mapView;

    private Double myLat;
    private Double myLng;

    private SharedPreferencesHelper sharedPrefs;
    private String userId;

    private ArrayList<Site> sitesList = new ArrayList<>();
    private ArrayList<User> usersList = new ArrayList<>();

    private Button siteBtn;
    private Button userBtn;
    private MapViewModel mapViewModel;
    private SiteViewModel siteViewModel;
    private UserViewModel userViewModel;

    private Observer<Integer> mapViewSwitchObserver = integer -> {
        Log.d("MAP_VIEW", "mapViewSwitchObserver: " + integer);
        if (integer == MapViewModel.SITE_BTN) {
            siteBtn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            siteBtn.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            userBtn.setTextColor(getResources().getColor(R.color.colorLightWhite));
            userBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkAlpha60));
            loadMapSites();
        } else {
            userBtn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            userBtn.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            siteBtn.setTextColor(getResources().getColor(R.color.colorLightWhite));
            siteBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkAlpha60));
            loadMapUsers();
        }
    };
    private Observer<ArrayList<User>> usersListObserver = users -> {
        if (users != null) {
            usersList = users;
        } else {
            if (getView() != null) {
                Snackbar.make(getView(), R.string.user_data_error, Snackbar.LENGTH_LONG).show();
            }
        }
    };
    private Observer<ArrayList<Site>> sitesListObserver = sites -> {
        if (sites != null) {
            sitesList = sites;
            mapViewModel.mapSwitch.setValue(MapViewModel.SITE_BTN);
        } else {
            if (getView() != null) {
                Snackbar.make(getView(), R.string.sites_loading_error, Snackbar.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs = new SharedPreferencesHelper(requireActivity().getApplicationContext());
        userId = sharedPrefs.getValueString(USER_ID);

        mapViewModel = new MapViewModel();
        siteViewModel = new SiteViewModel();
        userViewModel = new UserViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false);
        dataBinding.setLifecycleOwner(getViewLifecycleOwner());
        dataBinding.setMapViewModel(mapViewModel);

        mapView = dataBinding.mapView;
        siteBtn = dataBinding.btnSite;
        userBtn = dataBinding.btnUser;

        mapViewModel.mapSwitch.observe(getViewLifecycleOwner(), mapViewSwitchObserver);
        siteViewModel.sitesList.observe(getViewLifecycleOwner(), sitesListObserver);
        userViewModel.usersList.observe(getViewLifecycleOwner(), usersListObserver);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    private void loadMapSites() {
        mMap.clear();
        if (!sitesList.isEmpty()) {
            for (Site site : sitesList) {
                if (site.getUncompleted_tasks() > 0) {
                    LatLng location = new LatLng(site.getLat(), site.getLng());
                    mMap.addMarker(new MarkerOptions().position(location).title(site.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.antenna_red)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.674545, 16.8636353), 7F));
                } else {
                    LatLng location = new LatLng(site.getLat(), site.getLng());
                    mMap.addMarker(new MarkerOptions().position(location).title(site.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.antenna_green)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.674545, 16.8636353), 7F));
                }
            }
            mapViewModel.showProgressBar.setValue(false);
        }
    }

    private void loadMapUsers() {
        mMap.clear();
        if (!usersList.isEmpty()) {
            for (User user : usersList) {
                if (user.getUser_id().toString().equals(userId)) {
                    myLat = user.getLat();
                    myLng = user.getLng();
                    LatLng location = new LatLng(myLat, myLng);
                    mMap.addMarker(new MarkerOptions().position(location).title(user.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.my_user_marker)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.674545, 16.8636353), 7F));
                }
            }
            for (User user : usersList) {
                if (!user.getUser_id().toString().equals(userId)) {
                    LatLng location = new LatLng(user.getLat(), user.getLng());
                    mMap.addMarker(new MarkerOptions().position(location).title(user.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker)).snippet(Functions.loadDistanceText(myLat, myLng, user.getLat(), user.getLng()) + " " + user.getRecorded()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.674545, 16.8636353), 7F));
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        siteViewModel.getAllSites();
        userViewModel.getAllUsers();
    }

}
