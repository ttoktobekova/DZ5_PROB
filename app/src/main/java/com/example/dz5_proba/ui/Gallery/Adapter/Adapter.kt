package com.example.dz5_proba.ui.Gallery.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dz5_proba.databinding.ItemTaskBinding
import com.example.dz5_proba.model.Task
class TaskAdapter(private val taskChanged: (Task) -> Unit) :
    ListAdapter<Task, TaskAdapter.TaskViewHolder>(DIFF_UTIL_CALL_BACK) {

    companion object {
        val DIFF_UTIL_CALL_BACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }
    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.apply {
                tvTitle.text = task.title
                cbDone.isChecked = task.done
                cbDone.setOnCheckedChangeListener { _, isChecked ->
                    taskChanged(task.copy(done = isChecked))
                }
            }
        }
    }
}
