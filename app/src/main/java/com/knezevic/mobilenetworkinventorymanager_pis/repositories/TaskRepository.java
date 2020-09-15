package com.knezevic.mobilenetworkinventorymanager_pis.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.knezevic.mobilenetworkinventorymanager_pis.api.APIManager;
import com.knezevic.mobilenetworkinventorymanager_pis.model.CreateTask;
import com.knezevic.mobilenetworkinventorymanager_pis.model.Task;
import com.knezevic.mobilenetworkinventorymanager_pis.services.TaskService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskRepository {

    private TaskService taskService = APIManager.getInstance().getTaskService();

    public void getAllTasks(MutableLiveData<ArrayList<Task>> tasksList) {
        taskService.getAllTasks().enqueue(new Callback<ArrayList<Task>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Task>> call, @NonNull Response<ArrayList<Task>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    tasksList.setValue(response.body());
                    Log.d("ALL_TASKS_RESPONSE", "onResponse: " + response.body().size());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Task>> call, @NonNull Throwable t) {
                Log.d("ALL_TASKS_RESPONSE", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getUserTasks(MutableLiveData<ArrayList<Task>> tasksList, MutableLiveData<String> userId) {
        taskService.getUserTasks(userId.getValue()).enqueue(new Callback<ArrayList<Task>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Task>> call, @NonNull Response<ArrayList<Task>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    tasksList.setValue(response.body());
                    Log.d("TASK_RESPONSE", "onResponse: " + response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Task>> call, Throwable t) {
                Log.d("TASK_RESPONSE", "onFailure: " + t.getMessage());
            }
        });

    }

    public void getTaskById(String taskId, MutableLiveData<Task> selectedTask) {
        taskService.getTaskById(taskId).enqueue(new Callback<ArrayList<Task>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Task>> call, @NonNull Response<ArrayList<Task>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Log.d("GetTaskById", "onResponse:  Task - Site Name -> " + response.body().get(0).getSite_name());
                    selectedTask.setValue(response.body().get(0));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Task>> call, @NonNull Throwable t) {
                Log.d("GetTaskById", "onFailure: " + t.getMessage());
            }
        });
    }

    public void addNewTask(CreateTask newTask, MutableLiveData<Boolean> result) {
        taskService.addNewTask(newTask).enqueue(new Callback<ArrayList<CreateTask>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CreateTask>> call, @NonNull Response<ArrayList<CreateTask>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(true);
                } else {
                    result.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<CreateTask>> call, @NonNull Throwable t) {
                Log.d("NEW_TASK_RESULT", "onFailure: " + t.getMessage());
                result.setValue(false);
            }
        });
    }

    public void updateTask(String taskId, MutableLiveData<Boolean> result) {
        Log.d("UPDATE_TASK_RESULT", "updateTask: " + taskId);
        taskService.updateTask(taskId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    result.setValue(true);
                } else {
                    result.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("UPDATE_TASK_RESULT", "onFailure: " + t.getMessage());
                result.setValue(false);
            }
        });
    }

    public void getTaskCategory(MutableLiveData<ArrayList<String>> categories) {
        taskService.getTaskCategories().enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call, @NonNull Response<ArrayList<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
                Log.d("TASK_CATEGORY_RESULT", "onFailure: " + t.getMessage());
            }
        });
    }
}
