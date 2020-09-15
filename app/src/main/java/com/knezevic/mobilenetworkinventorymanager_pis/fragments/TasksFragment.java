package com.knezevic.mobilenetworkinventorymanager_pis.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.knezevic.mobilenetworkinventorymanager_pis.R;
import com.knezevic.mobilenetworkinventorymanager_pis.activities.MainActivity;
import com.knezevic.mobilenetworkinventorymanager_pis.adapter.TaskRecyclerViewAdapter;
import com.knezevic.mobilenetworkinventorymanager_pis.databinding.FragmentTasksBinding;
import com.knezevic.mobilenetworkinventorymanager_pis.model.Task;
import com.knezevic.mobilenetworkinventorymanager_pis.util.SharedPreferencesHelper;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.AddTaskViewModel;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.TaskViewModel;

import java.util.ArrayList;

import static com.knezevic.mobilenetworkinventorymanager_pis.util.Constants.USER_ID;
import static com.knezevic.mobilenetworkinventorymanager_pis.util.Constants.USER_MANAGER;
import static com.knezevic.mobilenetworkinventorymanager_pis.util.Constants.USER_ROLE;

public class TasksFragment extends Fragment {

    private FragmentTasksBinding dataBinding;
    private TaskRecyclerViewAdapter taskRecyclerViewAdapter;
    private TaskViewModel taskViewModel;

    private String userRole;
    private SharedPreferencesHelper sharedPrefs;
    private NavController navController;
    public AddTaskViewModel addTaskViewModel;
    private AddTaskDialogFragment addTaskDialogFragment;

    private ArrayList<Integer> categoryButtonsList = new ArrayList<>();

    private Observer<Task> taskObserver = task -> {
        if (task != null && navController != null) {
            Bundle args = new Bundle();
            args.putString("task_id", task.getTask_id().toString());

            navController.navigate(R.id.action_nav_tasks_fragment_to_taskDetailsFragment, args);
        }
    };

    private Observer<ArrayList<Task>> taskListObserver = tasks -> {
        if (!tasks.isEmpty()
                && (addTaskDialogFragment.refreshTasks.getValue() == null
                || !addTaskDialogFragment.refreshTasks.getValue())) {
            Log.d("userTasks", "onChanged: " + tasks.size());
            taskViewModel.tasksSelectedList.setValue(tasks);
            setBtnColor(R.id.btnAll);
            taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(taskViewModel.tasksSelectedList.getValue());
            taskRecyclerViewAdapter.selectedTask.observe(getViewLifecycleOwner(), taskObserver);
            setupRecyclerView();

            taskViewModel.showProgressBar.setValue(false);
        } else if (!tasks.isEmpty()) {
            taskRecyclerViewAdapter.resetTasks(tasks);
            Log.d("TASK_RECYCLER_VIEW", "CHANGING TASKS");
        }
    };

    private Observer<Button> buttonObserver = button -> {
        setBtnColor(button.getId());
    };

    private Observer<Boolean> refreshObserver = refresh -> {
        Log.d("REFRESH_OBSERVER", "VALUE -> " + refresh);
        if (refresh) {
            getTasksList();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPrefs = new SharedPreferencesHelper(requireActivity().getApplicationContext());
        userRole = sharedPrefs.getValueString(USER_ROLE);
        if (userRole.equals(USER_MANAGER)) {
            setHasOptionsMenu(true);
        }
        super.onCreate(savedInstanceState);
        addTaskDialogFragment = new AddTaskDialogFragment();
        addTaskViewModel = new AddTaskViewModel();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.action_add_task).setOnMenuItemClickListener(item -> {
            addTaskDialogFragment.show(getParentFragmentManager(), "AddTaskDialogFragment");
            return true;
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        taskViewModel = new TaskViewModel();

        Log.d("USER_ROLE", "onCreateView: " + sharedPrefs.getValueString(USER_ROLE));

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            navController = mainActivity.getNavController();
        }

        categoryButtonsList.add(R.id.btnAll);
        categoryButtonsList.add(R.id.btnDone);
        categoryButtonsList.add(R.id.btnUndone);

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks, container, false);
        dataBinding.setLifecycleOwner(getViewLifecycleOwner());
        dataBinding.setTaskViewModel(taskViewModel);

        taskViewModel.userId.setValue(sharedPrefs.getValueString(USER_ID));
        taskViewModel.tasksList.observe(getViewLifecycleOwner(), taskListObserver);
        getTasksList();
        taskViewModel.tasksSelectedList.observe(getViewLifecycleOwner(), tasks -> {
            if (taskRecyclerViewAdapter != null) {
                taskRecyclerViewAdapter.resetTasks(tasks);
            }
        });
        taskViewModel.btnToChangeColor.observe(getViewLifecycleOwner(), buttonObserver);

        addTaskDialogFragment.refreshTasks.observe(getViewLifecycleOwner(), refreshObserver);

        return dataBinding.getRoot();
    }

    private void setBtnColor(Integer btnId) {
        Button btnHolder;
        for (Integer btn_id :
                categoryButtonsList) {
            btnHolder = requireView().findViewById(btn_id);
            if (!btn_id.equals(btnId)) {
                Log.d("BUTTONS_LIST_UNSELECTED", "onChanged: " + btn_id);
                btnHolder.setBackground(getResources().getDrawable(R.drawable.btn_category_shape));
                btnHolder.setTextColor(getResources().getColor(R.color.colorWhite));
            } else {
                btnHolder.setBackground(getResources().getDrawable(R.drawable.btn_category_selected_shape));
                btnHolder.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }

    private void setupRecyclerView() {
        RecyclerView rvTask = dataBinding.recyclerViewTask;

        Log.d("RECYCLER_VIEW_ITEMS", "setupRecyclerView: " + rvTask.toString());
        Log.d("RECYCLER_VIEW_ITEMS", "setupRecyclerView: " + taskRecyclerViewAdapter.toString());

        rvTask.setAdapter(taskRecyclerViewAdapter);
        rvTask.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
    }

    private void getTasksList() {
        if (userRole.equals(USER_MANAGER)) {
            taskViewModel.getAllTasks();
        } else {
            taskViewModel.getUserTasks();
        }
    }
}
