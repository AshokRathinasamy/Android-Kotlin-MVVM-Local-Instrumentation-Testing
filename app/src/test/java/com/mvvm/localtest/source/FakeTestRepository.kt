package com.mvvm.localtest.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.mvvm.localtest.data.Task
import com.mvvm.localtest.data.source.TaskRepository
import kotlinx.coroutines.runBlocking
import java.util.LinkedHashMap
import com.mvvm.localtest.data.Result.Success
import com.mvvm.localtest.data.Result.Error
import com.mvvm.localtest.data.Result

class FakeTestRepository : TaskRepository {

    var tasksServiceData: LinkedHashMap<String, Task> = LinkedHashMap()

    private var shouldReturnError = false

    private val observableTasks = MutableLiveData<Result<List<Task>>>()

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()
    }

    override suspend fun taskCount(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun refreshTask(taskId: String) {
        refreshTasks()
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        runBlocking { refreshTasks() }
        return observableTasks
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        runBlocking { refreshTasks() }
        return observableTasks.map { tasks ->
            when (tasks) {
                is Result.Loading -> Result.Loading
                is Error -> Error("Error", "Not found")
                is Success -> {
                    val task = tasks.data.firstOrNull() { it.id == taskId }
                        ?: return@map Error("Error", "Not found")
                    Success(task)
                }
            }
        }
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        if (shouldReturnError) {
            return Error("Exception","Test exception")
        }
        tasksServiceData[taskId]?.let {
            return Success(it)
        }
        return Error("Exception", "Could not find task")
    }

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        if (shouldReturnError) {
            return Error("Exception", "Test exception")
        }
        return Success(tasksServiceData.values.toList())
    }

    override suspend fun saveTask(task: Task) {
        tasksServiceData[task.id] = task
    }

    override suspend fun completeTask(task: Task) {
        val completedTask = task.copy(complete = true)
        tasksServiceData[task.id] = completedTask
        refreshTasks()
    }

    override suspend fun completeTask(taskId: String) {
        // Not required for the remote data source.
        throw NotImplementedError()
    }

    override suspend fun activateTask(task: Task) {
        val activeTask = task.copy(complete = false)
        tasksServiceData[task.id] = activeTask
        refreshTasks()
    }

    override suspend fun activateTask(taskId: String) {
        throw NotImplementedError()
    }

    override suspend fun clearCompletedTasks() {
        tasksServiceData = tasksServiceData.filterValues {
            !it.complete
        } as LinkedHashMap<String, Task>
    }

    override suspend fun deleteTask(taskId: String) {
        tasksServiceData.remove(taskId)
        refreshTasks()
    }

    override suspend fun deleteAllTasks() {
        tasksServiceData.clear()
        refreshTasks()
    }

    fun addTasks(vararg tasks: Task) {
        for (task in tasks) {
            tasksServiceData[task.id] = task
        }
        runBlocking { refreshTasks() }
    }
}