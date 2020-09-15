package com.knezevic.mobilenetworkinventorymanager_pis.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.knezevic.mobilenetworkinventorymanager_pis.R;
import com.knezevic.mobilenetworkinventorymanager_pis.databinding.FragmentSiteDetailsBinding;
import com.knezevic.mobilenetworkinventorymanager_pis.model.Site;
import com.knezevic.mobilenetworkinventorymanager_pis.model.User;
import com.knezevic.mobilenetworkinventorymanager_pis.util.Functions;
import com.knezevic.mobilenetworkinventorymanager_pis.util.SharedPreferencesHelper;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.SiteViewModel;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.UserViewModel;

import static com.knezevic.mobilenetworkinventorymanager_pis.util.Constants.USER_ID;

public class SiteDetailsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private SharedPreferencesHelper sharedPreferences;
    private SiteViewModel siteViewModel;
    private UserViewModel userViewModel;
    private FragmentSiteDetailsBinding dataBinding;
    private Site site;

    private Observer<Site> siteObserver = site -> {
        if (site != null) {
            dataBinding.setSite(site);
            this.site = site;
            loadSiteOnMap(site.getName(), site.getLat(), site.getLng());
            userViewModel.getProfileData();
        }
    };
    private Observer<User> userObserver = user -> {
        if (user != null) {
            loadUserOnMap(user.getName(), user.getLat(), user.getLng());
            dataBinding.setDistance(Functions.loadDistanceText(site.getLat(), site.getLng(), user.getLat(), user.getLng()));
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        siteViewModel = new SiteViewModel();
        userViewModel = new UserViewModel();
        sharedPreferences = new SharedPreferencesHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_site_details, container, false);
        mapView = dataBinding.siteMapView;

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel.userId.setValue(sharedPreferences.getValueString(USER_ID));
        siteViewModel.selectedSite.observe(getViewLifecycleOwner(), siteObserver);
        userViewModel.user.observe(getViewLifecycleOwner(), userObserver);

        if (getArguments() != null) {
            Log.d("SITE_DETAILS_FRAGMENT", "onViewCreated: " + getArguments().getString("site_id"));
            siteViewModel.getSiteById(getArguments().getString("site_id"));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void loadSiteOnMap(String siteName, Double siteLat, Double siteLng) {
        LatLng location = new LatLng(siteLat, siteLng);
        mMap.addMarker(new MarkerOptions().position(location).title(siteName).icon(BitmapDescriptorFactory.fromResource(R.drawable.antenna_black)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 8F));
    }

    private void loadUserOnMap(String userName, Double userLat, Double userLng) {
        LatLng userLocation = new LatLng(userLat, userLng);
        mMap.addMarker(new MarkerOptions().position(userLocation).title(userName).icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker)));
    }
}
