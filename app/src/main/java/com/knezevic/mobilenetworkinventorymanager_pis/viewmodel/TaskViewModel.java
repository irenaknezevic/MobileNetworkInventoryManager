package com.knezevic.mobilenetworkinventorymanager_pis.viewmodel;

import android.view.View;
import android.widget.Button;

import androidx.lifecycle.MutableLiveData;

import com.knezevic.mobilenetworkinventorymanager_pis.model.Task;
import com.knezevic.mobilenetworkinventorymanager_pis.repositories.TaskRepository;

import java.util.ArrayList;

public class TaskViewModel {
    public MutableLiveData<ArrayList<Task>> tasksList = new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> taskCategoriesList = new MutableLiveData<>();
    public MutableLiveData<String> userId = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Task>> tasksSelectedList = new MutableLiveData<>();
    public MutableLiveData<Button> btnToChangeColor = new MutableLiveData<>();
    public MutableLiveData<Task> selectedTask = new MutableLiveData<>();
    public MutableLiveData<Boolean> showProgressBar = new MutableLiveData<>(true);

    private TaskRepository taskRepository = new TaskRepository();

    public void getUserTasks() {
        taskRepository.getUserTasks(tasksList, userId);
    }

    public void getAllTasks() {
        taskRepository.getAllTasks(tasksList);
    }

    public void getTaskById(String taskId) {
        taskRepository.getTaskById(taskId, selectedTask);
    }

    public void showAll(View view) {
        btnToChangeColor.setValue((Button) view);
        tasksSelectedList.setValue(tasksList.getValue());
    }

    public void showCompleted(View view) {
        btnToChangeColor.setValue((Button) view);
        ArrayList<Task> newTaskList = new ArrayList<>();
        if (tasksList.getValue() != null) {
            for (Task task :
                    tasksList.getValue()) {
                if (task.getTask_status() == 1) {
                    newTaskList.add(task);
                }
            }
            tasksSelectedList.setValue(newTaskList);
        }
    }

    public void showUncompleted(View view) {
        btnToChangeColor.setValue((Button) view);
        ArrayList<Task> newTaskList = new ArrayList<>();
        if (tasksList.getValue() != null) {
            for (Task task :
                    tasksList.getValue()) {
                if (task.getTask_status() == 2) {
                    newTaskList.add(task);
                }
            }
            tasksSelectedList.setValue(newTaskList);
        }
    }

    public void getTaskCategories() {
        taskRepository.getTaskCategory(taskCategoriesList);
    }
}
