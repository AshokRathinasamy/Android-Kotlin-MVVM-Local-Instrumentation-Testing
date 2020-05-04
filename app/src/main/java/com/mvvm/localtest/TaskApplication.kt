package com.mvvm.localtest

import android.app.Application
import com.mvvm.localtest.data.source.TaskRepository

class TaskApplication : Application() {

    val taskRepository: TaskRepository
        get() = ServiceLocator.provideTasksRepository(this)

}