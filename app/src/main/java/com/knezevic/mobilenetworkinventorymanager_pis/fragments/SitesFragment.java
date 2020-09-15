package com.knezevic.mobilenetworkinventorymanager_pis.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.knezevic.mobilenetworkinventorymanager_pis.R;
import com.knezevic.mobilenetworkinventorymanager_pis.activities.MainActivity;
import com.knezevic.mobilenetworkinventorymanager_pis.adapter.SitesRecyclerViewAdapter;
import com.knezevic.mobilenetworkinventorymanager_pis.databinding.FragmentSitesBinding;
import com.knezevic.mobilenetworkinventorymanager_pis.model.Site;
import com.knezevic.mobilenetworkinventorymanager_pis.model.User;
import com.knezevic.mobilenetworkinventorymanager_pis.util.SharedPreferencesHelper;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.SiteViewModel;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.UserViewModel;

import java.util.ArrayList;

import static com.knezevic.mobilenetworkinventorymanager_pis.util.Constants.USER_ID;


public class SitesFragment extends Fragment {

    private FragmentSitesBinding dataBinding;
    private SiteViewModel siteViewModel;
    private SitesRecyclerViewAdapter sitesRecyclerViewAdapter;

    private UserViewModel userViewModel;
    private SharedPreferencesHelper sharedPrefs;
    private String userId;
    private User loggedUser;

    private ArrayList<Site> sitesList;
    private ArrayList<Site> searchedSitesList = new ArrayList<>();

    private SearchView searchView;
    private NavController navController;

    private Observer<Site> siteObserver = site -> {
        if (site != null && navController != null) {
            Bundle args = new Bundle();
            args.putString("site_id", site.getSite_id().toString());

            navController.navigate(R.id.action_sites_fragment_to_details_fragment, args);
        }
    };

    private Observer<ArrayList<Site>> sitesListObserver = sites -> {
        if (!sites.isEmpty()) {
            Log.d("SITES", "onCreate: " + siteViewModel.sitesList.getValue().get(0).getName());
            //Postavljanje RecyclerView-a
            sitesList = sites;
            sitesRecyclerViewAdapter = new SitesRecyclerViewAdapter(sites);
            sitesRecyclerViewAdapter.selectedSite.observe(getViewLifecycleOwner(), siteObserver);
            setupRecyclerView();

            siteViewModel.showProgressBar.setValue(false);
        }
    };

    private Observer<User> userObserver = user -> loggedUser = user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        siteViewModel = new SiteViewModel();
        userViewModel = new UserViewModel();

        sharedPrefs = new SharedPreferencesHelper(requireActivity().getApplicationContext());
        userId = sharedPrefs.getValueString(USER_ID);

        userViewModel.userId.setValue(userId);
        userViewModel.getProfileData();
        userViewModel.user.observe(getViewLifecycleOwner(), userObserver);

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            navController = mainActivity.getNavController();
        }

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sites, container, false);

        siteViewModel.getAllSites();
        siteViewModel.sitesList.observe(getViewLifecycleOwner(), sitesListObserver);

        dataBinding.setSiteViewModel(siteViewModel);
        dataBinding.setLifecycleOwner(getViewLifecycleOwner());

        return dataBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sites_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.action_sort_by_distance).setOnMenuItemClickListener(item -> {
            if (sitesRecyclerViewAdapter != null && loggedUser != null) {
                Location location = new Location("");
                location.setLatitude(loggedUser.getLat());
                location.setLongitude(loggedUser.getLng());
                sitesRecyclerViewAdapter.orderByDistance(location);
            }
            Log.d("PROGRESS_BAR", "onCreateOptionsMenu: " + dataBinding.getSiteViewModel().showProgressBar.getValue());
            return false;
        });
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!query.equals("")) {
                    if (!searchedSitesList.isEmpty()) {
                        searchedSitesList.clear();
                    }
                    for (Site oItem : sitesList) {
                        if (
                                (oItem.getName() != null && oItem.getName().toLowerCase().contains(query.toLowerCase()))
                                        || (oItem.getMark() != null && oItem.getMark().toLowerCase().contains(query.toLowerCase()))
                                        || (oItem.getAddress() != null && oItem.getAddress().toLowerCase().contains(query.toLowerCase()))
                                        || (oItem.getSiteTechs() != null && oItem.getSiteTechs().toLowerCase().contains(query.toLowerCase()))
                        ) {
                            searchedSitesList.add(oItem);
                        }
                    }
                    sitesRecyclerViewAdapter.setSites(searchedSitesList);
                } else {
                    if (sitesRecyclerViewAdapter != null) {
                        sitesRecyclerViewAdapter.setSites(sitesList);
                    }
                }
                return true;
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView rvSites = dataBinding.recyclerViewSite;
        rvSites.setAdapter(sitesRecyclerViewAdapter);
        rvSites.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
    }
}
