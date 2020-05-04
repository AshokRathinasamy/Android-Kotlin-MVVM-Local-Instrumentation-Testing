package com.mvvm.localtest.data.source

import androidx.lifecycle.LiveData
import com.mvvm.localtest.util.wrapEspressoIdlingResource
import com.mvvm.localtest.data.Task
import com.mvvm.localtest.data.Result
import com.mvvm.localtest.data.Result.Success
import com.mvvm.localtest.data.Result.Error
import com.mvvm.localtest.data.source.local.TasksDataSource
import com.mvvm.localtest.data.source.remote.ApiCall
import com.mvvm.localtest.data.source.remote.SafeApiRequest
import kotlinx.coroutines.*

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 */
class DefaultTaskRepository(
    private val tasksRemoteDataSource: ApiCall,
    private val tasksLocalDataSource: TasksDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): SafeApiRequest(), TaskRepository {

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                try {
                    updateTasksFromRemoteDataSource()
                } catch (ex: Exception) {
                    return Error("Error Message", ex.message!!)
                }
            }
            return tasksLocalDataSource.getTasks()
        }
    }

    override suspend fun taskCount() : Int {
        wrapEspressoIdlingResource {
            val listData = tasksLocalDataSource.getTasks()
            if (listData is Success) {
                return listData.data.size
            } else {
                return 0
            }
        }
    }

    override suspend fun refreshTasks() {
        wrapEspressoIdlingResource {
            updateTasksFromRemoteDataSource()
        }
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        wrapEspressoIdlingResource {
            return tasksLocalDataSource.observeTasks()
        }
    }

    override suspend fun refreshTask(taskId: String) {
        wrapEspressoIdlingResource {
            updateTaskFromLocalDataSource(taskId)
        }
    }

    private suspend fun updateTasksFromRemoteDataSource() {
        wrapEspressoIdlingResource {
            var remoteTasks = apiRequest { tasksRemoteDataSource.getAllData() }

            if (remoteTasks is Success) {

                tasksLocalDataSource.deleteAllTasks()
                tasksLocalDataSource.saveAllTask(remoteTasks.data)

                // Real apps might want to do a proper sync.
                /*tasksLocalDataSource.deleteAllTasks()
                remoteTasks.data.forEach { task ->
                    tasksLocalDataSource.saveTask(task)
                }*/
            } else if (remoteTasks is Error) {
                throw Exception("Api error")
            }
        }
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        wrapEspressoIdlingResource {
            return tasksLocalDataSource.observeTask(taskId)
        }
    }

    private suspend fun updateTaskFromLocalDataSource(taskId: String) {
        wrapEspressoIdlingResource {
            val remoteTask = tasksLocalDataSource.getTask(taskId)

            if (remoteTask is Success) {
                tasksLocalDataSource.saveTask(remoteTask.data)
            }
        }
    }

    /**
     * Relies on [getTasks] to fetch data and picks the task with the same ID.
     */
   override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                updateTaskFromLocalDataSource(taskId)
            }
            return tasksLocalDataSource.getTask(taskId)
        }
    }

    override suspend fun saveTask(task: Task) {
        wrapEspressoIdlingResource {
            coroutineScope {
//                launch { tasksRemoteDataSource.saveTask(task) }
                launch { tasksLocalDataSource.saveTask(task) }
            }
        }
    }

    override suspend fun completeTask(task: Task) {
        wrapEspressoIdlingResource {
            coroutineScope {
                launch { tasksLocalDataSource.completeTask(task) }
            }
        }
    }

    override suspend fun completeTask(taskId: String) {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                (getTaskWithId(taskId) as? Success)?.let { it ->
                    completeTask(it.data)
                }
            }
        }
    }

    override suspend fun activateTask(task: Task) = withContext<Unit>(ioDispatcher) {
        wrapEspressoIdlingResource {
            coroutineScope {
                launch { tasksLocalDataSource.activateTask(task) }
            }
        }
    }

    override suspend fun activateTask(taskId: String) {
      wrapEspressoIdlingResource {
          withContext(ioDispatcher) {
              (getTaskWithId(taskId) as? Success)?.let { it ->
                  activateTask(it.data)
              }
          }
      }
    }

    override suspend fun clearCompletedTasks() {
      wrapEspressoIdlingResource {
          coroutineScope {
              launch { tasksLocalDataSource.clearCompletedTasks() }
          }
      }
    }

    override suspend fun deleteAllTasks() {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                coroutineScope {
                    launch { tasksLocalDataSource.deleteAllTasks() }
                }
            }
        }
    }

    override suspend fun deleteTask(taskId: String) {
        wrapEspressoIdlingResource {
            coroutineScope {
                launch { tasksLocalDataSource.deleteTask(taskId) }
            }
        }
    }

    private suspend fun getTaskWithId(id: String): Result<Task> {
        wrapEspressoIdlingResource {
            return tasksLocalDataSource.getTask(id)
        }
    }
}
