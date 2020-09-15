package com.knezevic.mobilenetworkinventorymanager_pis.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.knezevic.mobilenetworkinventorymanager_pis.R;
import com.knezevic.mobilenetworkinventorymanager_pis.databinding.DialogFragmentConfirmBinding;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.CompletedTaskViewModel;

public class ConfirmDialogFragment extends DialogFragment {
    private DialogFragmentConfirmBinding dataBinding;
    private CompletedTaskViewModel completedTaskViewModel;
    MutableLiveData<Boolean> isCompleted = new MutableLiveData<>(false);

    ConfirmDialogFragment() {
        completedTaskViewModel = new CompletedTaskViewModel();
    }

    private Observer<Boolean> resultObserver = resultSuccessful -> {
        if (resultSuccessful) {
            Toast.makeText(getContext(), R.string.task_complete_success, Toast.LENGTH_SHORT).show();
            isCompleted.setValue(true);
            dismiss();
        } else {
            Toast.makeText(getContext(), R.string.task_complete_failed, Toast.LENGTH_SHORT).show();
        }
    };

    private Observer<Boolean> closeDialogObserver = closeDialog -> {
        if (closeDialog) {
            dismiss();
            completedTaskViewModel.closeDialog.setValue(false);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_confirm, container, false);
        completedTaskViewModel.result = new MutableLiveData<>();
        dataBinding.setCompletedTaskViewModel(completedTaskViewModel);
        Log.d("ID_CONFIRM", "onCreateView: " + getArguments().getString("task_id"));
        completedTaskViewModel.taskId.setValue(getArguments().getString("task_id"));
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        completedTaskViewModel.result.observe(getViewLifecycleOwner(), resultObserver);
        completedTaskViewModel.closeDialog.observe(getViewLifecycleOwner(), closeDialogObserver);
    }
}
