package com.mvvm.localtest.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class Task @JvmOverloads constructor(

    @ColumnInfo(name = "title", defaultValue = "Title")
    var title: String = "",

    @ColumnInfo(name = "description", defaultValue = "Description")
    var description: String = "",

    @ColumnInfo(name = "imageSource", defaultValue = "")
    val img_src: String = "https://cdn4.iconfinder.com/data/icons/ikooni-outline-seo-web/128/seo-12-512.png",

    @ColumnInfo(name = "completed")
    var complete: Boolean = false,

    @PrimaryKey() @ColumnInfo(name = "entryid")
    var id: String = UUID.randomUUID().toString()
) {
    val titleForList: String
        get() = if (title.isNotEmpty()) title else description


    val isActive
        get() = !complete

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()
}