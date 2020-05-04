package com.mvvm.localtest.tasks

import android.util.Log
import androidx.lifecycle.*
import com.mvvm.localtest.util.Event
import com.mvvm.localtest.data.Task
import com.mvvm.localtest.data.source.TaskRepository
import kotlinx.coroutines.launch
import com.mvvm.localtest.data.Result
import com.mvvm.localtest.R

class TasksViewModel(private val repository: TaskRepository): ViewModel() {

    private val _openTaskEvent = MutableLiveData<Event<String>>()
    val openTaskEvent: LiveData<Event<String>> = _openTaskEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _newTaskEvent = MutableLiveData<Event<Unit>>()
    val newTaskEvent: LiveData<Event<Unit>> = _newTaskEvent

    private var resultMessageShown: Boolean = false

    private val _realEstateList = repository.observeTasks().switchMap {
        val listData = MutableLiveData<List<Task>>()
        when(it){
            is Result.Success -> listData.value = it.data
            else -> listData.value = emptyList()
        }
        listData
    }

    val realEstateList: LiveData<List<Task>> = _realEstateList

    init {
//        loadTasks(true)
    }

    fun openTask(taskId: String) {
        _openTaskEvent.value = Event(taskId)
    }

    fun loadTasks(forceUpdate: Boolean) {
        viewModelScope.launch {
            val value = repository.getTasks(forceUpdate)

            if (value is Result.Success) {
                Log.e("value ", value.data.toString());
            }
        }
    }

    fun showEditResultMessage(result: Int) {
        if (resultMessageShown) return
        when (result) {
            EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_saved_task_message)
            ADD_EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_added_task_message)
            DELETE_RESULT_OK -> showSnackbarMessage(R.string.successfully_deleted_task_message)
        }
        resultMessageShown = true
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    fun addNewTask() {
        _newTaskEvent.value = Event(Unit)
    }
}