package com.knezevic.mobilenetworkinventorymanager_pis.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.knezevic.mobilenetworkinventorymanager_pis.model.Site;
import com.knezevic.mobilenetworkinventorymanager_pis.repositories.SiteRepository;

import java.util.ArrayList;

public class SiteViewModel extends ViewModel {

    public MutableLiveData<ArrayList<Site>> sitesList = new MutableLiveData<>();
    public MutableLiveData<Site> selectedSite = new MutableLiveData<>();
    public MutableLiveData<Boolean> showProgressBar = new MutableLiveData<>(true);

    private SiteRepository siteRepository = new SiteRepository();

    public void getAllSites() {
        siteRepository.getAllSites(sitesList);
    }

    public void getSiteById(String siteId) {
        siteRepository.getSiteById(siteId, selectedSite);
    }
}
