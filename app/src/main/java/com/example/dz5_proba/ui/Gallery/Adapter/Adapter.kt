package com.example.dz5_proba.ui.Gallery.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.dz5_proba.databinding.ItemTaskBinding
import com.example.dz5_proba.model.Task
class TaskAdapter(private val taskChanged:(Task)->Unit) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DIFF_UTIL_CALL_BACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            binding = ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_UTIL_CALL_BACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem.id == newItem.id
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem
        }
    }

    inner class TaskViewHolder(
        val binding: ItemTaskBinding
    ) : ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.run {
                tvTitle.text = task.title
                cbDone.isChecked = task.done
                cbDone.setOnCheckedChangeListener { buttonView, isChecked ->
                    taskChanged(task.copy(done = isChecked))
                }
            }
        }
    }
}