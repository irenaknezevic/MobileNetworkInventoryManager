package com.knezevic.mobilenetworkinventorymanager_pis.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.knezevic.mobilenetworkinventorymanager_pis.R;
import com.knezevic.mobilenetworkinventorymanager_pis.databinding.DialogFragmentAddTaskBinding;
import com.knezevic.mobilenetworkinventorymanager_pis.model.CreateTask;
import com.knezevic.mobilenetworkinventorymanager_pis.model.Site;
import com.knezevic.mobilenetworkinventorymanager_pis.model.User;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.AddTaskViewModel;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.SiteViewModel;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.TaskViewModel;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.UserViewModel;

import java.util.ArrayList;

public class AddTaskDialogFragment extends DialogFragment {
    private DialogFragmentAddTaskBinding dataBinding;
    private AddTaskViewModel addTaskViewModel;
    private SiteViewModel siteViewModel;
    private UserViewModel userViewModel;
    private TaskViewModel taskViewModel;

    MutableLiveData<Boolean> refreshTasks = new MutableLiveData<>(false);

    private ArrayList<String> sitesList = new ArrayList<>();
    private ArrayList<String> techniciansList = new ArrayList<>();
    private ArrayList<Integer> techniciansIdsList = new ArrayList<>();
    private ArrayList<String> categoriesList = new ArrayList<>();
    private ArrayAdapter<String> dataAdapter;

    AddTaskDialogFragment() {
        addTaskViewModel = new AddTaskViewModel();
        siteViewModel = new SiteViewModel();
        userViewModel = new UserViewModel();
        taskViewModel = new TaskViewModel();
    }

    private AdapterView.OnItemSelectedListener onSiteSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (addTaskViewModel.createTask.getValue() != null) {
                addTaskViewModel.createTask.getValue().setSite_id(position + 1);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener onTechniciansSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (addTaskViewModel.createTask.getValue() != null) {

                addTaskViewModel.createTask.getValue().setUser_id(techniciansIdsList.get(position));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener onCategoriesSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (addTaskViewModel.createTask.getValue() != null) {
                addTaskViewModel.createTask.getValue().setTask_category(position + 1);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private Observer<ArrayList<Site>> sitesListObserver = sites -> {
        sitesList.clear();
        if (!sites.isEmpty()) {
            for (Site site : sites) {
                sitesList.add(site.getName() + " (" + site.getMark() + ")");
            }
            setUpSiteSpinner(sitesList);
        }
    };

    private Observer<ArrayList<User>> techniciansListObserver = technicians -> {
        techniciansIdsList.clear();
        techniciansList.clear();
        if (!technicians.isEmpty()) {
            for (User user : technicians) {
                if (user.getRole().equals("2")) {
                    techniciansIdsList.add(user.getUser_id());
                    techniciansList.add(user.getName() + " " + user.getSurname());
                }
                setUpTechniciansSpinner(techniciansList);
            }
        }
    };

    private Observer<ArrayList<String>> categoriesListObserver = categories -> {
        categoriesList.clear();
        if (!categories.isEmpty()) {
            categoriesList.addAll(categories);

            setUpCategoriesSpinner(categoriesList);
        }
    };

    private Observer<Boolean> resultObserver = resultSuccessful -> {
        if (resultSuccessful) {
            Toast.makeText(getContext(), R.string.task_added_successfully, Toast.LENGTH_SHORT).show();
            refreshTasks.setValue(true);
            addTaskViewModel.createTask = new MutableLiveData<>(new CreateTask());
            dismiss();
        } else {
            Toast.makeText(getContext(), R.string.adding_task_failed, Toast.LENGTH_SHORT).show();
        }
    };

    private Observer<Boolean> closeDialogObserver = closeDialog -> {
        if (closeDialog) {
            dismiss();
            addTaskViewModel.closeDialog.setValue(false);
            addTaskViewModel.createTask = new MutableLiveData<>(new CreateTask());
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_add_task, container, false);
        addTaskViewModel.result = new MutableLiveData<>();
        dataBinding.setAddTaskViewModel(addTaskViewModel);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addTaskViewModel.result.observe(getViewLifecycleOwner(), resultObserver);
        addTaskViewModel.closeDialog.observe(getViewLifecycleOwner(), closeDialogObserver);

        siteViewModel.getAllSites();
        siteViewModel.sitesList.observe(getViewLifecycleOwner(), sitesListObserver);

        userViewModel.getAllUsers();
        userViewModel.usersList.observe(getViewLifecycleOwner(), techniciansListObserver);

        taskViewModel.getTaskCategories();
        taskViewModel.taskCategoriesList.observe(getViewLifecycleOwner(), categoriesListObserver);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addTaskViewModel.result.removeObservers(getViewLifecycleOwner());
        addTaskViewModel.closeDialog.removeObservers(getViewLifecycleOwner());

    }

    private void setUpSiteSpinner(ArrayList<String> sitesList) {
        dataAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sitesList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dataBinding != null) {
            dataBinding.spinnerSite.setAdapter(dataAdapter);
            dataBinding.spinnerSite.setOnItemSelectedListener(onSiteSelectedListener);
        }
    }

    private void setUpTechniciansSpinner(ArrayList<String> techniciansList) {
        dataAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, techniciansList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dataBinding != null) {
            dataBinding.spinnerUser.setAdapter(dataAdapter);
            dataBinding.spinnerUser.setOnItemSelectedListener(onTechniciansSelectedListener);
        }
    }

    private void setUpCategoriesSpinner(ArrayList<String> categoriesList) {
        dataAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoriesList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dataBinding != null) {
            dataBinding.spinnerCategory.setAdapter(dataAdapter);
            dataBinding.spinnerCategory.setOnItemSelectedListener(onCategoriesSelectedListener);
        }
    }
}
