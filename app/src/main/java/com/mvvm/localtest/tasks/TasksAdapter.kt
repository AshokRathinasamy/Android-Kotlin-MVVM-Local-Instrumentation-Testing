package com.mvvm.localtest.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.localtest.data.Task
import com.mvvm.localtest.databinding.RowItemTaskBinding
import com.mvvm.localtest.tasks.TasksViewModel

class TasksAdapter(private val viewModel: TasksViewModel) :
    ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    class TaskViewHolder private constructor(val binding: RowItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModels: TasksViewModel, realestate: Task) {
            binding.viewModel = viewModels
            binding.itemRealEstate = realestate
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TaskViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowItemTaskBinding.inflate(layoutInflater, parent, false)
                return TaskViewHolder(binding)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(
        oldItem: Task,
        newItem: Task
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Task,
        newItem: Task
    ): Boolean {
        return oldItem == newItem
    }

}