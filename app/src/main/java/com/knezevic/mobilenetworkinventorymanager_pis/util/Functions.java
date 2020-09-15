package com.knezevic.mobilenetworkinventorymanager_pis.util;

import android.location.Location;

public class Functions {
    public static String loadDistanceText(Double firstLat, Double firstLng, Double secondLat, Double secondLng) {
        Location firstLocation = new Location("");
        firstLocation.setLatitude(firstLat);
        firstLocation.setLongitude(firstLng);

        Location secondLocation = new Location("");
        secondLocation.setLatitude(secondLat);
        secondLocation.setLongitude(secondLng);

        return Math.round(firstLocation.distanceTo(secondLocation) / 1000 * 100.00) / 100.00 + " km";
    }
}
