package com.mvvm.localtest.tasks

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mvvm.localtest.data.Task

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Task>?){
    items?.let {
        (listView.adapter as TasksAdapter).submitList(items)
    }
}

@BindingAdapter("loadImage")
fun setLoadImage(imageView: ImageView, imgUrl: String ){
    Glide.with(imageView.context).load(imgUrl).into(imageView)
}