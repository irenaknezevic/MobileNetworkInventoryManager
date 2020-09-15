package com.knezevic.mobilenetworkinventorymanager_pis.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.knezevic.mobilenetworkinventorymanager_pis.R;
import com.knezevic.mobilenetworkinventorymanager_pis.databinding.FragmentTaskDetailsBinding;
import com.knezevic.mobilenetworkinventorymanager_pis.model.Task;
import com.knezevic.mobilenetworkinventorymanager_pis.model.User;
import com.knezevic.mobilenetworkinventorymanager_pis.util.Functions;
import com.knezevic.mobilenetworkinventorymanager_pis.util.SharedPreferencesHelper;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.TaskViewModel;
import com.knezevic.mobilenetworkinventorymanager_pis.viewmodel.UserViewModel;

import static com.knezevic.mobilenetworkinventorymanager_pis.util.Constants.USER_ID;
import static com.knezevic.mobilenetworkinventorymanager_pis.util.Constants.USER_MANAGER;

public class TaskDetailsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private SharedPreferencesHelper sharedPreferences;
    private TaskViewModel taskViewModel;
    private UserViewModel userViewModel;
    private FragmentTaskDetailsBinding dataBinding;
    private Task task;
    private ConfirmDialogFragment confirmDialogFragment;
    private MenuItem taskCompletedBtn;

    private Observer<Task> taskObserver = task -> {
        if (task != null) {
            Log.d("TASK_DETAILS", "Task: user_id " + task.getUser_id());
            Log.d("TASK_DETAILS", "Task: closing " + task.getTask_closing_time());
            dataBinding.setTask(task);
            this.task = task;
            loadTaskOnMap(task.getSite_name(), task.getSite_lat(), task.getSite_lng());
            userViewModel.getProfileData();

            if (task.getTask_status() == 2) {
                setHasOptionsMenu(true);
            }
        }
    };

    private Observer<User> userObserver = user -> {
        if (user != null) {
            loadUserOnMap(user.getName(), user.getLat(), user.getLng());
            dataBinding.setDistance(Functions.loadDistanceText(task.getSite_lat(), task.getSite_lng(), user.getLat(), user.getLng()));

            if (user.getRole().equals(USER_MANAGER)) {
                dataBinding.setTechnician(task.getUser_name() + " " + task.getUser_surname());
            }
        }
    };

    private Observer<Boolean> isCompletedObserver = isCompleted -> {
        if (isCompleted) {
            taskCompletedBtn.setVisible(false);
            if (getArguments() != null) {
                Log.d("TASK_DETAILS_FRAGMENT", "onViewCreated: " + getArguments().getString("task_id"));
                taskViewModel.getTaskById(getArguments().getString("task_id"));
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskViewModel = new TaskViewModel();
        userViewModel = new UserViewModel();
        sharedPreferences = new SharedPreferencesHelper(requireContext());
        confirmDialogFragment = new ConfirmDialogFragment();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.task_details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.action_complete).setOnMenuItemClickListener(item -> {
            if (getArguments() != null && getArguments().getString("task_id") != null) {
                Bundle args = new Bundle();
                args.putString("task_id", getArguments().getString("task_id"));
                confirmDialogFragment.setArguments(args);
                confirmDialogFragment.show(getParentFragmentManager(), "ConfirmDialogFragment");
            } else {
                Toast.makeText(getContext(), R.string.task_id_failed, Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        taskCompletedBtn = menu.findItem(R.id.action_complete);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_details, container, false);
        dataBinding.setTechnician("");
        mapView = dataBinding.taskMapView;

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel.userId.setValue(sharedPreferences.getValueString(USER_ID));
        taskViewModel.selectedTask.observe(getViewLifecycleOwner(), taskObserver);
        userViewModel.user.observe(getViewLifecycleOwner(), userObserver);
        confirmDialogFragment.isCompleted.observe(getViewLifecycleOwner(), isCompletedObserver);

        if (getArguments() != null) {
            Log.d("TASK_DETAILS_FRAGMENT", "onViewCreated: " + getArguments().getString("task_id"));
            taskViewModel.getTaskById(getArguments().getString("task_id"));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void loadTaskOnMap(String siteName, Double siteLat, Double siteLng) {
        LatLng location = new LatLng(siteLat, siteLng);
        mMap.addMarker(new MarkerOptions().position(location).title(siteName).icon(BitmapDescriptorFactory.fromResource(R.drawable.antenna_black)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 8F));
    }

    private void loadUserOnMap(String userName, Double userLat, Double userLng) {
        LatLng userLocation = new LatLng(userLat, userLng);
        mMap.addMarker(new MarkerOptions().position(userLocation).title(userName).icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        taskViewModel.selectedTask.removeObservers(getViewLifecycleOwner());
        userViewModel.user.removeObservers(getViewLifecycleOwner());
    }
}
