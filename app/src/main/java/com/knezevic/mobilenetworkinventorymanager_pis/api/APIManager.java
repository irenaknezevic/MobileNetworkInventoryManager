package com.knezevic.mobilenetworkinventorymanager_pis.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.knezevic.mobilenetworkinventorymanager_pis.services.SiteService;
import com.knezevic.mobilenetworkinventorymanager_pis.services.TaskService;
import com.knezevic.mobilenetworkinventorymanager_pis.services.UserService;
import com.knezevic.mobilenetworkinventorymanager_pis.util.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIManager {
    private static APIManager instance;
    private UserService userService;
    private SiteService siteService;
    private TaskService taskService;

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private APIManager() {
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder.baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userService = retrofit.create(UserService.class);
        siteService = retrofit.create(SiteService.class);
        taskService = retrofit.create(TaskService.class);
    }

    public static APIManager getInstance() {
        if (instance == null) {
            instance = new APIManager();
        }
        return instance;
    }

    public UserService getUserService() {
        return userService;
    }

    public SiteService getSiteService() {
        return siteService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

}
