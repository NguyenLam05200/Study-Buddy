package com.example.studybuddy.ui.todolist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.R
import com.example.studybuddy.ui.todolist.data.TaskModel

class TaskAdapter(
    private var tasks: List<TaskModel>,
    val onTaskChecked: (Int, Boolean) -> Unit,
    val onTaskLongClicked: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.task_textview)
        val checkBox: CheckBox = view.findViewById(R.id.task_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.textView.text = task.text

        // Đặt giá trị CheckBox mà không gọi lại listener
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = task.isChecked

        // Đặt listener cho CheckBox
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onTaskChecked(position, isChecked)
        }

        holder.itemView.setOnLongClickListener {
            onTaskLongClicked(position)
            true
        }
    }


    override fun getItemCount() = tasks.size

    fun setTasks(newTasks: List<TaskModel>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

}

