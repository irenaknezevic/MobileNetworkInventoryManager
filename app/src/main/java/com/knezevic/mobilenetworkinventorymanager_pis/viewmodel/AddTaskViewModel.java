package com.knezevic.mobilenetworkinventorymanager_pis.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.knezevic.mobilenetworkinventorymanager_pis.model.CreateTask;
import com.knezevic.mobilenetworkinventorymanager_pis.repositories.TaskRepository;

public class AddTaskViewModel extends ViewModel {
    public MutableLiveData<CreateTask> createTask = new MutableLiveData<>(new CreateTask());
    public MutableLiveData<Boolean> result = new MutableLiveData<>();
    public MutableLiveData<Boolean> closeDialog = new MutableLiveData<>(false);
    private TaskRepository taskRepository = new TaskRepository();

    public void clickSubmit() {
        CreateTask task = createTask.getValue();
        assert task != null;
        if (task.getSite_id() != null
                && task.getUser_id() != null
                && task.getTask_category() != null
                && task.getTask_description() != null) {
            Log.d("BUTTON_ADD_TASK", "clickSubmit: site_id " + createTask.getValue().getSite_id());
            Log.d("BUTTON_ADD_TASK", "clickSubmit: user_id " + createTask.getValue().getUser_id());
            Log.d("BUTTON_ADD_TASK", "clickSubmit: task_category " + createTask.getValue().getTask_category());
            Log.d("BUTTON_ADD_TASK", "clickSubmit: task_description " + createTask.getValue().getTask_description());
            taskRepository.addNewTask(createTask.getValue(), result);
        }
    }

    public void clickClose() {
        closeDialog.setValue(true);
    }
}
