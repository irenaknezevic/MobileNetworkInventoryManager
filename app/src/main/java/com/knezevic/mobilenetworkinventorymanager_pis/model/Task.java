package com.knezevic.mobilenetworkinventorymanager_pis.model;

public class Task {
    private Integer task_id;
    private Integer site_id;
    private String site_mark;
    private String site_name;
    private String site_address;
    private Integer user_id;
    private String user_name;
    private String user_surname;
    private Integer is_2G_available;
    private Integer is_3G_available;
    private Integer is_4G_available;
    private Double site_lat;
    private Double site_lng;
    private String site_directions;
    private String site_power_supply;
    private String task_description;
    private Integer task_category;
    private String task_category_name;
    private Integer task_status;
    private String task_status_name;
    private String task_opening_time;
    private String task_closing_time;

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public Integer getSite_id() {
        return site_id;
    }

    public void setSite_id(Integer site_id) {
        this.site_id = site_id;
    }

    public String getSite_mark() {
        return site_mark;
    }

    public void setSite_mark(String site_mark) {
        this.site_mark = site_mark;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getSite_address() {
        return site_address;
    }

    public void setSite_address(String site_address) {
        this.site_address = site_address;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_surname() {
        return user_surname;
    }

    public void setUser_surname(String user_surname) {
        this.user_surname = user_surname;
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

    public Double getSite_lat() {
        return site_lat;
    }

    public void setSite_lat(Double site_lat) {
        this.site_lat = site_lat;
    }

    public Double getSite_lng() {
        return site_lng;
    }

    public void setSite_lng(Double site_lng) {
        this.site_lng = site_lng;
    }

    public String getSite_directions() {
        return site_directions;
    }

    public void setSite_directions(String site_directions) {
        this.site_directions = site_directions;
    }

    public String getSite_power_supply() {
        return site_power_supply;
    }

    public void setSite_power_supply(String site_power_supply) {
        this.site_power_supply = site_power_supply;
    }

    public String getTask_description() {
        return task_description;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public Integer getTask_category() {
        return task_category;
    }

    public void setTask_category(Integer task_category) {
        this.task_category = task_category;
    }

    public String getTask_category_name() {
        return task_category_name;
    }

    public void setTask_category_name(String task_category_name) {
        this.task_category_name = task_category_name;
    }

    public Integer getTask_status() {
        return task_status;
    }

    public void setTask_status(Integer task_status) {
        this.task_status = task_status;
    }

    public String getTask_status_name() {
        return task_status_name;
    }

    public void setTask_status_name(String task_status_name) {
        this.task_status_name = task_status_name;
    }

    public String getTask_opening_time() {
        return task_opening_time;
    }

    public void setTask_opening_time(String task_opening_time) {
        this.task_opening_time = task_opening_time;
    }

    public String getTask_closing_time() {
        return task_closing_time;
    }

    public void setTask_closing_time(String task_closing_time) {
        this.task_closing_time = task_closing_time;
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
        return tech;
    }


}
