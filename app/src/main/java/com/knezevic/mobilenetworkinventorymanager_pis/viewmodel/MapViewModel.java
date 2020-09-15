package com.knezevic.mobilenetworkinventorymanager_pis.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {
    public static final int SITE_BTN = 0;
    public static final int USER_BTN = 1;

    public MutableLiveData<Integer> mapSwitch = new MutableLiveData<>();
    public MutableLiveData<Boolean> showProgressBar = new MutableLiveData<>(true);

    public void switchMapViewToSites() {
        mapSwitch.setValue(SITE_BTN);
    }

    public void switchMapViewToUsers() {
        mapSwitch.setValue(USER_BTN);
    }
}
