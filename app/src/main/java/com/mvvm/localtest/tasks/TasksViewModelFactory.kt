package com.mvvm.localtest.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mvvm.localtest.data.source.TaskRepository

class TasksViewModelFactory(
    private val repository: TaskRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TasksViewModel(repository) as T
    }
}