package com.knezevic.mobilenetworkinventorymanager_pis.services;


import com.knezevic.mobilenetworkinventorymanager_pis.model.CreateTask;
import com.knezevic.mobilenetworkinventorymanager_pis.model.Task;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TaskService {
    @GET("json.php?action=get_all_tasks")
    Call<ArrayList<Task>> getAllTasks();

    @GET("json.php?action=get_tasks_for_user")
    Call<ArrayList<Task>> getUserTasks(
            @Query("user_id") String userId);

    @GET("json.php?action=get_task_by_id")
    Call<ArrayList<Task>> getTaskById(
            @Query("task_id") String taskId);

    @GET("json.php?action=get_task_categories")
    Call<ArrayList<String>> getTaskCategories();

    @POST("json.php?action=add_new_task")
    Call<ArrayList<CreateTask>> addNewTask(
            @Body CreateTask newTask);

    @FormUrlEncoded
    @POST("json.php?action=update_task")
    Call<Void> updateTask(
            @Field("task_id") String taskId);
}
