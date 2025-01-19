package com.example.studybuddy.ui.todolist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.R
import com.example.studybuddy.data.local.model.Task

class TaskAdapter(
    private var tasks: List<Task>,
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
        holder.checkBox.isChecked = task.isChecked

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onTaskChecked(position, isChecked)
        }

        holder.itemView.setOnLongClickListener {
            onTaskLongClicked(position)
            true
        }
    }

    override fun getItemCount() = tasks.size

    fun setTasks(newTasks: List<Task>) {
        Log.d("___TEST_TASK_ADAPTER", "Updating tasks: ${newTasks.size}")
        tasks = newTasks
        notifyDataSetChanged()
    }

}
