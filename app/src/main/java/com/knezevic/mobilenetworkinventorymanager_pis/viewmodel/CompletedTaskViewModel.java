package com.knezevic.mobilenetworkinventorymanager_pis.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.knezevic.mobilenetworkinventorymanager_pis.repositories.TaskRepository;

public class CompletedTaskViewModel extends ViewModel {
    public MutableLiveData<String> taskId = new MutableLiveData<>();
    public MutableLiveData<Boolean> result = new MutableLiveData<>();
    public MutableLiveData<Boolean> closeDialog = new MutableLiveData<>(false);
    private TaskRepository taskRepository = new TaskRepository();

    public void clickConfirm() {
        Log.d("taskId", "clickConfirm: " + taskId.getValue());
        taskRepository.updateTask(taskId.getValue(), result);
    }

    public void clickClose() {
        closeDialog.setValue(true);
    }
}
