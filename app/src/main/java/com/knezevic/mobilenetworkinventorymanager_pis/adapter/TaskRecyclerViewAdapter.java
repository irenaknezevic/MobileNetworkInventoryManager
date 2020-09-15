package com.knezevic.mobilenetworkinventorymanager_pis.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.knezevic.mobilenetworkinventorymanager_pis.R;
import com.knezevic.mobilenetworkinventorymanager_pis.databinding.TaskListItemBinding;
import com.knezevic.mobilenetworkinventorymanager_pis.model.Task;

import java.util.ArrayList;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Task> tasks;
    public MutableLiveData<Task> selectedTask = new MutableLiveData<>();

    public TaskRecyclerViewAdapter(ArrayList<Task> tasksList) {
        tasks = tasksList;
    }

    public void resetTasks(ArrayList<Task> newTasks) {
        tasks = newTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TaskListItemBinding taskListItemBinding = TaskListItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolderTasks(taskListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ViewHolderTasks viewHolderTasks = (ViewHolderTasks) holder;
        viewHolderTasks.bind(tasks.get(position));
        viewHolderTasks.itemView.setOnClickListener(v -> selectedTask.setValue(tasks.get(position)));
    }

    @Override
    public int getItemCount() {
        if (tasks != null) {
            return tasks.size();
        } else {
            return 0;
        }
    }

    public class ViewHolderTasks extends RecyclerView.ViewHolder {
        TaskListItemBinding taskListItemBinding;

        private ViewHolderTasks(@NonNull TaskListItemBinding taskListItemBinding) {
            super(taskListItemBinding.getRoot());
            this.taskListItemBinding = taskListItemBinding;
        }

        void bind(Task task) {
            taskListItemBinding.setTask(task);
            if (task.getTask_status() == 1) {
                taskListItemBinding.getRoot()
                        .setBackground(taskListItemBinding.getRoot()
                                .getContext().getResources()
                                .getDrawable(R.drawable.green_drawable));
            } else {
                taskListItemBinding.getRoot()
                        .setBackground(taskListItemBinding.getRoot()
                                .getContext().getResources()
                                .getDrawable(R.drawable.red_drawable));
            }
            taskListItemBinding.executePendingBindings();
        }
    }
}
