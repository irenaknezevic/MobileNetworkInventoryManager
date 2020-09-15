package com.knezevic.mobilenetworkinventorymanager_pis.adapter;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.knezevic.mobilenetworkinventorymanager_pis.databinding.SitesListItemBinding;
import com.knezevic.mobilenetworkinventorymanager_pis.model.Site;

import java.util.ArrayList;
import java.util.Collections;

public class SitesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Site> sites;
    public MutableLiveData<Site> selectedSite = new MutableLiveData<>();
    private boolean ascDesc = true;

    public SitesRecyclerViewAdapter(ArrayList<Site> sites) {
        this.sites = sites;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SitesListItemBinding sitesListItemBinding = SitesListItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolderSites(sitesListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ViewHolderSites viewHolderSites = (ViewHolderSites) holder;
        viewHolderSites.bind(sites.get(position));
        viewHolderSites.itemView.setOnClickListener(v -> selectedSite.setValue(sites.get(position)));
    }

    @Override
    public int getItemCount() {
        if (sites != null) {
            return sites.size();
        } else {
            return 0;
        }
    }

    public void setSites(ArrayList<Site> newSites) {
        this.sites = newSites;
        notifyDataSetChanged();
    }

    public void orderByDistance(Location currentLocation) {
        if (ascDesc) {
            orderByDistanceAscending(currentLocation);
            ascDesc = !ascDesc;
        } else {
            orderByDistanceDescending(currentLocation);
            ascDesc = !ascDesc;
        }
    }

    private void orderByDistanceAscending(Location currentLocation) {
        Location firstLocation = new Location("");
        Location secondLocation = new Location("");

        Collections.sort(this.sites, (firstSite, secondSite) -> {
            firstLocation.setLatitude(firstSite.getLat());
            firstLocation.setLongitude(firstSite.getLng());
            secondLocation.setLatitude(secondSite.getLat());
            secondLocation.setLongitude(secondSite.getLng());
            return (int) (currentLocation.distanceTo(firstLocation) - currentLocation.distanceTo(secondLocation));
        });
        notifyDataSetChanged();
    }

    private void orderByDistanceDescending(Location currentLocation) {
        Location firstLocation = new Location("");
        Location secondLocation = new Location("");

        Collections.sort(this.sites, (firstSite, secondSite) -> {
            firstLocation.setLatitude(firstSite.getLat());
            firstLocation.setLongitude(firstSite.getLng());
            secondLocation.setLatitude(secondSite.getLat());
            secondLocation.setLongitude(secondSite.getLng());
            return (int) (currentLocation.distanceTo(secondLocation) - currentLocation.distanceTo(firstLocation));
        });
        notifyDataSetChanged();
    }

    public static class ViewHolderSites extends RecyclerView.ViewHolder {
        SitesListItemBinding sitesListItemBinding;

        private ViewHolderSites(@NonNull SitesListItemBinding sitesListItemBinding) {
            super(sitesListItemBinding.getRoot());
            this.sitesListItemBinding = sitesListItemBinding;
        }

        void bind(Site site) {
            sitesListItemBinding.setSite(site);
            sitesListItemBinding.executePendingBindings();
        }
    }
}
