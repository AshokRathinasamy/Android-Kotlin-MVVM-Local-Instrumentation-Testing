package com.mvvm.localtest.source

import com.mvvm.localtest.MainCoroutineRule
import com.mvvm.localtest.data.Result
import com.mvvm.localtest.data.Task
import com.mvvm.localtest.data.source.DefaultTaskRepository
import com.mvvm.localtest.data.source.remote.ApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultTasksRepositoryTest {

    private val task1 = Task("TaskTitleOne", "Description1")
    private val task2 = Task("TaskTitleTwo", "Description2")
    private val task3 = Task("TaskTitleThree", "Description3")
    private val remoteTasks = listOf(task1, task2).sortedBy { it.id }
    private val localTasks = listOf(task3).sortedBy { it.id }
    private val newTasks = listOf(task3).sortedBy { it.id }
    private lateinit var tasksLocalDataSource: FakeDataSource

    // Class under test
    private lateinit var tasksRepository: DefaultTaskRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        tasksLocalDataSource = FakeDataSource(localTasks.toMutableList())
        // Get a reference to the class under test
        tasksRepository = DefaultTaskRepository(
            ApiCall(), tasksLocalDataSource, Dispatchers.Main
        )
    }

    @Test
    fun getTasks_requestsAllTasksFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
        // When tasks are requested from the tasks repository
//        val tasks = tasksRepository.getTasks(true) as Result.Success

        // Then tasks are loaded from the remote data source
//        assertThat(tasks.data, IsEqual(remoteTasks))
    }
}