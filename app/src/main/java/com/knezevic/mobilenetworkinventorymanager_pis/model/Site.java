package com.knezevic.mobilenetworkinventorymanager_pis.model;

public class Site {
    private Integer site_id;
    private String mark;
    private String name;
    private String address;
    private Integer is_2G_available;
    private Integer is_3G_available;
    private Integer is_4G_available;
    private Double lat;
    private Double lng;
    private String directions;
    private String power_supply;
    private Integer uncompleted_tasks;

    public Integer getSite_id() {
        return site_id;
    }

    public void setSite_id(Integer site_id) {
        this.site_id = site_id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIs_2G_available() {
        return is_2G_available;
    }

    public void setIs_2G_available(Integer is_2G_available) {
        this.is_2G_available = is_2G_available;
    }

    public Integer getIs_3G_available() {
        return is_3G_available;
    }

    public void setIs_3G_available(Integer is_3G_available) {
        this.is_3G_available = is_3G_available;
    }

    public Integer getIs_4G_available() {
        return is_4G_available;
    }

    public void setIs_4G_available(Integer is_4G_available) {
        this.is_4G_available = is_4G_available;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getPower_supply() {
        return power_supply;
    }

    public void setPower_supply(String power_supply) {
        this.power_supply = power_supply;
    }

    public Integer getUncompleted_tasks() {
        return uncompleted_tasks;
    }

    public void setUncompleted_tasks(Integer uncompleted_tasks) {
        this.uncompleted_tasks = uncompleted_tasks;
    }

    public String getSiteTechs() {
        String tech = "";
        if (getIs_2G_available() != 0) {
            tech += "2G";
        }
        if (getIs_3G_available() != 0) {
            if (tech.isEmpty()) {
                tech += "3G";
            } else {
                tech += ", 3G";
            }
        }
        if (getIs_4G_available() != 0) {
            if (tech.isEmpty()) {
                tech += "4G";
            } else {
                tech += ", 4G";
            }
        }
//        Log.d("SITE_TECH", "getSiteTechs: "+tech);
        return tech;
    }
}
