package com.knezevic.mobilenetworkinventorymanager_pis.model;

public class CreateTask {
    private Integer site_id;
    private Integer user_id;
    private String task_description;
    private Integer task_category;

    public Integer getSite_id() {
        return site_id;
    }

    public void setSite_id(Integer site_id) {
        this.site_id = site_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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
}
