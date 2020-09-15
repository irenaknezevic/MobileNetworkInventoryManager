package com.knezevic.mobilenetworkinventorymanager_pis.services;

import com.knezevic.mobilenetworkinventorymanager_pis.model.Site;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SiteService {
    @GET("json.php?action=get_all_sites")
    Call<ArrayList<Site>> getAllSites();

    @GET("json.php?action=get_site_by_id")
    Call<ArrayList<Site>> getSiteById(@Query("site_id") String siteId);
}
