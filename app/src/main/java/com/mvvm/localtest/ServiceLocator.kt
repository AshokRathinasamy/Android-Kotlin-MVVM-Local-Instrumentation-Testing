package com.mvvm.localtest


import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.mvvm.localtest.data.source.DefaultTaskRepository
import com.mvvm.localtest.data.source.TaskRepository
import com.mvvm.localtest.data.source.local.TasksDataSource
import com.mvvm.localtest.data.source.local.TasksLocalDataSource
import com.mvvm.localtest.data.source.local.ToDoDatabase
import com.mvvm.localtest.data.source.remote.ApiCall
import kotlinx.coroutines.runBlocking
/**
 * A Service Locator for the [TasksRepository]. This is the prod version, with a
 * the "real" [TasksRemoteDataSource].
 */
object ServiceLocator {

    private val lock = Any()
    private val apiCall = ApiCall()
    private var database: ToDoDatabase? = null
    @Volatile
    var tasksRepository: TaskRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): TaskRepository {
        synchronized(this) {
            return tasksRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): TaskRepository {
        val newRepo = DefaultTaskRepository(apiCall, createTaskLocalDataSource(context))
        tasksRepository = newRepo
        return newRepo
    }

    private fun createTaskLocalDataSource(context: Context): TasksDataSource {
        val database = database ?: createDataBase(context)
        return TasksLocalDataSource(database.taskDao())
    }

    private fun createDataBase(context: Context): ToDoDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            ToDoDatabase::class.java, "Tasks.db"
        ).build()
        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            runBlocking {
//                TasksRemoteDataSource.deleteAllTasks()
            }
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            tasksRepository = null
        }
    }
}
