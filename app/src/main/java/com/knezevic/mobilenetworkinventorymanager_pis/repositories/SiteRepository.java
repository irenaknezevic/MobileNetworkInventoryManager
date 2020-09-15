package com.knezevic.mobilenetworkinventorymanager_pis.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.knezevic.mobilenetworkinventorymanager_pis.api.APIManager;
import com.knezevic.mobilenetworkinventorymanager_pis.model.Site;
import com.knezevic.mobilenetworkinventorymanager_pis.services.SiteService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteRepository {
    private SiteService siteService = APIManager.getInstance().getSiteService();

    public void getAllSites(MutableLiveData<ArrayList<Site>> sitesList) {
        siteService.getAllSites().enqueue(new Callback<ArrayList<Site>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Site>> call, @NonNull Response<ArrayList<Site>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    sitesList.setValue(response.body());
                    Log.d("SITES_RESPONSE", "onResponse: " + response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Site>> call, @NonNull Throwable t) {
                Log.d("SITES_RESPONSE", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getSiteById(String siteId, MutableLiveData<Site> selectedSite) {
        siteService.getSiteById(siteId).enqueue(new Callback<ArrayList<Site>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Site>> call, @NonNull Response<ArrayList<Site>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Log.d("GetSiteById", "onResponse:  Site Name -> " + response.body().get(0).getName());
                    selectedSite.setValue(response.body().get(0));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Site>> call, @NonNull Throwable t) {
                Log.d("GetSiteById", "onFailure: " + t.getMessage());
            }
        });
    }
}
