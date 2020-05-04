package com.mvvm.localtest.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mvvm.localtest.data.Task

@Dao
interface TasksDao {

    @Query("SELECT * FROM Tasks")
    fun observeTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM Tasks WHERE entryid = :taskId")
    fun observeTaskById(taskId: String): LiveData<Task>

    @Query("SELECT * FROM Tasks")
    suspend fun getTasks(): List<Task>

    @Query("SELECT * FROM Tasks WHERE entryid = :taskId")
    suspend fun getTaskById(taskId: String): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTask(task: List<Task>)

    @Update
    suspend fun updateTask(task: Task): Int

    @Query("UPDATE tasks SET completed = :completed WHERE entryid = :taskId")
    suspend fun updateCompleted(taskId: String, completed: Boolean)

    @Query("DELETE FROM Tasks WHERE entryid = :taskId")
    suspend fun deleteTaskById(taskId: String): Int

    @Query("DELETE FROM Tasks")
    suspend fun deleteTasks()

    @Query("DELETE FROM Tasks WHERE completed = 1")
    suspend fun deleteCompletedTasks(): Int

}