package com.mvvm.localtest.source

import androidx.lifecycle.LiveData
import com.mvvm.localtest.data.Task
import com.mvvm.localtest.data.source.local.TasksDataSource
import com.mvvm.localtest.data.Result.Success
import com.mvvm.localtest.data.Result.Error
import com.mvvm.localtest.data.Result

class FakeDataSource(var tasks: MutableList<Task>? = mutableListOf()) : TasksDataSource {
        override suspend fun getTasks(): Result<List<Task>> {
            tasks?.let { return Success(ArrayList(it)) }
            return Error("DataBase Error", "Task not found!")
        }

        override fun observeTasks(): LiveData<Result<List<Task>>> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override suspend fun refreshTasks() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun observeTask(taskId: String): LiveData<Result<Task>> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override suspend fun getTask(taskId: String): Result<Task> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override suspend fun refreshTask(taskId: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    override suspend fun saveAllTask(task: List<Task>) {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(task: Task) {
            tasks?.add(task)
        }

        override suspend fun completeTask(task: Task) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override suspend fun completeTask(taskId: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override suspend fun activateTask(task: Task) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override suspend fun activateTask(taskId: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override suspend fun clearCompletedTasks() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override suspend fun deleteAllTasks() {
            tasks?.clear()
        }

        override suspend fun deleteTask(taskId: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
}